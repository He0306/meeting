<template>
<!-- 部门管理 -->
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理员</el-breadcrumb-item>
      <el-breadcrumb-item>部门管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!--  搜索框、搜索按钮、重置按钮、批量删除按钮  -->
    <div style="padding: 10px 0">
      <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入部门名称" v-model="departmentname"
                clearable></el-input>
      <el-button style="margin-left: 3px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 3px" type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 5px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" :disabled="delBtlStatus" @click="deleteBatch(null)">批量删除<i class="el-icon-remove-outline"></i></el-button>
    </div>

    <!--  主体数据表格  -->
    <el-table :data="tableData" v-loading="loading" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" :default-sort = "{prop: 'departmentid'}">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="departmentid" label="部门编号" sortable width="100">
      </el-table-column>
      <el-table-column prop="departmentname" label="部门名称">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="300">
        <template slot-scope="scope">
          <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.departmentid)">删除<i class="el-icon-delete"></i></el-button>
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

    <!--新增和编辑弹窗-->
    <el-dialog title="部门信息" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form label-width="80px" :model="form" ref="form"  :rules="editFormRules">
        <el-form-item label="部门名称" prop="departmentname">
          <el-input v-model="form.departmentname" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Manage",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      departmentname: '',
      form: {},
      multipleSelection: [],
      headerBg: 'headerBg',
      dialogFormVisible: false,
      delBtlStatus: true,
      editFormRules: {
        departmentname: [
          {required: true, message: '请输入部门名称', trigger: 'blur'}
        ]
      },
      loading: false
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
      this.loading = true
      this.request.get("/department/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentname: this.departmentname,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading = false
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
      this.departmentname = ""
      this.load()
    },
    //新增
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.request.post("/department/save", this.form).then(res => {
            if (res.data) {
              this.$message({
                showClose: true,
                type: "success",
                message: "保存成功",
              })
              this.load()
              this.dialogFormVisible = false
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
      this.dialogFormVisible = true
      this.form = {}
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))  //深拷贝
      this.dialogFormVisible = true
    },
    //根据ID删除
    handleDelete(departmentid) {
      this.$confirm('确定要删除此部门吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.delete("/department/delete/" + departmentid).then(res => {
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
    deleteBatch(departmentid) {
      let ids = []
      if (departmentid) {
        ids.push(departmentid)
      } else {
        this.multipleSelection.forEach(row => {
          ids.push(row.departmentid)
        })
      }
      this.$confirm('确定要删除您选中的部门吗？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.request.post("/department/delete/batch", ids).then(res => {
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

  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}

</style>