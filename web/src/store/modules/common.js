import {getVersions} from '@/api/main'
import router from '@/router'
import store from '@/store'

const common = {
  state: {
    // 存放所有的安卓版本
    androidVersions: [],
    // 存放所有的iOS版本
    iOSVersions: [],
    // 存放所有的公告模版
    notices: [],
    appInfo: {
      appId: '',
      appKey: '',
      appName: '',
      //菜单权限列表
      perms: []
    },
  },

  mutations: {
    SET_ANDROID_VERSIONS: (state, versions) => {
      state.androidVersions = versions
    },
    SET_IOS_VERSIONS: (state, versions) => {
      state.iOSVersions = versions
    },
    SET_NOTICES: (state, notices) => {
      state.notices = notices
    },
    SET_APPINFO: (state, appInfo) => {
      state.appInfo = appInfo
    },
  },

  actions: {
    getAndroidVersions({commit, state}) {
      return new Promise((resolve, reject) => {
        getVersions('ANDROID_VERSION').then((response) => {
          const data = response.data
          commit('SET_ANDROID_VERSIONS', data.repData)
          resolve()
        }).catch(error => {
          reject(error)
        })

      })
    },
    getIOSVersions({commit, state}) {
      return new Promise((resolve, reject) => {
        getVersions('IOS_VERSION').then((response) => {
          const data = response.data
          commit('SET_IOS_VERSIONS', data.repData)
          resolve()
        }).catch(error => {
          reject(error)
        })

      })
    },
    getNotices({commit, state}) {
      return new Promise((resolve, reject) => {
        getVersions('TEMPLATE').then((response) => {
          const data = response.data
          commit('SET_NOTICES', data.repData)
          resolve()
        }).catch(error => {
          reject(error)
        })

      })
    },
    UpdateAppInfo({ commit }, appinfo) {
      console.log('UpdateAppInfo3', appinfo);
      commit('SET_APPINFO', appinfo)
      sessionStorage.setItem('APPINFO', JSON.stringify(appinfo))
    }
  }
}

export default common
