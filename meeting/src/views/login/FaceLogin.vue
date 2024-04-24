<template>
  <div id="bbb">
    <router-view></router-view>
    <h1 style="margin-top: 50px;margin-bottom: 10px">人脸登录</h1>
    <video
        id="videoCamera"
        :width="videoWidth"
        :height="videoHeight"
        :x5-video-player-fullscreen="true"
        autoplay
    ></video>
    <canvas
        style="display: none"
        id="canvasCamera"
        :width="videoWidth"
        :height="videoHeight"
    ></canvas>

    <el-button @click="setImage" type="primary" icon="el-icon-camera" style="margin-top: 10px;width: 150px;height: 40px">人脸登录</el-button>
    <el-upload :http-request="uploadImg" :show-file-list="false" action="#">
      <el-button type="primary" style="margin-top: 10px;width: 150px;height: 40px"><i class="el-icon-picture"></i>图片上传</el-button>
    </el-upload>
  </div>
</template>
<script>

import {setRoutes} from "@/router";

export default {
  name: "FaceLogin",
  data() {
    return {
      // 视频调用相关数据开始
      videoWidth: 550,
      videoHeight: 450,
      imgSrc: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      openVideo: false,
      //视频调用相关数据结束
      postVideoImg: "", // 图片上传后获取的url链接
    };
  },
  mounted() {
    // 第一步打开摄像头
    this.getCompetence(); //调用摄像头
    // 获取地理位置
    let geolocation = new BMapGL.Geolocation();
    geolocation.getCurrentPosition(function(r){
      if(this.getStatus() === BMAP_STATUS_SUCCESS){
        const province = r.address.province
        const city = r.address.city
        localStorage.setItem("location", province + city)
      }
    });
  },
  methods: {
    // 第三步、拍照图转换file格式上传，
    // 第四步、获取图片url链接
    async postImg() {
      let formData = new FormData();
      formData.append("file", this.base64ToFile(this.imgSrc, "png"));
      formData.append("flag", "videoImg"); // 额外参数
      //使用axios对应的后台上传图片接口
      this.request.post("http://localhost:8888/faceLogin",formData).then(res=>{
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
      })
    },
    //图片上传登录
    uploadImg(fileObj){
      let formData = new FormData();
      formData.set("file", fileObj.file);
      //使用axios对应的后台上传图片接口
      this.request.post("/faceLogin",formData).then(res=>{
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
      })
    },
    // 调用权限（打开摄像头功能）
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
    //  第二步、绘制图片（拍照功能）
    setImage() {
      var _this = this;
      // canvas画图
      _this.thisContext.drawImage(_this.thisVideo, 0, 0);
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL("image/png");
      _this.imgSrc = image; //赋值并预览图片

      //这里是调用上传图片接口=====
      this.postImg(); // 绘制完图片调用图片上传接口
    },
    // 关闭摄像头
    stopNavigator() {
      this.thisVideo.srcObject.getTracks()[0].stop();
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
  },
  destroyed: function () {
    // 离开当前页面
    this.stopNavigator(); // 关闭摄像头
  },
};
</script>
<style>
.result_img {
  width: 226px;
  height: 195px;
  background: #d8d8d8;
}
#bbb {
  margin: auto;
  width: 500px;
  text-align: center;
}
</style>
