<template>
  <div class="base-view">
    <AppItem class="app-item-view" v-for="item in apps" :key="item.id" :itemModel="item" :isAdmin="isAdmin" @deleteClick="doDelete(item)"
             @editClick="doEdit(item)" @getDetail="getDetail(item)"/>

    <div class="app-add-base-view" v-if="isAdmin" @click="addApp">
      <img class="app-add-logo" src="@/assets/tianjia.png"/>
      <h1 class="app-add-label">新增应用</h1>
    </div>

    <el-dialog :title="dialogType == 0 ? '新增应用' : '编辑应用'" :visible.sync="dialogFormAppEditVisible">
      <el-form :model="appInfo">
        <el-form-item label="应用名" require="true">
          <el-input v-model="appInfo.name" autocomplete="off" placeholder="请输入应用名"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="dialog-btn-ok" @click="changeAppEditClick">确 定</el-button>
        <el-button class="dialog-btn-cancel" @click="dialogFormAppEditVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import AppItem from './component/item'
  import store from '@/store'
  import {saveKeys} from '@/utils/enums'
  import {Message} from 'element-ui'
  import {
    getAllApps,
    getAndroidVersions,
    getIOSVersions,
    appUpdate,
    appInsert,
    appDelete,
    selectMenuPermissionsByAppIDAndUserId
  } from '@/api/main'

  export default {
    name: 'MainPage',
    components: {AppItem},
    data() {
      return {
        msg: 'Welcome to Your Vue.js App',
        // 模型{ appId, appKey, name, updateDate}
        apps: [],
        // 是否显示应用编辑框
        dialogFormAppEditVisible: false,
        // 编辑框类型：0-新增，1-编辑
        dialogType: 0,
        appInfo: {
          name: '',
          appId: 0,
        },
        isEdit: false,
        isAdmin: false
      }
    },
    created() {
      this.isAdmin = this.$store.getters.accessUser.isAdmin == 1;
      console.log('创建首页')
      this.reload(function () {
        var that = this
        store.dispatch('getAndroidVersions').then(() => {
          console.log('安卓所有版本号：', store.getters.androidVersions)
          // 将安卓所有版本号存入sessionStorage
          sessionStorage.setItem(saveKeys().ANDROID_VERSIONS,JSON.stringify(store.getters.androidVersions));
        })
        store.dispatch('getIOSVersions').then(() => {
          console.log('iOS所有版本号：', store.getters.iOSVersions)
          // 将iOS所有版本号存入sessionStorage
          sessionStorage.setItem(saveKeys().IOS_VERSIONS,JSON.stringify(store.getters.iOSVersions));
        })

        store.dispatch('getNotices').then(() => {
          console.log('所有通知类型：', store.getters.notices)
          // 将所有通知类型存入sessionStorage
          sessionStorage.setItem(saveKeys().ALL_NOTICE_TYPES,JSON.stringify(store.getters.notices));
        })
      });
    },
    methods: {
      reload(success) {
        new Promise((resolve, reject) => {
          getAllApps().then(response => {
            const data = response.data
            console.log('所有APP：', data)
            this.apps = data.repData
            success()
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 点击删除
      doDelete(item) {
        console.log('删除：', item);
        new Promise((resolve, reject) => {
          appDelete(item).then(response => {
            if (response != undefined) {
              this.reload();
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 点击编辑
      doEdit(item) {
        this.isEdit = true;
        this.appInfo.name = item.name
        this.appInfo.appId = item.appId
        this.dialogFormAppEditVisible = true;
        this.dialogType = 1;
      },
      addApp() {//添加APP
        this.isEdit = false;
        this.dialogFormAppEditVisible = true;
        this.dialogType = 0;
      },
      changeAppEditClick() {
        if(this.appInfo.name == '') {
          Message({
            message: '请输入应用名',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        this.dialogFormAppEditVisible = false;
        if (this.isEdit) {
          new Promise((resolve, reject) => {
            appUpdate(this.appInfo).then(response => {
              if (response != undefined) {
                this.reload();
              }
              this.appInfo.name = ''
              this.appInfo.appId = ''
              resolve(response)
            }).catch(error => {
              this.appInfo.name = ''
              this.appInfo.appId = ''
              reject(error)
            })
          })
        } else {
          new Promise((resolve, reject) => {
            appInsert(this.appInfo).then(response => {
              if (response != undefined) {
                this.reload();
              }
              this.appInfo.name = ''
              this.appInfo.appId = ''
              resolve(response)
            }).catch(error => {
              this.appInfo.name = ''
              this.appInfo.appId = ''
              reject(error)
            })
          })
        }

      },
      // 进入app详情
      getDetail(item) {
        console.log('进入App详情：', item)
        new Promise((resolve, reject) => {
          selectMenuPermissionsByAppIDAndUserId({appId: item.appId}).then(response => {
            if (response != undefined) {
              // perms
              const data = response.data.repData;
              console.log('获取角色:', data);
              if (data.length > 0){
                var paramsList = [];
                for (let index in data) {
                  paramsList.push(data[index].perms)
                }

                // 进入app详情储存当前appId、appKey、appName到sessionStorage
                let info = {
                  appId: item.appId,
                  appKey: item.appKey,
                  appName: item.name
                };
                sessionStorage.setItem(saveKeys().APP_INFO,JSON.stringify(info));

                this.$store.dispatch('UpdateAppInfo', {appId: item.appId, appKey: item.appKey, perms: paramsList, appName: item.name})
                if (paramsList.length > 0 ) {
                  // 获取第一个权限，作为跳转的默认页面
                  if(paramsList[0] == 'system:user:notice') {
                    this.$router.push({path: '/app/notification'});
                  }else if(paramsList[0] == 'system:user:version') {
                    this.$router.push({path: '/app/version'});
                  }else if(paramsList[0] == 'system:user:members') {
                    this.$router.push({path: '/app/member'});
                  }
                }else {// 没有此App的权限，弹出提示
                  Message({
                    message: '没有编辑此App的权限',
                    type: 'warning',
                    duration: 5 * 1000
                  });
                }
              }
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../styles/common.css";

  .base-view {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    padding: 20px;
    align-items: center;
    justify-content: flex-start;
  }

  .app-item-view {
    margin: 10px;
  }

  .app-add-base-view {
    cursor:pointer;
    margin: 10px;
    width: 23%;
    height: 170px;
    background-color:#fff;
    padding: 15px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    -moz-box-shadow: 0px 0px 7px rgba(54, 141, 255, 0.2);
    -webkit-box-shadow: 0px 0px 7px rgba(54, 141, 255, 0.2);
    box-shadow: 0px 0px 7px rgba(54, 141, 255, 0.2);

    border-width: 1px;
    border-color:green;
    border-style: dashed;
  }

  .app-add-logo {
    object-fit: contain;
    width: 20%;
  }

  .app-add-label {
    font-size: 16px;
  }
</style>
