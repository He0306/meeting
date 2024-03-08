import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/Login'),
    },
    {
        path: '/faceLogin',
        name: 'FaceLogin',
        component: () => import('../views/login/FaceLogin')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/register/Register')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404/NotFound')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
}


export const setRoutes = () => {
    //拼装动态路由
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('Manger')) {
            const managerRote = {
                path: '/',
                name: 'layout',
                component: () => import("../components/Layout"),
                redirect: '/home',
                children: [
                    {path: 'person', name: '个人信息', component: () => import('../views/Personal Center/Person')},
                    {path: 'updatePwd', name: '修改密码', component: () => import('../views/Personal Center/UpdatePwd')}
                ]
            }
            const menus = JSON.parse(storeMenus)
            menus.forEach(item => {
                if (item.path) {   //当前仅当path不为空的时候才去设置
                    let itemMenu = {
                        path: item.path.replace("/", ""),
                        name: item.name,
                        component: () => import('../views/' + item.pagePath + '.vue'),
                    }
                    managerRote.children.push(itemMenu)
                } else if (item.children.length) {
                    item.children.forEach(item => {
                        if (item.path) {   //当前仅当path不为空的时候才去设置
                            let itemMenu = {
                                path: item.path.replace("/", ""),
                                name: item.name,
                                component: () => import('../views/' + item.pagePath + '.vue'),
                            }
                            managerRote.children.push(itemMenu)
                        }
                    })
                }
            })
            //动态添加到现在的路由对象去
            router.addRoute(managerRote)
        }
    }
}
setRoutes()

//路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  //设置路由名称
    //store.commit("setPath")  //触发store的数据更新
    //未找到路由的情况
    if (!to.matched.length) {
        const storeMenus = localStorage.getItem("menus")
        if (storeMenus) {
            next("/404")
        } else {
            //跳回登录页面
            next("/login")
        }
    }
    next() //放行路由
})
export default router
