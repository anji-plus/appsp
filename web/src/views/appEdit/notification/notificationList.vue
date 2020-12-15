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
      <el-button style="marginBottom:20px;" type="primary" icon="el-icon-plus" plain @click="addAppNotice">新增公告</el-button>
      <el-table :row-style="getRowClass" :header-row-style="getRowClass" :header-cell-style="getRowClass"
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="公告名称"
          width="80" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="公告标题" width="80" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="公告内容" align="center">
          <template slot-scope="scope">
            <span class="noticeInfo" style="margin-left: 10px">{{ scope.row.details }}</span>
          </template>
        </el-table-column>
        <el-table-column label="公告模板" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.templateTypeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-switch
              :value="scope.row.enableFlag == 0 ? false: true"
              @change="handleSwitchChange(scope.$index)"
              active-color="#4CD964"
              inactive-color="#E5E5EA">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="展示时间" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.startTime }}-{{scope.row.endTime}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template slot-scope="scope">
            <!-- <el-button
              size="mini"
              @click="handleStart(scope.$index, scope.row)"
              :style="{color: scope.row.enableFlag==1? '#65c294': '#faa755'}">{{scope.row.enableFlag==1? "启用": "禁用"}}
            </el-button> -->
            <el-button
              size="mini"
              @click="handleView(scope.$index, scope.row)">查看
            </el-button>
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
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
  import {getNoticeTable, editNoticeEnable, deleteNotice} from '@/api/noticeEdit'
  import {getAppEnums, saveKeys} from '@/utils/enums'
  export default {
    name: 'NoticeList',
    data() {
      return {
        id: '',
        appKey:'',
        appName: '',
        total: 0,
        pageNo: 1,
        pageSize: 10,
        tableData: []
      }
    },
    created() {
      var that = this

      let info = JSON.parse(sessionStorage.getItem(saveKeys().APP_INFO));
      that.id = info.appId;
      that.appKey = info.appKey;
      that.appName = info.appName;
      
      that.reload()
    },
    methods: {
      reload() {
        var that = this
        return new Promise((resolve, reject) => {
          getNoticeTable(that.id, that.pageNo, that.pageSize).then(response => {
            if (response != undefined){
              const data = response.data
              console.log('Version Table：', data)
              that.tableData = data.repData.rows;
              that.total = data.repData.total;
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      handleStart(index, row) { //启用禁用
        console.log(index, row);
        var that = this
        var nData = this.tableData[index];
        if (nData.enableFlag == 1) {
          nData.enableFlag = 0;
        } else {
          nData.enableFlag = 1;
        }
        editNoticeEnable(nData).then(response => {
          that.reload();
        }).catch(error => {
          reject(error)
        })
      },
      // 状态开关
      handleSwitchChange(index) {
        var that = this
        var nData = this.tableData[index];
        if (nData.enableFlag == 1) {
          nData.enableFlag = 0;
        } else {
          nData.enableFlag = 1;
        }
        editNoticeEnable(nData).then(response => {
          that.reload();
        }).catch(error => {
          reject(error)
        })
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
            deleteNotice(nData).then(response => {
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

      handleView(index, row) { //查看
        console.log(index, row);
        this.saveNotificationInfo(this.tableData[index], getAppEnums().SELELCT);
        this.$router.push({
          path: '/app/noticeView',
          query: {type: getAppEnums().SELELCT}
        });
      },
      handleEdit(index, row) { //编辑
        console.log(index, row);
        this.saveNotificationInfo(this.tableData[index], getAppEnums().EDIT);
        this.$router.push({
          path: '/app/noticeEdit',
          query: {type: getAppEnums().EDIT}
        });
      },
      // 新增公告
      addAppNotice() {
        let data = {
          appId: this.id,
        };
        this.saveNotificationInfo(data, getAppEnums().INSERT);
        this.$router.push({
          path: '/app/niticeAdd', query: {
            "data": {
              "appId": this.id,
            }, type: getAppEnums().INSERT
          }
        });
      },
      getRowClass({ row, column, rowIndex, columnIndex }) {
          return "background:#EEF1FF;paddingTop: 20px;paddingBottom: 20px;";
      },
      // 进入详情前需要调用此方法将详情需要的内容存到sessionStorage
      saveNotificationInfo(data, type) {
        let notiDetail = {
          data: data,
          type: type
        };
        sessionStorage.setItem(saveKeys().NOTIFICATION_DETAIL+type,JSON.stringify(notiDetail));
      },
    }
  }
</script>
<style scoped>
  .top{
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    padding: 20px;
    margin: 20px;

  }
  .demonstration {
    font-size: larger;
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

  .btn-view {
    display: flex;
    flex-direction: row;
  }
  .btn-base {
    padding: 10px;
    width: 125px;
    height: 35px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    background: rgba(24,96,238,0.10);
    border: 1px solid #1860EE;
    border-radius: 2px;
    border-radius: 2px;
    margin-right: 20px;
    margin-bottom: 20px;
  }
  .btn-img {
    object-fit: contain;
    width: 16px;
    height: 16px;
  }
  .btn-label {
    width: 100%;
    height: 35px;
    text-align: center;
    line-height: 35px;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #1860EE;
  }
  .noticeInfo {
    overflow: hidden;  /** 隐藏超出的内容 **/
    word-break: break-all;
    text-overflow: ellipsis; /** 多行 **/
    display: -webkit-box; /** 对象作为伸缩盒子模型显示 **/
    -webkit-box-orient: vertical; /** 设置或检索伸缩盒对象的子元素的排列方式 **/
    -webkit-line-clamp: 2; /** 显示的行数 **/
    margin-bottom: .3rem;
  }
</style>
