<template>
  <el-row class="tac">
    <el-col>
      <el-menu class="el-menu-vertical-demo"
      :router="true">
        <!-- 用户部分 -->
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-user"></i>
            <span class="black">用户资料</span>
          </template>
          <el-menu-item-group>
            <template slot="title">个人信息</template>
            <el-menu-item index="1-1"><i class="el-icon-document"></i>预约信息</el-menu-item>
            <el-menu-item index="1-2"><i class="el-icon-document"></i>失约记录</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <template slot="title">修改用户资料</template>
            <el-menu-item index="/home/user_info"><i class="el-icon-edit"></i>修改个人信息</el-menu-item>
            <el-menu-item index="/home/user_pwd"><i class="el-icon-edit"></i>修改密码</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <!-- 场地部分 -->
        <el-menu-item>
          <i class="el-icon-school"></i>
          <span slot="title" class="black" index="">场地使用</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="2-1">场地预约</el-menu-item>
          <el-menu-item index="2-2">场地预约修改</el-menu-item>
          <el-menu-item index="2-3">场地预约退订</el-menu-item>
          <el-menu-item index="2-4">场地公告</el-menu-item>
        </el-menu-item-group>
        <!-- 赛事部分 -->
        <el-menu-item>
          <i class="el-icon-trophy"></i>
          <span slot="title" class="black" index="">赛事管理</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="3-1">赛事创建</el-menu-item>
          <el-menu-item index="3-2">赛事修改</el-menu-item>
          <el-menu-item index="3-3">赛事取消</el-menu-item>
          <el-menu-item index="3-4">赛事查询</el-menu-item>
          <el-menu-item index="3-5">赛事公告</el-menu-item>
        </el-menu-item-group>
        <!-- 器材管理 -->
        <el-menu-item>
          <i class="el-icon-basketball"></i>
          <span slot="title" class="black" index="">器材管理</span>
        </el-menu-item>
        <el-menu-item-group>
          <el-menu-item index="/home/tools_borrow">器材预约</el-menu-item>
        </el-menu-item-group>
        <!-- 管理员部分 -->
        <el-submenu  index="">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span class="black">管理员</span>
          </template>
          <el-submenu index="5-1">
            <template slot="title">学生用户管理</template>
            <el-menu-item index="5-1-1">学生用户注册</el-menu-item>
            <el-menu-item index="5-1-2">学生密码修改</el-menu-item>
            <el-menu-item index="5-1-3">删除学生用户</el-menu-item>
          </el-submenu>
          <el-submenu index="5-2">
            <template slot="title">管理员用户管理</template>
            <el-menu-item index="5-2-1">添加管理员</el-menu-item>
            <el-menu-item index="5-2-2">删除管理员</el-menu-item>
            <el-menu-item index="5-2-3">查询管理员</el-menu-item>
          </el-submenu>
          <el-submenu index="5-3">
            <template slot="title">场地管理</template>
            <el-menu-item index="5-3-1">场地公告发布</el-menu-item>
            <el-menu-item index="5-3-2">场地预约处理</el-menu-item>
          </el-submenu>
          <el-submenu index="5-4">
            <template slot="title">器材管理</template>
            <el-menu-item index="/home/tools_info">器材管理</el-menu-item>
            <el-menu-item index="/home/tools_rent">器材租借</el-menu-item>
            <el-menu-item index="/home/tools_fix">器材报修</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
export default {
  methods: {
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
</style>