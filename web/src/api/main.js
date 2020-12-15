import request from './axios'

//查询所有app信息
export function getAllApps() {
    return request({
        url: '/userAppRole/selectAppInfo/v1',
        method: 'post'
    })
}

//根据类型获取字典信息
export function getVersions(type) {
    const data = {
        type: type
      }
    return request({
        url: '/dict/selectByType/v1',
        method: 'post',
        data
    })
}

//添加字典信息
export function addDicts(model) {
  const data = model
  return request({
    url: '/dict/insert/v1',
    method: 'post',
    data
  })
}


//查询所有角色
export function getRoles() {
  const data = {}
  return request({
    url: '/role/select/v1',
    method: 'post',
    data
  })
}

//根据角色id 更新角色
export function updateRoleByRoleId(model) {
  const data = model
  return request({
    url: '/role/update/v1',
    method: 'post',
    data
  })
}




//根据角色roleId查询对应菜单
export function selectByRoleId(model) {
  const data = model
  return request({
    url: '/roleMenu/selectByRoleId/v1',
    method: 'post',
    data
  })
}

//查询所有菜单
export function selectAllMenus() {
  const data = {}
  return request({
    url: '/menu/select/v1',
    method: 'post',
    data
  })
}


//更新角色菜单关联表
export function roleMenuUpdateByMenuIds(model) {
  const data = model
  return request({
    url: '/roleMenu/update/v1',
    method: 'post',
    data
  })
}

//新增角色及菜单
export function insertRoleAndMenu(model) {
  const data = model
  return request({
    url: '/role/insertRoleAndMenu/v1',
    method: 'post',
    data
  })
}


//根据项目id查询未加入对应应用管理的用户
export function selectNoJoinApplicationByAppId(appId) {
  const data = {
    appId: appId
  }
  return request({
    url: '/role/selectNoJoinApplicationByAppId/v1',
    method: 'post',
    data
  })
}

// 删除用户项目关联表数据
export function userAppRoleDelete(model) {
  const data = model
  return request({
    url: '/userAppRole/delete/v1',
    method: 'post',
    data
  })
}

// 更新应用名
export function appUpdate(model) {
  const data = model
  return request({
    url: '/app/update/v1',
    method: 'post',
    data
  })
}

// 新建应用
export function appInsert(model) {
  const data = model
  return request({
    url: '/app/insert/v1',
    method: 'post',
    data
  })
}

// 删除应用
export function appDelete(model) {
  const data = model
  return request({
    url: '/app/delete/v1',
    method: 'post',
    data
  })
}
// 根据 appId 和 userId 查询菜单信息
export function selectMenuPermissionsByAppIDAndUserId(model) {
  const data = model
  return request({
    url: '/userAppRole/selectMenuPermissionsByAppIDAndUserId/v1',
    method: 'post',
    data
  })
}
