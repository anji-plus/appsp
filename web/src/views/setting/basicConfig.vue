<template>
  <div>
    <h1 class="title">基础设置</h1>
    <div class="top">
      <span class="title">Android版本</span>
        <el-button class="addBtn" icon="el-icon-circle-plus-outline"
                   @click="addDictClick('Android版本', 'ANDROID_VERSION')">添加
        </el-button>
        <div>
          <el-tag v-for="item in androidVersions"
                  :key="item.name" class="elTag">
            {{item.value}}
          </el-tag>
        </div>
      <div class="top1">
        <span class="title">iOS版本</span>
        <el-button class="addBtn" icon="el-icon-circle-plus-outline" @click="addDictClick('iOS版本', 'IOS_VERSION')">添加
        </el-button>
        <div>
          <el-tag v-for="item in iOSVersions"
                  :key="item.name" class="elTag">
            {{item.value}}
          </el-tag>
        </div>
      </div>
      <div class="top1">
        <span class="title">公告名称</span>
        <el-button class="addBtn" icon="el-icon-circle-plus-outline" @click="addDictClick('公告名称', 'TEMPLATE')">添加
        </el-button>
        <div>
          <el-popover :key="item.name" v-for="item in notations"
            placement="top-start"
            title=""
            width="200"
            trigger="hover"
            :content="'标识：' + item.name">
            <el-tag class="elTag" slot="reference">
              {{item.value}}
            </el-tag>
            <!-- <el-tag v-for="item in notations"
                  :key="item.name" class="elTag" slot="reference">
              {{item.value}}
            </el-tag> -->
          </el-popover>
        </div>
      </div>
      <div class="top1">
        <span class="title">角色配置</span>
        <el-button class="addBtn" icon="el-icon-circle-plus-outline" @click="addRoleClick">添加</el-button>
        <div>
          <el-tag v-for="item in roles"
                  :key="item.roleName" class="elTag" @click="changeMenuPermissionsClick(item)" style="cursor:pointer">
            {{item.roleName}}
          </el-tag>
        </div>
      </div>
    </div>


    <el-dialog :title="dicts.title" :visible.sync="dialogFormDictVisible">
      <el-form :model="dicts">
        <el-form-item :label="dicts.title" :label-width="formLabelWidth">
          <el-input v-model="dicts.value" autocomplete="off" :placeholder="'请输入'+ dicts.title"></el-input>
        </el-form-item>
        <el-form-item label="公告标识" :label-width="formLabelWidth" v-if="dicts.type == 'TEMPLATE'">
          <el-input v-model="dicts.name" autocomplete="off" :placeholder="'请输入公告标识'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="dialog-btn-ok" @click="addClick">确 定</el-button>
        <el-button class="dialog-btn-cancel" @click="dialogFormDictVisible = false">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog title="管理权限" :visible.sync="dialogFormMeunVisible">
      <el-form :model="menuUpdate">
        <el-form-item label="角色名" :label-width="formLabelWidth">
          <el-input v-model="menuUpdate.roleName" autocomplete="off" placeholder="请输入角色名"></el-input>
        </el-form-item>
      </el-form>
      <el-checkbox-group v-model="menuUpdate.menuIds">
        <el-checkbox v-for="item in menus"
                     :key="item.name" :label="item.menuId">{{item.name}}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button class="dialog-btn-ok" @click="changeMenuClick">确 定</el-button>
        <el-button class="dialog-btn-cancel" @click="dialogFormMeunVisible = false">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog title="角色配置" :visible.sync="dialogFormRoleVisible">
      <el-form :model="addRoleInfo">
        <el-form-item label="角色名" :label-width="formLabelWidth">
          <el-input v-model="addRoleInfo.roleName" autocomplete="off" placeholder="请输入角色名"></el-input>
        </el-form-item>
      </el-form>
      <h3>菜单配置</h3>
      <el-checkbox-group v-model="addRoleInfo.menuIds">
        <el-checkbox v-for="item in menus"
                     :key="item.name" :label="item.menuId">{{item.name}}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button class="dialog-btn-ok" @click="changeRoleClick">确 定</el-button>
        <el-button class="dialog-btn-cancel" @click="dialogFormRoleVisible = false">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
  import {getUsers, addUsers, deleteUsers} from '@/api/users'
  import {
    getVersions,
    getRoles,
    addDicts,
    selectByRoleId,
    selectAllMenus,
    roleMenuUpdateByMenuIds,
    insertRoleAndMenu,
    updateRoleByRoleId
  } from '@/api/main'
  import {Message} from 'element-ui'

  export default {
    name: 'VersionList',
    data() {
      return {
        androidVersions: [],
        iOSVersions: [],
        notations: [],
        roles: [],
        dicts: {
          title: '',
          type: '',
          value: '',
          name: '',
        },
        dialogFormDictVisible: false,
        dialogFormMeunVisible: false,
        dialogFormRoleVisible: false,
        formLabelWidth: '120px',
        //修改角色信息
        menuUpdate: {
          menuIds: [],
          roleId: 0,
          roleName: '',
        },
        //添加角色信息
        addRoleInfo: {
          menuIds: [],
          roleName: ''
        },
        menus: []
      }
    },
    created() {
      this.reload()
    },
    methods: {
      reload() {
        var that = this
        new Promise((resolve, reject) => {
          getVersions('ANDROID_VERSION').then((response) => {
            if (response != undefined) {
              const data = response.data.repData

              that.androidVersions = data
            }
            resolve()
          }).catch(error => {
            reject(error)
          })

        })
        new Promise((resolve, reject) => {
          getVersions('IOS_VERSION').then((response) => {
            if (response != undefined) {
              const data = response.data.repData
              that.iOSVersions = data
            }
            resolve()
          }).catch(error => {
            reject(error)
          })

        })
        new Promise((resolve, reject) => {
          getVersions('TEMPLATE').then((response) => {
            if (response != undefined) {
              const data = response.data.repData
              that.notations = data
            }
          }).catch(error => {
            reject(error)
          })

        })

        this.loadAllMenus()
        new Promise((resolve, reject) => {
          selectAllMenus().then((response) => {
            if (response != undefined) {
              const data = response.data.repData
              that.menus = data
            }
          }).catch(error => {
            reject(error)
          })
        })

      },
      loadAllMenus() { //加载所有菜单
        return new Promise((resolve, reject) => {
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
      addDictClick(title, type) { //添加字典类型事件
        this.dialogFormDictVisible = true;
        this.dicts.title = title;
        this.dicts.type = type;
        this.dicts.value='';
        this.dicts.name = '';

      },
      addClick() { //添加字典类型确定事件
        var that = this
        if(that.dicts.value == '' && that.dicts.type == 'ANDROID_VERSION') {
          Message({
            message: '请输入Android版本',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(that.dicts.value == '' && that.dicts.type == 'IOS_VERSION') {
          Message({
            message: '请输入iOS版本',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(that.dicts.name == '' && that.dicts.type == 'TEMPLATE') {
          Message({
            message: '请输入公告标识',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(that.dicts.value == '' && that.dicts.type == 'TEMPLATE') {
          Message({
            message: '请输入公告名称',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        that.dialogFormDictVisible = false;
        new Promise((resolve, reject) => {
          addDicts(that.dicts).then(response => {
            if (response != undefined) {
              that.reload();
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })

      },
      changeMenuPermissionsClick(item) { //修改角色菜单权限
        console.log(item)

        this.menuUpdate.roleId = item.roleId
        this.menuUpdate.roleName = item.roleName

        new Promise((resolve, reject) => {
          selectByRoleId(item).then(response => {
            if (response != undefined) {
              var menu = response.data.repData;

              var newArray = [];
              menu.forEach(function (element) {
                newArray.push(element.menuId);
              })

              this.menuUpdate.menuIds = newArray
              console.log(this.menuUpdate.menuIds);

              this.dialogFormMeunVisible = true;
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })


      },
      changeMenuClick() { //管理权限确定按钮事件
        if(this.menuUpdate.roleName == '') {
          Message({
            message: '请输入角色名',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(this.menuUpdate.menuIds.length == 0) {
          Message({
            message: '请配置菜单权限',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        this.dialogFormMeunVisible = false;
        new Promise((resolve, reject) => {
          updateRoleByRoleId(this.menuUpdate).then(response => {
            if (response != undefined) {
              this.loadAllMenus();
            }
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
        new Promise((resolve, reject) => {
          roleMenuUpdateByMenuIds(this.menuUpdate).then(response => {
            if (response != undefined) {

              this.loadAllMenus();
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
      addRoleClick() { //添加角色配置事件
        this.addRoleInfo.menuIds = [];
        this.addRoleInfo.roleName = '';
        this.dialogFormRoleVisible = true;
      },
      changeRoleClick() { //添加角色配置确定按钮
        if(this.addRoleInfo.roleName == '') {
          Message({
            message: '请输入角色名',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        if(this.addRoleInfo.menuIds.length == 0) {
          Message({
            message: '请配置菜单权限',
            type: 'warning',
            duration: 5 * 1000
          });
          return;
        }
        this.dialogFormRoleVisible = false;

        new Promise((resolve, reject) => {
          insertRoleAndMenu(this.addRoleInfo).then(response => {
            if (response != undefined) {

              this.loadAllMenus();
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

  .top1 {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    margin: 20px 0px 20px 0px;
    padding: 5px 10px 5px 10px;
  }

  .demonstration {
    font-size: larger;
  }

  .elTag {
    margin: 10px;
    color: #1E3FB3;
    background: #EBEFFF;
    border-radius: 4px;
    border-radius: 4px;
  }

  .title{
    font-size: 16px;
    margin-right: 20px;
  }

  .addBtn {
    background-color: #1E3FB3;
    color: #fff;
  }

</style>
