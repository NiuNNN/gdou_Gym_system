<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="查询学生用户" title="返回主页">
      </el-page-header>
      <div class="show">
        <div class="search-form">
          <!-- 搜索栏 -->
          <el-form ref="formData" :model="formData"  label-position="right" label-width="100px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="学号">
                  <el-input v-model="formData.usercode" placeholder="学号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-button type="primary" @click="getAll()" class="search" icon="el-icon-search" style="margin-left:15px">查询</el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="show-table">
          <div class="table-container">
            <el-table
              :data="tableData"
              stripe
              style="width: 100%"
              border>
              <el-table-column
                prop="code"
                label="学号">
              </el-table-column>
              <el-table-column
                prop="name"
                label="姓名">
              </el-table-column>
              <el-table-column
                prop="age"
                label="年龄">
              </el-table-column>
              <el-table-column
                prop="sex"
                label="性别">
              </el-table-column>
              <el-table-column
                prop="phone"
                label="手机号">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    @click="getDetail(scope.$index, scope.row)">查看</el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    @click="handelDelete(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
    <!-- 页码选择器 -->
    <div class="pagination-container">
      <div class="block">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page.sync="pagination.currentPage"
          :page-size="pagination.pageSize"
          layout="prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>
    <div class="detail">
      <el-dialog
      :visible.sync="dialogVisible"
      width="60%">
        <div class="left">
          <el-descriptions class="margin-top" title="用户信息" :column="3" direction="vertical" border>
            <el-descriptions-item label="学号">{{user.code}}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{user.name}}</el-descriptions-item>
            <el-descriptions-item label="性别">{{user.sex}}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{user.phone}}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{user.age}}</el-descriptions-item>
            <el-descriptions-item label="邮编">{{user.zip}}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{user.email}}</el-descriptions-item>
            <el-descriptions-item label="联系地址">{{user.address}}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 用户头像 -->
        <div class="right">
          <img :src="user.avatar" alt="">
          <span>学生头像</span>
        </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      formData:{
        usercode:'',
      },//查询预约用表单
      pagination: {//分页相关模型数据
        currentPage: 1,//当前页码
        pageSize:12,//每页显示的记录数
        total:0,//总记录数
      },
      tableData:[],
      dialogVisible:false,
      user:{
        code:'',
        name:'',
        sex:'',
        phone:'',
        age:'',
        zip:'',
        email:'',
        address:'',
        avatar:''
      },
    }
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    getAll(){
      let param=''
      param+='?code='+ this.formData.usercode
      request({
        url:'users/getAll/'+this.pagination.currentPage+'/'+this.pagination.pageSize+param,
        method:'get'
      }).then(res=>{
        // console.log(res);
        this.pagination.pageSize = res.data.data.size//表格大小
        this.pagination.currentPage = res.data.data.current//当前页数
        this.pagination.total = res.data.data.total//总页数
        this.tableData= res.data.data.records
      })
    },
    //切换页码
    handleCurrentChange(currentPage) {
      //修改页码值为当前选中的页码值
      this.pagination.currentPage = currentPage;
      //执行查询
      this.getAll();
    },
    //获取详细信息
    getDetail(index,row){
      // console.log(row);
      this.user = row
      this.dialogVisible = true
    },
    //删除
    handelDelete(index,row){
      this.$confirm('是否要删除该用户，确定？',"提示",{type:'info'}).then(()=>{
        request({
        url:'users/'+row.code,
        method:'delete'
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.$message.success('删除成功')
        }
        else if(code === 500){
          this.$message.error('该用户存在预约或未缴清费用')
        }
        else{
          this.$message.error('系统出错,请稍后再试')
        }
      }).finally(()=>{
        this.getAll()
      })
      })
    }
  },
  created(){
    this.getAll()
  }
}
</script>

<style lang="less" scoped>
.descriptions-container{
  width: 100%;
  .descriptions-box{
    position: relative;
    margin: 0 auto;
    width: 98%;
    border-radius: 4px;
    overflow: hidden;
    height: 800px;
    .show{
      width: 90%;
      margin: 50px auto;
      /deep/.el-date-editor{
        width: 186.74px;
      }
    }
  }
}
  .pagination-container{
    top: -130px;
    position: relative;
    width: 50%;
    margin: 30px auto;
    .block{
      margin: 0 auto;
    }
  }
.left{
    margin-left: 10%;
    width: 60%;
    float: left;
  }
  .right{
    margin-top: 80px;
  margin-left: 5%;
  float: left;
  width: 178px;
  img{
    width: 178px;
    height: 178px;
    border-radius: 15px;
  }
  span{
        margin-top: 5px;
        display: block;
        width: 178px;
        font-size: 14px;
        color: #909399;
        text-align: center;
      }
}
</style>
<style lang="less">
.detail{
  .el-dialog{
  height: 480px;
  .el-dialog__body{
    height: 300px;
  }
  }
}
</style>