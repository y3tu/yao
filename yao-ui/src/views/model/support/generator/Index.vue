<template>
    <div class="app-container">
        <div class="filter-container">

            <el-autocomplete
                    clearable
                    v-model="dataSourceConfig"
                    :fetch-suggestions="queryDataSourceConfig"
                    placeholder="请选择数据源"
                    class="filter-item search-item"
                    @select="handleSelect"/>
            <el-input clearable v-model="tableName" placeholder="请输入表名" style="width:200px" class="filter-item search-item" @keyup.enter.native="search"/>
            <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="search">搜索</el-button>
        </div>

        <el-table
                border
                v-loading="pageLoading"
                :data="tableData"
                size="small"
                style="width: 100%;">
            <el-table-column label="序号" width="80" align="center">
                <template slot-scope="scope">
                    <div>{{ scope.$index + 1 }}</div>
                </template>
            </el-table-column>

            <el-table-column :show-overflow-tooltip="true" prop="name" label="表名"/>

            <el-table-column :show-overflow-tooltip="true" prop="remarks" label="备注">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="left">
                        <p>{{tableData[scope.$index].remarks}}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-input v-model="tableData[scope.$index].remarks" class="edit-input" style="width: 300px"/>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="280px" align="center">
                <template slot-scope="scope">
                    <el-row>
                        <el-col :span="4">
                            <el-button size="mini" style="margin-right: 2px" type="text">
                                <router-link :to="'/support/generator/preview?tableName=' + scope.row.name+'&dataSourceConfigId='+dataSourceConfigId">
                                    预览
                                </router-link>
                            </el-button>
                        </el-col>
                        <el-col :span="10">
                            <Table :name="scope.row.name" :remarks="scope.row.remarks" :data-source-config-id="dataSourceConfigId"/>
                        </el-col>
                        <el-col :span="10">
                            <Tree :name="scope.row.name" :remarks="scope.row.remarks" :data-source-config-id="dataSourceConfigId"/>
                        </el-col>
                    </el-row>
                </template>
            </el-table-column>

        </el-table>

        <!--分页组件-->
        <el-pagination
                :total="total"
                style="margin-top: 8px;"
                layout="total, prev, pager, next, sizes"
                :current-page="currentPage"
                :page-size="pageSize"
                @size-change="sizeChange"
                @current-change="pageChange">
        </el-pagination>

    </div>
</template>

<script>
    import pageMixins from '@/mixins/page'
    import {getTables, getGeneratorConfig, getDataSourceConfig} from './Api'
    import {Message} from "element-ui";
    import Config from './Config'
    import Table from "./Table";
    import Tree from "./Tree"

    export default {
        name: 'generatorMgr',
        mixins: [pageMixins],
        components: {Config, Table, Tree},
        data() {
            return {
                tableName: '',
                tableData: [],
                pageLoading: false,
                currentPage: 1, // 当前页码
                total: 20, // 总条数
                pageSize: 10, // 每页的数据条数
                dataSourceConfig: '',
                dataSourceConfigId: ''
            }

        },
        created() {
            this.$nextTick(() => {
                this.init();
            })
        },
        methods: {
            init() {
            },
            search() {
                if (this.$isEmpty(this.dataSourceConfigId)) {
                    Message({
                        message: '请选择数据源',
                        type: 'warning',
                        duration: 5 * 1000
                    });
                    return;
                }
                this.pageLoading = true;
                getTables({
                    "dataSourceConfigId": this.dataSourceConfigId
                }).then(res => {
                    this.tableData = res.data;
                    let list = this.tableData.filter((item, index) =>
                        item.name.includes(this.tableName)
                    );
                    this.list = list.filter((item, index) =>
                        index < this.currentPage * this.pageSize && index >= this.pageSize * (this.currentPage - 1)
                    );
                    this.total = list.length;
                    this.tableData = this.list;
                    this.pageLoading = false;
                }).catch(err => {
                    this.pageLoading = false;
                })
            },
            sizeChange(val) {
                this.currentPage = 1;
                this.pageSize = val;
                this.search();
            },
            pageChange(val) {
                this.currentPage = val;
                this.search();
            },
            async queryDataSourceConfig(queryString, cb) {
                await getDataSourceConfig().then(res => {
                    let data = res.data;
                    data.forEach(dataSourceConfig => {
                        dataSourceConfig.value = dataSourceConfig.name;
                    });
                    let result = data ? data.filter(this.createFilter(queryString)) : data;
                    // 调用 callback 返回建议列表的数据
                    clearTimeout(this.timeout);
                    this.timeout = setTimeout(() => {
                        cb(result);
                    }, 1000 * Math.random());

                });
            },
            createFilter(queryString) {
                return (dataSourceConfig) => {
                    return (dataSourceConfig.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
                };
            },
            handleSelect(val) {
                this.dataSourceConfigId = val.id;
            }
        }

    }
</script>

<style>

</style>