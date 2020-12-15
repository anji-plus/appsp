const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  versionLastTab: state => state.app.versionLastTab,
  accessUser: state => state.user.accessUser,
  permission_routes: state => state.permission.routes,
  addRouters: state=> state.permission.addRoutes,
  roles: state=> state.user.roles,
  androidVersions: state=> state.common.androidVersions,
  iOSVersions: state=> state.common.iOSVersions,
  notices: state=> state.common.notices,
  appInfo: state=> state.common.appInfo
}
export default getters
