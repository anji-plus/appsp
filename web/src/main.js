import Vue from 'vue'
import App from './App'
import router from './router'
import store from "./store"

import ElementUI from "element-ui"
Vue.use(ElementUI)
import 'element-ui/lib/theme-chalk/index.css'

import 'normalize.css/normalize.css'
import './styles/index.scss'

import "./icons"
// import './mock' // mock 本地模拟数据 后端联调注释掉
// 导入登录权限控制
import './permission'

import 'github-markdown-css'

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
