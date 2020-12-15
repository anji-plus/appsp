import request from './axios'

export function loginByUsername(username, password, captchaVerification) {
  console.log('用户信息：', username,password)
  const data = {
    username,
    password,
    captchaVerification
  }
  return request({
    url: '/login/v1',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/login/logout',
    method: 'post'
  })
}

export function getUserInfo(token) {
  return request({
    url: '/user/info/v1',
    method: 'get',
    params: { token }
  })
}

