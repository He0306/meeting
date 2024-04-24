<template>
    <div style="line-height: 60px; display: flex">
        <div style="flex: 1;font-size: 20px">
            <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
            <span style="font-size: 18px;margin-left: 15px;">当前时间：{{nowDate}}</span>
        </div>
        <template>
            <div id="full-screen" style="margin-right: 20px;cursor:pointer;">
                <i class="el-icon-full-screen" @click="fullScreen()" >全屏</i>
            </div>
        </template>
        <el-dropdown style="width: 100px; cursor: pointer" trigger="click">
  <b class="el-dropdown-link">
    {{ employee.employeename }}<i class="el-icon-setting" style="margin-left: 8px"></i>
  </b>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <span @click="person">个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <span @click="updatePass">修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <span @click="logout">注销</span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    import screenfull from 'screenfull'
    import dayjs from 'dayjs' //时间格式转换插件
    export default {
        name: "Header",
        props: {
            collapseBtnClass: String,
            employee: Object
        },
        data() {
            return {
                nowDate: '',
            }
        },
        computed: {
            currentPathName() {
                return this.$store.state.currentPathName;　　//需要监听的数据
            }
        },
        created() {
            setInterval(() => {
                this.nowDate = dayjs().format('YYYY-MM-DD HH:mm:ss')
            }, 10)
        },
        methods: {
            collapse() {
                this.$emit("asideCollapse")
            },
            logout() {
              this.$confirm('确定要退出登录吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.$store.commit("logout")
              })
            },
            updatePass() {
                this.$router.push('/updatePwd')
            },
            person() {
                this.$router.push('/person')
            },
            fullScreen () {
                if (!screenfull.isEnabled) {
                    this.$message({
                        showClose: true,
                        message: 'you browser can not work',
                        type: 'warning'
                    })
                    return false
                }
                screenfull.toggle()
            },
        }
    }
</script>

<style scoped>

</style>