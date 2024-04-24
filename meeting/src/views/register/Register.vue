<template>
  <div>
    <div style="width: 520px;height: 450px;margin: 130px auto" class="box">
      <div style="text-align: center;font-size: 30px;padding: 30px">用户注册</div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="姓  名" prop="employeename">
          <el-input v-model="ruleForm.employeename" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="账  号" prop="username">
          <el-input v-model="ruleForm.username" placeholder="请输入登录账号"></el-input>
        </el-form-item>
        <el-form-item label="部  门" prop="departmentid">
          <el-select v-model="ruleForm.departmentid" placeholder="请选择部门">
            <el-option v-for="dep in department" :key="dep.departmentid" :label="dep.departmentname" :value="dep.departmentid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密  码" prop="password">
          <el-input type="password" v-model="ruleForm.password" placeholder="请输入登录密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirm">
          <el-input type="password" v-model="ruleForm.confirm" placeholder="请确认密码"></el-input>
        </el-form-item>
        <div class="login"><router-link to="/">已有账号？去登录</router-link></div>
        <el-form-item>
          <el-button type="primary" @click="register" class="btn1">注册</el-button>&nbsp;&nbsp;
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    let validateUserName = (rule,value,callback)=>{
      if(!value){
        callback(new Error("真实姓名不能为空"))
      }else{
        if(value!==""){
          const realnameReg = /^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,5})$/;
          if(!realnameReg.test(value)){
            callback(new Error('您的真实姓名格式错误,请输入1—5个英文或汉字!'));
          }
        }
        callback();
      }
    }
    return {
      ruleForm: {
        username: '',
        password: '',
        confirm: '',
        departmentid: '',
      },
      department: [],
      rules: {
        employeename: [
          {required: true, validator:validateUserName, trigger: 'blur'},
        ],
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        departmentid: [
          {required: true, message: '请选择部门', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
        confirm: [
          {required: true, message: '请在次输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
      }
    };
  },
  created() {
    this.request.get("/department/all").then(res=>{
      this.department = res.data
    })
  },
  methods: {
    register() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.ruleForm.password !== this.ruleForm.confirm) {
            this.$message({
              showClose: true,
              message: '两次密码不一致',
              type: "error",
            })
            return false
          }
          this.request.post("/employee/register" ,this.ruleForm).then(res=>{
            if (res.code === 200){
              this.$message({
                showClose: true,
                message: "恭喜，注册成功",
                type: "success"
              })
              this.$router.push("/login")
            }else {
              this.$message({
                showClose: true,
                message: res.msg,
                type: "error"
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
.box {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.demo-ruleForm {
  width: 450px;
}

.btn1 {
  width: 200px;
  height: 40px;
  margin-left: 50px;
  margin-top: 10px;
}

a {
  text-decoration: none;
  color: #cccccc;
  font-size: 12px;
  margin-left: 350px;
}
</style>