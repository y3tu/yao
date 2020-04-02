import axios from 'axios'
import {Message} from 'element-ui'
import {authorizationValue} from '@/settings'
import store from '@/store/index'
import {getToken, getRefreshToken, getExpireTime} from '@/utils/auth'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 请求超时时间，10s
const requestTimeOut = 10 * 1000;
// 成功状态
const success = 200;
// 更换令牌的时间区间
const checkRegion = 5 * 60 * 1000;
// 提示信息显示时长
const messageDuration = 5 * 1000;

// 系统全局请求对象
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: requestTimeOut,
    responseType: 'json',
    validateStatus(status) {
        return status === success
    }
});

// 系统令牌刷新请求对象
const refresh_service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: requestTimeOut,
    responseType: 'json',
    validateStatus(status) {
        return status === success
    }
});

//请求拦截
service.interceptors.request.use(
    config => {
        let _config = config;
        try {
            const expireTime = getExpireTime();
            if (expireTime) {
                const left = expireTime - new Date().getTime();
                const refreshToken = getRefreshToken();
                if (left < checkRegion && refreshToken) {
                    _config = queryRefreshToken(_config, refreshToken)
                } else {
                    if (getToken()) {
                        _config.headers['Authorization'] = 'bearer ' + getToken()
                    }
                }
            }
        } catch (e) {
            console.error(e)
        }
        return _config
    },
    error => {
        console.log(error);
        return Promise.reject(error)
    }
);

//响应拦截
service.interceptors.response.use(response => {
    if (response.data !== undefined && response.data !== null && response.data !== '') {
        let status = response.data.status;
        if (status !== undefined && status === "ERROR") {
            Message({
                message: response.data.message,
                type: 'error',
                duration: 5 * 1000
            });
            throw new Error(response.data.message);
        }

        if (status !== undefined && status === 'WARN') {
            Message({
                message: response.data.message,
                type: 'warning',
                duration: 5 * 1000
            });
            throw new Error(response.data.message);
        }
    }
    return response.data;
}, (error) => {
    if (error.response) {

        if (error.toString().indexOf('Error: timeout') !== -1) {
            Message({
                message: '网络请求超时',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(error)
        }
        if (error.toString().indexOf('Error: Network Error') !== -1) {
            Message({
                message: '网络请求错误',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(error)
        }

        if (error.toString().indexOf('503') !== -1) {
            Message({
                message: '服务暂时不可用，请稍后再试!' || 'Error',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(error)
        }

        const errorMessage = error.response.data === null ? '系统内部异常，请联系网站管理员' : error.response.data.message;
        switch (error.response.status) {
            case 404:
                Message({
                    message: '很抱歉，资源未找到',
                    type: 'error',
                    duration: messageDuration
                });
                break;
            case 403:
                Message({
                    message: '很抱歉，您暂无该操作权限',
                    type: 'error',
                    duration: messageDuration
                });
                break;
            case 401:
                Message({
                    message: '很抱歉，您没有权限',
                    type: 'error',
                    duration: messageDuration
                });
                break;
            default:
                Message({
                    message: errorMessage,
                    type: 'error',
                    duration: messageDuration
                });
                break
        }
    }
    return Promise.reject(error)
});

const request = {
    refresh(url, params) {
        params['grant_type'] = 'refresh_token';
        return refresh_service.post(url, params, {
            transformRequest: [(params) => {
                return tansParams(params)
            }],
            headers: {
                'Authorization': authorizationValue
            }
        })
    },
    login(url, params) {
        params['grant_type'] = 'password';
        return service.post(url, params, {
            transformRequest: [(params) => {
                return tansParams(params)
            }],
            headers: {
                'Authorization': authorizationValue
            }
        })
    },
    page(url, params) {
        return service({
            url: url,
            method: 'post',
            data: params
        })
    },
    post(url, params) {
        return service.post(url, params, {
            transformRequest: [(params) => {
                return tansParams(params)
            }],
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    postData(url,params){
        return service({
            url: url,
            method: 'post',
            data: params
        })
    },
    putData(url,params){
        return service({
            url: url,
            method: 'put',
            data: params
        })
    },
    deleteData(url,params){
        return service({
            url: url,
            method: 'delete',
            data: params
        })
    },
    put(url, params) {
        return service.put(url, params, {
            transformRequest: [(params) => {
                return tansParams(params)
            }],
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    },
    get(url, params) {
        let _params;
        if (Object.is(params, undefined)) {
            _params = ''
        } else {
            _params = '?';
            for (const key in params) {
                if (params.hasOwnProperty(key) && params[key] !== null) {
                    _params += `${key}=${params[key]}&`
                }
            }
        }
        return service.get(`${url}${_params}`)
    },
    delete(url, params) {
        let _params;
        if (Object.is(params, undefined)) {
            _params = ''
        } else {
            _params = '?';
            for (const key in params) {
                if (params.hasOwnProperty(key) && params[key] !== null) {
                    _params += `${key}=${params[key]}&`
                }
            }
        }
        return service.delete(`${url}${_params}`)
    },
    download(url, params, filename) {
        NProgress.start();
        return service.post(url, params, {
            // transformRequest: [(params) => {
            //     return tansParams(params)
            // }],
            responseType: 'blob'
        }).then((response) => {
            //处理返回的文件流
            let blob = new Blob([response], {type: 'application/zip'});
            if (window.navigator.msSaveOrOpenBlob) {// 兼容IE10
                navigator.msSaveBlob(blob, filename);
            } else {// 其他非IE内核支持H5的浏览器
                let url = window.URL.createObjectURL(blob);
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = url;
                link.setAttribute('download', filename);
                document.body.appendChild(link);
                link.click()
            }
            NProgress.done()
        }).catch((r) => {
            console.error(r);
            NProgress.done();
            Message({
                message: '下载失败',
                type: 'error',
                duration: messageDuration
            })
        })
    },
    upload(url, params) {
        return service.post(url, params, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    }
};

function tansParams(params) {
    let result = '';
    Object.keys(params).forEach((key) => {
        if (!Object.is(params[key], undefined) && !Object.is(params[key], null)) {
            result += encodeURIComponent(key) + '=' + encodeURIComponent(params[key]) + '&'
        }
    });
    return result
}

async function queryRefreshToken(config, refreshToken) {
    const result = await request.refresh('auth/oauth/token', {
        refresh_token: refreshToken
    });
    if (result.status === success) {
        saveData(result.data);
        config.headers['Authorization'] = 'bearer ' + getToken()
    }
    return config
}

function saveData(data) {
    store.commit('account/setAccessToken', data.access_token);
    store.commit('account/setRefreshToken', data.refresh_token);
    const current = new Date();
    const expireTime = current.setTime(current.getTime() + 1000 * data.expires_in);
    store.commit('account/setExpireTime', expireTime)
}

export default request
