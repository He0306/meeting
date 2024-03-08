<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>数据监控</el-breadcrumb-item>
    </el-breadcrumb>

    <iframe :src="src" style="width: 100%;height: 580px" />
  </div>
</template>

<script>
export default {
  name: "Druid",
  data(){
    return{
      src: 'http://localhost:8888/druid/login.html'
    }
  },
  created() {

  },
  methods:{
    getUrl(){

    }
  }
}
</script>

<style scoped>

</style>