<template>
  <div class="header-container">
    <div class="logo">
      <span>广东海洋大学体育管理平台</span>
    </div>
    <div class="userinfo">
      <el-avatar :size="60">
        <img :src="getImgUrl()" alt="">
      </el-avatar>
      <span style="font-size:13px">欢迎回来，{{ username }}</span>
      <el-button type="primary" plain @click="loginout">退出</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
    }
  },
  created(){
    this.getImgUrl()
  },
  computed:{
    username(){
      let name = localStorage.getItem('username')
      let userclass = localStorage.getItem('userclass')
      let username;
      if(userclass === "user"){
        username = name+" 同学"
      }
      else{
        username = name+" 管理员" 
      }
      return username
    }
  },
  methods: {
    loginout(){
      localStorage.removeItem('username');
      localStorage.removeItem('Authorization');
      localStorage.removeItem('signTime');
      localStorage.removeItem('userclass')
      this.$router.push('/user_login');
    },
    getImgUrl(){
      let userid = localStorage.getItem('userid')
      var imgUrl=''
      request({
        url:'/users/toGetUserAvatar/'+userid,
        method:'Get',
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          imgUrl=res.data.data
         /*  const binaryData = [];
          binaryData.push(imgUrl);
          this.$refs.imgCode.src = window.URL.createObjectURL(new Blob(binaryData,{type:'application/jpg;chartset=UTF-8'})); */
          console.log(imgUrl);
        }
        else{
          console.log(res.data);
        }
      })
      // let userid = localStorage.getItem('userid')
      // let imgUrl ='http://localhost:80/images/avatar/'+ userid + '.jpg'
      return imgUrl
    }
  },
}
</script>

<style lang="less" scoped>
.header-container{
  position: absolute;
  width: 80%;
  height: 79px;
  border-bottom: 1px solid #dcdfe6;
  .logo{
    display: inline-block;
    span{
      height: 79px;
      color: #409eff;
      line-height: 79px;
      font-size: 30px;
      font-weight: 700;
      padding-left: 15px;
    }
  }
  .userinfo{
    z-index: 999;
    height: 79px;
    display: inline-block;
    float: right;
    margin-right: 15px;
    color: #888;
    .el-button{
      margin-left: 30px;
    }
  }
  .el-avatar--circle{
    position: relative;
    top: 18px;
    right: 14px;
  }
}
</style>