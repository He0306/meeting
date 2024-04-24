<template>
  <!-- 我的会议 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      <el-breadcrumb-item>我的会议</el-breadcrumb-item>
    </el-breadcrumb>

    <!--  搜索框、搜索按钮、重置按钮  -->
    <div style="padding: 10px 0">
      <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入会议名称" v-model="meetingname" clearable></el-input>
      <el-button style="margin-left: 3px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 3px" type="warning" @click="reset">重置</el-button>
    </div>

    <!--  主体数据表格  -->
    <el-table :data="tableData" v-loading="loadingData" border stripe :header-cell-class-name="headerBg">
      <el-table-column prop="meetingname" label="会议名称" width="100px"></el-table-column>
      <el-table-column prop="roomname" label="会议室名称" width="100px"></el-table-column>
      <el-table-column prop="signinstarttime" label="会议开始签到时间" sortable></el-table-column>
      <el-table-column prop="signinendtime" label="会议结束签到时间" sortable></el-table-column>
      <el-table-column prop="starttime" label="会议开始时间" sortable></el-table-column>
      <el-table-column prop="endtime" label="会议结束时间" sortable></el-table-column>
      <el-table-column prop="reservationistname" label="会议预定者" width="100px"></el-table-column>
      <el-table-column prop="description" label="会议描述"></el-table-column>
      <el-table-column prop="status" label="签到状态" width="100px">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0 && nowDate >= scope.row.signinendtime" type="danger">未签到</el-tag>
          <el-tag size="small" v-if="scope.row.status === 0 && nowDate <= scope.row.signinstarttime" type="info">签到未开始</el-tag>
          <el-tag size="small" v-if="scope.row.status === 0 && nowDate <= scope.row.signinendtime && nowDate >= scope.row.signinstarttime">正在签到</el-tag>
          <el-tag size="small" v-else-if="scope.row.status === 1" type="success">已签到</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="signIn(scope.row.meetingid)" v-if="scope.row.status === 0 && nowDate <= scope.row.signinendtime && nowDate >= scope.row.signinstarttime">签到<i class="el-icon-check"></i></el-button>
        </template>
      </el-table-column>
    </el-table>

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

    <!--  签到  -->
    <el-dialog title="签到" :visible.sync="visible" @close="onCancel" width="1065px" center top="20px">
      <div class="box">
        <video id="videoCamera" class="canvas" :width="videoWidth" :height="videoHeight" autoPlay></video>
        <canvas id="canvasCamera" class="canvas" :width="videoWidth" :height="videoHeight"></canvas>
      </div>
      <div slot="footer">
        <el-button v-if="open === false" @click="drawImage" icon="el-icon-camera" size="small">拍照</el-button>
        <el-button v-if="open" @click="getCompetence" icon="el-icon-video-camera" size="small">打开摄像头</el-button>
        <el-button v-else @click="stopNavigator" icon="el-icon-switch-button" size="small">关闭摄像头</el-button>
        <el-button v-if="open === false" @click="resetCanvas" icon="el-icon-refresh" size="small">重置</el-button>
        <el-button @click="onUpload" :loading="loading" type="primary" icon="el-icon-circle-check" size="small">提交</el-button>
        <el-upload action :http-request="uploadImg"  :show-file-list="false" style="display: inline-block;margin-left: 7px">
          <el-button type="primary" style="margin-left: 5px"><i class="el-icon-picture"></i>图片上传</el-button>
        </el-upload>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import dayjs from 'dayjs' //时间格式转换插件
