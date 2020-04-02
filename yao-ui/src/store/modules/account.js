import db from '@/utils/localstorage'

export default {
    namespaced: true,
    state: {
        accessToken: db.get('ACCESS_TOKEN'),//token
        refreshToken: db.get('REFRESH_TOKEN'),//刷新token
        expireTime: db.get('EXPIRE_TIME', 0),//token失效时间
        user: db.get('USER'),//当前登录用户信息
        permissions: db.get('PERMISSIONS'),//用户权限
        routes: []//用户菜单路由
    },
    mutations: {
        setAccessToken(state, val) {
            db.save('ACCESS_TOKEN', val);
            state.accessToken = val
        },
        setRefreshToken(state, val) {
            db.save('REFRESH_TOKEN', val);
            state.refreshToken = val
        },
        setExpireTime(state, val) {
            db.save('EXPIRE_TIME', val);
            state.expireTime = val
        },
        setUser(state, val) {
            db.save('USER', val);
            state.user = val
        },
        setPermissions(state, val) {
            db.save('PERMISSIONS', val);
            state.permissions = val
        },
        setRoutes(state, val) {
            state.routes = val
        }
    }
}
