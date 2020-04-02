<template>
    <div class="resource">
        <el-row :gutter="10">
            <el-col :xs="24" :sm="12">
                <div class="app-container">
                    <div class="filter-container">
                        <el-input v-model="resourceName" :placeholder="$t('table.resource.resourceName')" class="filter-item search-item"/>
                        <el-button class="filter-item" type="primary" plain @click="search">
                            {{ $t('table.search') }}
                        </el-button>
                        <el-button class="filter-item" type="warning" plain @click="reset">
                            {{ $t('table.reset') }}
                        </el-button>
                        <el-dropdown v-has-any-permission="['resource:create','resource:delete','resource:export']" trigger="click" class="filter-item">
                            <el-button>
                                {{ $t('table.more') }}<i class="el-icon-arrow-down el-icon--right"/>
                            </el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item v-has-permission="['resource:create']" @click.native="add">{{ $t('table.add') }}</el-dropdown-item>
                                <el-dropdown-item v-has-permission="['resource:delete']" @click.native="deleteResource">{{ $t('table.delete') }}</el-dropdown-item>
                                <el-dropdown-item v-has-permission="['resource:export']" @click.native="exportExcel">{{ $t('table.export') }}</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                    <el-tree
                            ref="resourceTree"
                            :props="treeProps"
                            :data="resourceTree"
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
                        <span>{{ resource.resourceId === '' ? this.$t('common.add') : this.$t('common.edit') }}</span>
                    </div>
                    <div>
                        <el-form :inline="true" ref="form" :model="resource" :rules="rules" label-position="right" label-width="100px">
                            <el-form-item :label="$t('table.resource.type')" prop="type">
                                <el-radio-group style="width: 300px" v-model="resource.type" :disabled="resource.resourceId !== ''">
                                    <el-radio-button :label="2">目录</el-radio-button>
                                    <el-radio-button :label="0">{{ $t('common.resource.menu') }}</el-radio-button>
                                    <el-radio-button :label="1">{{ $t('common.resource.button') }}</el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item :label="$t('table.resource.parentId')" prop="parentId">
                                <treeselect
                                        v-model="resource.parentId"
                                        :normalizer="treeSelectNormalizer"
                                        :multiple="false"
                                        :options="resourceTree"
                                        :clear-value-text="$t('common.clear')"
                                        placeholder=" "
                                        style="width:500px"/>
                            </el-form-item>
                            <el-form-item :label="$t('table.resource.resourceName')" prop="resourceName">
                                <el-input style="width: 500px" v-model="resource.resourceName"/>
                            </el-form-item>
                            <el-form-item v-show="resource.type === 0" label="外链菜单" prop="iframe">
                                <el-radio-group v-model="resource.iframe" size="mini">
                                    <el-radio-button :label="true">是</el-radio-button>
                                    <el-radio-button :label="false">否</el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item v-show="resource.type === 0" label="菜单缓存" prop="cache">
                                <el-radio-group v-model="resource.cache" size="mini">
                                    <el-radio-button :label="true">是</el-radio-button>
                                    <el-radio-button :label="false">否</el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item v-show="resource.type === 0" label="菜单隐藏" prop="hidden">
                                <el-radio-group v-model="resource.hidden" size="mini">
                                    <el-radio-button :label="true">是</el-radio-button>
                                    <el-radio-button :label="false">否</el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item v-show="resource.type !== 1" :label="$t('table.resource.icon')" prop="icon">
                                <el-input v-model="resource.icon" style="width: 500px">
                                    <el-button slot="append" icon="el-icon-brush" style="padding-left: 0;" @click="chooseIcons"/>
                                </el-input>
                            </el-form-item>
                            <el-form-item v-show="resource.type !== 1" :label="$t('table.resource.component')" prop="component">
                                <el-input v-model="resource.component"/>
                            </el-form-item>
                            <el-form-item v-show="resource.type === 0" label="组件名称" prop="componentName">
                                <el-input v-model="resource.componentName" placeholder="匹配组件内Name字段"/>
                            </el-form-item>
                            <el-form-item v-show="resource.type !== 1 " :label="$t('table.resource.path')" prop="path">
                                <el-input v-model="resource.path"/>
                            </el-form-item>
                            <el-form-item v-show="resource.type !== 2 " :label="$t('table.resource.permission')" prop="permission">
                                <el-input v-model="resource.permission"/>
                            </el-form-item>
                            <el-form-item v-show="resource.type !== 1" :label="$t('table.resource.orderNum')" prop="orderNum">
                                <el-input-number v-model="resource.orderNum" :min="0" :max="100" @change="handleNumChange"/>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-card>
                <el-card class="box-card" style="margin-top: -2rem;">
                    <el-row>
                        <el-col :span="24" style="text-align: right">
                            <el-button type="primary" plain :loading="buttonLoading" @click="submit">{{ resource.resourceId === '' ?
                                this.$t('common.add') :
                                this.$t('common.edit') }}
                            </el-button>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
        <Icons :dialog-visible="iconVisible"
               @close="iconVisible = false"
               @choose="chooseIcon"/>
    </div>
