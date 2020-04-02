<style lang="scss">
    @import "../styles/unlock.scss";
</style>

<template>
    <transition name="show-unlock">
        <div class="unlock-body-con" v-if="showUnlock" @keydown.enter="handleUnlock">
            <div @click="handleClickAvatar" class="unlock-avatar-con" :style="{marginLeft: avatarLeft}">
                <img class="unlock-avatar-img" :src="avatar">
                <div class="unlock-avatar-cover">
                    <span><i class="el-icon-unlock"></i></span>
                    <p>解锁</p>
                </div>
            </div>
            <div class="unlock-avatar-under-back" :style="{marginLeft: avatarLeft}"></div>
            <div class="unlock-input-con">
                <div class="unlock-input-overflow-con">
                    <div class="unlock-overflow-body" :style="{right: inputLeft}">
                        <input ref="inputEle" v-model="password" class="unlock-input" type="password" placeholder="密码同登录密码"/>
                        <button ref="unlockBtn"
                                @mousedown="unlockMousedown"
                                @mouseup="unlockMouseup"
                                @click="handleUnlock"
                                style="height: 48px"
                                class="unlock-btn">
                            <i class="el-icon-key"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="unlock-locking-tip-con">已锁定</div>
            <el-button type="warning" class="unlock-logout-btn" @click="logout">退出登录</el-button>
        </div>
    </transition>
</template>

<script>
    import db from '@/utils/localstorage'

    export default {
        name: "Unlock",
        data() {
            return {
                avatarLeft: "0px",
                inputLeft: "400px",
                password: "",
                check: null
            };
        },
        props: {
            showUnlock: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            avatar() {
                let avatar = this.$store.state.account.user.avatar;
                return require(`@/assets/avatar/${avatar}`)
            }
        },
        methods: {
            unlock() {
                this.avatarLeft = "0px";
                this.inputLeft = "400px";
                this.password = "";
                db.save("locking", "0");
                this.$emit("on-unlock");
            },
            logout() {
                db.clear();
                location.reload();
            },
            handleClickAvatar() {
                this.avatarLeft = "-180px";
                this.inputLeft = "0px";
                this.$refs.inputEle.focus();
            },
            handleUnlock() {
                if (this.password == "") {
                    this.$message({
                        message: '请输入密码！',
                        type: 'error'
                    });
                    return;
                }
                // 将用户输入的密码this.password与数据库用户密码对比
                let flag = false;
                this.$get('upms/user/checkPassword', {password: this.password}).then(
                    res => {
                        if (res.data) {
                            this.unlock();
                        } else {
                            this.$message({
                                message: '密码错误！',
                                type: 'error'
                            });
                        }
                    }
                );
            },
            unlockMousedown() {
                this.$refs.unlockBtn.className = "unlock-btn click-unlock-btn";
            },
            unlockMouseup() {
                this.$refs.unlockBtn.className = "unlock-btn";
            }
        }
    };
</script>
