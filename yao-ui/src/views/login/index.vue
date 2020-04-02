<template>

    <div class="login-container">
        <div class="login-info">
            <div class="title">Yao</div>
            <div class="sub-title">{{ $t('common.system') }}</div>
            <div class="desc">1. {{ $t('common.desc.a') }}</div>
            <div class="desc">2. {{ $t('common.desc.b') }}</div>
            <div class="desc">3. {{ $t('common.desc.c') }}</div>
            <div class="desc">4. {{ $t('common.desc.d') }}</div>
            <div class="desc">5. {{ $t('common.desc.e') }}</div>
        </div>

        <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-form" autocomplete="off" label-position="left">
            <div class="title-container">
                <h3 class="title">
                    {{ $t('login.title') }}
                </h3>
                <lang-select class="set-language"/>
            </div>

            <span v-if="login.type === 'password'">
                <el-form-item prop="username">
                      <el-input
                              ref="username"
                              v-model="loginForm.username"
                              :placeholder="$t('login.username')"
                              prefix-icon="el-icon-user"
                              name="username"
                              type="text"
                              autocomplete="off"
                              @keyup.enter.native="handleLogin"/>
                </el-form-item>
                <el-form-item prop="password">
                      <el-input
                              ref="password"
                              v-model="loginForm.password"
                              prefix-icon="el-icon-key"
                              type="password"
                              :placeholder="$t('login.password')"
                              name="password"
                              autocomplete="off"
                              :show-password="true"
                              @keyup.enter.native="handleLogin"/>
                </el-form-item>
                <el-form-item prop="code" class="code-input">
                      <el-input
                              ref="code"
                              v-model="loginForm.code"
                              prefix-icon="el-icon-lock"
                              :placeholder="$t('login.code')"
                              name="code"
                              type="text"
                              autocomplete="off"
                              style="width: 70%"
                              @keyup.enter.native="handleLogin"/>
                </el-form-item>
                <img :src="imageCode" alt="codeImage" class="code-image" @click="getCodeImage">
                <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;" @click.native.prevent="handleLogin">
                  {{ $t('login.logIn') }}
                </el-button>
            </span>

            <span v-if="login.type==='mobile'">
                <el-form-item prop="mobile">
                     <el-input
                             ref="mobile"
                             v-model="loginForm.mobile"
                             :placeholder="$t('login.mobile')"
                             prefix-icon="el-icon-mobile-phone"
                             name="mobile"
                             type="text"
                             autocomplete="off"
                             @keyup.enter.native="handleMobileLogin"/>
                </el-form-item>
                <el-form-item prop="mobileCode">
                    <el-row :gutter="8">
                        <el-col :span="9">
                             <el-input
                                     ref="mobileCode"
                                     v-model="loginForm.mobileCode"
                                     prefix-icon="el-icon-lock"
                                     :placeholder="$t('login.mobileCode')"
                                     name="mobileCode"
                                     type="text"
                                     autocomplete="off"
                                     @keyup.enter.native="handleMobileLogin"/>
                        </el-col>
                        <el-col :span="15">
                             <el-button ref="smsButton" @click="sendMobileCode" style="width:100%" :disabled="sms.isDisabled">{{sms.btnName}}</el-button>
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;" @click.native.prevent="handleMobileLogin">
                  {{ $t('login.logIn') }}
                </el-button>
            </span>

            <span v-if="login.type === 'social'">
                {{ $t('login.chooseToSignIn') }}
                <div>
                  <template v-for="(l, index) in logo">
                    <div :key="index" class="logo-wrapper">
                      <img :src="resolveLogo(l.img)" alt="" :class="{ 'radius': l.radius }" @click="socialLogin(l.name)">
                    </div>
                  </template>
                </div>
            </span>

            <span v-if="login.type === 'bind'" style="margin-top: -1rem">
                 <el-tabs v-model="tabActiveName" @tab-click="handleTabClick">
                    <el-tab-pane :label="$t('common.bindLogin')" name="bindLogin">
                        <el-form-item prop="bindUsername">
                          <el-input
                                  ref="bindUsername"
                                  v-model="loginForm.bindUsername"
                                  :placeholder="$t('login.username')"
                                  prefix-icon="el-icon-user"
                                  name="bindUsername"
                                  type="text"
                                  autocomplete="off"/>
                        </el-form-item>
                        <el-form-item prop="bindPassword">
                          <el-input
                                  ref="bindPassword"
                                  v-model="loginForm.bindPassword"
                                  :placeholder="$t('login.password')"
                                  prefix-icon="el-icon-key"
                                  name="bindPassword"
                                  type="password"
                                  :show-password="true"
                                  autocomplete="off"/>
                        </el-form-item>
                        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;" @click.native.prevent="bindLogin">
                          {{ $t('common.bindLogin') }}
                        </el-button>
                    </el-tab-pane>
                      <el-tab-pane :label="$t('common.signLogin')" name="signLogin">
                        <el-form-item prop="signUsername">
                          <el-input
                                  ref="signUsername"
                                  v-model="loginForm.signUsername"
                                  :placeholder="$t('login.username')"
                                  prefix-icon="el-icon-user"
                                  name="signUsername"
                                  type="text"
                                  autocomplete="off"/>
                        </el-form-item>
                        <el-form-item prop="signPassword">
                          <el-input
                                  ref="signPassword"
                                  v-model="loginForm.signPassword"
                                  :placeholder="$t('login.password')"
                                  prefix-icon="el-icon-key"
                                  name="signPassword"
                                  type="password"
                                  :show-password="true"
                                  autocomplete="off"/>
                        </el-form-item>
                        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;" @click.native.prevent="signLogin">
                          {{ $t('common.signLogin') }}
                        </el-button>
                      </el-tab-pane>
                 </el-tabs>
            </span>

            <el-dropdown class="login-type" placement="top-end">
                <span class="el-dropdown-link">
                  <el-link type="primary">{{ $t('login.otherLoginType') }}</el-link>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :disabled="login.type === 'password'" @click.native="login.type = 'password'">{{ $t('login.type.password') }}
                    </el-dropdown-item>
                    <el-dropdown-item :disabled="login.type === 'mobile'" @click.native="login.type = 'mobile'">{{ $t('login.type.mobile') }}
                    </el-dropdown-item>
                    <el-dropdown-item :disabled="login.type === 'social'" @click.native="login.type = 'social'">{{ $t('login.type.social') }}
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>

        </el-form>

        <span class="login-footer">© 2020 <a target="_blank" href="http://www.y3tu.com">Y3tu</a> - Yao</span>

    </div>

