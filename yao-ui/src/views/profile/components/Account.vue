<template>
    <el-form ref="form" :model="user" :rules="rules" label-position="right" label-width="80px" class="form">
        <el-form-item :label="$t('table.user.email')" prop="email">
            <el-input v-model="user.email"/>
        </el-form-item>
        <el-form-item :label="$t('table.user.mobile')" prop="mobile">
            <el-input v-model="user.mobile"/>
        </el-form-item>
        <el-form-item :label="$t('table.user.department')" prop="deptId">
            <treeselect
                    v-model="user.departmentId"
                    :normalizer="treeSelectNormalizer"
                    :multiple="false"
                    :options="department"
                    :clear-value-text="$t('common.clear')"
                    placeholder=" "
                    @select="onSelect"
            />
        </el-form-item>
        <el-form-item :label="$t('table.user.sex')" prop="sex">
            <el-select v-model="user.sex" value="" placeholder="">
                <el-option value="0" :label="$t('common.sex.male') "/>
                <el-option value="1" :label="$t('common.sex.female') "/>
                <el-option value="2" :label="$t('common.sex.secret') "/>
            </el-select>
        </el-form-item>
        <el-form-item :label="$t('table.user.desc')" prop="description">
            <el-input v-model="user.description" type="textarea" rows="3"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" plain :loading="buttonLoading" @click="submit">{{ $t('common.edit') }}</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {validMobile} from '@/utils/my-validate'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    export default {
        components: {Treeselect},
        props: {
            user: {
                type: Object,
                default: () => {
                    return {
                        name: '',
                        email: ''
                    }
                }
            }
        },
        data() {
            return {
                department: [],
                treeSelectNormalizer(node) {
                    if (node.children == null || node.children.length < 1) {
                        delete node.children
                    }
                    return {
                        label: node.name
                    }
                },
                buttonLoading: false,
                deptName: '',
                change: false,
                rules: {
                    email: {type: 'email', message: this.$t('rules.email'), trigger: 'blur'},
                    mobile: [
                        {required: true, message: this.$t('rules.require'), trigger: 'blur'},
                        {
                            validator: (rule, value, callback) => {
                                if (value !== '' && !validMobile(value)) {
                                    callback(this.$t('rules.mobile'))
                                }
                                if (!this.user.userId) {
                                    this.$get(`upms/user/checkMobile/${value}`).then((r) => {
                                        if (!r.data) {
                                            callback(this.$t('rules.mobileExist'))
                                        } else {
                                            callback()
                                        }
                                    })
                                } else {
                                    callback()
                                }
                            }, trigger: 'blur'
                        }],
                    sex: {required: true, message: this.$t('rules.require'), trigger: 'change'},
                    description: {max: 100, message: this.$t('rules.noMoreThan100'), trigger: 'blur'}
                }
            }
        },
        mounted() {
            this.initDepartment()
        },
        methods: {
            submit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.buttonLoading = true;
                        const temp = {...this.user};
                        temp.lastLoginTime = temp.modifyTime = temp.createTime = null;
                        this.$put('upms/user/profile', {...temp}).then(() => {
                            this.buttonLoading = false;
                            this.$message({
                                message: this.$t('tips.updateSuccess'),
                                type: 'success'
                            });
                            if (this.change) {
                                this.user.department = this.department
                            }
                            this.$store.commit('account/setUser', this.user)
                        }).catch(err => {
                            this.buttonLoading = false
                        })
                    } else {
                        return false
                    }
                })
            },
            onSelect(a, b) {
                this.departmentName = a.label;
                this.change = true
            },
            initDepartment() {
                this.$get('upms/department/tree').then((r) => {
                    this.department = r.data;
                }).catch((error) => {
                    console.error(error);
                    this.$message({
                        message: this.$t('tips.getDataFail'),
                        type: 'error'
                    })
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
    .form {
        padding: 10px 0 0 0;
    }
</style>
