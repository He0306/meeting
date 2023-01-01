import Vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from "@/router";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentPathName: ''
  },
  getters: {
  },
  mutations: {
    setPath(state){
      this.state.currentPathName = localStorage.getItem("currentPathName")
    },
    logout(){
      //清空缓存
      localStorage.removeItem("employee")
      localStorage.removeItem("menus")
      localStorage.removeItem("token")
      localStorage.removeItem("location")
      router.push("/login")
      //重置路由
      resetRouter()

    }
  },
  actions: {
  },
  modules: {
  }
})