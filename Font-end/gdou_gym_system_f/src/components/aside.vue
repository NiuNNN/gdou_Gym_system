<template>
  <el-row class="tac">
    <el-col>
      <el-menu class="el-menu-vertical-demo"
      :router="true">
        <!-- 用户部分 -->
        <el-submenu index="1" v-if="flag">
          <template slot="title">
            <i class="el-icon-user"></i>
            <span class="black">用户资料</span>
          </template>
          <el-menu-item-group>
            <template slot="title">个人信息</template>
            <el-menu-item index="/home/user_break"><i class="el-icon-document"></i>失约记录 <span class="dot" v-if="dot>0">{{dot}}</span></el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <template slot="title">修改用户资料</template>
            <el-menu-item index="/home/user_info"><i class="el-icon-edit"></i>修改个人信息</el-menu-item>
            <el-menu-item index="/home/user_pwd"><i class="el-icon-edit"></i>修改密码</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <!-- 管理员部分 -->
        <el-submenu  index="5" v-else>
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span class="black">管理员</span>
          </template>
          <el-submenu index="5-1">
            <template slot="title">学生用户管理</template>
            <el-menu-item index="/home/user_create">学生用户注册</el-menu-item>
            <el-menu-item index="/home/user_update">学生密码修改</el-menu-item>
            <el-menu-item index="/home/superuser_search">查询学生用户</el-menu-item>
          </el-submenu>
          <el-submenu index="5-2">
            <template slot="title">管理员用户管理</template>
            <el-menu-item index="/home/superuser_info">修改管理员信息</el-menu-item>
            <el-menu-item index="/home/superuser_pwd">修改管理员密码</el-menu-item>
            <el-menu-item index="/home/superuser_create">添加管理员</el-menu-item>
            <el-menu-item index="/home/superuser_searchs">查询管理员用户</el-menu-item>
          </el-submenu>
          <el-submenu index="5-3">
            <template slot="title">场地管理</template>
            <el-menu-item index="/home/state">场地增删改查</el-menu-item>
            <el-menu-item index="/home/gonggao">场地公告发布</el-menu-item>
            <el-menu-item index="/home/usershiyue">场地失约处理</el-menu-item>
          </el-submenu>
          <el-submenu index="5-4">
            <template slot="title">比赛管理</template>
            <el-menu-item index="/home/game_judge">裁判管理</el-menu-item>
          </el-submenu>
          <el-submenu index="5-5">
            <template slot="title">器材管理</template>
            <el-menu-item index="/home/tools_info">器材管理</el-menu-item>
            <el-menu-item index="/home/tools_rent">器材租借</el-menu-item>
            <el-menu-item index="/home/tools_fix">器材报修</el-menu-item>
          </el-submenu>
        </el-submenu>
        <!-- 场地部分 -->
        <el-menu-item>
          <i class="el-icon-school"></i>
          <span slot="title" class="black" index="">场地使用</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="/home/yuyue">场地预约</el-menu-item>
          <el-menu-item index="/home/usergonggao">场地公告</el-menu-item>
        </el-menu-item-group>
        <!-- 赛事部分 -->
        <el-menu-item>
          <i class="el-icon-trophy"></i>
          <span slot="title" class="black" index="">赛事管理</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="/home/game_create">赛事创建</el-menu-item>
          <el-menu-item index="/home/game_update">赛事修改</el-menu-item>
          <el-menu-item index="/home/game_delete">赛事取消</el-menu-item>
          <el-menu-item index="/home/game_query">赛事查询</el-menu-item>
          <el-menu-item index="/home/game_show">赛事公告</el-menu-item>
        </el-menu-item-group>
        <!-- 器材管理 -->
        <el-menu-item>
          <i class="el-icon-basketball"></i>
          <span slot="title" class="black" index="">器材管理</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="/home/tools_borrow">器材预约</el-menu-item>
        </el-menu-item-group>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      flag:true,
      dot:0,
    }
  },
  methods:{
    changeFlag(){
      let userclass = localStorage.getItem('userclass')
      if(userclass === 'user') this.flag = true
      else this.flag = false
    },
    handleClose(done) {
      done();
    },
    getCount(){
      let usercode = localStorage.getItem('userid')
      request({
        url:'break/count/'+usercode,
        method:'get'
      }).then(res=>{
        this.dot = res.data.data
      })
    }
  },
  created(){
    this.changeFlag()
    this.getCount()
  }
}
</script>

<style lang="less" scoped>
.black{
  font-size: 16px;
  color: #333;
  font-weight: 700;
}

.el-menu {
  margin-bottom:100px ;
}
.dot{
  display: inline-block;
  background-color: #f56c6c;
  border-radius: 10px;
  color: #fff;
  display: inline-block;
  font-size: 12px;
  height: 18px;
  line-height: 18px;
  padding: 0 6px;
  text-align: center;
  white-space: nowrap;
  border: 1px solid #fff;
}
</style>