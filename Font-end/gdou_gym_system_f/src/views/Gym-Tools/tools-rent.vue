<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="器材租借" title="返回主页">
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
          </el-form>
        </div>
        <!-- 展示信息栏 -->
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
                prop="state"
                label="是否已领取">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    @click="getDetail(scope.$index, scope.row)">查看</el-button>
                  <el-button
                    size="mini"
                    type="primary"
                    @click="getReceive(scope.$index, scope.row)">归还</el-button>
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
        <el-button type="primary" @click="handleDelete()">取消预约</el-button>
        <el-button type="primary" @click="handle()">领取</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗一 有器材不在仓库中 -->
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
        <el-button type="primary" @click="changeTools()">更换器材</el-button>
        <el-button type="primary" @click="handleRent()">继续租借</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗二 成功替换后的弹窗-->
    <el-dialog
      title="预约处理"
      :visible.sync="dialoghandel2"
      width="30%">
      <el-descriptions class="margin-top" :column="2">
        <el-descriptions-item label="器材编号">{{newTools}}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel()">确定</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗三 不成功后的弹窗-->
    <el-dialog
      title="预约处理"
      :visible.sync="dialoghandel3"
      width="30%">
      <span>没有多余的器材存在仓库中，无法进行替换</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleDelete()">取消预约</el-button>
        <el-button type="primary" @click="handleRent()">继续租借</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗四 不成功后的弹窗-->
    <el-dialog
      title="预约处理"
      :visible.sync="dialoghandel4"
      width="30%">
      <el-descriptions class="margin-top" :column="2">
        <el-descriptions-item label="器材编号">{{newTools}}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel()">确定</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗五 全部器材不在库中的弹窗-->
    <el-dialog
      title="预约处理"
      :visible.sync="dialoghandel5"
      width="30%">
      <span>预约的器材都不在库中,已帮你取消订单</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel()">确定</el-button>
      </span>
    </el-dialog>
    <!-- 回收器材 弹窗-->
    <el-dialog
      title="回收器材"
      :visible.sync="dialoghandelReceive"
      width="40%">
      <el-descriptions class="margin-top" :column="2">
        <el-descriptions-item label="订单编号">{{detail.ordercode}}</el-descriptions-item>
        <el-descriptions-item label="用户号">{{detail.usercode}}</el-descriptions-item>
        <el-descriptions-item label="器材编号">{{detail.toolscode}}</el-descriptions-item>
        <el-descriptions-item label="预约日期">{{detail.date}}</el-descriptions-item>
        <el-descriptions-item label="预约时间段" >{{detail.time}}</el-descriptions-item>
        <el-descriptions-item label="实际领取时间" >{{detail.actually}}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleReceive()">确定回收</el-button>
      </span>
    </el-dialog>
    <!-- 租借收费显示 弹窗-->
    <el-dialog
      title="回收器材"
      :visible.sync="dialoghandelReceivePrice"
      width="40%">
      <el-descriptions class="margin-top" :column="2">
        <el-descriptions-item label="订单编号">{{detail.ordercode}}</el-descriptions-item>
        <el-descriptions-item label="用户号">{{detail.usercode}}</el-descriptions-item>
        <el-descriptions-item label="器材编号">{{detail.toolscode}}</el-descriptions-item>
        <el-descriptions-item label="实际领取时间">{{detail.actually}}</el-descriptions-item>
        <el-descriptions-item label="实际回收时间">{{detail.ractually}}</el-descriptions-item>
        <el-descriptions-item label="超时" v-if="over">{{overtime}} 分钟</el-descriptions-item>
        <el-descriptions-item label="需缴费">
          <el-tag size="medium">￥ {{detail.price}}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handlePay()">确定</el-button>
      </span>
    </el-dialog>
    <!-- 超时处理 -->
    <el-dialog
      title="超时未领取处理"
      :visible.sync="dialogVisibleOverTime"
      width="30%"
      :before-close="handleClose">
      <span>请选择操作</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="register()">登 记</el-button>
        <el-button type="primary" @click="Pay()">缴 费</el-button>
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
      dialoghandel2:false,
      newTools:'',//替换后返回回来的器材数据
      dialoghandel3:false,
      dialoghandel4:false,
      dialoghandel5:false,
      dialoghandelReceive:false,
      dialoghandelReceivePrice:false,
      dialoghandelhandleDamage:false,
      handelList:[],
      detail:{
        username:'',
        usercode:'',
        ordercode:'',
        toolscode:'',
        actually:'',
        date:'',
        time:'',
        ractually:'',
        price:'',
        overtime:''
      },//用于存放详细信息
      over:false,
      overtime:'',
      damageOption:[],
      damage:[],
      dialogVisibleOverTime:false
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
            this.dialogdetail = false;
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
        // console.log(res);
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
            this.dialogVisibleOverTime = true
            this.dialogdetail = false;
          }
          else{
            this.dialogdetail = false;
            this.handelList = res.data.data
            this.dialoghandel1= true
          }
        }
        else{
          this.dialogdetail = false;
          this.$message.error('系统出错,请稍后再试')
          this.getAll()
        }
      })
    },
    //取消预约
    handleDelete(){
      let token  = localStorage.getItem('Authorization')
      request({
        url:'price/cancel/'+token+'/'+this.detail.ordercode,
        method:'delete'
      }).then(res=>{
        // console.log(res);
        let code = res.data.code
        if(code === 200){
          this.$message.success('成功取消预约')
          this.dialoghandel1 = false
          this.getAll()
        }
        else if(code === 401){
          localStorage.removeItem('username');
          localStorage.removeItem('Authorization');
          localStorage.removeItem('signTime');
          localStorage.removeItem('userclass')
          this.$router.push('/user_login');
        }
        else{
          this.dialoghandel1 = false
          this.$message.error('系统出错,请稍后再试')
          this.getAll()
        }
      })
    },
    //更换器材
    changeTools(){
      let token  = localStorage.getItem('Authorization')
      // console.log(this.detail.ordercode);
      request({
        url:'price/change/'+token+'/'+this.detail.ordercode,
        method:'get'
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.dialoghandel1 = false;
          this.newTools = res.data.toolslist
          this.dialoghandel2 = true
          this.$message.success("器材全部领取成功")
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
          this.dialoghandel1 = false;
          this.dialoghandel3 = true
          this.$message.info('器材室中没有多余的器材进行替换')
        }
        else{
          this.dialoghandel1 = false;
          this.$message.error('系统出错,请稍后再试')
          this.getAll()
        }
      })
    },
    //取消弹窗
    cancel(){
      this.dialoghandel2 = false
      this.dialoghandel4 = false
      this.dialoghandel5 = false
    },
    //继续租借
    handleRent(){
      this.dialoghandel1 =false
      let token  = localStorage.getItem('Authorization')
      request({
        url:'price/continue'+'/'+token+'/'+this.detail.ordercode,
        method:'get'
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.newTools = res.data.data
          this.dialoghandel3 = false
          this.dialoghandel4 = true
          this.$message.success('器材领取成功')
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
          this.dialoghandel3 = false
          this.dialoghandel5= true
          this.getAll()
        }
        else{
          this.dialoghandel3 = false
          this.$message.error('系统出错,请稍后再试')
          this.getAll()
        }
      })
    },
    //获取用户的租借记录
    getReceive(inde,row){
      // console.log(row);
      if(row.state === '否'){
        this.$message.info('此订单还未领取')
      }
      else if(row.price !== null){
        this.$message.info('此订单已经归还器材')
      }
      else{
        // console.log(row);
        let token  = localStorage.getItem('Authorization')
        request({
          url:'price/receive/'+token+'/'+row.id,
          method:'get'
        }).then(res=>{
          // console.log(res);
          let code = res.data.code
          if(code === 200){
            this.detail.ordercode = row.id
            this.detail.usercode = row.usercode
            this.detail.toolscode = res.data.toolscode
            this.detail.actually = res.data.actually
            this.detail.date = row.date
            this.detail.time = row.time
            this.dialoghandelReceive = true
          }
          else if(code === 401){
            localStorage.removeItem('username');
            localStorage.removeItem('Authorization');
            localStorage.removeItem('signTime');
            localStorage.removeItem('userclass')
            this.$router.push('/user_login');
          }
          else{
            this.dialogdetail = false;
            this.$message.error('系统出错,请稍后再试')
          }
        })
      }
    },
    //回收器材
    handleReceive(){
      this.dialoghandelReceive = false
      let token  = localStorage.getItem('Authorization')
      request({
        url:'price/getPrice/'+token+'/'+this.detail.ordercode,
        method:'get'
      }).then(res=>{
        // console.log(res);
        let code = res.data.code
        if( code === 200){
          this.detail.ractually = res.data.ractually
          this.detail.price = res.data.price
          this.detail.overtime = res.data.overtime
          this.overtime = res.data.overtime
          // console.log(this.detail.overtime);
          if(this.detail.overtime = ''){
            this.over = false
          }
          else{
            this.over = true
            this.$message.error("超时归还！")
          }
          // console.log(this.over);
          this.dialoghandelReceivePrice = true
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
    //确定收账
    handlePay(){
      // console.log(this.detail);
      let token  = localStorage.getItem('Authorization')
      this.$confirm("是否已经收款，确定？","提示",{type:"info"}).then(()=>{
        request({
          url:'price/handlePay/'+token+'/'+this.detail.ordercode+'/'+this.detail.price,
          method:'get'
        }).then(res=>{
          let code = res.data.code
          if(code === 200){
            this.dialoghandelReceivePrice = false
            this.$message.success('操作成功')
            this.getAll()
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
      }).catch(()=>{
        this.$message.info("取消操作");
      });
    },
    //对超时进行登记
    register(){
      request({
        url:'',
        method:'get'
      }).then(res=>{
        console.log(res);
        this.$message.success('已对用户超时未领取进行登记')
      }).finally(()=>{
        this.dialogVisibleOverTime = false
        this.getAll();
      })
    },
    //对超时未领取的订单进行缴费
    Pay(){
       this.$confirm("是否已经收款，确定？","提示",{type:"info"}).then(()=>{
        request({
          url:'',
          method:'get'
        }).then(res=>{
          console.log(res);
          this.$message.success('已对用户超时未领取进行登记')
        }).finally(()=>{
          this.dialogVisibleOverTime = false
          this.getAll();
        })
      })
    }
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