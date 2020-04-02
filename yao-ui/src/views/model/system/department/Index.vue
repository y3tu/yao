<template>
    <div class="Department">
        <el-row :gutter="10">
            <el-col :xs="24" :sm="12">
                <div class="app-container">
                    <div class="filter-container">
                        <el-input v-model="departmentName" :placeholder="$t('table.department.departmentName')" class="filter-item search-item"/>
                        <el-button class="filter-item" type="primary" plain @click="search">
                            {{ $t('table.search') }}
                        </el-button>
                        <el-button class="filter-item" type="warning" plain @click="reset">
                            {{ $t('table.reset') }}
                        </el-button>
                        <el-dropdown v-has-any-permission="['department:create','department:delete','department:export']" trigger="click" class="filter-item">
                            <el-button>
                                {{ $t('table.more') }}<i class="el-icon-arrow-down el-icon--right"/>
                            </el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item v-has-permission="['department:create']" @click.native="add">{{ $t('table.add') }}</el-dropdown-item>
                                <el-dropdown-item v-has-permission="['department:delete']" @click.native="deleteDepartment">{{ $t('table.delete') }}</el-dropdown-item>
                                <el-dropdown-item v-has-permission="['department:export']" @click.native="exportExcel">{{ $t('table.export') }}</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                    <el-tree
                            ref="departmentTree"
                            :props="treeProps"
                            :data="departmentTree"
                            :check-strictly="true"
                            show-checkbox
                            accordion
                            node-key="id"
                            highlight-current
                            :filter-node-method="filterNode"
                            @node-click="nodeClick"/>
                </div>
            </el-col>
            <el-col :xs="24" :sm="12">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>{{ department.departmentId === '' ? this.$t('common.add') : this.$t('common.edit') }}</span>
                    </div>
                    <div>
                        <el-form ref="form" :model="department" :rules="rules" label-position="right" label-width="100px">
                            <el-form-item :label="$t('table.department.parentId')" prop="parentId">
                                <tree-select
                                        v-model="department.parentId"
                                        :normalizer="treeSelectNormalizer"
                                        :multiple="false"
                                        :options="departmentTree"
                                        :clear-value-text="$t('common.clear')"
                                        placeholder=" "
                                        style="width:100%"/>
                            </el-form-item>
                            <el-form-item :label="$t('table.department.departmentName')" prop="departmentName">
                                <el-input v-model="department.departmentName"/>
                            </el-form-item>
                            <el-form-item :label="$t('table.department.orderNum')" prop="orderNum">
                                <el-input-number v-model="department.orderNum" :min="0" :max="100" @change="handleNumChange"/>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-card>
                <el-card class="box-card" style="margin-top: -2rem;">
                    <el-row>
                        <el-col :span="24" style="text-align: right">
                            <el-button type="primary" plain :loading="buttonLoading" @click="submit">{{ department.departmentId === '' ? this.$t('common.add') : this.$t('common.edit') }}
                            </el-button>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    import TreeSelect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    export default {
        name: 'DepartmentManager',
        components: {TreeSelect},
        data() {
            return {
                departmentName: '',
                buttonLoading: false,
                departmentTree: [],
                department: this.initDepartment(),
                treeProps: {
                    label: 'name',
                    children: 'children',
                },
                treeSelectNormalizer(node) {
                    if (node.children == null || node.children.length < 1) {
                        delete node.children
                    }
                    return {
                        label: node.name
                    }
                },
                rules: {
                    departmentName: [
                        {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                        {min: 3, max: 10, message: this.$t('rules.range3to10'), trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initDepartmentTree()
        },
        methods: {
            initDepartment() {
                return {
                    departmentId: '',
                    departmentName: '',
                    parentId: null,
                    orderNum: 0
                }
            },
            initDepartmentTree() {
                this.$get('upms/department/tree').then((r) => {
                    this.departmentTree = r.data
                })
            },
            exportExcel() {
            },
            handleNumChange(val) {
                this.department.orderNum = val
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1
            },
            nodeClick(node) {
                let data = node.data;
                this.department.parentId = node.parentId;
                if (this.department.parentId === '0') {
                    this.department.parentId = null
                }
                this.department.orderNum = data.orderNum;
                this.department.departmentName = data.departmentName;
                this.department.departmentId = data.departmentId;
                this.$refs.form.clearValidate()
            },
            add() {
                this.resetForm()
            },
            deleteDepartment() {
                const checked = this.$refs.departmentTree.getCheckedKeys();
                if (checked.length === 0) {
                    this.$message({
                        message: this.$t('tips.noNodeSelected'),
                        type: 'warning'
                    })
                } else {
                    this.$confirm(this.$t('tips.confirmDeleteNode'), this.$t('common.tips'), {
                        confirmButtonText: this.$t('common.confirm'),
                        cancelButtonText: this.$t('common.cancel'),
                        type: 'warning'
                    }).then(() => {
                        this.department.departmentIds = checked.join(',');
                        this.$delete(`upms/department/delByIds/${this.department.departmentIds}`).then(() => {
                            this.$message({
                                message: this.$t('tips.deleteSuccess'),
                                type: 'success'
                            });
                            this.reset()
                        })
                    }).catch(() => {
                        this.$refs.departmentTree.setCheckedKeys([])
                    })
                }
            },
            search() {
                this.$refs.departmentTree.filter(this.departmentName)
            },
            reset() {
                this.initDepartmentTree();
                this.departmentName = '';
                this.resetForm()
            },
            submit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.buttonLoading = true;
                        this.department.createTime = this.department.modifyTime = null;
                        if (this.department.departmentId) {
                            this.$put('upms/department/update', {...this.department}).then(() => {
                                this.buttonLoading = false;
                                this.$message({
                                    message: this.$t('tips.updateSuccess'),
                                    type: 'success'
                                });
                                this.reset()
                            }).catch(()=>{
                                this.buttonLoading = false;
                            });
                        } else {
                            this.$post('upms/department/create', {...this.department}).then(() => {
                                this.buttonLoading = false;
                                this.$message({
                                    message: this.$t('tips.createSuccess'),
                                    type: 'success'
                                });
                                this.reset()
                            }).catch(()=>{
                                this.buttonLoading = false;
                            });
                        }
                    } else {
                        return false
                    }
                })
            },
            resetForm() {
                this.$refs.form.clearValidate();
                this.$refs.form.resetFields();
                this.department = this.initDepartment()
            }
        }
    }
</script>
<style lang="scss" scoped>
    .Department {
        margin: 10px;

        .app-container {
            margin: 0 0 10px 0 !important;
        }
    }

    .el-card.is-always-shadow {
        box-shadow: none;
    }

    .el-card {
        border-radius: 0;
        border: none;

        .el-card__header {
            padding: 10px 20px !important;
            border-bottom: 1px solid #f1f1f1 !important;
        }
    }
</style>
<style lang="scss">
    .vue-treeselect__menu {
        max-height: 165px !important;
    }
</style>
