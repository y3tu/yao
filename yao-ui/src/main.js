import Vue from 'vue'

import 'normalize.css/normalize.css'
import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' //全局css

import App from './App'
import store from './store'
import router from './router'

import i18n from './lang' //国际化
import './icons' //图标
import './utils/error-log' //错误日志
import request from '@/utils/request'
import {isEmpty,isNotEmpty} from "./utils/my-validate"
// 数据字典
import dict from './components/Dict'
import * as filters from './filters'

import {hasPermission, hasNoPermission, hasAnyPermission} from './utils/permissionDirect'

const Plugins = [
    hasPermission,
    hasNoPermission,
    hasAnyPermission
];

Plugins.map((plugin) => {
    Vue.use(plugin)
});

Vue.use(dict);

Vue.use(Element, {
    i18n: (key, value) => i18n.t(key, value)
});

Vue.prototype.$post = request.post;
Vue.prototype.$get = request.get;
Vue.prototype.$put = request.put;
Vue.prototype.$delete = request.delete;
Vue.prototype.$page = request.page;
Vue.prototype.$download = request.download;
Vue.prototype.$upload = request.upload;
Vue.prototype.$login = request.login;
Vue.prototype.$isEmpty = isEmpty;
Vue.prototype.$isNotEmpty = isNotEmpty;

Vue.config.productionTip = false;

//注册filter
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
});

// 设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = false;

new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: h => h(App)
});
