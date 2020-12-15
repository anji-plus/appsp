<template>
  <div>
    <h1 class="title">{{title}}</h1>
    <h1 class="title-item">公告名</h1>
    <div class="top">
      <el-form ref="tableData" :inline="true" :model="tableData" class="demo-form-inline" label-width="90px">
        <el-form-item label="公告名称" :rules="{required: true}">
          <el-input v-model="tableData.name" placeholder="公告名称" :disabled="type==1"></el-input>
        </el-form-item>
        <el-form-item label="公告模板" :required="true">
          <el-select v-model="tableData.templateTypeName" placeholder="请选择" :disabled="type==1" @change="selectChange">
            <el-option
              v-for="item in tableData.options"
              :key="item.id"
              :label="item.value"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <h1 class="title-item">公告信息</h1>
    <div class="top">
      <el-form ref="tableData" :model="tableData" label-width="100px">
        <el-form-item label="公告标题" :rules="{required: true}" inline-message="true">
          <el-input v-model="tableData.title" placeholder="公告标题" :disabled="type==1"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" :required="true">
          <el-input type="textarea" v-model="tableData.details" :disabled="type==1"></el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-button type="primary" @click.prevent="submit" :disabled="type==1">提交</el-button>
        </el-form-item> -->
      </el-form>
    </div>
    <h1 class="title-item">持续时间</h1>
    <div class="top">
      <el-form :inline="true" :model="tableData" class="demo-form-inline" label-width="90px">
          <el-form-item label="开始时间" :required="true" :disabled="type==1">
            <el-date-picker
              v-model="tableData.startTime"
              type="datetime"
              :editable="false"
              prefix-icon="el-icon-caret-bottom"
              value-format="yyyy-MM-dd HH:mm:ss"
              :clearable='false'
              :disabled="type==1">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" :required="true">
            <el-date-picker
              v-model="tableData.endTime"
              type="datetime"
              :editable="false"
              prefix-icon="el-icon-caret-bottom"
              value-format="yyyy-MM-dd HH:mm:ss"
              :clearable='false'
              :disabled="type==1">
            </el-date-picker>
          </el-form-item>

        </el-form>
    </div>
    <div class="row vCenter" v-if="type!=1">
      <div class="btn-submit" @click.prevent="submit" :disabled="type==1">提交</div>
    </div>
  </div>
</template>

<script>
  import {addNotice, updateNotice} from '@/api/noticeEdit'
  import {getAppEnums, saveKeys} from '@/utils/enums'
  import {getVersions} from '@/api/main'
  import store from '@/store'
  import {Message, Notification} from 'element-ui'


  export default {
    name: '公告新增',
    data() {
      return {
        type: 0,
        tableData: {},
        title: "",
        value1: '',
        startPlaceholder: '',
        endPlaceholder: '',
        appName: '',
      }
    },
    created() {
      var that = this

      let routeType = that.$route.query.type;
      let info = JSON.parse(sessionStorage.getItem(saveKeys().APP_INFO));
      let appDetail = JSON.parse(sessionStorage.getItem(saveKeys().NOTIFICATION_DETAIL+routeType));

      that.type = appDetail.type;
      that.tableData = appDetail.data;
      that.appName = info.appName;

      console.log(that.tableData)
      if (that.type == getAppEnums().INSERT) { //新增
        that.title = that.appName + "-公告新增"
      } else if (that.type == getAppEnums().EDIT) { //编辑
        that.title = that.appName + "-公告编辑"
      } else { //查看
        that.title = that.appName + "-公告查看"
      }
      let allNotices = JSON.parse(sessionStorage.getItem(saveKeys().ALL_NOTICE_TYPES));
      that.tableData.options = allNotices;
      // that.tableData.options = store.getters.notices;
    },
    methods: {
      selectChange(e) {
        console.log('selectChange:', e.value, e.name)
        console.log(this.tableData.templateTypeName);
        this.tableData.templateTypeName = e.value;
        this.tableData.templateType = e.name;
      },
      submit() { //提交
        if (this.type == getAppEnums().INSERT) { //新增
          this.addAppNotices();
        } else if (this.type == getAppEnums().EDIT) { //编辑
          this.updateAppNotices();
        }
      },
      addAppNotices() {//新增
        var that = this
        return new Promise((resolve, reject) => {
          addNotice(this.tableData).then(response => {
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
      updateAppNotices() {//更新
        var that = this
        return new Promise((resolve, reject) => {
          updateNotice(this.tableData).then(response => {
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
      }
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
  .row {
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .vCenter {
    justify-content: center;
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
</style>
