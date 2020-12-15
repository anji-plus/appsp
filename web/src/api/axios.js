import axios from 'axios'
import { Message, Notification } from 'element-ui'
import store from '@/store'
import router from '@/router'
import { getToken,removeName, removeIsAdmin, removeToken } from '@/utils/storage'


axios.defaults.baseURL = process.env.BASE_API;

// create an axios instance
const service = axios.create({
  withCredentials: true,
  timeout: 40000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=UTF-8'
  }
})

// request interceptor
service.interceptors.request.use(
  config => {
    if (store.getters.accessUser != null && store.getters.accessUser.token) {
      config.headers['Authorization'] = store.getters.accessUser.token
    }
    return config
  },
  error => {
    console.log(error);

    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log(res, 'res')
    if (res.repCode == 'noLogin' || res.repCode == '0104') {
      Message({
        message: res.repMsg,
        type: 'warning',
        duration: 5 * 1000
      })
      store.dispatch('LogOut').then(res => {
        router.replace({path: '/login'})
      })
    } else if (res.repCode != '0000') {
      Message({
        message: res.repMsg,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return response
    }
  },
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过 xmlhttprequest 来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
