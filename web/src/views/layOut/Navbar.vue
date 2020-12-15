<template>
  <div class="navbar">
    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container"/>

    <breadcrumb class="breadcrumb-container"/>

    <div class="right-menu">
      <!-- <label class="international right-menu-item" style="color: #00c5dc;">应用管理中心</label> -->
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper flex">
          <img src="@/assets/logo.png" class="user-avatar">
          <span class="mr5">{{name}}</span>
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>
              <span>首页</span>
            </el-dropdown-item>
          </router-link>
          <!-- <el-dropdown-item>
            <span style="display:block;" @click="updatePwd"> 修改密码</span>
          </el-dropdown-item> -->
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout"> 退出登录 </span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" :append-to-body="true">
      <el-form :model="user">z
        <el-form-item label="原密码" :label-width="formLabelWidth">
          <el-input v-model="user.password" style="width: 400px" autocomplete="off" show-password></el-input>
        </el-form-item>

        <el-form-item label="新密码" :label-width="formLabelWidth">
          <el-input v-model="user.newPassword" style="width: 400px" autocomplete="off" show-password></el-input>
        </el-form-item>

        <el-form-item label="确认新密码" :label-width="formLabelWidth">
          <el-input v-model="user.reNewPassword" style="width: 400px" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updatePwdCommit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'

  export default {
    data() {
      return {
        dialogFormVisible: false,
        user: {
          password: null,
          newPassword: null,
          reNewPassword: null
        },
        formLabelWidth: '200px',
        name: ''
      }
    },
    components: {
      Breadcrumb,
      Hamburger,
    },
    computed: {
      ...mapGetters([
        'sidebar',
      ])
    },
    created() {
      this.name = this.$store.getters.accessUser.name
    },
    methods: {
      toggleSideBar() {
        this.$store.dispatch('toggleSideBar')
      },
      logout() {
        this.$confirm('确定要退出吗', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('LogOut').then(() => {
            sessionStorage.clear()
            location.reload()// In order to re-instantiate the vue-router object to avoid bugs
          })
        }).catch(() => {

        })
      },
      updatePwd() {
        this.dialogFormVisible = true
      },
      updatePwdCommit() {
        if (this.user.newPassword != this.user.reNewPassword) {
          this.error('两次输入的密码不一致')
          return
        }
        // request({
        //   url: '/user/user/updatePwd',
        //   method: 'post',
        //   data: {
        //     password: Encrypt(this.user.password),
        //     newPassword: Encrypt(this.user.newPassword)
        //   }
        // }).then((res) => {
        //   if (res.data.code == '200') {
        //     this.success('更新成功')
        //     this.dialogFormVisible = false
        //   } else {
        //     this.error(res.data.msg)
        //   }
        // })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .navbar {
    height: 50px;
    // line-height: 50px;
    border-radius: 0px !important;

    .hamburger-container {
      line-height: 58px;
      height: 50px;
      float: left;
      padding: 0 10px;
    }

    .breadcrumb-container {
      float: left;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }
    .right-menu {
      // float: right;
      // height: 100%;
      margin-right: 20px;
      &:focus {
        outline: none;
      }
      .right-menu-item {
        display: inline-block;
        margin: 0 8px;
      }
      .screenfull {
        height: 20px;
      }
      .international {
        vertical-align: top;
        margin-top: 10px;
        margin-right: 15px;
        cursor: pointer;
      }
      .avatar-container {
        // height: 50px;
        margin-right: 30px;
        .mr5{
          vertical-align: top;
          display: inline-block;
          line-height: 40px;
        }
        .avatar-wrapper {
          // margin-top: 5px;
          // position: relative;
          .name-font{
            font-size:18px;
            font-weight: bold
          }
          .user-avatar {
            cursor: pointer;
            width: 35px;
            height: 35px;
            border-radius: 10px;
            margin-right:10px;
          }

          .el-icon-caret-bottom {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 14px;
            font-size: 12px;
          }
        }
      }
    }
  }
</style>
