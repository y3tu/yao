import Vue from 'vue'
import Vuex from 'vuex'
import account from './modules/account'
import setting from './modules/setting'
import tagsView from './modules/tagsView'
import getters from './getters'


Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        account,
        setting,
        tagsView
    },
    getters
});

export default store
