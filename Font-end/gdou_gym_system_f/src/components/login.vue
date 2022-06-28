<template>
  <div class="login-container">
    <div class="notice-box">
      <div class="notice-header">
        <span>广东海洋大学体院管理系统</span>
      </div>
      <div class="notice-break-line">
      </div>
      <div class="notice-content">
        <p>
          &nbsp; &nbsp;广东海洋大学体育馆始建于2005年，于2005年5月施工到2010年10份正式竣工，于2010年11月17日正式投入使用。该馆共投入4000余万元。
          主校区体育馆位于校园中心区域，依山傍海，气势恢宏，与周围错落有致的楼群及雕塑相映成趣，构成一幅中西风格相统一、自然与人文相协调的和谐风景。
        </p>
        <p>
          &nbsp; &nbsp;体育馆占地面积1万4平方米，建筑面积高达13500平方米，共有四层。首层作为主场馆，共设有2397个座位，主场馆内最多可以容纳5717人。
          主场馆内除了设有体育学院办公室、运动器材室、运动员休息室和会议室以外，还有一个标准篮球场，其面积等同于12个羽毛球场和两个排球场。除了日常可供师生进行体育锻炼外，
          也可举行军训动员大会、大型文艺演出等大型活动。值得注意的是，第二层除了设有台球、健身房外，还设有保龄球馆。第三、四层则分别设有乒乓球室和武术馆、体操馆。
        </p>
        <p>
          &nbsp; &nbsp;本系统包括体育赛事管理，体育器材管理，体育场地管理等。以web应用形式提供服务，可以通过互联网进行访问。
        </p>
      </div>
    </div>
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
  background-size: 1920px auto;
  .notice-box{
    position: absolute;
    width: 630px;
    height: 435px;
    border-radius: 6px;
    background-color: rgba(0,0,0,0.66);
    border-radius: 4px;
    top: 20%;
    left: 18%;
    padding-top: 15px;
    padding-left: 30px;
    padding-right: 30px;
    .notice-header{
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
    .notice-break-line{
      background-color: white;
      width: 100%;
      margin-top: 14px;
      margin-bottom: 20px;
      height: 2px;
    }
    .notice-content{
      p{
        color: #fff;
      }
    }
  }
  .login-box{
    position: absolute;
    width: 350px;
    height: 450px;
    /* background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); */
    border-radius: 6px;
    background-color: rgba(0,0,0,0.66);
    border-radius: 4px;
    left: 65%;
    top: 20%;
    .login-header{
      padding-top: 15px;
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