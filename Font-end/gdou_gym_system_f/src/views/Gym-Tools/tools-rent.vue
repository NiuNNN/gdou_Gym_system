<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="器材租借" title="返回主页">
      </el-page-header>
      <div class="show">
        <div class="search-form">
          <el-form ref="formData" :model="formData"  label-position="right" label-width="100px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="学号">
                  <el-input v-model="formData.usercode" placeholder="学号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="预约时间" prop="price">
                    <el-date-picker
                      v-model="formData.date"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="预约时间段" prop="price">
                    <el-select v-model="time" placeholder="选择时间段">
                      <el-option
                        v-for="(item,index) in timeList"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
            </el-row>
            <el-button type="primary" @click="getAll()" class="search" icon="el-icon-search">查询</el-button>
            <span class="ps">*请先查询再选择器材数量</span>
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
                prop="id"
                label="订单编号">
              </el-table-column>
              <el-table-column
                prop="usercode"
                label="学号">
              </el-table-column>
              <el-table-column
                prop="date"
                label="预约日期">
              </el-table-column>
              <el-table-column
                prop="time"
                label="预约时间段">
              </el-table-column>
              <el-table-column
                prop="toolslist"
                label="器材订单编号">
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
                    @click="handleReceive(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
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
    <!-- 显示用户租借详细信息 -->
    <el-dialog
      title="预约信息"
      :visible.sync="dialogdetail"
      width="30%">
      <el-descriptions class="margin-top" title="预约信息" :column="2">
        <el-descriptions-item label="用户号">{{detail.usercode}}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{detail.username}}</el-descriptions-item>
        <el-descriptions-item label="订单编号">{{detail.ordercode}}</el-descriptions-item>
        <el-descriptions-item label="器材编号">{{detail.toolscode}}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handle()">领取</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗1 -->
    <el-dialog
      title="预约处理"
      :visible.sync="dialoghandel1"
      width="30%">
      <el-descriptions class="margin-top" :column="2" v-for="(item,index) in handelList" :key="index" :id="item">
        <el-descriptions-item label="器材编号">{{item}}</el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-tag size="small" type="danger">器材尚未归还</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleDelete()">取消预约</el-button>
        <el-button type="primary">更换器材</el-button>
        <el-button type="primary">继续租借</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request.js'
import {searchList,timeFormat,tFormat} from '@/api/time.js'
export default {
  data() {
    return {
      formData:{
        usercode:'',
        date:'',
        time:''
      },//查询预约用表单
      timeList:[],
      time:'',
      tableData:[],
      pagination: {//分页相关模型数据
          currentPage: 1,//当前页码
          pageSize:5,//每页显示的记录数
          total:0,//总记录数
      },
      dialoghandel1:false,
      dialogdetail:false,
      handelList:[],
      detail:{
        username:'',
        usercode:'',
        ordercode:'',
        toolscode:''
      },//用于存放详细信息
    }
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    getTime(){
      this.timeList = searchList
    },
    //切换页码
    handleCurrentChange(currentPage) {
      //修改页码值为当前选中的页码值
      this.pagination.currentPage = currentPage;
      //执行查询
      this.getAll();
    },
    //查询器材
    getAll(){
      let token  = localStorage.getItem('Authorization')
      if(this.time=='' || this.time=='------') this.formData.time='0'
      else{
        this.formData.time = timeFormat(this.time)
      }
      let param=''
      param+='?usercode='+ this.formData.usercode
      param+='&date='+this.formData.date
      param+='&time='+this.formData.time
      request({
        url:'price/'+token+'/'+this.pagination.currentPage+'/'+this.pagination.pageSize+param,
        method:'get'
      }).then(res=>{
        // console.log(res);
        let code = res.data.code
        let records = []
        if(code === 200){
          this.pagination.pageSize = res.data.data.size//表格大小
          this.pagination.currentPage = res.data.data.current//当前页数
          this.pagination.total = res.data.data.total//总页数
          records= res.data.data.records
          for (const item of records) {
            item.time = tFormat(item.time)
            // console.log(item.time);
          }
          // console.log(records);
          this.tableData = records
        }
        else if(code === 401){
          localStorage.removeItem('username');
          localStorage.removeItem('Authorization');
          localStorage.removeItem('signTime');
          localStorage.removeItem('userclass')
          this.$router.push('/user_login');
        }
        else{
          this.$message.error('系统出错,请稍后再试')
        }
      })
    },
    //获取用户详细信息
    getDetail(inde,row){
      if(row.state === '是'){
        this.$message.info('此订单已经领取器材')
      }else{
        let token  = localStorage.getItem('Authorization')
        this.dialogdetail = true
        request({
          url:'borrows/detail/'+token+'/'+row.id+'/'+row.usercode,
          method:'get'
        }).then(res=>{
          // console.log(res);
          let code = res.data.code
          if(code === 200){
            this.detail.username = res.data.username
            this.detail.ordercode = row.id
            this.detail.usercode = row.usercode
            this.detail.toolscode = res.data.toolscode
          }
          else if(code === 401){
            localStorage.removeItem('username');
            localStorage.removeItem('Authorization');
            localStorage.removeItem('signTime');
            localStorage.removeItem('userclass')
            this.$router.push('/user_login');
          }
          else{
            this.$message.error('系统出错,请稍后再试')
          }
        })
      }
    },
    //领取
    handle(){
      let token  = localStorage.getItem('Authorization')
      request({
        url:'price/'+token+'/'+this.detail.ordercode,
        method:'get'
      }).then(res=>{
        console.log(res);
        let code = res.data.code
        if(code === 200 ){
          this.$message.success("器材全部领取成功")
            this.dialogdetail = false;
            this.getAll()
        }
        else if(code === 401){
          localStorage.removeItem('username');
          localStorage.removeItem('Authorization');
          localStorage.removeItem('signTime');
          localStorage.removeItem('userclass')
          this.$router.push('/user_login');
        }
        else if(code === 500){
          if(res.data.message === '01'){
            this.$message.info('还没到预定领取时间')
            this.dialogdetail = false;
            this.getAll()
          }
          else if(res.data.message === '02'){
            this.$message.error('超过预定领取时间，请进行下一步操作')
          }
          else{
            this.dialogdetail = false;
            this.dialoghandel1= true
            this.handelList = res.data.data
          }
        }
        else{
          this.$message.error('系统出错,请稍后再试')
          this.getAll()
        }
      })
    },
    //取消预约
    handleDelete(){
    },
  },
  created() {
    this.getTime()
    this.getAll()
  },
}
</script>

<style lang="less" scoped>
.descriptions-container{
  width: 100%;
  .descriptions-box{
    position: relative;
    margin: 30px auto;
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
      /deep/.search{
        margin-left: 80px;
        margin-top: 20px;
        margin-bottom: 50px;
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
}
</style>