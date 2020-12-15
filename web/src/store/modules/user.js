import { loginByUsername, logout, getUserInfo } from '@/api/login'
import {getAccessUser, setAccessUser, removeAccessUser} from '@/utils/storage'
import router from '@/router'
import store from '@/store'

const user = {
  state: {
    accessUser: getAccessUser(),
    status: '',
    code: '',
    avatar: '',
    roles: [],
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_ACCESSUSER: (state, user) => {
      state.accessUser = user
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.userName.trim()
      const captchaVerification = userInfo.captchaVerification
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password, captchaVerification).then(response => {
          const data = response.data.repData
          setAccessUser(data)
          commit('SET_ACCESSUSER', data)
          sessionStorage.setItem('ACCESSUSER', JSON.stringify(data))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          // 由于mockjs 不支持自定义状态码只能这样hack
          if (!response.data) {
            reject('Verification failed, please login again.')
          }
          const data = response.data.repData
          setAccessUser(data)
          commit('SET_ACCESSUSER', data)
          sessionStorage.setItem('ACCESSUSER', JSON.stringify(data))
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        commit('SET_ACCESSUSER', '')
        sessionStorage.setItem('ACCESSUSER', null)
        sessionStorage.setItem('APPINFO', null)
        removeAccessUser()
        resolve()
      })
    },
  }
}

export default user
