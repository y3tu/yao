import Vue from 'vue'
import {getDataByDictName} from "@/views/model/support/dict/Api";

export default class Dict {
    constructor(dict) {
        this.dict=dict;
    }

    async init(names,completeCallback){
        if (names === undefined || name === null) {
            throw new Error('need Dict names')
        }
        const ps = [];
        names.forEach(name=>{
            Vue.set(this.dict.dict, name, {});
            Vue.set(this.dict.label, name, {});
            Vue.set(this.dict, name, []);

            ps.push(getDataByDictName(name).then(data=>{
                let dictDataArr = data.data;
                this.dict[name].splice(0,0,...dictDataArr);
                dictDataArr.forEach(dictData=>{
                    Vue.set(this.dict.dict[name],dictData.value,dictData);
                    Vue.set(this.dict.label[name], dictData.value, dictData.name)
                })
            }))
        });
        await Promise.all(ps);
        completeCallback()
    }
}