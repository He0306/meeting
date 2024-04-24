<template>
  <div>
    <div style="width: 500px;height: 350px;margin: 150px auto" class="box">
      <div style="text-align: center;font-size: 30px;padding: 30px">用户登录</div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="账  号" prop="username">
          <el-input prefix-icon="el-icon-user" v-model="ruleForm.username" placeholder="请输入登录账号"></el-input>
        </el-form-item>
        <el-form-item label="密  码" prop="password">
          <el-input prefix-icon="el-icon-lock" type="password" show-password v-model="ruleForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <div class="register"><router-link to="/register">免费注册</router-link></div>
        <el-form-item>
          <el-button type="primary" @click="toFaceLogin" class="btnFace" plain>人脸识别登录</el-button>
          <el-button type="primary" :loading="loading" @click.native.prevent="submitForm('ruleForm')" class="btnLongin">
            <span v-if="!loading">登录</span>
            <span v-else>登录中...</span>
          </el-button>&nbsp;&nbsp;
          <el-button @click="resetForm('ruleForm')" class="btnReset" >重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      ruleForm: {
        username: 'admin',
        password: '123456',
      },
      loading: false,
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'change'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
      }
    };
  },
  mounted() {
    // 获取地理位置
    let geolocation = new BMapGL.Geolocation();
    geolocation.getCurrentPosition(function(r){
      if(this.getStatus() === BMAP_STATUS_SUCCESS){
        const province = r.address.province
        const city = r.address.city
        localStorage.setItem("location", JSON.stringify(province + city))
      }
    });
  },

  methods: {
    submitForm(ruleForm) {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.request.post("/employee/login", this.ruleForm).then(res => {
            if (res.code === 200) {
              localStorage.setItem("employee", JSON.stringify(res.data))  //把用户信息存在浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))
              localStorage.setItem("token", JSON.stringify(res.data.token))
              this.$notify.success({
                title:"登录成功",
                message:`欢迎回来~ 亲爱的${res.data.employeename}`
              });
              //设置当前用户的路由
              setRoutes()
              this.$router.push("/")
            } else {
              this.$message({
                showClose: true,
                message: res.msg,
                type: 'error',
                duration: 1000
              })
            }
            this.loading = false
          })
        }
      })
    },
    resetForm(ruleForm) {
      this.$refs['ruleForm'].resetFields();
    },
    toFaceLogin(){
      this.$router.push('/faceLogin')
    }
  }
}
</script>

<style scoped>
.box {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.demo-ruleForm {
  width: 450px;
}
.btnLongin {
  width: 100px;
  height: 35px;
  margin-top: 20px;
}
.btnFace {
  width: 100px;
  height: 35px;
  margin-top: 20px;
}
.btnReset {
  width: 100px;
  height: 35px;
  margin-top: 20px;
  margin-left: -2px;
}

.register {
  font-size: 10px;
  margin-left: 400px;
}

a {
  text-decoration: none;
  color: #cccccc;
}
.el-input__inner {
  border-radius: 25px;
}
</style>