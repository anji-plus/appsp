import request from './axios'

//公告查询
export function getNoticeTable(appId, pageNo, pageSize) {
    const data = {
        appId: appId,
        pageNo: pageNo,
        pageSize: pageSize
      }
    return request({
        url: '/notice/select/v1',
        method: 'post',
        data
    })
}


//公告新增
export function addNotice(appModel) {
  const data = appModel
  return request({
    url: '/notice/insert/v1',
    method: 'post',
    data
  })
}


//公告启用或禁用
export function editNoticeEnable(appModel) {
  const data = appModel
  return request({
    url: '/notice/enable/v1',
    method: 'post',
    data
  })
}

//公告更新
export function updateNotice(appModel) {
  const data = appModel
  return request({
    url: '/notice/update/v1',
    method: 'post',
    data
  })
}



//公告删除
export function deleteNotice(appModel) {
  const data = appModel
  return request({
    url: '/notice/delete/v1',
    method: 'post',
    data
  })
}