</template>

<script>
    import LangSelect from '@/components/LangSelect'
    import {socialLoginUrl} from '@/settings'
    import {randomNum} from '@/utils'
    import db from '@/utils/localstorage'
    import axios from 'axios'

    export default {
        name: 'Login',
        components: {LangSelect},
        data() {
            return {
                codeUrl: `${process.env.VUE_APP_BASE_API}auth/captcha`,
                socialLoginUrl: socialLoginUrl,
                authUser: null,
                loading: false,
                imageCode: '',
                tabActiveName: 'bindLogin',
                showDialog: false,
                redirect: undefined,
                otherQuery: {},
                randomId: randomNum(24, 16),
                page: {
                    width: window.screen.width * 0.5,
                    height: window.screen.height * 0.5
                },
                login: {
                    type: 'password'
                },
                logo: [
                    {img: 'gitee.png', name: 'gitee', radius: true},
                    {img: 'github.png', name: 'github', radius: true},
                    {img: 'tencent_cloud.png', name: 'tencent_cloud', radius: true},
                    {img: 'qq.png', name: 'qq', radius: false},
                    {img: 'dingtalk.png', name: 'dingtalk', radius: true},
                    {img: 'microsoft.png', name: 'microsoft', radius: false}
                ],
                sms: {
                    btnName: this.$t('login.smsButton'),
                    isDisabled: false,
                    time: 60
                },
                loginForm: {
                    username: 'guest',
                    password: '1234qwer',
                    mobile: '',
                    mobileCode: '',
                    bindUsername: '',
                    bindPassword: '',
                    signUsername: '',
                    signPassword: ''
                },
                rules: {
                    username: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    password: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    mobile: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    mobileCode: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    code: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    bindUsername: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    bindPassword: {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                    signUsername: [
                        {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                        {min: 4, max: 10, message: this.$t('rules.range4to10'), trigger: 'blur'}
                    ],
                    signPassword: [
                        {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                        {min: 6, max: 20, message: this.$t('rules.range6to20'), trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            db.clear();
            this.getCodeImage();
        },
        methods: {
            //用户密码登录
            handleLogin() {
                let username_c = false;
                let password_c = false;
                let code_c = false;
                this.$refs.loginForm.validateField('username', e => {
                    if (!e) {
                        username_c = true
                    }
                });
                this.$refs.loginForm.validateField('password', e => {
                    if (!e) {
                        password_c = true
                    }
                });
                this.$refs.loginForm.validateField('code', e => {
                    if (!e) {
                        code_c = true
                    }
                });
                if (username_c && password_c && code_c) {
                    this.loading = true;
                    const that = this;
                    this.$login('auth/oauth/token', {
                        ...that.loginForm,
                        key: this.randomId
                    }).then((res) => {
                        this.saveLoginData(res);
                        this.getUserDetailInfo();
                        this.loginSuccessCallback()
                    }).catch((error) => {
                        console.error(error);
                        that.loading = false;
                        that.getCodeImage()
                    })
                }
            },
            //手机验证码登录
            handleMobileLogin() {
                let mobile_c = false;
                let mobileCode_c = false;
                this.$refs.loginForm.validateField('mobile', e => {
                    if (!e) {
                        mobile_c = true
                    }
                });
                this.$refs.loginForm.validateField('mobileCode', e => {
                    if (!e) {
                        mobileCode_c = true
                    }
                });
                if (mobile_c && mobileCode_c) {
                    this.loading = true;
                    const that = this;
                    this.$login('auth/mobile/token', {
                        ...that.loginForm,
                        key: this.randomId
                    }).then((res) => {
                        this.saveLoginData(res);
                        this.getUserDetailInfo();
                        this.loginSuccessCallback()
                    }).catch((error) => {
                        console.error(error);
                        that.loading = false;
                        that.getCodeImage()
                    })
                }
            },
            //发送手机验证码
            sendMobileCode() {
                let that = this;
                that.$refs.loginForm.validateField('mobile', (error) => {
                    if (!error) {
                        that.$get('auth/mobile/' + that.loginForm.mobile).then((res) => {
                            if (res.status === 'SUCCESS') {
                                this.$notify({
                                    title: '验证码发送成功',
                                    message: '验证码为:' + res.data,
                                    type: 'success',
                                    duration: 6000
                                });
                                doInterval()
                            }
                        });
                    }
                });

                function doInterval() {
                    that.sms.isDisabled = true;
                    let interval = window.setInterval(function () {
                        that.sms.btnName = '(' + that.sms.time + that.$t('login.smsTimeCount');
                        --that.sms.time;
                        if (that.sms.time < 0) {
                            that.sms.btnName = that.$t('login.smsAgainButton');
                            that.sms.time = 60;
                            that.sms.isDisabled = false;
                            window.clearInterval(interval)
                        }
                    }, 1000)
                }
            },
            //获取图形验证码
            getCodeImage() {
                axios({
                    method: 'GET',
                    url: `${this.codeUrl}?key=${this.randomId}`,
                    responseType: 'arraybuffer'
                }).then(res => {
                    return 'data:image/png;base64,' + btoa(
                        new Uint8Array(res.data)
                            .reduce((data, byte) => data + String.fromCharCode(byte), '')
                    )
                }).then((res) => {
                    this.imageCode = res
                }).catch((e) => {
                    if (e.toString().indexOf('429') !== -1) {
                        this.$message({
                            message: this.$t('tips.tooManyRequest'),
                            type: 'error'
                        })
                    } else {
                        this.$message({
                            message: this.$t('tips.getCodeImageFailed'),
                            type: 'error'
                        })
                    }
                })
            },
            //切换tab
            handleTabClick(tab, event) {
                this.tabActiveName = tab.name
            },
            //第三方登录
            socialLogin(oauthType) {
                const url = `${this.socialLoginUrl}/${oauthType}/login`;
                window.open(url, 'newWindow', `resizable=yes, height=${this.page.height}, width=${this.page.width}, top=10%, left=10%, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no`);
                window.addEventListener('message', this.resolveSocialLogin, false)
            },
            resolveSocialLogin(e){
                const data = e.data;
                const that = this;
                if (data.message === 'not_bind') {
                    that.login.type = 'bind';
                    const authUser = data.data;
                    that.authUser = authUser;
                    that.$confirm(that.$t('common.current') + authUser.source + that.$t('common.socialAccount') + authUser.nickname + that.$t('common.socialTips'), that.$t('common.tips'), {
                        confirmButtonText: that.$t('common.signLogin'),
                        cancelButtonText: that.$t('common.bindLogin'),
                        type: 'warning'
                    }).then(() => {
                        that.tabActiveName = 'signLogin'
                    }).catch(() => {
                        that.tabActiveName = 'bindLogin'
                    })
                } else if (data.message === 'social_login_success') {
                    that.saveLoginData(data.data.token);
                    that.getUserDetailInfo();
                    that.loginSuccessCallback()
                } else {
                    // do nothing
                }
            },
            bindLogin() {
                let username_c = false;
                let password_c = false;
                this.$refs.loginForm.validateField('bindUsername', e => { if (!e) { username_c = true } });
                this.$refs.loginForm.validateField('bindPassword', e => { if (!e) { password_c = true } });
                if (username_c && password_c) {
                    this.loading = true;
                    const that = this;
                    const params = {
                        bindUsername: that.loginForm.bindUsername,
                        bindPassword: that.loginForm.bindPassword,
                        ...that.authUser
                    };
                    params.token = null;
                    that.$post('auth/social/bind/login', params).then((r) => {
                        const data = r.data;
                        this.saveLoginData(data);
                        this.getUserDetailInfo();
                        this.loginSuccessCallback()
                    }).catch((error) => {
                        console.error(error);
                        that.loading = false
                    })
                }
            },
            signLogin() {
                let username_c = false;
                let password_c = false;
                this.$refs.loginForm.validateField('signUsername', e => { if (!e) { username_c = true } });
                this.$refs.loginForm.validateField('signPassword', e => { if (!e) { password_c = true } });
                if (username_c && password_c) {
                    this.loading = true;
                    const that = this;
                    const params = {
                        bindUsername: that.loginForm.signUsername,
                        bindPassword: that.loginForm.signPassword,
                        ...that.authUser
                    };
                    params.token = null;
                    that.$post('auth/social/sign/login', params).then((r) => {
                        const data = r.data;
                        this.saveLoginData(data);
                        this.getUserDetailInfo();
                        this.loginSuccessCallback()
                    }).catch((error) => {
                        console.error(error);
                        that.loading = false
                    })
                }
            },
            resolveLogo(logo) {
                return require(`@/assets/logo/${logo}`)
            },
            saveLoginData(data) {
                this.$store.commit('account/setAccessToken', data.access_token);
                this.$store.commit('account/setRefreshToken', data.refresh_token);
                const current = new Date();
                const expireTime = current.setTime(current.getTime() + 1000 * data.expires_in);
                this.$store.commit('account/setExpireTime', expireTime)
            },
            getUserDetailInfo() {
                this.$get('auth/user').then((r) => {
                    this.$store.commit('account/setUser', r.principal);
                    this.$message({
                        message: this.$t('tips.loginSuccess'),
                        type: 'success'
                    });
                    this.loading = false;
                    this.$router.push('/')
                }).catch((error) => {
                    this.$message({
                        message: this.$t('tips.loginFail'),
                        type: 'error'
                    });
                    console.error(error);
                    this.loading = false
                })
            },
            //登录成功更新登录时间
            loginSuccessCallback() {
                this.$get('upms/user/success').catch((e) => {
                    console.log(e)
                })
            }

        }
    }
</script>

<style lang="scss">
    @import "login";
</style>
<style lang="scss" scoped>
    @import "login-scoped";
</style>

