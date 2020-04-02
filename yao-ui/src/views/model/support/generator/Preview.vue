<template>
    <el-tabs v-model="activeName" type="card">
        <el-tab-pane v-for="item in data" :key="item.name" :lazy="true" :label="item.name" :name="item.name">
            <Java :value="item.content" :height="height"/>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
    import Java from '@/components/JavaEdit/index'
    import {genPreview} from "./Api";

    export default {
        name: 'Preview',
        components: { Java },
        data() {
            return {
                data: null,
                height: '',
                activeName: 'Entity'
            }
        },
        created() {
            this.height = document.documentElement.clientHeight - 180 + 'px';
            let data = {
                tableName:this.$route.query.tableName,
                dataSourceConfigId:this.$route.query.dataSourceConfigId
            };
            genPreview(data).then(data => {
                this.data = data
            }).catch(() => {
                this.$router.go(-1)
            })
        }
    }
</script>