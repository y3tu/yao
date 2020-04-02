<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="page.entity.name" placeholder="数据源名称" class="filter-item search-item"
                      @keyup.enter.native="search"/>
            <el-button class="filter-item" type="primary" plain @click="search">
                {{ $t('table.search') }}
            </el-button>
            <el-button class="filter-item" type="warning" plain @click="reset">
                {{ $t('table.reset') }}
            </el-button>
            <el-dropdown v-has-any-permission="['user:create','user:delete','user:reset','user:export']" trigger="click" class="filter-item">
                <el-button>
                    {{ $t('table.more') }}<i class="el-icon-arrow-down el-icon--right"/>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-has-permission="['dataSourceConfig:create']" @click.native="create">{{ $t('table.add') }}</el-dropdown-item>
                    <el-dropdown-item v-has-permission="['dataSourceConfig:delete']" @click.native="batchDelete">{{ $t('table.delete') }}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>

        <el-table
                ref="table"
                :key="tableKey"
                v-loading="pageLoading"
                :data="page.records"
                style="width: 100%;"
                @selection-change="onSelectChange"
                @sort-change="sortChange">
            <el-table-column type="selection" align="center" width="30px"/>
            <el-table-column label="数据源名称" prop="name" align="center" min-width="120px">
                <template slot-scope="scope">
                    <span>{{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column label="驱动" :show-overflow-tooltip="true" align="center" min-width="150px">
                <template slot-scope="scope">
                    <span>{{ scope.row.driverClass }}</span>
                </template>
            </el-table-column>
            <el-table-column label="数据库类型" align="center" min-width="60px">
                <template slot-scope="scope">
                    <span>{{ scope.row.type }}</span>
                </template>
            </el-table-column>
            <el-table-column label="数据库连接url" :show-overflow-tooltip="true" align="center" min-width="100px">
                <template slot-scope="scope">
                    <span>{{ scope.row.jdbcUrl }}</span>
                </template>
            </el-table-column>
            <el-table-column label="登录用户名" align="center" min-width="100px">
                <template slot-scope="scope">
                    <span>{{ scope.row.username }}</span>
                </template>
            </el-table-column>
            <el-table-column label="登录密码" :show-overflow-tooltip="true" align="center" min-width="100px">
                <template slot-scope="scope">
                    <span>{{ scope.row.password }}</span>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" align="center" min-width="180px" sortable="custom">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="更新时间" prop="modifyTime" align="center" min-width="180px" sortable="custom">
                <template slot-scope="scope">
                    <span>{{ scope.row.modifyTime }}</span>
                </template>
            </el-table-column>
            <el-table-column :label="$t('table.operation')" align="center" min-width="150px" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <i v-hasPermission="['dataSourceConfig:test']" class="el-icon-connection table-operation" style="color: #87d068;" @click="test(row)"/>
                    <i v-hasPermission="['dataSourceConfig:update']" class="el-icon-setting table-operation" style="color: #2db7f5;" @click="edit(row)"/>
                    <i v-hasPermission="['dataSourceConfig:delete']" class="el-icon-delete table-operation" style="color: #f50;" @click="singleDelete(row)"/>
                    <el-link v-has-no-permission="['user:view','user:update','user:delete']" class="no-perm">
                        {{ $t('tips.noPermission') }}
                    </el-link>
                </template>
            </el-table-column>
        </el-table>

        <!--分页组件-->
        <el-pagination
                :total="page.total"
                :current-page="page.current"
                style="margin-top: 8px;"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="sizeChange"
                @current-change="pageChange"/>

        <edit
                ref="edit"
                :dialog-visible="dialog.isVisible"
                :title="dialog.title"
                @success="editSuccess"
                @close="editClose"/>
    </div>
</template>

<script>
    import pageMixins from '@/mixins/page'
    import Edit from './Edit'

    export default {
        name: 'dataSourceConfig',
        mixins: [pageMixins],
        components: {Edit},
        data() {
            return {
                tableKey: 0,
                dialog: {
                    isVisible: false,
                    title: ''
                },
                selection: []
            }
        },
        created() {
            this.$nextTick(() => {
                this.init()
            })
        },
        methods: {
            init() {
                this.findPage();
            },
            findPageInit() {
                this.pageUrl = 'support/common/dataSourceConfig/page';
                return true;
            },
            search() {
                this.findPage();
            },
            reset() {
                this.resetPage();
                this.search();
            },
            create() {
                this.dialog.title = this.$t('common.add');
                this.dialog.isVisible = true
            },
            singleDelete(row) {
                this.$refs.table.toggleRowSelection(row, true);
                this.batchDelete()
            },
            batchDelete() {
                if (!this.selection.length) {
                    this.$message({
                        message: this.$t('tips.noDataSelected'),
                        type: 'warning'
                    });
                    return
                }
                let contain = false;
                this.$confirm(this.$t('tips.confirmDelete'), this.$t('common.tips'), {
                    confirmButtonText: this.$t('common.confirm'),
                    cancelButtonText: this.$t('common.cancel'),
                    type: 'warning'
                }).then(() => {
                    const ids = [];
                    this.selection.forEach((data) => {
                        ids.push(data.id)
                    });
                    this.delete(ids)
                }).catch(() => {
                    this.clearSelections()
                })
            },
            delete(ids) {
                this.$delete(`support/common/dataSourceConfig/delete/${ids}`).then(() => {
                    this.$message({
                        message: this.$t('tips.deleteSuccess'),
                        type: 'success'
                    });
                    this.search()
                })
            },
            edit(row) {
                this.$refs.edit.setDataSourceConfig(row);
                this.dialog.title = this.$t('common.edit');
                this.dialog.isVisible = true
            },
            onSelectChange(selection) {
                this.selection = selection
            },
            editClose() {
                this.dialog.isVisible = false
            },
            editSuccess() {
                this.search()
            },
            //测试连接
            test(row) {
                this.$get('support/common/dataSourceConfig/testConnect/' + row.id).then(res => {
                    this.$message({
                        message: '连接成功！',
                        type: 'success'
                    });
                })
            }
        }

    }
</script>

<style>

</style>