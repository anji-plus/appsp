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
      <el-button style="marginBottom:20px;" type="primary" icon="el-icon-plus" plain @click="handleMember">添加成员</el-button>
      <el-table :row-style="getRowClass" :header-row-style="getRowClass" :header-cell-style="getRowClass"
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="用户名" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账号" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column label="角色" align="center">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.roleName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              :disabled="userId==scope.row.userId"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              :disabled="userId==scope.row.userId"
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
      <el-dialog :title="addMemberInfo.title" :visible.sync="dialogFormMemberVisible">
        <el-form :model="addMemberInfo">
          <el-form-item label="用户信息" require="true">
            <el-select :disabled="!isAdd" v-model="selectUserId" @change="elSelectchange" placeholder="请选择" filterable>
              <el-option
                v-for="item in addMemberInfo.users"
                :key="item.userId"
                :label="item.name"
                :value="item.userId">
                <span style="float: left">{{ item.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{item.username}}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="isAdd" label="已选择">
            <span style="float: left; color: #0384ff;">{{ addMemberInfo.selectItem.name }}</span>
            <span style="float: left; margin-left: 30px; color: #0384ff; font-size: 13px">{{addMemberInfo.selectItem.username}}</span>
          </el-form-item>
          <el-form-item>
            <el-radio-group v-model="addMemberInfo.roleId">
              <el-radio v-for="item in roles"
                        :key="item.value"
                        :label="item.roleId"
                        :value="item.roleName">{{item.roleName}}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button class="dialog-btn-ok" @click="changeMemberClick">确 定</el-button>
          <el-button class="dialog-btn-cancel" @click="dialogFormMemberVisible = false">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {selectUsersByAppId, userAppRoleInsert, userAppRoleUpdate} from '@/api/users'
  import {selectNoJoinApplicationByAppId, getRoles, userAppRoleDelete} from '@/api/main'
  import {saveKeys} from '@/utils/enums'
  import {Message} from 'element-ui'

  export default {
    name: 'VersionList',
    data() {
      return {
        id: '',
        appKey: '',
        appName: '',
        total: 0,
        pageNo: 1,
        pageSize: 10,
        tableData: [],
        addMemberInfo: {
          title: '', //弹框标题
          users: [], //为加入该应用的用户信息
          selectItem: '', //选择用户信息
          roleId: '' //选择角色id
        },
        insertItem: {
          appId: '',
          roleId: '',
          userId: ''
        },
        isAdd: false,
        roles: {},
        dialogFormMemberVisible: false,
        userId:0,
        selectUserId: '',
      }
    },
    created() {
      var that = this
      let accessUser = JSON.parse(sessionStorage.getItem(saveKeys().ACCESSUSER))
      let info = JSON.parse(sessionStorage.getItem(saveKeys().APP_INFO));
      console.log('ACCESSUSER:',accessUser);

      that.id = info.appId;
      that.appKey = info.appKey;
      that.appName = info.appName;

      that.userId = accessUser.userId;
      that.loadRols()
      that.reload()
    },
    methods: {
      loadRols() {
        new Promise((resolve, reject) => {
          getRoles().then((response) => {
            if (response != undefined) {
              const data = response.data.repData
              this.roles = data
            }
          }).catch(error => {
            reject(error)
          })
        })
      },
      reload() {
        var that = this
        that.selectUserId = '';
        new Promise((resolve, reject) => {
          selectUsersByAppId(that.id).then(response => {
            if (response != undefined) {
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
      handleEdit(index, row) { //编辑
        console.log(index, row);
        this.changeDialogInfo()
        this.addMemberInfo.roleId = this.tableData[index].roleId;
        this.addMemberInfo.name = this.tableData[index].name;
        let selectItem = {
          name: this.tableData[index].name,
          username: this.tableData[index].username,
          userId: this.tableData[index].userId,
        };
        this.addMemberInfo.selectItem = selectItem;
        this.insertItem.userId = this.tableData[index].userId;
        this.isAdd = false;
        this.dialogFormMemberVisible = true;
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
            new Promise((resolve, reject) => {
            userAppRoleDelete(nData).then(response => {
                if (response != undefined) {
                  this.changeDialogInfo();
                  this.reload();
                }
                resolve(response)
              }).catch(error => {
                reject(error)
              })
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
      // 添加成员
      handleMember() {
        this.addMemberInfo.title = '添加成员';
        new Promise((resolve, reject) => {
          selectNoJoinApplicationByAppId(this.id).then(response => {
            if (response != undefined) {
              const data = response.data.repData
              this.addMemberInfo.users = data;
              this.changeDialogInfo()
              this.isAdd = true;
              this.dialogFormMemberVisible = true;

            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })


      },
      changeDialogInfo() {
        this.addMemberInfo.roleId = '';
        this.addMemberInfo.selectItem = '';
      },
      changeMemberClick() {
        if(this.addMemberInfo.selectItem == '') {
          Message({
            message: '请选择用户',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(this.addMemberInfo.roleId == '') {
          Message({
            message: '请选择角色',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        this.dialogFormMemberVisible = false;
        this.insertItem.appId = this.id;
        if (this.isAdd) {
          this.insertItem.userId = this.addMemberInfo.selectItem.userId;
        }
        this.insertItem.roleId = this.addMemberInfo.roleId;
        if (this.insertItem.userId == undefined || this.insertItem.userId == '') {
          Message({
            message: "用户信息不能为空",
            type: 'error',
            duration: 1000
          })
          return;
        }
        if (this.insertItem.roleId == undefined || this.insertItem.roleId == '') {
          Message({
            message: "角色不能为空",
            type: 'error',
            duration: 1000
          })
          return;
        }
        if (this.isAdd) {
          new Promise((resolve, reject) => {
            userAppRoleInsert(this.insertItem).then(response => {
              if (response != undefined) {
                this.changeDialogInfo();
                this.reload();
              }
              resolve(response)
            }).catch(error => {
              reject(error)
            })
          })
        } else {
          new Promise((resolve, reject) => {
            userAppRoleUpdate(this.insertItem).then(response => {
              if (response != undefined) {
                this.changeDialogInfo();
                this.reload();
              }
              resolve(response)
            }).catch(error => {
              reject(error)
            })
          })
        }


      },
      elSelectchange(e) {
        let obj = this.addMemberInfo.users.filter(item => item.userId === e);
        if (obj.length > 0) {
          this.addMemberInfo.selectItem = obj[0];
          console.log('成员：', this.addMemberInfo.selectItem.name);
        }
      },
      getRowClass({ row, column, rowIndex, columnIndex }) {
          return "background:#EEF1FF;paddingTop: 20px;paddingBottom: 20px;";
      },
    }
  }
</script>
<style scoped>
  @import "../../../styles/common.css";

  .top {
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
</style>