</template>
<script>
    import Icons from './Icons'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    export default {
        name: 'resourceManage',
        components: {Icons, Treeselect},
        data() {
            return {
                iconVisible: false,
                buttonLoading: false,
                selection: [],
                resourceTree: [],
                resourceName: '',
                resource: this.initResource(),
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
                    resourceName: [
                        {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                        {min: 2, max: 10, message: this.$t('rules.range2to10'), trigger: 'blur'}
                    ],
                    path: {max: 100, message: this.$t('rules.noMoreThan100'), trigger: 'blur'},
                    component: {max: 100, message: this.$t('rules.noMoreThan100'), trigger: 'blur'},
                    permission: {max: 50, message: this.$t('rules.noMoreThan50'), trigger: 'blur'}
                }
            }
        },
        mounted() {
            this.initResourceTree()
        },
        methods: {
            initResourceTree() {
                this.$get('upms/resource/getResourceTree').then((r) => {
                    this.resourceTree = r.data;
                })
            },
            initResource() {
                return {
                    resourceId: '',
                    resourceName: '',
                    parentId: null,
                    path: '',
                    component: '',
                    componentName: '',
                    permission: '',
                    type: 0,
                    orderNum: 0,
                    icon: '',
                    iframe: false,
                    cache: false,
                    hidden: false
                }
            },
            exportExcel() {
                this.$download('upms/resource/excel', {
                    resourceName: this.resourceName
                }, `resource_${new Date().getTime()}.xlsx`)
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1
            },
            nodeClick(data, node, v) {
                this.resource.parentId = data.parentId;
                if (this.resource.parentId === '-1') {
                    this.resource.parentId = null
                }
                data = data.data;
                this.resource.orderNum = data.orderNum;
                this.resource.type = data.type;
                this.resource.permission = data.permission;
                this.resource.path = data.path;
                this.resource.component = data.component;
                this.resource.componentName = data.componentName;
                this.resource.icon = data.icon;
                this.resource.resourceName = data.resourceName;
                this.resource.resourceId = data.resourceId;
                this.resource.iframe = data.iframe;
                this.resource.cache = data.cache;
                this.resource.hidden = data.hidden;
                this.$refs.form.clearValidate()
            },
            handleNumChange(val) {
                this.resource.orderNum = val
            },
            chooseIcons() {
                this.iconVisible = true
            },
            chooseIcon(icon) {
                this.resource.icon = icon;
                this.iconVisible = false
            },
            submit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.buttonLoading = true;
                        this.resource.createTime = this.resource.modifyTime = null;

                        if (this.resource.resourceId) {
                            this.$put('upms/resource/update', {...this.resource}).then(() => {
                                this.buttonLoading = false;
                                this.$message({
                                    message: this.$t('tips.updateSuccess'),
                                    type: 'success'
                                });
                                this.reset()
                            }).catch(() => {
                                this.buttonLoading = false;
                            });
                        } else {
                            this.$post('upms/resource/create', {...this.resource}).then(() => {
                                this.buttonLoading = false;
                                this.$message({
                                    message: this.$t('tips.createSuccess'),
                                    type: 'success'
                                });
                                this.reset()
                            }).catch(() => {
                                this.buttonLoading = false;
                            });
                        }
                    } else {
                        return false
                    }
                })
            },
            reset() {
                this.initResourceTree();
                this.resourceName = '';
                this.resetForm()
            },
            search() {
                this.$refs.resourceTree.filter(this.resourceName)
            },
            add() {
                this.resetForm()
            },
            deleteResource() {
                const checked = this.$refs.resourceTree.getCheckedKeys();
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
                        this.resource.resourceIds = checked.join(',');
                        this.$delete(`upms/resource/delete/${this.resource.resourceIds}`).then(() => {
                            this.$message({
                                message: this.$t('tips.deleteSuccess'),
                                type: 'success'
                            });
                            this.reset()
                        })
                    }).catch(() => {
                        this.$refs.resourceTree.setCheckedKeys([])
                    })
                }
            },
            resetForm() {
                this.$refs.form.clearValidate();
                this.$refs.form.resetFields();
                this.resource = this.initResource()
            }
        }
    }
</script>
<style lang="scss" scoped>
    .resource {
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
