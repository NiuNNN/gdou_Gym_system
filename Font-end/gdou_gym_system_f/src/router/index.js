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
import UserPwd from '@/views/Gym-User/user-pwd.vue';//修改用户密码
import UserBreak from '@/views/Gym-User/user-breakContract.vue';//用户的失约记录

//Superuser
import User_create from '@/views/Gym-Superuser/user-create.vue';//创建用户
import User_update from '@/views/Gym-Superuser/user-update.vue';//修改用户密码
import Super_create from '@/views/Gym-Superuser/superuser-create.vue';//创建管理员
import SuperuserInfo from '@/views/Gym-Superuser/superuser-info.vue'; //修改管理员信息
import SuperuserPwd from '@/views/Gym-Superuser/superuser-pwd.vue';//修改管理员密码
import SuperSearch from '@/views/Gym-Superuser/superuser-searchuser.vue'; //查询普通用户
import SuperSearchS from '@/views/Gym-Superuser/superuser-searchsuper.vue'; //查询管理员用户

// Tools
import ToolsInfo from '@/views/Gym-Tools/tools-info.vue';//展示用户信息进行增删
import ToolsBor from '@/views/Gym-Tools/tools-borrow.vue';//器材租用修改
import ToolsRent from '@/views/Gym-Tools/tools-rent.vue';// 管理员器材租借
import ToolsFix from '@/views/Gym-Tools/tools_fix.vue'; //器材报修

//State
import State from '@/views/Gym-State/state';
import StateNotice from '@/views/Gym-State/gonggao';
import StateBook from '@/views/Gym-State/yuyue'
import StateUserNotice from '@/views/Gym-State/gonggao/usergonggao'
import StateFaild from '@/views/Gym-State/yuyue/shiyue'

// Game
import Game_create from '@/views/Gym-Game/Game-create.vue';
import Game_update from '@/views/Gym-Game/Game-update.vue';
import Game_query from '@/views/Gym-Game/Game-query.vue';
import Game_delete from '@/views/Gym-Game/Game-delete.vue';
import Game_judge from "@/views/Gym-Game/Game-judge.vue";
import Game_show from '@/views/Gym-Game/Game-show.vue';

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
      {path:'user_info',component:UserInfo},
      {path:'user_pwd',component:UserPwd},
      {path:'user_break',component:UserBreak},
      {path:'tools_info',component:ToolsInfo},
      {path:'tools_borrow',component:ToolsBor},
      {path:'tools_rent',component:ToolsRent},
      {path:'tools_fix',component:ToolsFix},
      {path:'user_create', component:User_create},
      {path:'user_update', component:User_update},
      {path:'superuser_info',component:SuperuserInfo},
      {path:'superuser_pwd',component:SuperuserPwd},
      {path:'superuser_create',component:Super_create},
      {path:'superuser_search',component:SuperSearch},
      {path:'superuser_searchs',component:SuperSearchS},
      {path:'state',component:State},
      {path:'gonggao',component:StateNotice},
      {path:'yuyue',component:StateBook},
      {path:'usergonggao',component:StateUserNotice},
      {path:'usershiyue',component:StateFaild},
      {path:"game_create",component:Game_create},
      {path:'game_update',component:Game_update},
      {path:"game_query",component:Game_query},
      {path:"game_delete",component:Game_delete},
      {path:"game_judge",component:Game_judge},
      {path:'game_show',component:Game_show}
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
    let userclass = localStorage.getItem('userclass')
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
