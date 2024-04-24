<template>
  <!-- 预定会议 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>会议预定</el-breadcrumb-item>
      <el-breadcrumb-item>预定会议</el-breadcrumb-item>
    </el-breadcrumb>

    <el-form :model="form" ref="form" :rules="rules" label-width="450px">
      <el-form-item label="会议名称" prop="meetingname">
        <el-input v-model="form.meetingname" style="width: 220px;"></el-input>
      </el-form-item>
      <el-form-item label="预计人数" prop="numberofparticipants">
        <el-input v-model="form.numberofparticipants" style="width: 220px;" disabled></el-input>
      </el-form-item>
      <el-form-item label="会议开始签到时间" prop="signinstarttime">
        <el-date-picker
            v-model="form.signinstarttime"
            type="datetime"
            placeholder="选择会议开始日期"
            value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议签到结束时间" prop="signinendtime">
        <el-date-picker
            v-model="form.signinendtime"
            type="datetime"
            placeholder="选择会议开始日期"
            value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议开始时间" prop="starttime">
        <el-date-picker
            v-model="form.starttime"
            type="datetime"
            placeholder="选择会议开始日期"
            value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议结束时间" prop="endtime">
        <el-date-picker
            v-model="form.endtime"
            type="datetime"
            placeholder="选择会议结束日期"
            value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议室名称" prop="roomid">
        <el-select v-model="form.roomid" placeholder="请选择会议室" @change="onInstitutionChange">
          <el-option v-for="meeting in meeting" :key="meeting.roomid" :label="meeting.roomname"
                     :value="meeting.roomid"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="会议说明" prop="description">
        <el-input type="textarea" v-model="form.description" style="width: 350px;"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')" class="btn">预定会议</el-button>
        <el-button type="danger" @click="reset('form')" class="btn">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "BookScheduled",
  data() {
    return {
      meeting: {},
      form: {},
      rules: {
        meetingname: [
          {required: true, message: '请输入会议名称', trigger: 'blur'}
        ],
        numberofparticipants: [
          {required: true, message: '请输入预计参会人数', trigger: 'blur'}
        ],
        signinstarttime: [
          {required: true, message: '请选择会议开始签到时间', trigger: 'blur'}
        ],
        signinendtime: [
          {required: true, message: '请选择会议签到结束时间', trigger: 'blur'}
        ],
        starttime: [
          {required: true, message: '请选择会议开始时间', trigger: 'blur'}
        ],
        endtime: [
          {required: true, message: '请选择会议结束时间', trigger: 'blur'}
        ],
        roomid: [
          {required: true, message: '请选择会议室', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入会议说明', trigger: 'blur'}
        ]
      },
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      numberofparticipants: ''
    }
  },
  created() {
    //请求会议室数据
    this.onInstitutionChange();
  },
  methods: {
    //预定会议请求
    onSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.request.post("/meeting/addMeeting/" + this.employee.employeeid, this.form).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$message({
                showClose: true,
                type: "success",
                message: "预定会议成功",
              })
              this.$router.push("/Scheduled")
            } else {
              this.$message({
                showClose: true,
                type: "error",
                message: "预定会议失败",
              })
            }
          })
        }
      })
    },
    //  重置
    reset(form) {
      this.$refs['form'].resetFields();
    },
    //将会议室可容纳人数赋值到预计人数中
    onInstitutionChange(obj) {
      this.request.post("/meetingroom/list").then(res => {
        this.meeting = res.data
        res.data.forEach(el => {
          if (el.roomid === obj) {
            this.form.numberofparticipants = el.capacity
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>