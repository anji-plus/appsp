const appEnums = {
  'SELELCT': 1, //查看
  'INSERT': 2, //新增
  'EDIT': 3, //编辑
};

const sessionStorageKeys = {
  ACCESSUSER: 'ACCESSUSER',
  APP_INFO: 'APP_INFO',
  VERSION_DETAIL: 'VERSION_DETAIL',
  NOTIFICATION_DETAIL: 'NOTIFICATION_DETAIL',
  ANDROID_VERSIONS: 'ANDROID_VERSIONS',
  IOS_VERSIONS: 'IOS_VERSIONS',
  ALL_NOTICE_TYPES: 'ALL_NOTICE_TYPES'
};


export const getAppEnums = () => {
  return appEnums;
};

export const saveKeys = () => {
  return sessionStorageKeys;
};
