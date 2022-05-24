import Vue from 'vue'
import VueRouter from 'vue-router'

// 登录模块
import user_login from '@/views/Gym-Login/user-login.vue'; //用户登录
import super_login from '@/views/Gym-Login/super-login.vue'; //管理员登录
import forget_user from '@/views/Gym-Login/forget-user.vue'; //用户忘记密码
import forget_super from '@/views/Gym-Login/forget-super.vue'; //管理员忘记密码
import create_user from '@/views/Gym-Login/create-user.vue'; //创建用户

//主页
import Home from '@/views/Home.vue';
// 子页
import Main from '@/views/Main.vue';

// User
import UserInfo from '@/views/Gym-User/user-info.vue'; //修改用户信息


// 导入不拦截地址
import pathArr from '@/router/pathArr.js';

Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    redirect:'/user_login'
  },
  { path:'/user_login', component:user_login },
  { path:'/super_login', component:super_login },

  { path:'/forget_user', component:forget_user },

  { path:'/forget_super', component:forget_super },

  { path:'/create_user', component:create_user },
  
  {
    path:'/home',
    component:Home,
    redirect:'/home/main',
    children:[
      {path: 'main', component: Main},
      {path:'user_info',component:UserInfo}
    ]
  },
]

const router = new VueRouter({
  routes
})

//前端拦截
router.beforeEach((to, from, next)=>{
  //判断跳转的路径是否是登录页面，若是，则不拦截，直接跳转；
  //若不是，则验证Authorization是否为空
  //若为空，则说明未登录，跳转到登录页面。若不为空，说明已登录，直接跳转到相应页面
  if(pathArr.indexOf(to.path) !== -1){
    next();
  }else {
    let Authorization = localStorage.getItem('Authorization');
    if(Authorization === null || Authorization === ''){
      // console.log('1');
      next('/user_login');
    }else{
      // console.log('2');
      next();
    }
  }
});


export default router
