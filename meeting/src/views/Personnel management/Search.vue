<template>
<!-- 人员管理 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理员</el-breadcrumb-item>
      <el-breadcrumb-item>员工管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!--  多条件查询  -->
    <div style="padding: 10px 0">
      <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入员工姓名" v-model="employeename"
                clearable></el-input>
      <el-input style="width:250px" suffix-icon="el-icon-phone" placeholder="请输入手机号" v-model="phone"
                clearable></el-input>
      <el-input style="width:250px" suffix-icon="el-icon-message" placeholder="请输入电子邮箱" v-model="email"
                clearable></el-input>
      <el-input style="width:250px" suffix-icon="el-icon-position" placeholder="请输入部门名称" v-model="departmentname"
                clearable></el-input>
      <el-button style="margin-left: 3px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 3px" type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 5px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="primary" @click="exp" style="margin-left: 10px">导出<i class="el-icon-download"></i></el-button>
      <el-button type="danger" :disabled="delBtlStatus" @click="deleteBatch(null)">批量删除<i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  主体数据表格  -->

    <el-table :data="tableData" border
              v-loading="loadingData"
              stripe
              :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange"
              :default-sort = "{prop: 'employeeid'}">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="employeeid" label="员工ID" sortable width="90">
      </el-table-column>
      <el-table-column prop="employeename" label="员工姓名">
      </el-table-column>
      <el-table-column prop="faceimg" label="人脸照片" width="80px">
        <template v-slot:default="scope">
          <el-image style="width: 40px; height: 40px; border:none;cursor: pointer;"
                    :src="'http://localhost:8888/download?name='+scope.row.faceimg"
                    :preview-src-list="['http://localhost:8888/download?name='+scope.row.faceimg]">
            <div slot="error" class="image-slot" style="text-align: center;">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="账号">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column prop="email" label="电子邮箱" width="150">
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
        <el-tag size="small" v-if="scope.row.status === 0" type="warning">未审批</el-tag>
        <el-tag size="small" v-else-if="scope.row.status === 1" type="success">正常</el-tag>
        <el-tag size="small" v-else-if="scope.row.status === 2" type="danger">审批未通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="departmentname" label="所属部门">
      </el-table-column>
      <el-table-column prop="role" label="角色">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.role === 'ROLE_NORMAL'" type="success">普通用户</el-tag>
          <el-tag size="small" v-else-if="scope.row.role === 'ROLE_ADMIN'" type="danger">管理员</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="warning" size="small" @click="handleEdit(scope.row,scope.row.employeeid)">编辑<i class="el-icon-edit"></i></el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.employeeid)">删除<i class="el-icon-delete"></i></el-button>
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

    <!--编辑弹窗-->
    <el-dialog title="编辑用户" :visible.sync="editFormVisible" width="35%" center top="20px">
      <el-form label-width="80px" :model="form" ref="editForm" :rules="editRules">
        <el-form-item label="人脸照片" prop="faceimg">
       <el-button @click="onTake" icon="el-icon-camera" size="small">拍照上传</el-button>
          <el-upload style="margin-left: 110px;margin-top: -30px"
              class="avatar-uploader"
              action :http-request="uploadImg"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeUpload"
              ref="upload">
            <img v-if="form.faceimg" :src="'http://localhost:8888/download?name='+form.faceimg" class="avatar" alt="图片">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="员工姓名" prop="employeename">
          <el-input v-model="form.employeename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">未审批</el-radio>
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="2">审批未通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属部门" prop="departmentid">
          <el-select v-model="form.departmentid" placeholder="请选择所属部门">
            <el-option v-for="dep in department" :key="dep.departmentid" :label="dep.departmentname" :value="dep.departmentid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio label='ROLE_NORMAL'>普通用户</el-radio>
            <el-radio label='ROLE_ADMIN'>管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top: -40px">
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="edit">确 定</el-button>
      </div>
    </el-dialog>

    <!--新增弹窗-->
    <el-dialog title="新增用户" :visible.sync="addFormVisible" center fullscreen>
      <el-form label-width="80px" :model="form" ref="form" :rules="editRules">
        <el-form-item label="员工姓名" prop="employeename">
          <el-input v-model="form.employeename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
          <el-alert title="初始密码为111111" :closable="false" type="info" style="line-height: 12px;"></el-alert>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">未审批</el-radio>
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="2">审批未通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属部门" prop="departmentid">
          <el-select v-model="form.departmentid" placeholder="请选择所属部门">
            <el-option v-for="dep in department" :key="dep.departmentid" :label="dep.departmentname" :value="dep.departmentid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio :label="1">普通用户</el-radio>
            <el-radio :label="2">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top: -40px">
        <el-button @click="addFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

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

  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    let validateUserName = (rule, value, callback) => {
      if (!value) {
        callback(new Error("真实姓名不能为空"))
      } else {
        if (value !== "") {
          const realnameReg = /^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,5})$/;
          if (!realnameReg.test(value)) {
            callback(new Error('您的真实姓名格式错误,请输入1—5个英文或汉字!'));
          }
        }
        callback();
      }
    };
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
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      employeename: '',
      email: '',
      phone: '',
      departmentname: '',
      form: {},
      multipleSelection: [],
      headerBg: 'headerBg',
      editFormVisible: false,
      addFormVisible: false,
      delBtlStatus: true,
      editRules: {
        employeename: [
          {required: true,validator:validateUserName, trigger: 'blur'}
        ],
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'}
        ],
        phone: [
          {required: true,validator:vaildateNumber, trigger: 'blur'},
        ],
        email: [
          {required: true,validator:validateEmail, trigger: 'blur'}
        ],
        status: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ],
        departmentid: [
          {required: true, message: '请选择部门', trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择角色', trigger: 'blur'}
        ],
        faceimg:[
          {required: true, message: '请选择照片上传或者拍照上传', trigger: 'blur'}
        ]
      },
      department: [],
      loadingData: false,
      imgSrc: "",
      visible: false,//弹窗
      loading: false,//上传按钮加载
      open: false,//控制摄像头开关
      thisVideo: null,
      thisContext: null,
      thisCancas: null,
      videoWidth: 500,
      videoHeight: 400,
      employeeid:'',
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
      this.request.get("/employee/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          employeename: this.employeename,
          phone: this.phone,
          email: this.email,
          departmentname: this.departmentname
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
        this.loadingData = false
      });
      this.request.get('/department/all').then(res=>{
        this.department = res.data
    })

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
      this.employeename = ""
      this.departmentname = ""
      this.phone = ""
      this.email = ""
      this.load()
    },
    //新增
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.request.post("/employee/save", this.form).then(res => {
            if (res.data) {
              this.$message({
                showClose: true,
                type: "success",
                message: "保存成功",
              })
              this.load()
              this.addFormVisible = false
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
    edit() {
      this.$refs['editForm'].validate((valid) => {
        if (valid) {
          this.request.post("/employee/edit", this.form).then(res => {
            if (res.data) {
              this.$message({
                showClose: true,
                type: "success",
                message: "保存成功",
              })
              this.load()
              this.editFormVisible = false
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
    //打开新增弹窗
    handleAdd() {
      this.addFormVisible = true
      this.form = {}
    },
    //编辑
    handleEdit(row,employeeid) {
      this.employeeid = employeeid
      this.form = JSON.parse(JSON.stringify(row))  //深拷贝
      this.editFormVisible = true
    },
    //根据ID删除
    handleDelete(employeeid) {
      this.$confirm('确定要删除此员工吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.delete("/employee/delete/" + employeeid).then(res => {
          if (res.data) {
            this.$message({
              showClose: true,
              type: "success",
              message: "删除成功",
            })
            this.load()
          } else {
            this.$message({
              showClose: true,
              type: "error",
              message: "删除失败",
            })
            this.load()
          }
        })
      })

    },
    //批量删除 val:数组
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.delBtlStatus = val.length === 0
    },
    //批量删除
    deleteBatch(employeeid) {
      let ids = []
      if (employeeid) {
        ids.push(employeeid)
      } else {
        this.multipleSelection.forEach(row => {
          ids.push(row.employeeid)
        })
      }
      this.$confirm('确定要删除您选中的员工吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.post("/employee/delete/batch", ids).then(res => {
          if (res.data) {
            this.$message({
              showClose: true,
              type: "success",
              message: "批量删除成功",
            })
            this.load()
          } else {
            this.$message({
              showClose: true,
              type: "error",
              message: "批量删除失败",
            })
          }
        })
      })

    },
    //导出
    exp(){
      window.open("http://localhost:8888/employee/export")
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
    //上传成功回调
    handleAvatarSuccess (response, file, fileList) {
      //this.form.faceimg = `http://localhost:8888/download?name=${response.data}`
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
      formData.append("employeeid",this.employeeid)
      this.request.post("/registered",formData).then(res=>{
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
        this.request.post("/registered",formData,{params:{employeeid: this.employeeid}}).then(res=>{
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
.avatar-uploader {
  margin-bottom: -10px;
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
  width: 120px;
  height: 120px;
  line-height: 130px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-icon-plus:before {
  content: "上传人脸照片" !important;
  font-size: 12px;
  color: #000;
}

</style>