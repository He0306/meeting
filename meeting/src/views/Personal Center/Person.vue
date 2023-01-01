<template>
  <el-card style="width: 1000px;height: 620px; margin:0px auto 0;">
      <el-form label-width="80px" style="width: 500px;height: 500px;margin: 10px auto 0" :model="form" ref="form" :rules="Rules">
        <el-form-item label="真实姓名" prop="employeename">
          <el-input v-model="form.employeename" autocomplete="off"></el-input>
        </el-form-item>
      <el-form-item label="账号">
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
        <el-alert title="需要修改请联系管理员" :closable="false" type="info" style="line-height: 12px;"></el-alert>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="form.departmentid" disabled>
            <el-option v-for="dep in department" :key="dep.departmentid" :label="dep.departmentname" :value="dep.departmentid"></el-option>
          </el-select>
          <el-alert title="需要修改请联系管理员" :closable="false" type="info" style="line-height: 12px;"></el-alert>
        </el-form-item>
        <el-form-item label="添加人脸" prop="faceimg">
          <el-button @click="onTake" icon="el-icon-camera" size="small">拍照上传</el-button>
          <el-upload
              class="avatar-uploader"
              action=""
              :http-request="uploadImg"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeUpload"
              ref="upload">
            <img v-if="form.faceimg" :src="'http://localhost:8888/download?name='+form.faceimg" class="avatar" alt="图片">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      <el-button type="primary" @click="save" style="margin-left: 200px;margin-top: 5px;width: 100px">确 定</el-button>
    </el-form>

    <!--  拍照上传弹窗  -->
    <el-dialog title="拍照上传" :visible.sync="visible" @close="onCancel" width="1065px" top="20px">
      <div class="box">
        <video id="videoCamera" class="canvas" :width="videoWidth" :height="videoHeight" autoPlay></video>
        <canvas id="canvasCamera" class="canvas" :width="videoWidth" :height="videoHeight"></canvas>
      </div>
      <div slot="footer">
        <el-button v-if="open === false" @click="drawImage" icon="el-icon-camera" size="small">拍照</el-button>
        <el-button v-if="open" @click="getCompetence" icon="el-icon-video-camera" size="small">打开摄像头</el-button>
        <el-button v-else @click="stopNavigator" icon="el-icon-switch-button" size="small">关闭摄像头</el-button>
        <el-button v-if="open === false" @click="resetCanvas" icon="el-icon-refresh" size="small">重置</el-button>
        <el-button @click="onUpload" :loading="loading" type="primary" icon="el-icon-upload2" size="small">上传</el-button>
      </div>
    </el-dialog>
  </el-card>


</template>

<script>
export default {
  name: "Person",
  data(){
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
    let vaildateNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('电话号码不能为空'));
      } else {
        if (value !== "") {
          var reg = /^1[3456789]\d{9}$/;
          if (!reg.test(value)) {
            callback(new Error('请输入有效的手机号码'));
          }
        }
        callback();
      }
    };
    let validateEmail = (rule, value, callback) => {
      if (value === "") {
        callback(new Error('邮箱不能为空'));
      } else {
        if (value !== "") {
          var regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
          if (!regEmail.test(value)) {
            callback(new Error('请输入有效的邮箱'));
          }
        }
        callback();
      }
    };
    return{
      form: {},
      department: [],
      departmentname: '',
      imgSrc: "",
      visible: false,//弹窗
      loading: false,//上传按钮加载
      open: false,//控制摄像头开关
      thisVideo: null,
      thisContext: null,
      thisCancas: null,
      videoWidth: 500,
      videoHeight: 400,
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      Rules:{
        employeename: [
          {required: true,validator:validateUserName, trigger: 'blur'}
        ],
        phone: [
          {required: true,validator:vaildateNumber, trigger: 'blur'},
        ],
        email: [
          {required: true,validator:validateEmail, trigger: 'blur'}
        ],
        faceimg: [
          {required: true,message: '请选择照片上传或者拍照上传', trigger: 'blur'}
        ]
      },
      employeeid: localStorage.getItem("employeeid")
    }
  },
  created() {
   this.getUser().then(res=>{
     this.form = res
   })
    this.request.get("/department/all").then(res=>{
      this.department = res.data
    })
  },
  methods:{
    async getUser(){
      return (await this.request.get("/employee/username/" + this.employee.username)).data
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.request.post("/employee/personalSave", this.form).then(res => {
            if (res.data) {
              this.$message({
                showClose: true,
                type: "success",
                message: "更新个人信息成功",
              })
              //触发父级更新User方法
              this.$emit("refreshUser")
              //更新浏览器存储的用户信息
              this.getUser().then(res => {
                console.log(res)
                localStorage.setItem("employee", JSON.stringify(res))  //深拷贝
              })
            } else {
              this.$message({
                showClose: true,
                type: "error",
                message: "更新个人信息失败",
              })
            }
          })
        }
      })
    },
    //上传成功回调
    handleAvatarSuccess (res) {
      //this.form.faceimg = `http://localhost:8888/download?name=${res.data}`
    },
    //设置上传规则
    beforeUpload (file) {
      if(file){
        const suffix = file.name.split('.')[1]
        const size = file.size / 1024 / 1024 < 2
        if(['png','jpeg','jpg'].indexOf(suffix) < 0){
          this.$message.error('上传图片只支持 png、jpeg、jpg 格式！')
          this.$refs.upload.clearFiles()
          return false
        }
        if(!size){
          this.$message.error('上传文件大小不能超过 2MB!')
          return false
        }
        return file
      }
    },
    onTake() {
      this.visible = true;
      this.getCompetence();
    },
    onCancel() {
      this.visible = false;
      this.resetCanvas();
      this.stopNavigator();
    },
    //图片上传
    uploadImg(fileObj){
      let formData = new FormData();
      formData.append("file",fileObj.file)
      this.request.post("/registered",formData,{params:{employeeid: this.employee.employeeid}}).then(res=>{
        if (res.code === 200){
          this.form.faceimg = res.data
          this.$message({
            showClose: true,
            type: 'success',
            message: '人脸照片上传成功，请点击确定',
          })
          this.visible = false
          this.load()
        }else {
          this.$message({
            showClose: true,
            type: 'error',
            message: '人脸照片上传识别，请重新上传',
          })
        }
      })
    },
    //base64转成文件后上传
    onUpload() {
      if (this.imgSrc) {
        let formData = new FormData()
        formData.append("file",this.base64ToFile(this.imgSrc,"png"));
        formData.append("flag", "videoImg"); // 额外参数
        this.loading = true
        this.request.post("/registered",formData,{params:{employeeid: this.employee.employeeid}}).then(res=>{
          if (res.code === 200){
            this.form.faceimg = res.data
            this.$message({
              showClose: true,
              type: 'success',
              message: '人脸照片上传成功，请点击确定',
            })
            this.visible = false
          }else {
            this.$message({
              showClose: true,
              type: 'error',
              message: '人脸照片上传识别，请重新上传',
            })
          }
          this.loading = false
        })
      }
      else {
        this.$message({
          showClose: true,
          message: '请点击拍照',
          type: 'warning',
        });
      }
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
      this.imgSrc = this.thisCancas.toDataURL('img/png');
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
    },
  },
  beforeDestroy() {
    this.stopNavigator()
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  margin-top: -30px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 148px;
  height: 148px;
  display: block;
}
.avatar-uploader .el-icon-plus:before {
  content: "上传人脸照片" !important;
  font-size: 12px;
  color: #000;
}

</style>