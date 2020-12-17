'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://open-appsp.anji-plus.com/sp"', //开发环境
  // BASE_API: '"http://10.108.14.27:8081/sp"', //开发环境
})
