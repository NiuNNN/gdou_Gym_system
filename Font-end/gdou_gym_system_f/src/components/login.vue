<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <span>{{ title }}</span>
      </div>
      <div class="form-inline">
        <label for="code">账号:</label>
        <el-input
          placeholder="请输入账号"
          v-model="loginForm.code"
          clearable>
        </el-input>
      </div>
      <div class="form-inline">
        <label for="password">密码:</label>
        <el-input placeholder="请输入密码" v-model="loginForm.password" show-password></el-input>
      </div>
      <div class="form-login">
        <el-button type="primary" @click.native.prevent="handlelogin">登录</el-button>
      </div>
      <div class="form-choose">
        <a class="forget" @click="forget">忘了密码?</a>
        <a class="create" @click="create" v-show="show">注册新账号</a>
        <a class="superuser" @click="go">{{ login_name }}</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm:{
        code:'',
        password:''
      }
    }
  },
  props:{
    title:{
      default: '',
      type: String
    },
    login_name:{
      default:'',
      type:String
    },
    show:{
      default:true,
      type:Boolean
    }
  },
  methods:{
    handlelogin(e){
      this.$emit("login", {val:this.loginForm});
    },
    forget(){
      this.$emit("forget");
    },
    create(){
      this.$emit("create")
    },
    go(){
      this.$emit("go")
    }
  }
}
</script>

<style lang="less" scoped>
.login-container{
  height: 100%;
  background:url('@/assets/img/bgc.jpg') no-repeat;
  background-size: 1519px auto;
  .login-box{
    position: absolute;
    width: 430px;
    height: 450px;
    /* background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); */
    border-radius: 6px;
    background-color: rgba(0,0,0,0.66);
    border-radius: 4px;
    left: 65%;
    top: 20%;
    .login-header{
      width: 100%;
      span{
        display: block;
        width: 100%;
        height: 50px;
        text-align: center;
        line-height: 50px;
        font-size: 30px;
        color: #fff;
        font-weight: 600;
      }
    }
    .form-inline{
      // background-color: purple;
      margin: 50px auto;
      height: 30px;
      width: 286px;
      label{
        display: inline-block;
        // background-color: blue;
        width: 55px;
        height: 30px;
        line-height: 30px;
        text-align: left;
        font-weight: 600;
        color: #fff;
      }
      .el-input{
        width: 230px;
        height: 26px;
      }
    }
    .form-login{
      width: 280px;
      height: 47px;
      background-color: #fff;
      border-radius: 15px;
      margin: 20px auto 0;
      /deep/ button{
        width: 280px;
      }
    }
    .form-choose{

      margin-top: 70px;
      a{
        color: #fff;
        font-size: 12px;
      }
      a:hover{
        border-bottom: 1px solid #fff;
        cursor: pointer;
      }
      .forget,.create{
        margin-top: 30px;
        float: left;
        margin-left: 15px;
        font-size: 14px;
      }
      .superuser{
        margin-top: 30px;
        text-align: right;
        margin-right: 15px;
        float: right;
        font-size: 14px;
      }
    }
  }
  /deep/.el-button--primary{
    background-color: #fff;
    border-radius: 15px;
    border: #fff;
    height: 47px;
    span{
      color: #000;
      font-size: 14px;
      font-weight: 700;
    }
  }
  /deep/.el-input input {
    background: none;
    text-indent:1px;
    -webkit-appearance: none;
    border-radius: 14px;
    border: 1px solid white;
    box-sizing: border-box;
    color: white;
    display: block;
    font-size: inherit;
    height: 42px;
    line-height: 1;
    outline: 0;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    width: 100%;
  }
}
</style>