import Vue from 'vue'
import Router from 'vue-router'
import Wapper from '@/views/layOut/index'

Vue.use(Router)
/**
 * @hidden Boolean  ture 表示不在siderbar 中显示
 * @rloes Array   ['admin','edit']  admin权限  edit 编辑权限 不同的权限
 * @meta Object
 *   title: siderbar名称
 *   icon : siderbar图标 为icons文件夹下面svg图片
 */

// 不需要权限都可展示的静态路由
export const constantRoutes = [
  {
    path: '', //首页index
    name: 'index',
    component: Wapper,
    redirect:'/index',
    children:[
      {
        path:'index',
        component: ()=>import("../views/home/appManagement"),
        name: '应用管理',
        meta: { title: '应用管理', icon: 'main-app', noCache: false, affix: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    hidden: true,
    component: ()=> import("./../views/login.vue")
  },
  {
    path: '/404',
    name: '404',
    hidden: true,
    component: ()=> import("./../views/404.vue")
  }
]

const createRouter = () => new Router({
  // mode:'history',  需要后端支持
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // the relevant part
}

// 需要根据交涉权限 动态展示的路由--- 写在此处
export const asyncRoutes = [
  {
    path: '/account',
    name: 'account',
    component: Wapper,
    isAdmin: 1,
    children:[
      {
        path:'account',
        component: ()=> import("@/views/account/accountManagement"),
        name: '账号管理',
        meta: { title: '账号管理', icon: 'main-account', noCache: false, affix: true }
      }
    ]
  },
  {
    path: '/icon',
    component: Wapper,
    isAdmin: 0,
    meta: { title: '集成文档', icon: 'main-doc'},
    children: [
      {
        path: 'android',
        component: () => import('@/views/AdsManage/androidDocument'),
        name: 'android文档',
        meta: { title: 'android集成', icon: '', noCache: false },
      },
      {
        path: 'ios',
        component: () => import('@/views/AdsManage/iosDocument'),
        name: 'ios文档',
        meta: { title: 'iOS集成', icon: '', noCache: false },
      },
      {
        path: 'flutter',
        component: () => import('@/views/AdsManage/flutterDocument'),
        name: 'flutter文档',
        meta: { title: 'Flutter集成', icon: '', noCache: false },
      }

    ]
  },
  {
    path: '/setting',
    component: Wapper,
    isAdmin: 1,
    children:[
      {
        path:'setting',
        component: ()=> import("@/views/setting/basicConfig"),
        name: '基础设置',
        meta: { title: '基础设置', icon: 'main-setting', noCache: false, affix: true }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export const appRoutes = [
  {
    path: '/version',
    name: 'version',
    perms: 'system:user:version',
    component: Wapper,
    children:[
      {
        path:'/app/version',
        component: ()=> import("@/views/appEdit/version/versionList"),
        name: '版本管理',
        meta: { title: '版本管理', icon: 'main-version', noCache: true, affix: true }
      }
    ]
  },{
    path: '/notification',
    name: 'notification',
    perms: 'system:user:notice',
    component: Wapper,
    children:[
      {
        path:'/app/notification',
        component: ()=> import("@/views/appEdit/notification/notificationList"),
        name: '公告管理',
        meta: { title: '公告管理', icon: 'main-notice', noCache: false, affix: true }
      }
    ]
  },{
    path: '/member',
    name: 'member',
    perms: 'system:user:members',
    component: Wapper,
    children:[
      {
        path:'/app/member',
        component: ()=> import("@/views/appEdit/member/memberList"),
        name: '成员管理',
        meta: { title: '成员管理', icon: 'main-member', noCache: false, affix: true }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

// 其他不在左侧菜单中展示的菜单
export const otherRoutes = [
  {
    path: '/versionAddiOS',
    name: 'versionAddiOS',
    component: Wapper,
    children:[
      {
        path:'/app/versionAddiOS',
        component: ()=> import("@/views/appEdit/version/edit"),
        name: 'VersionEdit',
        meta: { title: '版本新增', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },{
    path: '/versionAddAndroid',
    name: 'versionAddAndroid',
    component: Wapper,
    children:[
      {
        path:'/app/versionAddAndroid',
        component: ()=> import("@/views/appEdit/version/edit"),
        name: 'VersionEdit',
        meta: { title: '版本新增', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },{
    path: '/versionEdit',
    name: 'versionEdit',
    component: Wapper,
    children:[
      {
        path:'/app/versionEdit',
        component: ()=> import("@/views/appEdit/version/edit"),
        name: 'VersionEdit',
        meta: { title: '版本编辑', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },{
    path: '/niticeAdd',
    name: 'niticeAdd',
    component: Wapper,
    children:[
      {
        path:'/app/niticeAdd',
        component: ()=> import("@/views/appEdit/notification/noticeEdit"),
        name: '公告新增',
        meta: { title: '公告新增', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },{
    path: '/noticeEdit',
    name: 'noticeEdit',
    component: Wapper,
    children:[
      {
        path:'/app/noticeEdit',
        component: ()=> import("@/views/appEdit/notification/noticeEdit"),
        name: '公告编辑',
        meta: { title: '公告编辑', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },{
    path: '/noticeView',
    name: 'noticeView',
    component: Wapper,
    children:[
      {
        path:'/app/noticeView',
        component: ()=> import("@/views/appEdit/notification/noticeEdit"),
        name: '公告查看',
        meta: { title: '公告查看', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  }
]

export default router
