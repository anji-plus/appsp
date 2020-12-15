import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// permission judge function
function hasPermission (roles, permissionRoles) {
  if (roles.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}

const whiteList = ['/login', '/auth-redirect', '/portal', '/myWorkOrder', '/workDetails', '/framework', '/menhulogin', '/feedback'] // no redirect whitelist
let addRouFlag = false

// 拦截是否登录
router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  // console.log("--localStorage--- ", Object.values(localStorage.getItem(ACCESSUSER)))

  if (store.getters.accessUser && store.getters.accessUser.token) { // determine if there has token
    /* has token */
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) { // 判断当前用户是否已拉取完user_info信息
        var roles = ['other']
        console.log(store.getters.accessUser)
        if (store.getters.accessUser != null){
          if (store.getters.accessUser.isAdmin == 1){
            roles = ['admin'];
          }
        }

        store.commit('SET_ROLES', roles)

        if (!addRouFlag) {
          console.log('addRouFlag:', addRouFlag);
          addRouFlag = true
          store.dispatch('GenerateRoutes', { roles }).then(() => { // 根据roles权限生成可访问的路由表
            store.dispatch('SetCurrentRoutes', to.path).then(() => {
              router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
            })
          })
          .catch((err) => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
        }
      } else {
        store.dispatch('SetCurrentRoutes', to.path)
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        if (hasPermission(store.getters.roles, to.meta.roles)) {
          if (to.matched.length === 0) {
            next({ path: '/404' })
          } else {
            next()
          }
        } else {
          next({ path: '/404', replace: true, query: { noGoBack: true } })
        }
        // 可删 ↑
      }
    }
  } else {
    /* has no token */
    if (whiteList.indexOf(to.path) !== -1 || to.path.indexOf('portal') !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
