'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"uat"',
  BASE_API: '"http://open-appsp.anji-plus.com/sp"',
})
