<template>
    <div>
        <h1 class="title">{{appName}}</h1>
        <el-tabs class="tabBase" v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="Android" name="adnroid">
                <h1 class="title-item">当前版本</h1>
                <div class="top">
                    <el-form ref="form" :model="form" label-width="80px">
                        <el-form :inline="true" :model="form" class="demo-form-inline" label-width="80px">
                            <el-form-item label="版本名:">
                                <label>{{form.versionName}}</label>
                            </el-form-item>
                            <el-form-item label="版本号:">
                                <label>{{form.versionCode}}</label>
                            </el-form-item>
                        </el-form>
                        <el-form-item label="是否强更:">
                            <label>{{form.force}}</label>
                        </el-form-item>
                        <el-form-item label="下载地址:">
                            <label>{{form.downloadUrl}}</label>
                        </el-form-item>

                        <!-- <el-form-item label="活动区域">
                            <el-select v-model="form.region" placeholder="请选择活动区域">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="活动时间">
                            <el-col :span="11">
                            <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>
                            </el-col>
                            <el-col class="line" :span="2">-</el-col>
                            <el-col :span="11">
                            <el-time-picker placeholder="选择时间" v-model="form.date2" style="width: 100%;"></el-time-picker>
                            </el-col>
                        </el-form-item>
                        <el-form-item label="即时配送">
                            <el-switch v-model="form.delivery"></el-switch>
                        </el-form-item>
                        <el-form-item label="活动性质">
                            <el-checkbox-group v-model="form.type">
                            <el-checkbox label="美食/餐厅线上活动" name="type"></el-checkbox>
                            <el-checkbox label="地推活动" name="type"></el-checkbox>
                            <el-checkbox label="线下主题活动" name="type"></el-checkbox>
                            <el-checkbox label="单纯品牌曝光" name="type"></el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                        <el-form-item label="特殊资源">
                            <el-radio-group v-model="form.resource">
                            <el-radio label="线上品牌商赞助"></el-radio>
                            <el-radio label="线下场地免费"></el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="活动形式">
                            <el-input type="textarea" v-model="form.desc"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">立即创建</el-button>
                            <el-button>取消</el-button>
                        </el-form-item> -->
                    </el-form>
                </div>

                <h1 class="title-item">历史版本</h1>
                <div class="top">
                    <el-table
                        :data="tableData"
                        style="width: 100%">
                        <el-table-column
                            prop="date"
                            label="日期"
                            width="180">
                        </el-table-column>
                        <el-table-column
                            prop="name"
                            label="姓名"
                            width="180">
                        </el-table-column>
                        <el-table-column
                            prop="address"
                            label="地址">
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
            <el-tab-pane label="iOS" name="ios">iOS</el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
  export default {
    data() {
      return {
        appId: '',
        appKey: '',
        appName: '',

        activeName: 'adnroid',

        form: {
            versionName: '1.0.1',
            versionCode: '29',
            force: '否',
            downloadUrl: 'http://10.108.26.127/download/64cf5a851f37c6c0ab7a3186a2377d5df9e3b042-233e-4ac8-868d-ac3dff5771fc.apk',

          name: '123123',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },

        tableData: [{
            date: '2016-05-02',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
          }, {
            date: '2016-05-04',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1517 弄'
          }, {
            date: '2016-05-01',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1519 弄'
          }, {
            date: '2016-05-03',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1516 弄'
          }],
      };
    },
    created() {
      var that = this

      that.appId = that.$store.getters.appInfo.appId;
      that.appKey = that.$store.getters.appInfo.appKey;
      that.appName = that.$store.getters.appInfo.appName;

      that.reload()
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
</script>

<style scoped>
    .tabBase {
        margin-left: 20px;
        margin-right: 20px;
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
    .top {
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        padding: 20px;
        margin: 20px;
    }
</style>