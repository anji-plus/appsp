<template>
  <div>
    <h1 class="title">账号管理</h1>
    <div class="top">
      <el-form :inline="true" :model="user" class="demo-form-inline">
          <el-form-item label="用户名">
            <el-input v-model="user.name" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item label="账号">
            <el-input v-model="user.username" placeholder="账号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="btn-search" @click="onSelectSubmit">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="btn-clear" @click="onClearSubmit">清空</el-button>
          </el-form-item>
        </el-form>
    </div>
    <div class="top">
      <el-button type="primary" icon="el-icon-plus" plain @click="addUserClick" style="marginBottom: 20px;">新建用户</el-button>
      <el-table :row-style="getRowClass" :header-row-style="getRowClass" :header-cell-style="getRowClass"
        :data="tableData"
        style="width: 100%">
        <el-table-column label="账号">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用户名">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              :style="{color: '#fff'}"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="user.pageNo"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="user.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog title="新建用户" :visible.sync="dialogFormVisible">
      <el-form :model="userForm">
        <el-form-item label="账号" :label-width="formLabelWidth">
          <el-input v-model="userForm.username" autocomplete="off" maxlength="18"></el-input>
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="userForm.name" autocomplete="off" maxlength="18"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="dialog-btn-ok" @click="addClick">确 定</el-button>
        <el-button class="dialog-btn-cancel" @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getUsers,addUsers,deleteUsers} from '@/api/users'
  import {Message} from 'element-ui'

  export default {
    name: 'VersionList',
    data() {
      return {
        id: '',
        appKey: '',
        total: 0,
        user: {
          pageNo: 1,
          pageSize: 10,
          username: '',
          name: ''
        },
        userForm:{
          username: '',
          name: ''
        },
        dialogFormVisible: false,
        formLabelWidth: '120px',
        tableData: []
      }
    },
    created() {
      this.reload()
    },
    methods: {
      reload() {
        var that = this
        return new Promise((resolve, reject) => {
          //username,name ,pageNo, pageSize
          getUsers(that.user.username, that.user.name, that.user.pageNo, that.user.pageSize).then(response => {
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
      handleDelete(index, row) { //删除
        console.log(index, row);
        var that = this
        that.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            var nData = this.tableData[index];
            deleteUsers(nData).then(response => {
              that.reload();
            }).catch(error => {
              reject(error)
            })
        }).catch(() => {

        });
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.user.pageSize = val;
        this.reload();

      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.user.pageNo = val;
        this.reload();
      },
      addUserClick() { //新建用户
        this.dialogFormVisible = true;
      },
      onSelectSubmit(){
        this.reload();
      },
      onClearSubmit() {
        this.user.name = '';
        this.user.username = '';
      },
      addClick(){
        var that = this
        if(that.userForm.username == '') {
          Message({
            message: '请输入账号，最多18位',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(that.userForm.name == '') {
          Message({
            message: '请输入用户名，最多18位',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        return new Promise((resolve, reject) => {
          //username,name ,pageNo, pageSize
          addUsers(that.userForm).then(response => {
            if (response != undefined) {
              that.reload();
            }
            resolve(response)
            that.dialogFormVisible = false;
            that.userForm.username = '';
            that.userForm.name = '';
          }).catch(error => {
            reject(error)
          })
        })


      },
      getRowClass({ row, column, rowIndex, columnIndex }) {
          return "background:#EEF1FF;paddingTop: 10px;paddingBottom: 10px;";
      },
    }
  }
</script>
<style scoped>
  @import "../../styles/common.css";
  .title {
    font-family: PingFangSC-Medium;
    font-size: 21px;
    color: #666666;
    margin-left: 20px;
  }
  .top {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    padding: 20px;
    margin: 20px;

  }
  .btn-search {
    width: 100px;
    height: 40px;
    color: white;
    background: #1E3FB3;
    border-radius: 8px;
    border-radius: 8px;
  }
  .btn-clear {
    width: 100px;
    height: 40px;
    color: #1E3FB3;
    border-style: inset;
    border: 1px solid #1E3FB3;
    border-radius: 8px;
    border-radius: 8px;
  }

  .demonstration {
    font-size: larger;
  }
</style>
