<template>
<Login 
    title="用户登录" 
    login_name="管理员登录" 
    :show="true"
    @login="handlelogin"
    @forget="forget_user"
    @create="create_user"
    @go="go_super">
  </Login>
</template>

<script>
import { mapMutations } from 'vuex';
import request from '@/utils/request.js';
import Login from '@/components/login.vue'
export default {
  data () {
    return {
      loginForm: {
        code: '',
        password: ''
      }
    }
  },
  components:{
    Login
  },
  methods: {
    // 引入loginChange方法
    ...mapMutations(['loginChange']),
    //登录
    handlelogin (e) {
      this.loginForm = e.val
      // console.log(this.loginForm);
      if(this.loginForm.code&&this.loginForm.password){
        request({
        //使用封装好的axios 进行网络请求
        url:'/logins/user',
        method:'post',
        data:{
          //需要提交的数据
          code:this.loginForm.code,
          password:this.loginForm.password
        }
      }).then(res=>{
        //获取后端返回的数据
        let state = res.data.state;
        //判断是否登录成功
        if(state === 200){
          //加入到全局变量中
          //打印token
          console.log(res.data.token);
          this.loginChange({
            userid:res.data.userid,
            username: res.data.name,
            Authorization: res.data.token,
            userclass: res.data.userclass,
            signTime: new Date().getTime()
          });
          this.$router.push('/home');
        }
        else{
          this.$message.error('用户名或密码错误');
        }
      })
      }
      else{
        this.$message.error('用户名或密码错误,请重新填写');
      }
    },
    //跳转到管理员登录
    go_super(){
      this.$router.push('/super_login')
    },
    //跳转到忘记用户密码
    forget_user(){
      this.$router.push('/forget_user')
    },
    //跳转到创建用户
    create_user(){
      this.$router.push('/create_user')
    }
  }
}
</script>

<style lang="less" scoped>
</style>