export default {
  name: "Meeting",
  data() {
    return {
      nowDate: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      meetingname: '',
      headerBg: 'headerBg',
      imgSrc: "",
      visible: false,//弹窗
      loading: false,//上传按钮加载
      open: false,//控制摄像头开关
      thisVideo: null,
      thisContext: null,
      thisCancas: null,
      videoWidth: 500,
      videoHeight: 400,
      meetingid: '',
      loadingData: false,
      location: localStorage.getItem("location")
    }
  },
  //进入页面刷新数据
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    //分页查询
    load() {
      this.loadingData = true
      this.request.get("/bookings/myMeeting/" + this.employee.employeeid, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          meetingname: this.meetingname,
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
    //签到
    signIn(meetingid){
      this.meetingid = meetingid
      this.visible = true
      this.getCompetence()
    },
    onCancel() {
      this.visible = false;
      this.resetCanvas();
      this.stopNavigator();
    },
    //图片上传签到
    handleFileUploadSuccess(res){
      if (res.code === 400) {
        this.$message({
          showClose: true,
          message: res.msg,
          type: 'error',
        })
      }else {
        this.$message({
          showClose: true,
          message: res.data,
          type: 'success'
        })
      }
      this.load()
    },
    //base64转成文件后上传
    onUpload() {
      if (this.imgSrc) {
        let formData = new FormData()
        formData.append("file",this.base64ToFile(this.imgSrc,"png"));
        formData.append("flag", "videoImg"); // 额外参数
        formData.append("meetingid",this.meetingid)
        formData.append("employeeid",localStorage.getItem("employeeid"))
        formData.append("location",this.location)
        this.loading = true
        this.request.post("/SingIn",formData).then(res=>{
          if (res.code === 200){
            this.$message({
              showClose: true,
              type: 'success',
              message: res.data,
            })
            this.loading = false
            this.visible = false
            this.load()
          }else {
            this.$message({
              showClose: true,
              type: 'error',
              message: res.msg,
            })
            this.loading = false
          }
        })
      }
      else {
        this.$message({
          message: '请点击拍照',
          type: 'warning',
          duration: 1000
        });
      }
    },
    //图片上传
    uploadImg(fileObj) {
      let formData = new FormData();
      formData.append("file",fileObj.file)
      formData.append("meetingid",this.meetingid)
      formData.append("employeeid",localStorage.getItem("employeeid"))
      formData.append("location",this.location)
      this.request.post("/SingIn",formData).then(res=>{
        if (res.code === 200){
          this.$message({
            showClose: true,
            type: 'success',
            message: res.data,
          })
          this.loading = false
          this.visible = false
          this.load()
        }else {
          this.$message({
            showClose: true,
            type: 'error',
            message: res.msg,
          })
        }
      })
    },
    // 调用摄像头权限
    getCompetence() {
      //必须在model中render后才可获取到dom节点,直接获取无法获取到model中的dom节点
      this.$nextTick(() => {
        const _this = this;
        this.open = false;//切换成关闭摄像头
        this.thisCancas = document.getElementById('canvasCamera');
        this.thisContext = this.thisCancas.getContext('2d');
        this.thisVideo = document.getElementById('videoCamera');
        // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
        if (navigator.mediaDevices === undefined) {
          navigator.mediaDevices = {}
        }
        // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
        // 使用getUserMedia，因为它会覆盖现有的属性。
        // 这里，如果缺少getUserMedia属性，就添加它。
        if (navigator.mediaDevices.getUserMedia === undefined) {
          navigator.mediaDevices.getUserMedia = function (constraints) {
            // 首先获取现存的getUserMedia(如果存在)
            let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia;
            // 有些浏览器不支持，会返回错误信息
            // 保持接口一致
            if (!getUserMedia) {
              return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
            }
            // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
            return new Promise(function (resolve, reject) {
              getUserMedia.call(navigator, constraints, resolve, reject)
            })
          }
        }
        const constraints = {
          audio: false,
          video: {width: _this.videoWidth, height: _this.videoHeight, transform: 'scaleX(-1)'}
        };
        navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ('srcObject' in _this.thisVideo) {
            _this.thisVideo.srcObject = stream
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream)
          }
          _this.thisVideo.onloadedmetadata = function (e) {
            _this.thisVideo.play()
          }
        }).catch(err => {
          this.$message({
            showClose: true,
            message: '没有开启摄像头权限或浏览器版本不兼容.',
            type: 'warning'
          });
        });
      });
    },
    //绘制图片
    drawImage() {
      // 点击，canvas画图
      this.thisContext.drawImage(this.thisVideo, 0, 0, this.videoWidth, this.videoHeight);
      // 获取图片base64链接
      this.imgSrc = this.thisCancas.toDataURL('image/png');
    },
    // base64 转为 file
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[1]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: mime });
    },
    //清空画布
    clearCanvas(id) {
      let c = document.getElementById(id);
      let cxt = c.getContext("2d");
      cxt.clearRect(0, 0, c.width, c.height);
    },
    //重置画布
    resetCanvas() {
      this.imgSrc = "";
      this.clearCanvas('canvasCamera');
    },
    //关闭摄像头
    stopNavigator() {
      if (this.thisVideo) {
        this.thisVideo.srcObject.getTracks()[0].stop();
        this.open = true;//切换成打开摄像头
      }
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}

</style>