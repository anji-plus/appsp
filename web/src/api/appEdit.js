import request from './axios'

//版本查询
export function getVersionTable(appId, pageNo, pageSize, platform = '') {
    const data = {
        appId: appId,
        pageNo: pageNo,
        pageSize: pageSize,
        platform: platform
      }
    return request({
        url: '/version/select/v1',
        method: 'post',
        data
    })
}

// 校验是否可以新增版本
export function checkVersionState(appId, platform) {
  const data = {
      appId: appId,
      platform: platform
    }
  return request({
      url: '/version/queryVersionState/v1',
      method: 'post',
      data
  })
}

//版本新增
export function addVersion(appModel) {
  const data = appModel
  return request({
    url: '/version/insert/v1',
    method: 'post',
    data
  })
}


//版本启用或禁用
export function editVersionEnable(appModel) {
  const data = appModel
  return request({
    url: '/version/enable/v1',
    method: 'post',
    data
  })
}

//版本更新
export function updateVersion(appModel) {
  const data = appModel
  return request({
    url: '/version/update/v1',
    method: 'post',
    data
  })
}



//版本删除
export function deleteVersion(appModel) {
  const data = appModel
  return request({
    url: '/version/delete/v1',
    method: 'post',
    data
  })
}



//apk上传
export function apkUpload(appModel) {
  const data = appModel
  return request({
    url: 'upload/uploadFile/v1',
    timeout: 300000,
    method: 'post',
    data
  })
}
