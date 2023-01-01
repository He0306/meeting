<template>
  <div>
    <el-container style="min-height: 100vh; ">
      <!--    侧边栏    -->
      <el-aside :width="sideWidth + 'px'"  style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
      </el-aside>

      <!--    顶部栏    -->
      <el-container>
        <el-header style="border-bottom: 1px solid #ccc">
          <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :employee="employee"/>
        </el-header>
        <!--主体-->
        <el-main>
          <router-view @refreshUser="getUser"/>
        </el-main>
    </el-container>
    </el-container>

  </div>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: "Layout",
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      employee: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
      this.getUser()
  },
  methods: {
    //侧边栏收缩
    collapse() {  //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {   //收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   //展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser(){
      let username = localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")).username : ""
      //从后台获取数据
      this.request.get("/employee/username/" + username).then(res=>{
        console.log(res)
        this.employee = res.data
      })
    }
  }
}
</script>

<style>
.el-aside {
  color: #333;
}
</style>