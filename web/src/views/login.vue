<template>
  <div class="loginBaseView">
    <div class="login-container">
    <Verify
      @success="success"
      :mode="'pop'"
      :captchaType="'blockPuzzle'"
            :imgSize="{ width: '330px', height: '155px' }"
            ref="verify"
    ></Verify>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <el-form-item class="logoView">
        <img class="logoImg" src="../assets/logo.png"/>
      </el-form-item>
      <h3 class="title">加加移动服务平台</h3>
      <el-form-item prop="userName">
        <span class="svg-container">
          <!-- <i class="el-icon-user"/> -->
          <img class="inputIcon" src="../assets/username_icon.png"/>
        </span>
        <el-input v-model="loginForm.userName" name="userName" type="text" auto-complete="on" placeholder="请输入用户名" maxlength="18"/>
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <!-- <i class="el-icon-lock"/> -->
          <img class="inputIcon" src="../assets/password_icon.png"/>
        </span>
        <el-input :type="pwdType" v-model="loginForm.password" name="password" auto-complete="on" placeholder="请输入密码" maxlength="18"/>
        <span class="show-pwd" @click="showPwd">
          <i class="el-icon-view"/>
        </span>
      </el-form-item>
      <el-form-item class="loginView">
        <el-button :loading="loading" type="primary" class="loginButton"  @click="handlerLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
  </div>
</template>

<script>
//引入组件
import Verify from "./../components/verifition/Verify";
import { aesEncrypt } from '@/utils/aes'
export default {
  name: 'Login',
  components: {
    Verify
  },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!/^[A-Za-z0-9]+$/.test(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        userName: '',
        password: '',
        captchaVerification: '',
      },
      loginRules: {
        userName: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePass }]
      },
      loading: false,
      pwdType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created(){
    this.keyupSubmit();
  },
  methods: {
    // 图形验证码验证成功
    success(params){// params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      this.loginForm.captchaVerification = params.captchaVerification
      this.loading = true
      console.log('图形验证码验证成功', params)
      const data = {
        userName: this.loginForm.userName,
        password: aesEncrypt(this.loginForm.password),
        captchaVerification: this.loginForm.captchaVerification,
      }
      this.$store.dispatch('LoginByUsername', data).then(() => {
        this.loading = false
        // this.$router.push({ path: this.redirect || '/' })
        this.$router.push({ path: '/' })
      }).catch(() => {
        this.loading = false
      })
    },
    // 监听键盘事件
    keyupSubmit(){
      document.onkeydown=e=>{
        let _key=window.event.keyCode;
        if(_key===13){// 回车
          this.handlerLogin();
        }
      }
    },
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handlerLogin(){
      var that = this
      that.$refs.loginForm.validate((valid)=>{
        if (valid) {
          that.loginForm.captchaVerification = ""
          that.$refs.verify.show()
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>


<style rel="stylesheet/scss" lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

  $bg:#283443;
  $light_gray:#666666;
  $cursor: #666666;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    // .login-container .el-input input{
    //   color: $cursor;
    //   &::first-line {
    //     color: $light_gray;
    //   }
    // }
  }

  .loginBaseView {
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
    padding: 100px;
    background: url('../assets/login_bg.jpg') no-repeat;
    background-size: cover;
  }

  /* reset element-ui css */
  .login-container {
    .el-input {
      display: inline-block;
      height: 40px;
      width: 85%;
      background-color: #fff;
      input {
        background: transparent;
        background-color: #fff;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 40px;
        caret-color: $cursor;
        &:-webkit-autofill {
          -webkit-box-shadow: 0 0 0px 1000px #fff inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }
    .el-form-item {
      // border: 1px solid rgba(255, 255, 255, 0.1);
      background: #fff;
      border-width: 1px;
      border-style: solid;
      border-color: #979797;
      border-radius: 5px;
      color: #454545;
    }
    .logoView {
      width: 100%;
      display: flex;
      flex-direction: row;
      align-items: flex-end;
      justify-content: center;
      border-style: none;
    }
    .logoImg {
      width: 65px;
      height: 65px;
      object-fit: contain;
    }
    .loginView {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: center;
      border-style: none;
    }
    .loginButton {
      height: 47px;
      width: 220px;
      font-size: 18px;
      background: -webkit-linear-gradient(left, #6786FF , #172EB8); /* Safari 5.1 - 6.0 */
      background: -o-linear-gradient(right, #6786FF, #172EB8); /* Opera 11.1 - 12.0 */
      background: -moz-linear-gradient(right, #6786FF, #172EB8); /* Firefox 3.6 - 15 */
      background: linear-gradient(to right, #6786FF , #172EB8); /* 标准的语法 */
    }
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  // min-height: 100%;
  // width: 100%;
  // background-color: $bg;
  right: 100px;
  // width: 520px;
  width: 380px;
  border-radius: 20px;
  background-color: #fff;
  overflow: hidden;
  .title {
    font-size: 26px;
    font-weight: 400;
    color: #666666;
    margin: -20px auto 40px auto;
    text-align: center;
  }
  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 50px 35px 50px 35px;
    margin: 0 auto;
    overflow: hidden;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
  .svg-container {
    // padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    // background-color: chocolate;
  }
  .inputIcon {
      width: 20px;
      height: 20px;
      margin-left: 15px;
      margin-top: 13px;
      object-fit: contain;
    }
  .title-container {
    position: relative;
    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .set-language {
      color: #fff;
      position: absolute;
      top: 3px;
      font-size:18px;
      right: 0px;
      cursor: pointer;
    }
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }
}
</style>
