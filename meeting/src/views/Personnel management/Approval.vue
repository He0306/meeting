<template>
<!-- 注册审批 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理员</el-breadcrumb-item>
      <el-breadcrumb-item>注册审批</el-breadcrumb-item>
    </el-breadcrumb>

    <!--  主体数据表格  -->
    <el-table v-loading="loading" :data="tableData" border stripe :header-cell-class-name="headerBg">
      <el-table-column prop="employeeid" label="员工编号" width="80">
      </el-table-column>
      <el-table-column prop="employeename" label="员工姓名">
      </el-table-column>
      <el-table-column prop="username" label="账号">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="300">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handlePass(scope.row.employeeid)">通过<i class="el-icon-circle-check"></i></el-button>
          <el-button type="danger" size="small" @click="handlePassFail(scope.row.employeeid)">不通过<i class="el-icon-circle-close"></i></el-button>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
export default {
  name: "Approval",
  data() {
    return {
      tableData: [],
      headerBg: 'headerBg',
      loading: false
    }
  },
  //进入页面刷新数据
  created() {
    //请求查询数据
    this.load()
  },
  methods: {
    //查询
    load() {
      this.loading = true
      this.request.post("/approve/getStatus").then(res => {
        console.log(res)
        this.tableData = res.data
        this.loading = false
      });
    },

    //根据ID通过该申请
    handlePass(employeeid) {
      this.$confirm('确定要通过审批吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.post("/approve/updateStatusAdopt/" + employeeid).then(res => {
          if (res.data) {
            this.$notify.success({
              title:"通过成功",
              message:"通过该申请成功，请分配部门"
            });
            this.load()
            this.$router.push("/Search")
          } else {
            this.$message({
              showClose: true,
              type: "error",
              message: "通过该申请失败",
            })
          }
        })
      })

    },
    //根据ID不通过该申请
    handlePassFail(employeeid){
      this.$confirm('确定不通过此注册审批吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.post("/approve/updateStatusFail/" + employeeid).then(res => {
          if (res.data) {
            this.$message({
              showClose: true,
              type: "success",
              message: "不通过该申请成功",
            })
            this.load()
            this.$router.push("/Search")
          } else {
            this.$message({
              showClose: true,
              type: "error",
              message: "不通过该申请失败",
            })
          }
        })
      })

    },
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}

</style>