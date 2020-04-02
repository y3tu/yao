<template>
    <div>
        <el-button type="primary" size="mini" @click="to">简单表格生成</el-button>

        <el-dialog :visible.sync="dialog" :fullscreen="isFullScreen" title="简单表格生成配置" append-to-body width="70%">

            <div class="filter-container">
                <el-select v-model="formLineNum" placeholder="表单排列">
                    <el-option label="一行一列" value="1"/>
                    <el-option label="一行两列" value="2"/>
                    <el-option label="一行三列" value="3"/>
                    <el-option label="一行四列" value="4"/>
                </el-select>
            </div>

            <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
                <el-table-column label="序号" width="80" align="center">
                    <template slot-scope="scope">
                        <div>{{ scope.$index + 1 }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="字段名称" width="150"/>
                <el-table-column prop="typeName" label="字段类型"/>
                <el-table-column prop="comment" label="字段标题" width="150">
                    <template slot-scope="scope">
                        <el-popover trigger="hover" placement="left">
                            <p>{{data[scope.$index].comment}}</p>
                            <div slot="reference" class="name-wrapper">
                                <el-input v-model="data[scope.$index].comment" class="edit-input" style="width: 200px"/>
                            </div>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="enableShow" label="表格中显示">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.enableShow === true ?'显示':'不显示'" placement="top">
                            <el-switch v-model="data[scope.$index].enableShow"/>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="enableEdit" label="可添加编辑">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.enableEdit === true ?'可添加编辑':'不可添加编辑'" placement="top">
                            <el-switch v-model="data[scope.$index].enableEdit"/>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="表单类型" width="140">
                    <template slot-scope="scope">
                        <el-select v-model="data[scope.$index].formType" :disabled="!data[scope.$index].enableEdit" class="edit-input" clearable placeholder="请选择">
                            <el-option label="文本输入框" value="1"/>
                            <el-option label="选择下拉框" value="2"/>
                            <el-option label="日期选择器" value="3"/>
                            <el-option label="时间选择器" value="4"/>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="enableValidate" label="表单非空验证" width="95">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.enableValidate === true ?'验证':'不验证'" placement="top">
                            <el-switch v-model="data[scope.$index].enableValidate" :disabled="!data[scope.$index].enableEdit"/>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="enableSearch" label="可搜索" width="95">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.enableSearch=== true ?'可搜索':'不可搜索'" placement="top">
                            <el-switch v-model="data[scope.$index].enableSearch"/>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="搜索框类型" width="140">
                    <template slot-scope="scope">
                        <el-select v-model="data[scope.$index].searchType" :disabled="!data[scope.$index].enableSearch" class="edit-input" clearable placeholder="请选择">
                            <el-option label="文本输入框" value="1"/>
                            <el-option label="选择下拉框" value="2"/>
                            <el-option label="日期选择器" value="3"/>
                            <el-option label="时间选择器" value="4"/>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="enableSort" label="可排序" width="95">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.enableSort=== true ?'可排序':'不可排序'" placement="top">
                            <el-switch v-model="data[scope.$index].enableSort"/>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column align="center" fixed="right" label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer">
                <el-button icon="el-icon-full-screen" @click="toggleFull">全屏开/关</el-button>
                <el-button @click="cancel">取消</el-button>
                <el-button :loading="genLoading" type="primary" @click="doSubmit">生成</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import {getTableColumn, build} from './Api'

    export default {
        name: 'generatorColumn',
        props: {
            name: {
                type: String,
                required: true
            },
            remarks: {
                type: String,
                required: true
            },
            dataSourceConfigId: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                dialog: false,
                loading: false,
                genLoading: false,
                data: [],
                formLineNum: "",
                isFullScreen: false,
            }
        },
        methods: {
            to() {
                this.dialog = true;
                this.time = 130;
                this.$nextTick(() => {
                    this.initData();
                })
            },
            initData() {
                const _this = this;
                //初始化数据
                _this.data = [];
                _this.loading = true;
                let param = {
                    tableName: _this.name,
                    dataSourceConfigId: _this.dataSourceConfigId
                };
                getTableColumn(param).then(res => {
                    let table = res.data;
                    _this.data.push(...table.columns);
                    _this.loading = false;
                }).catch(err => {
                    _this.dialog = false;
                    _this.loading = false;
                })
            },
            toggleFull() {
                this.isFullScreen = this.isFullScreen !== true;
            },
            cancel() {
                this.dialog = false
            },
            handleDelete(index) {
                this.data.splice(index, 1);
            },
            doSubmit() {
                this.genLoading = true;

                let param = {
                    formLineNum: this.formLineNum,
                    tableName: this.name,
                    remarks: this.remarks,
                    dataSourceConfigId: this.dataSourceConfigId,
                    columns: this.data
                };
                build(param).then(res => {
                    this.dialog = false;
                    this.genLoading = false
                }).catch(err => {
                    this.genLoading = false;
                    console.log(err.response.data.message)
                })
            }
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss">
    .edit-input {

        .el-input__inner {
            border: none;
        }
    }
</style>