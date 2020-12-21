import { asyncRoutes, appRoutes, otherRoutes, constantRoutes } from '@/router'
import store from "../index";

/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      // state.routes = constantRoutes.concat(routes)
    },
    SET_MENU_ROUTES: (state, routes) => {
      state.routes = routes
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { roles } = data
        var accessedRoutes = []
        accessedRoutes = accessedRoutes.concat(asyncRoutes)
        accessedRoutes = accessedRoutes.concat(appRoutes)
        accessedRoutes = accessedRoutes.concat(otherRoutes)
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      })
    },
    SetCurrentRoutes({ commit }, path) {
      return new Promise(resolve => {
        let currentRoutes
        if (path.startsWith('/app/')) {
          // var perms = store.getters.appInfo.perms;
          // if(perms == null || perms.length == 0) {
          //   let appinfo = JSON.parse(sessionStorage.getItem('APPINFO'))
          //   perms = appinfo.perms
          // }
          let appinfo = JSON.parse(sessionStorage.getItem('APPINFO'))
          var perms = appinfo.perms
          const routes = perms.map(function(p){
            return appRoutes.find(function(r){
              return r.perms == p;
            });
          })
          //动态加权限
          currentRoutes= routes;
        }else {
          var accessUser = store.getters.accessUser
          if(accessUser == null) {
            accessUser = JSON.parse(sessionStorage.getItem('ACCESSUSER'))
          }

          if (accessUser != null && accessUser.isAdmin == 1){ //admin
            //动态加权限
            currentRoutes = constantRoutes.concat(asyncRoutes)
          } else {
            //排除用户管理
            currentRoutes = constantRoutes.concat(asyncRoutes.filter((item) => {
              return item.isAdmin == 0;
            }))
          }
        }
        commit('SET_MENU_ROUTES', currentRoutes)
        resolve(currentRoutes)
      })
    }
  }
}

export default permission
