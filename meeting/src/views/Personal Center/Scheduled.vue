<template>
  <!-- 我的预定 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      <el-breadcrumb-item>我的预定</el-breadcrumb-item>
    </el-breadcrumb>

    <!--  搜索框、搜索按钮、重置按钮  -->
    <div style="padding: 10px 0">
      <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入会议名称" v-model="meetingname"
                clearable></el-input>
      <el-button style="margin-left: 3px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 3px" type="warning" @click="reset">重置</el-button>
    </div>

    <!--  主体数据表格  -->
    <el-table :data="tableData" v-loading="loadingData" border stripe :header-cell-class-name="headerBg">
      <el-table-column prop="meetingname" label="会议名称" width="110px"></el-table-column>
      <el-table-column prop="roomname" label="会议室名称" width="130px"></el-table-column>
      <el-table-column prop="starttime" label="会议开始时间" sortable></el-table-column>
      <el-table-column prop="endtime" label="会议结束时间" sortable></el-table-column>
      <el-table-column prop="reservationtime" label="会议预定时间"></el-table-column>
      <el-table-column fixed="right" label="操作" width="480">
        <template slot-scope="scope">
          <el-button type="danger" size="small" @click="cancelmeeting(scope.row.meetingid)">撤销会议<i
              class="el-icon-circle-close"></i></el-button>
          <el-button type="primary" size="small" @click="participants(scope.row.meetingid)">查看考勤信息<i
              class="el-icon-view"></i></el-button>
          <el-button type="success" size="small" @click="addMeeting(scope.row.meetingid)">添加参会员工<i
              class="el-icon-circle-plus-outline"></i></el-button>
          <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--  查看考勤信息的弹窗  -->
    <el-dialog title="查看考勤信息" :visible.sync="dialogVisible" fullscreen center>
      <div style="margin-bottom: 10px">
        参加会议总人数：{{totalNumber}} 人<el-divider direction="vertical"></el-divider>
        已签到人数：{{totalNumber-signedNumber}} 人<el-divider direction="vertical"></el-divider>
        未签到人数：{{signedNumber}} 人 <el-divider direction="vertical"></el-divider>
        <el-button type="primary" @click="exp(meetingid)" style="margin-left: 10px">导出考勤信息<i class="el-icon-download"></i></el-button>
      </div>
      <el-table :data="employeeData" border stripe :header-cell-class-name="headerBg">
        <el-table-column prop="employeename" label="员工姓名" width="100px"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="140px"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="170px"></el-table-column>
        <el-table-column prop="departmentname" label="所属部门" width="100px"></el-table-column>
        <el-table-column prop="checkintime" label="签到时间"></el-table-column>
        <el-table-column prop="location" label="签到地点"></el-table-column>
        <el-table-column prop="status" label="签到状态" width="100px">
          <template slot-scope="scope">
            <el-tag size="small" v-if="scope.row.status === 0 && nowDate >= scope.row.signinendtime" type="danger">未签到
            </el-tag>
            <el-tag size="small" v-if="scope.row.status === 0 && nowDate <= scope.row.signinstarttime" type="info">
              签到未开始
            </el-tag>
            <el-tag size="small"
                    v-if="scope.row.status === 0 && nowDate <= scope.row.signinendtime && nowDate >= scope.row.signinstarttime">
              正在签到
            </el-tag>
            <el-tag size="small" v-else-if="scope.row.status === 1" type="success">已签到</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button type="success" size="small" @click="signature(scope.row.meetingid,scope.row.employeeid)"
                       v-if="scope.row.status === 0 && nowDate >= scope.row.signinendtime">补签<i
                class="el-icon-check"></i></el-button>
            <el-button type="danger" size="small" @click="DeleteEmp(scope.row.employeeid,scope.row.meetingid)">删除<i
                class="el-icon-delete"></i></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!--  分页  -->
    <div style="padding-left: 400px;padding-top: 15px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5,10,15,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!--  撤销会议原因弹窗  -->
    <el-dialog title="撤销会议原因" :visible.sync="dialogRevokeVisible" width="30%" center>
      <el-form label-width="150px" :model="formRevoke" ref="formRevoke" :rules="formRevokeFormRules">
        <el-form-item label="会议名称" prop="meetingname">
          <el-input v-model="formRevoke.meetingname" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="取消会议原因" prop="canceledreason">
          <el-input v-model="formRevoke.canceledreason" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRevokeVisible = false">取 消</el-button>
        <el-button type="primary" @click="revoke">确 定</el-button>
      </div>
    </el-dialog>

    <!--  添加参会员工弹窗  -->
    <el-dialog title="选择参会员工" :visible.sync="dialogMeetingVisible" width="44%" center>
      <div style="margin-bottom: 10px;text-align: center"><el-tag type="danger">注意：不要提交重复的参会人员</el-tag></div>
      <el-transfer
          :titles="['选择参会人员','已选参会人员']"
          :button-texts="['移除', '添加']"
          filterable
          filter-placeholder="请输入员工姓名"
          v-model="checked"
          :data="transferData"
          @change="getObject">
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogMeetingVisible = false">取 消</el-button>
        <el-button type="primary" @click="addEmployee">确 定</el-button>
      </div>
    </el-dialog>

    <!--  编辑弹窗  -->
    <el-dialog title="编辑会议信息" :visible.sync="dialogEditVisible" width="30%" top="50px" center>
      <el-form label-width="150px" :model="formEdit" ref="formEdit" :rules="editFormRules">
        <el-form-item label="会议名称" prop="meetingname">
          <el-input v-model="formEdit.meetingname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="会议室名称" prop="roomid">
          <el-select v-model="formEdit.roomid">
            <el-option v-for="mt in meetingroom" :key="mt.roomid" :label="mt.roomname" :value="mt.roomid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="会议开始签到时间" prop="signinstarttime">
          <el-date-picker v-model="formEdit.signinstarttime"
                          type="datetime"
                          placeholder="选择预定日期时间"
                          value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会议结束签到时间" prop="signinendtime">
          <el-date-picker v-model="formEdit.signinendtime"
                          type="datetime"
                          placeholder="选择预定日期时间"
                          value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预计参加会议的人数" prop="numberofparticipants">
          <el-input v-model="formEdit.numberofparticipants" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="预计会议开始时间" prop="starttime">
          <el-date-picker v-model="formEdit.starttime"
                          type="datetime"
                          placeholder="选择预定日期时间"
                          value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预计会议结束时间" prop="endtime">
          <el-date-picker v-model="formEdit.endtime"
                          type="datetime"
                          placeholder="选择预定日期时间"
                          value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会议说明" prop="description">
          <el-input type="textarea" v-model="formEdit.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import dayjs from 'dayjs' //时间格式转换插件

