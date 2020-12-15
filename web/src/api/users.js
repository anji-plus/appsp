import request from './axios'

//查询用户
export function getUsers(username,name ,pageNo, pageSize) {
  const data = {
    username: username,
    name: name,
    pageNo: pageNo,
    pageSize: pageSize,
  }
  return request({
    url: '/user/queryByPage/v1',
    method: 'post',
    data
  })
}


//添加用户
// user/addUser/v1
export function addUsers(user) {
  const data = user
  return request({
    url: '/user/addUser/v1',
    method: 'post',
    data
  })
}
//删除用户
//
export function deleteUsers(user) {
  const data = user
  return request({
    url: '/user/deleteUserById/v1',
    method: 'post',
    data
  })
}



//userAppRole/selectByAppId/v1
//根据APPID 查询该APP所有用户
export function selectUsersByAppId(appId) {
  const data = {
    appId : appId
  }
  return request({
    url: '/userAppRole/selectByAppId/v1',
    method: 'post',
    data
  })
}

// 新增用户应用角色关联
export function userAppRoleInsert(item) {
  const data = item
  return request({
    url: '/userAppRole/insert/v1',
    method: 'post',
    data
  })
}


// 新增用户应用角色关联
// 更改项目用户角色
export function userAppRoleUpdate(item) {
  const data = item
  return request({
    url: '/userAppRole/update/v1',
    method: 'post',
    data
  })
}
