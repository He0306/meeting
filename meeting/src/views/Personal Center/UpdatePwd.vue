<template>
  <el-card style="width: 1000px;height: 600px; margin:5px auto 0">
    <el-form label-width="100px" size="small" :model="form" :rules="rules" ref="pass" style="width: 500px;height: 500px;margin: 80px auto 0">
      <el-form-item>
        <el-tag type="warning" size="medium"><i class="el-icon-lock">注意:</i>请保护好您的隐私</el-tag>
      </el-form-item>
      <el-form-item label="原密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "UpdatePwd",
  data() {
    return {
      form: {},
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      rules: {
        password: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {min: 6, message: '长度不少于6位', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 6, message: '长度不少于6位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, message: '长度不少于6位', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.form.username = this.employee.username
  },
  methods: {
      save() {
        this.$refs['pass'].validate((valid) => {
          if (valid) {
            if (this.form.newPassword !== this.form.confirmPassword) {
              this.$message.error("2次输入的新密码不相同")
              return false
            }
            this.request.post("/employee/updatePass",this.form).then(res=>{
              if (res.code === 200){
                this.$message({
                  showClose: true,
                  message: '修改密码成功',
                  type: 'success',
                })
                this.$store.commit("logout")
              }else {
                this.$message({
                  showClose: true,
                  message: res.msg,
                  type: 'error',
                })
              }
            })
          }
        })
      }
  }
}
</script>

<style scoped>
.el-button {
  margin-left: 100px;
  width: 100px
}

</style>