export default {
  name: "Scheduled",
  data() {
    return {
      nowDate: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      tableData: [],
      headerBg: 'headerBg',
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      dialogVisible: false,
      dialogFormVisible: false,
      dialogRevokeVisible: false,
      dialogMeetingVisible: false,
      dialogEditVisible: false,
      meetingid: '',
      formRevoke: {},
      formEdit: {},
      meetingroom: {},
      canceledreason: '',
      employeeData: [],
      formRevokeFormRules: {
        canceledreason: [
          {required: true, message: '请输入取消会议的原因', trigger: 'blur'}
        ]
      },
      editFormRules: {
        meetingname: [
          {required: true, message: '请输入会议名称', trigger: 'blur'}
        ],
        numberofparticipants: [
          {required: true, message: '请输入预计参加会议的人数', trigger: 'blur'}
        ],
        starttime: [
          {required: true, message: '请选择预计会议开始时间', trigger: 'blur'}
        ],
        endtime: [
          {required: true, message: '请选择预计会议结束时间', trigger: 'blur'}
        ],
        roomid: [
          {required: true, message: '请选择会议室', trigger: 'blur'}
        ],
        signinstarttime: [
          {required: true, message: '请选择会议开始签到时间', trigger: 'blur'}
        ],
        signinendtime: [
          {required: true, message: '请选择会议签到结束时间', trigger: 'blur'}
        ]
      },
      total: 0,
      pageNum: 1,
      pageSize: 5,
      meetingname: '',
      tableName: [], //数据表名
      transferData: [], //Transfer 的数据源 array[{ key, label, disabled }]
      checked: [], //穿梭框绑定的数据，选定到右侧框中的数据项的value组成的数组
      testData: [],
      signinstarttime: '',
      signinendtime: '',
      loc: '',
      totalNumber: '',
      signedNumber: '',
      loadingData: false,
    }
  },
  //进入页面刷新数据
  created() {
    this.load()
    //请求会议室数据
    this.request.post("/meetingroom/list").then(res => {
      console.log(res)
      this.meetingroom = res.data
    })
  },
  methods: {
    //请求分页查询数据
    load() {
      this.loadingData = true
      this.request.get("/bookings/mybookings/" + this.employee.employeeid, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          meetingname: this.meetingname
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
        this.loadingData = false
      });
    },
    //页数
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    //页码
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    //搜索重置按钮
    reset() {
      this.meetingname = ""
      this.load()
    },
    //查看考勤信息
    participants(meetingid) {
      this.meetingid = meetingid
      this.dialogVisible = true
      this.request.post("/bookings/participants/" + meetingid).then(res => {
        console.log(res)
        this.employeeData = res.data
      })
      this.request.post("/bookings/queryNum/" + meetingid).then(res=>{
        this.totalNumber = res.data
      })
      this.request.post("/bookings/signed/" + meetingid).then(res=>{
        this.signedNumber = res.data
      })
      this.signedNumber = ''
      this.totalNumber = ''
      this.employeeData = []
    },
    //删除参会员工
    DeleteEmp(employeeid, meetingid) {
      this.$confirm('确定要删除此员工参加此次会议吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.get("/addMeeting/delEmployee/" + meetingid + "/" + employeeid).then(res => {
          if (res.code === 200) {
            this.$message({
              showClose: true,
              type: "success",
              message: "删除该员工的会议成功",
            })
            this.dialogVisible = false
          } else {
            this.$message({
              showClose: true,
              type: "error",
              message: "删除该员工的会议失败",
            })
          }
        })
      })

    },
    //撤销会议
    revoke() {
      this.$refs['formRevoke'].validate((valid) => {
        if (valid) {
          this.request.post("/bookings/cancelmeeting/" + this.meetingid, this.formRevoke.canceledreason).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$message({
                showClose: true,
                type: 'success',
                message: '撤销会议成功',
              })
              this.canceledreason = ''
              this.dialogRevokeVisible = false
              this.load()
            } else {
              this.$message({
                showClose: true,
                type: 'error',
                message: '撤销会议失败',
              })
            }
          })
        }
      })
    },
    //撤销会议
    cancelmeeting(meetingid) {
      this.meetingid = meetingid
      this.dialogRevokeVisible = true
      this.request.post("/bookings/meetingById/" + meetingid).then(res => {
        this.formRevoke = res.data
      })
    },
    //获取所有员工
    addMeeting(meetingid) {
      this.dialogMeetingVisible = true
      this.meetingid = meetingid
      this.request.post("/employee/list").then(res => {
        console.log(res)
        let testData = res.data
        let data = [];
        for (let i = 0; i < testData.length; i++) {
          data.push({
            key: testData[i].employeeid,
            label: testData[i].employeename,
          });
        }
        this.transferData = data;
      })
    },
    //右侧列表元素变化时触发
    getObject() {
      console.log("选中的数据有" + this.checked)
    },
    //添加参会员工
    addEmployee() {
      this.request.post("/addMeeting/addEmployee/" + this.meetingid, this.checked).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$message({
            showClose: true,
            type: "success",
            message: "添加员工成功",
          })
          this.dialogMeetingVisible = false
        } else {
          this.$message({
            showClose: true,
            type: "error",
            message: "添加员工失败",
          })
        }
      })
    },
    //编辑
    save() {
      this.$refs['formEdit'].validate((valid) => {
        if (valid) {
          this.request.post("/meeting/save", this.formEdit).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$message({
                showClose: true,
                type: "success",
                message: "保存成功",
              })
              this.load()
              this.dialogEditVisible = false
            } else {
              this.$message({
                showClose: true,
                type: "error",
                message: "保存失败",
              })
            }
          })
        }
      })
    },
    //编辑
    handleEdit(row) {
      console.log(row)
      this.formEdit = JSON.parse(JSON.stringify(row))  //深拷贝
      this.dialogEditVisible = true
    },
    //补签
    signature(meetingid, employeeid) {
      const location = localStorage.getItem("location")
      this.request.post("/addMeeting/signature", {meetingid, employeeid, location}).then(res => {
        if (res.code === 200) {
          this.$message({
            showClose: true,
            type: "success",
            message: "补签成功",
          })
          this.dialogVisible = false
        } else {
          this.$message({
            showClose: true,
            type: "error",
            message: "补签失败",
          })
        }
      })
    },
    exp(meetingid){
      window.open("http://localhost:8888/bookings/exports/"+ meetingid)
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}

.el-table__fixed-right {
  height: 100% !important;
}

</style>