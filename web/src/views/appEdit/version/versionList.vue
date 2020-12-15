<template>
  <div>
    <h1 class="title">{{appName}}</h1>
    <h1 class="title-item">应用信息</h1>
    <div class="top">
      <span class="demonstration">AppKey:</span>
      <span class="demonstration">{{appKey}}</span>
    </div>
    <h1 class="title-item">数据信息</h1>
    <div class="top">
      <el-tabs v-model="platform" @tab-click="handlePlatformChange">
        <el-tab-pane label="Android" name="Android">
          <div class="btn-view">
            <!-- <el-button type="primary" @click="handleAndroid">新增Android版本</el-button> -->
            <div class="btn-base btn-iOS" @click="handleAndroid">
              <img class="btn-img" src="@/assets/img/android.png"/>
              <h1 class="btn-label btn-label-iOS">新增安卓版本</h1>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="iOS" name="iOS">
          <div class="btn-view">
            <!-- <el-button type="success" @click="handleiOS">新增iOS版本</el-button> -->
            <div class="btn-base btn-iOS" @click="handleiOS">
              <img class="btn-img" src="@/assets/img/iOS.png"/>
              <h1 class="btn-label btn-label-iOS">新增iOS版本</h1>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <el-table v-loading="tableLoading" :row-style="getRowClass" :header-row-style="getRowClass" :header-cell-style="getRowClass"
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="平台"
          width="80" align="center">
          <template slot-scope="scope">
            <!-- <span style="margin-left: 10px">{{ scope.row.platform }}</span> -->
            <img class="btn-img" src="@/assets/img/android.png" v-if="scope.row.platform == 'Android'"/>
            <img class="btn-img" src="@/assets/img/iOS.png" v-else/>
          </template>
        </el-table-column>
        <el-table-column label="版本名" width="80" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.versionName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="版本号" width="80" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.versionNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="日志" align="center">
          <template slot-scope="scope">
            <span class="show2Lines" style="margin-left: 10px">{{ scope.row.updateLog }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下载地址" align="center">
          <template slot-scope="scope">
            <span class="show2Lines" style="margin-left: 10px">{{ scope.row.downloadUrl }}</span>
          </template>
        </el-table-column>
        <el-table-column label="灰度发布" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px" v-if="scope.row.canaryReleaseEnable == 0">{{'未启用'}}</span>
            <span style="margin-left: 10px;color: #5CC1FC" v-else>{{'已启用'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="上下架" align="center">
          <template slot-scope="scope">
            <el-switch
              :value="scope.row.enableFlag == 0 ? false: true"
              @change="handleSwitchChange(scope.$index)"
              active-color="#4CD964"
              inactive-color="#E5E5EA">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleView(scope.$index, scope.row)">查看
            </el-button>
            <el-button
              size="mini" v-if="scope.row.enableFlag == 1"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <!-- <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNo"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {getVersionTable, editVersionEnable, deleteVersion} from '@/api/appEdit'
  import {getAppEnums, saveKeys} from '@/utils/enums'
  import store from '@/store'

  export default {
    name: '版本管理',
    data() {
      return {
        appId: '',
        appKey: '',
        appName: '',
        total: 0,
        pageNo: 1,
        pageSize: 10,
        tableData: [],

        // Table是否正在加载
        tableLoading: false,
        // 当前展示平台
        platform: 'Android'
      }
    },
    created() {
      var that = this
      that.platform = store.getters.versionLastTab;
      let info = JSON.parse(sessionStorage.getItem(saveKeys().APP_INFO));
      that.appId = info.appId;
      that.appKey = info.appKey;
      that.appName = info.appName;

      that.reload()
    },
    methods: {
      reload() {
        var that = this
        that.tableLoading = true;
        return new Promise((resolve, reject) => {
          getVersionTable(that.appId, that.pageNo, that.pageSize, that.platform).then(response => {
            if (response != undefined) {
              const data = response.data
              console.log('Version Table：', data)
              that.tableData = data.repData.rows;
              that.total = data.repData.total;
            }
            that.tableLoading = false;
            resolve(response)
          }).catch(error => {
            that.tableLoading = false;
            reject(error)
          })
        })
      },
      // Tab页切换：Android、iOS
      handlePlatformChange(tab, event) {
        this.reload()
        this.$store.dispatch('versionTabChange', tab.index == 0 ? 'Android' : 'iOS');
      },
      // 状态开关
      handleSwitchChange(index) {
        var that = this
        var nData = that.tableData[index];
        var param = JSON.parse(JSON.stringify(nData));
        if (param.enableFlag == 1) {
          param.enableFlag = 0;
        } else {
          param.enableFlag = 1;
        }
        // console.log('nData.enableFlag ' + nData.enableFlag)
        editVersionEnable(param).then(response => {
          that.reload();
          console.log('开关请求成功');
        }).catch(error => {
          reject(error)
          console.log('开关请求失败');
        })
      },
      handleView(index, row) { //查看
        console.log('查看tableData:', this.tableData[index]);
        this.saveAppInfo(this.tableData[index], getAppEnums().SELELCT);
        this.$router.push({
          path: '/app/versionEdit',
          query: {"data": this.tableData[index].platform, type: getAppEnums().SELELCT}
        });
      },
      handleEdit(index, row) {
        console.log('编辑tableData:', this.tableData[index]);
        this.saveAppInfo(this.tableData[index], getAppEnums().EDIT);
        this.$router.push({
          path: '/app/versionEdit',
          query: {"data": this.tableData[index].platform, type: getAppEnums().EDIT}
        });

        // if (this.tableData[home].platform == "Android"){
        //
        // } else if (this.tableData[home].platform == "iOS"){
        //   this.$router.push({path: '/app/versionEditiOS', query:{"data": this.tableData[home], type: getAppEnums().EDIT}});
        // }
      },
      handleDelete(index, row) { //删除
        console.log(index, row);
        var that = this
        that.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            var nData = this.tableData[index];
            deleteVersion(nData).then(response => {
              that.reload();
            }).catch(error => {
              reject(error)
            })
        }).catch(() => {

        });
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageSize = val;
        this.reload();

      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageNo = val;
        this.reload();
      },
      // 新增安卓
      handleAndroid() {
        let data = {
          appId: this.appId,
          platform: "Android",
          downloadUrl: "",
          versionName: "",
          versionNumber: "",
        };
        this.saveAppInfo(data, getAppEnums().INSERT);
        this.$router.push({
          path: '/app/versionAddAndroid',
          query: {
            "data": {
              "appId": this.appId,
              "platform": "Android",
              "downloadUrl": "",
              "versionName": "",
              "versionNumber": "",
            }, type: getAppEnums().INSERT
          }
        });

      },
      // 新增iOS
      handleiOS() {
        let data = {
          appId: this.appId,
          platform: "iOS",
          downloadUrl: "",
          versionName: "",
          versionNumber: "",
        };
        this.saveAppInfo(data, getAppEnums().INSERT);
        this.$router.push({
          path: '/app/versionAddiOS',
          query: {
            "data": {
              "appId": this.appId,
              "platform": "iOS",
              "downloadUrl": "",
              "versionName": "",
              "versionNumber": "",
            }, type: getAppEnums().INSERT
          }
        });
      },
      getRowClass({ row, column, rowIndex, columnIndex }) {
          return "background:#EEF1FF;paddingTop: 20px;paddingBottom: 20px;";
      },
      // 进入详情前需要调用此方法将详情需要的内容存到sessionStorage
      saveAppInfo(data, type) {
        let versionDetail = {
          data: data,
          type: type
        };
        sessionStorage.setItem(saveKeys().VERSION_DETAIL,JSON.stringify(versionDetail));
      },
    }
  }
</script>
<style scoped>
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

  .demonstration {
    font-size: larger;
  }

  .btn-view {
    display: flex;
    flex-direction: row;
  }
  .btn-base {
    cursor: pointer;
    padding: 10px;
    width: 130px;
    height: 35px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }
  .btn-android {
    background: rgba(24,96,238,0.10);
    border: 1px solid #1860EE;
    border-radius: 2px;
    border-radius: 2px;
    margin-right: 20px;
    margin-bottom: 20px;
  }
  .btn-iOS {
    background: rgba(92,193,252,0.10);
    border: 1px solid #5CC1FC;
    border-radius: 2px;
    border-radius: 2px;
    margin-bottom: 20px;
  }
  .btn-img {
    object-fit: contain;
    width: 16px;
    height: 16px;
    margin-right: 2px;
  }
  .btn-label {
    width: 100%;
    height: 35px;
    text-align: center;
    line-height: 35px;
    font-family: PingFangSC-Regular;
    font-size: 14px;
  }
  .btn-label-android {
    color: #1860EE;
  }
  .btn-label-iOS {
    color: #5CC1FC;
  }
  .show2Lines {
    overflow: hidden;  /** 隐藏超出的内容 **/
    word-break: break-all;
    text-overflow: ellipsis; /** 多行 **/
    display: -webkit-box; /** 对象作为伸缩盒子模型显示 **/
    -webkit-box-orient: vertical; /** 设置或检索伸缩盒对象的子元素的排列方式 **/
    -webkit-line-clamp: 2; /** 显示的行数 **/
    margin-bottom: .3rem;
  }
</style>
