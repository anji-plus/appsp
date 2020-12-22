<template>
  <div>
    <h1 class="title">{{title}}</h1>
    <!-- <div class="top">
      <span class="demonstration">AppKey:</span>
      <span class="demonstration">{{appKey}}</span>
    </div> -->
    <h1 class="title-item">下载信息</h1>
    <div class="top">
      <div class="row">
        <div class="sub-title-view">
          <label class="required-title">*</label>
          <label class="sub-title">下载地址</label>
        </div>
        <input class="download-input" v-model="tableData.downloadUrl" placeholder="下载地址" :disabled="checkDisabled()"/>
        <el-upload
          v-if="tableData.platform=='Android' && showUploadBtn()"
          :show-file-list="false"
          :action="actionUp"
          :file-list="fileList"
          :data="uploadData"
          :http-request="httpRequest"
          :before-upload="beforeFile"
          :on-success="successFile">
          <el-button class="btn-upload" :loading="isUploading">{{isUploading == true ? '正在上传' : '点击上传'}}</el-button>
          <!-- <div class="btn-upload">点击上传</div> -->
        </el-upload>
      </div>
    </div>
    <h1 class="title-item">基础信息</h1>
    <div class="top">
        <div class="row mb20">
          <div class="sub-title-view">
            <label class="required-title">*</label>
            <label class="sub-title">版本名</label>
          </div>
          <el-input class="short-input" v-model="tableData.versionName" onkeyup="value=value.replace(/[^\d\.]/g,'')" placeholder="版本名" :disabled="checkDisabled()"></el-input>

          <div class="sub-title-view">
            <label class="required-title" style="marginLeft: 30px">*</label>
            <label class="sub-title">版本号</label>
          </div>
          <el-input class="short-input" v-model="tableData.versionNumber" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="版本号" :disabled="checkDisabled()"></el-input>
        </div>
        <div class="row">
          <div class="sub-title-view">
            <label class="required-title">*</label>
            <label class="sub-title">更新日志</label>
          </div>
          <el-input class="long-input" :rows="5" type="textarea" v-model="tableData.updateLog" :disabled="type==1"></el-input>
        </div>
    </div>
    <div class="row">
      <h1 class="title-item">版本配置</h1>
      <!-- <el-switch style="marginLeft: 30px;"
        v-model="needForceUpdate" @change="forceUpdateChange"
        active-text="是否需要强制更新">
      </el-switch> -->
      <label class="sub-title-item">不选择代表非强制更新</label>
    </div>
    <div class="top row hBottom">
      <div class="column margin20" style="width: 45%">
        <label class="sub-title">强制更新历史版本</label>
        <el-select class="version-select"
            v-model="appUpdateVersionList"
            multiple
            @change="appVersionSelectChange"
            placeholder="请选择需要强制更新的版本"
            :disabled="type==1 || needForceUpdate==false">
            <el-option
              v-for="item in versionList"
              :key="item.versionName"
              :label="item.versionName"
              :value="item.versionName">
            </el-option>
          </el-select>
      </div>
      <img class="img-or margin20" src="@/assets/img/icon_or.png"/>
      <div class="column margin20" style="width: 45%">
        <label class="sub-title">强制更新系统版本</label>
        <el-select class="version-select"
            v-model="systemVersionList"
            multiple
            @change="systemVersionSelectChange"
            placeholder="请选择需要强制更新的版本"
            :disabled="type==1 || needForceUpdate==false">
            <el-option
              v-for="item in tableData.options"
              :key="item.value"
              :label="item.value"
              :value="item.name">
            </el-option>
          </el-select>
      </div>
    </div>
    <div class="row">
      <h1 class="title-item">灰度发布</h1>
      <el-popconfirm
        confirmButtonText='了解'
        cancelButtonText=''
        icon=""
        iconColor=""
        title="为了保证新版的稳定性，在全量发布之前做阶段发布，版本提交成功后，灰度发布将不可编辑。"
      >
        <img class="icon-tip" slot="reference" src="@/assets/img/icon_tip.png"/>
      </el-popconfirm>

      <el-switch style="marginLeft: 30px;"
        v-model="needCanary" @change="releaseUpdateChange" :disabled="checkDisabled()"
        active-text="">
      </el-switch>
    </div>
    <div class="top row vCenter" v-if="needCanary == true">
      <el-steps :active="canaryDays" align-center style="width: 100%;" finish-status="success">
        <el-step :title="getCanaryValue(0)"></el-step>
        <el-step :title="getCanaryValue(1)"></el-step>
        <el-step :title="getCanaryValue(2)"></el-step>
        <el-step :title="getCanaryValue(3)"></el-step>
        <el-step :title="getCanaryValue(4)"></el-step>
        <el-step :title="getCanaryValue(5)"></el-step>
        <el-step :title="getCanaryValue(6)"></el-step>
      </el-steps>
      <el-button type="primary" icon="el-icon-edit" :disabled="checkDisabled()" circle @click.prevent="showCanaryDialog = true"></el-button>
    </div>

    <div class="row vCenter">
      <div class="btn-submit" @click.prevent="submit" v-if="type!=1">提交</div>
    </div>


    <el-dialog title="发布率" :visible.sync="showCanaryDialog" @close="handleCanaryCancel">
      <el-form :model="canaryForm" class="demo-form-inline" :inline="true">
        <el-form-item label="第一天" label-width="60" style="width: 45%;" align="center">
          <el-input-number v-model="canaryForm.canarys[0]" @change="(value) => handleCanaryChange(value, 0)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第二天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[1]" @change="(value) => handleCanaryChange(value, 1)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第三天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[2]" @change="(value) => handleCanaryChange(value, 2)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第四天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[3]" @change="(value) => handleCanaryChange(value, 3)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第五天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[4]" @change="(value) => handleCanaryChange(value, 4)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第六天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[5]" @change="(value) => handleCanaryChange(value, 5)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="第七天" label-width="60" style="width: 45%" align="center">
          <el-input-number v-model="canaryForm.canarys[6]" @change="(value) => handleCanaryChange(value, 6)" :min="0" :max="100" label=""></el-input-number>
        </el-form-item>
      </el-form>
      <label class="tipLabel">提醒：若想设置三天发布，可以类似20、50、100、100、100、100、100</label>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCanaryCancel">取 消</el-button>
        <el-button type="primary" @click="handleCanaryConfirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addVersion, updateVersion, apkUpload, getVersionTable} from '@/api/appEdit'
  import {getAppEnums, saveKeys} from '@/utils/enums'
  import {getVersions} from '@/api/main'
  import store from '@/store'
  import {Message, Notification} from 'element-ui'

  export default {
    name: 'VersionEdit',
    data() {
      return {
        type: 0,
        // 是否已经发布，如果未发布，则所有内容都能编辑
        published: 0,
        actionUp: "/",
        fileList: [],
        uploadData: {},
        tableData: {
          downloadUrl: "",
          versionName: "",
          versionNumber: "",
          // 强制更新App历史版本
          needUpdateVersionList: [],
          // 强制更新系统版本
          versionConfigStrList: [],
          // 是否开启灰度发布
          canaryReleaseEnable: 0,
          // 灰度发布7天数组
          canaryReleaseStageList: []
        },
        title: "",
        appName: '',
        // 是否正在上传
        isUploading: false,
        // 该app的所有版本号
        versionList: [],
        // 是否需要强更
        needForceUpdate: true,
        // 需要强更的App版本
        appUpdateVersionList: [],
        // 需要强更的App版本-老数据，用于对比
        oldappUpdateVersionList: [],
        // 需要强更的系统版本
        systemVersionList: [],
        // 需要强更的系统版本-老数据，用于对比
        oldsystemVersionList: [],

        // 是否需要灰度发布
        needCanary: false,
        // 是否显示恢复发布编辑的对话框
        showCanaryDialog: false,
        // 已经灰度发布了多少天
        canaryDays: -1,
        // 灰度发布编辑框临时数据
        canaryForm: {
          canarys: [0,0,0,0,0,0,0],
        },
        // 默认发布率
        defaultCanarys: [0,0,0,0,0,0,0],
      }
    },
    created() {
      var that = this

      let info = JSON.parse(sessionStorage.getItem(saveKeys().APP_INFO));
      let appDetail = JSON.parse(sessionStorage.getItem(saveKeys().VERSION_DETAIL));
      console.log('appDetail-created：', appDetail);

      that.tableData = {};
      that.type = appDetail.type;
      that.tableData = appDetail.data;
      that.published = that.tableData.published
      that.appName = info.appName;

      if (that.type == getAppEnums().INSERT) { //新增
        that.title = that.appName + "-" + that.tableData.platform + "新增"
      } else if (that.type == getAppEnums().EDIT) { //编辑
        that.title = that.appName + "-" + that.tableData.platform + "编辑"
      } else { //查看
        that.title = that.appName + "-" + that.tableData.platform + "查看"
      }
      if (that.tableData.platform == "Android") {
        let androidVersions = JSON.parse(sessionStorage.getItem(saveKeys().ANDROID_VERSIONS));
        that.tableData.options = androidVersions;
        // that.tableData.options = store.getters.androidVersions;
      } else {
        let iOSVersions = JSON.parse(sessionStorage.getItem(saveKeys().IOS_VERSIONS));
        that.tableData.options = iOSVersions;
        // that.tableData.options = store.getters.iOSVersions;
      }
      let allValue = that.tableData.options.filter((item) => {
        return item.name == '全部';
      });
      if (allValue.length <= 0) {
        that.tableData.options.unshift({name: '全部', value: '全部'});
      }
      that.uploadData.appId = that.tableData.appId;
      that.appUpdateVersionList = that.tableData.needUpdateVersionList == null ? [] : that.tableData.needUpdateVersionList;
      that.systemVersionList = that.tableData.versionConfigStrList == null ? [] : that.tableData.versionConfigStrList;
      // if (that.appUpdateVersionList.length > 0 || that.systemVersionList.length > 0) {
      //   that.needForceUpdate = true;
      // }else {
      //   that.needForceUpdate = false;
      // }
      that.needCanary = that.tableData.canaryReleaseEnable == 1 ? true : false
      that.systemVersionSelectChange(that.systemVersionList);

      that.getVersionList(that.tableData.appId, that.tableData.platform);

      that.setCanarys()

      // 如果有灰度发布，计算已经过了几天
      if(that.tableData.canaryReleaseEnable > 0) {
        // 已经灰度发布了多少毫秒
        let mSeconds = that.tableData.canaryReleaseUseTime;
        console.log('已经灰度发布了多少毫秒:', mSeconds);
        // 一天有多少毫秒
        let daysMSeconds = 24*60*60*1000;
        console.log('一天有多少毫秒:', daysMSeconds);
        // 向下取整天数
        let days = Math.floor(mSeconds / daysMSeconds);
        console.log('向下取整天数:', days);
        that.canaryDays = days;
      }
    },
    methods: {
      uploadUrl() {

      },
      // 校验目标是否可编辑
      checkDisabled(){
        if(this.published == 0) {
          return false
        }else {
          if(this.type == 2) {
            return false
          }else {
            return true
          }
        }
      },
      // 安卓的上传按钮是否显示
      showUploadBtn() {
        if(this.published == 0) {
          return true
        }else {
          if(this.type == 2) {
            return true
          }
        }
        return false
      },
      // 获取app版本号
      getVersionList(appId, platform) {
        var that = this;
        return new Promise((resolve, reject) => {
          getVersionTable(appId, 1, 999, platform).then(response => {
            if (response != undefined) {
              const data = response.data
              that.versionList = data.repData.rows;
              let allValue = that.versionList.filter((item) => {
                return item.versionName == '全部';
              });
              if (allValue.length <= 0) {
                that.versionList.unshift({id: 'all', versionName: '全部'});
              }
              that.appVersionSelectChange(that.appUpdateVersionList);
              console.log('所有版本号：', that.versionList);
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 是否需要强制更新开关Change
      forceUpdateChange(value) {
        if (value == false) {// 清空所有已选的强更app版本和系统版本
          this.appUpdateVersionList = [];
          this.oldappUpdateVersionList = [];
          this.tableData.needUpdateVersionList = [];
          this.systemVersionList = [];
          this.oldsystemVersionList = [];
          this.tableData.versionConfigStrList = [];
        }
      },
      // app强更版本下拉框Change
      appVersionSelectChange(results) {
        var that = this;

        const allValues = [];
        // 保留所有值
        for (const item of that.versionList) {
          allValues.push(item.versionName)
        }
        // 用来储存上一次的值，可以进行对比
        const oldVal = that.oldappUpdateVersionList.length === 1 ? that.oldappUpdateVersionList[0] : [];
        // 若是全部选择
        if (results.includes('全部')) that.appUpdateVersionList = allValues;
        // 取消全部选中 上次有 当前没有 表示取消全选
        if (oldVal.includes('全部') && !results.includes('全部')) that.appUpdateVersionList = [];
        // 点击非全部选中 需要排除全部选中 以及 当前点击的选项
        // 新老数据都有全部选中
        if (oldVal.includes('全部') && results.includes('全部')) {
          const index = results.indexOf('全部');
          results.splice(index, 1); // 排除全选选项
          that.appUpdateVersionList = results;
        }
        // 全选未选 但是其他选项全部选上 则全选选上 上次和当前 都没有全选
        if (!oldVal.includes('全部') && !results.includes('全部')) {
          if (results.length === allValues.length - 1) that.appUpdateVersionList = ['全部'].concat(results)
        }
        // 储存当前最后的结果 作为下次的老数据
        that.oldappUpdateVersionList[0] = that.appUpdateVersionList;
        that.tableData.needUpdateVersionList = that.appUpdateVersionList.filter((value) => {
          return value != '全部';
        });
      },
      // 系统版本强更下拉框Change
      systemVersionSelectChange(results) {
        var that = this;
        const allValues = [];
        // 保留所有值
        for (const item of that.tableData.options) {
          allValues.push(item.name);
        }
        // console.log('所有系统版本:', allValues);
        // 用来储存上一次的值，可以进行对比
        const oldVal = that.oldsystemVersionList.length === 1 ? that.oldsystemVersionList[0] : [];
        // 若是全部选择
        if (results.includes('全部')) that.systemVersionList = allValues;
        // 取消全部选中 上次有 当前没有 表示取消全选
        if (oldVal.includes('全部') && !results.includes('全部')) that.systemVersionList = [];
        // 点击非全部选中 需要排除全部选中 以及 当前点击的选项
        // 新老数据都有全部选中
        if (oldVal.includes('全部') && results.includes('全部')) {
          const index = results.indexOf('全部');
          results.splice(index, 1); // 排除全选选项
          that.systemVersionList = results;
        }
        // 全选未选 但是其他选项全部选上 则全选选上 上次和当前 都没有全选
        if (!oldVal.includes('全部') && !results.includes('全部')) {
          if (results.length === allValues.length - 1) that.systemVersionList = ['全部'].concat(results);
        }
        // 储存当前最后的结果 作为下次的老数据
        that.oldsystemVersionList[0] = that.systemVersionList;
        that.tableData.versionConfigStrList = that.systemVersionList.filter((value) => {
          return value != '全部';
        });
      },
      // 灰度发布开关Change
      releaseUpdateChange(value) {
        var that = this;
        that.tableData.canaryReleaseEnable = value == true ? 1 : 0;
        that.needCanary = value;
        if (value == 1) {
          if(that.tableData.canaryReleaseStageList == null || that.tableData.canaryReleaseStageList.length <= 0) {
            that.tableData.canaryReleaseStageList = that.defaultCanarys;
            that.showCanaryDialog = true;
          }
        }else {
          that.tableData.canaryReleaseStageLis = [];
        }
      },
      // 灰度发布编辑
      handleCanaryChange(value, index) {
        var that = this;
        that.canaryForm.canarys[index] = value;
      },
      // 获取灰度发布每天的数据
      getCanaryValue(index, type='String') {
        var that = this;
        if(that.tableData.canaryReleaseStageList == null || that.tableData.canaryReleaseStageList.length <= 0) {
          if(type == 'String') {
            return '未设置';
          }else {
            return 0;
          }
        }else if(that.tableData.canaryReleaseStageList[index] == null) {
          if(type == 'String') {
            return '未设置';
          }else {
            return 0;
          }
        }else {
          if(type == 'String') {
            return that.tableData.canaryReleaseStageList[index] + '%';
          }else {
            return that.tableData.canaryReleaseStageList[index];
          }
        }
      },
      // 灰度发布编辑框确定按钮
      handleCanaryConfirm() {
        var that = this;
        if(that.canaryForm.canarys.filter((value) => {
          return value > 0;
        }).length <= 0) {
          Message({
            message: '不能全部设置为0',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        for (var i = 1; i < that.canaryForm.canarys.length; i++) {
          let last = that.canaryForm.canarys[i-1];
          let current = that.canaryForm.canarys[i];
          if(last > current) {
            Message({
              message: '后一天的值不能小于前一天',
              type: 'warning',
              duration: 5 * 1000
            });
            return;
          }
        }
        that.tableData.canaryReleaseStageList = [];
        that.tableData.canaryReleaseStageList = that.tableData.canaryReleaseStageList.concat(that.canaryForm.canarys);
        that.canaryForm.canarys = [];
        that.canaryForm.canarys = that.canaryForm.canarys.concat(that.canaryForm.canarys);
        that.showCanaryDialog = false;
      },
      // 灰度发布编辑框取消按钮
      handleCanaryCancel() {
        this.setCanarys()
        this.showCanaryDialog = false;
      },
      // 设置灰度发布编辑框的默认值
      setCanarys() {
        var that = this;
        that.canaryForm.canarys = [];
        if(that.tableData.canaryReleaseStageList != null) {
          that.canaryForm.canarys = that.canaryForm.canarys.concat(that.tableData.canaryReleaseStageList);
        }else {
          that.canaryForm.canarys = that.canaryForm.canarys.concat(that.defaultCanarys);
        }
      },
      // 提交之前的校验
      validateInfo() {
        var that = this;
        if(that.tableData.canaryReleaseEnable > 0 && (that.tableData.canaryReleaseStageList == null || that.tableData.canaryReleaseStageList.length <= 0)) {
          Message({
            message: '开启了灰度发布但是并未设置内容',
            type: 'warning',
            duration: 5 * 1000
          });
          return false;
        }
        if(!/^\d+(.\d+)*$/.test(that.tableData.versionName)) {
          Message({
            message: '版本名输入有误，请检查',
            type: 'warning',
            duration: 5 * 1000
          });
          return false;
        }
        return true;
      },
      submit() { //提交
        if (this.validateInfo() == false) {
          return;
        }
        if (this.type == getAppEnums().INSERT) { //新增
          this.addAppVersion();
        } else if (this.type == getAppEnums().EDIT) { //编辑
          this.updateAppVersion();
        }
      },
      addAppVersion() {//新增
        var that = this
        return new Promise((resolve, reject) => {
          addVersion(this.tableData).then(response => {
            if (response != undefined) {
              this.$router.go(-1)
              this.$store.dispatch('delView', this.$route);
              Message({
                message: "新增成功",
                type: 'success',
                duration: 1000
              })
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      updateAppVersion() {//版本更新
        var that = this
        return new Promise((resolve, reject) => {
          updateVersion(this.tableData).then(response => {
            if (response != undefined) {
              this.$router.go(-1)
              this.$store.dispatch('delView', this.$route);
              Message({
                message: "更新成功",
                type: 'success',
                duration: 1000
              })
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      /**
       * 文件上传之前 判断格式
       * */
      beforeFile(file) {
        let name = file.name
        let type = name.substring(name.lastIndexOf('.') + 1)
        if (type !== 'apk') {
          this.$message('请上传apk文件格式');
          return false
        }
      },
      /**
       *文件上传成功时的钩子
       * */
      successFile() {
        this.$message({
          message: '上传成功',
          type: 'success',
          duration: 1000
        })
      },
      httpRequest(data) {
        var formData = new FormData();
        formData.append('fileUpload', data.file);
        formData.append('appId', data.data.appId);
        if (!data.data.appId){
          Message({
            message: "appId不能为空",
            type: 'error',
            duration: 1000
          })
          return;
        }
        this.isUploading = true;
        apkUpload(formData).then(response => {
          if (response != undefined) {
            const data = response.data.repData;
            this.tableData.downloadUrl = data.downloadUrl;
            this.tableData.versionName = data.versionName;
            this.tableData.versionNumber = data.versionNumber;
            this.successFile();
            this.isUploading = false;
          }
        }, error => {
          this.isUploading = false;
        })
      },
    }
  }
</script>
<style scoped>
  .top {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    padding: 20px;
    margin: 20px;
  }
  .title {
    font-family: PingFangSC-Medium;
    font-size: 21px;
    color: #666666;
    margin-left: 20px;
  }
  .title-item {
    font-family: PingFangSC-Medium;
    font-size: 18px;
    color: #666666;
    margin-left: 20px;
  }
  .sub-title-item {
    color: darkgrey;
    font-size: 12px;
    text-align: end;
    margin-left: 10px;
  }
  .sub-title-view {
    width: 110px;
  }
  .sub-title {
    font-size: 14px;
    /* color: #2A2A2A; */
    text-align: right;
    line-height: 22px;
    margin-right: 20px;
  }
  .required-title {
    font-family: PingFangSC-Regular;
    font-size: 12px;
    color: #EA1515;
    text-align: right;
  }
  .row {
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .column {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  .vCenter {
    justify-content: center;
  }
  .hCenter {
    align-items: center;
  }
  .hBottom {
    align-items: flex-end;
  }
  .download-input {
    width: 60vw;
    height: 50px;
    outline:0;
    border-bottom-left-radius: 5px;
    border-top-left-radius: 5px;
    border-style: solid;
    border-width: 1px;
    border-style: solid;
    border-color:#DDDEE0;
    text-indent: 20px;
    color: #565656;
  }
  .download-input:focus {
    border-width: 1px;
    border-style: solid;
    border-color: #0284FF;
  }
  .btn-upload {
    height: 50px;
    color: white;
    line-height: 30px;
    padding: 10px;
    border-bottom-right-radius: 5px;
    border-top-right-radius: 5px;
    background-color: #1E3FB3;

    border-bottom-left-radius: 0px;
    border-top-left-radius: 0px;
  }
  .btn-submit {
    width: 200px;
    height: 50px;
    color: white;
    text-align: center;
    line-height: 30px;
    padding: 10px;
    border-radius: 5px;
    background-color: #1E3FB3;
  }
  .version-select {
    width: 100%;
  }
  .short-input {
    width: 20vw;
  }
  .long-input {
    width: 80%;
  }
  .mb20 {
    margin-bottom: 20px;
  }
  .margin20 {
    margin: 20px;
  }
  .img-or {
    width: 30px;
    height: 30px;
  }
  .icon-tip {
    width: 20px;
    height: 20px;
    margin-left: 10px;
    cursor: pointer;
  }
  input::-webkit-input-placeholder { 
    /* WebKit browsers */ 
    color: #C2C2C2; 
  }
  .tipLabel {
    color: #EA1515;
  }
</style>
