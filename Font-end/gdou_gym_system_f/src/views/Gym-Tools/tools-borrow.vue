<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="器材租用" title="返回主页">
      </el-page-header>
      <div class="show">
        <div class="add-form">
          <el-form ref="formData" :model="formData"  label-position="right" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="器材类型" prop="kind">
                    <el-select v-model="formData.kind" placeholder="请选择器材类型" @change="getPrice()">
                      <el-option v-for="(item,index) in kindList" :key="index" :label="item.kind" :value="item.kind">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="器材价格" prop="price">
                    <el-select v-model="formData.price" placeholder="请选择价格">
                      <el-option v-for="(item,index) in priceList" :key="index" :label="item.price" :value="item.price">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="预约时间" prop="price">
                    <el-date-picker
                      v-model="date"
                      type="date"
                      placeholder="选择日期"
                      :picker-options="pickerOptions"
                      value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
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
              <el-row>
                <el-col :span="12">
                  <span class="sp">可租用器材：</span>
                  <span class="sp" style="margin-left:0">{{count}} 个</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="器材数量" prop="price">
                    <el-input-number v-model="num" :min="0" :max="count" label="器材数量"></el-input-number>
                  </el-form-item>
                  <span><span class="ps">*请先查询再选择器材数量</span></span>
                </el-col>
                <el-col :span="3">
                  <el-button type="primary" @click="search()" class="search" icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="3">
                  <el-button type="primary" v-if="flag" @click="handleborrow()" class="search" icon="el-icon-s-claim">预约</el-button>
                  <el-button type="primary" v-else disabled class="search" icon="el-icon-s-claim">预约</el-button>
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
                prop="toolscode"
                label="编号"
                width="180">
              </el-table-column>
              <el-table-column
                prop="kind"
                label="种类"
                width="180">
              </el-table-column>
              <el-table-column
                prop="date"
                label="预约日期">
              </el-table-column>
              <el-table-column
                prop="time"
                label="预约时间">
              </el-table-column>
              <el-table-column
                prop="price"
                label="价格">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)">取消预约</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <!-- 租用回弹 -->
      <el-dialog
        title="成功预约"
        :visible.sync="dialogTools"
        width="30%">
        <el-descriptions class="margin-top" title="预约信息" :column="2">
            <el-descriptions-item label="用户名">{{username}}</el-descriptions-item>
            <el-descriptions-item label="用户号">{{userid}}</el-descriptions-item>
            <el-descriptions-item label="订单编号">{{ordercode}}</el-descriptions-item>
            <el-descriptions-item label="器材编号">{{toolscode}}</el-descriptions-item>
          </el-descriptions>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogTools = false">确 定</el-button>
        </span>
      </el-dialog>
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
  </div>  
</template>

<script>
import request from '@/utils/request.js'
import {timeList,timeFormat,tFormat} from '@/api/time.js'
export default {
  data() {
    return {
      kindList:[],//用户存放种类
      formData:{
        kind:'',
        price:''
      },//预约用的表单
      priceList:[],//用于存放价格
      count:0,//可用器材数量
      flag:false,//预约按钮
      date:'',
      timeList:[],
      time:'',
      num:0,//选择的器材数量
      dialogTools:false,//成功后返回数据
      pickerOptions: {
        disabledDate(time) {
          const date = new Date();
          date.setTime(date.getTime() - 3600 * 1000 * 24)
          return time.getTime() <= date;
        }
      },//时间选择器 date
      ordercode:'#',//订单号
      toolscode:'',//器材号集合
      pagination: {//分页相关模型数据
          currentPage: 1,//当前页码
          pageSize:5,//每页显示的记录数
          total:0,//总记录数
          usercode:''
      },
      tableData:[]
    }
  },
  methods:{
    //返回主页
    goBack () {
      this.$router.push('/home')
    },
    //获取器材类型
    getKind(){
      this.timeList = timeList
      request({
        url:"/kinds",
        method:"get"
      }).then(res=>{
        // console.log(res);
        this.kindList = res.data.data
        // console.log(this.kindList);
      })
    },
    //获取器材价格
    getPrice(){
      request({
        url:"/tools/getprice?kind="+this.formData.kind,
        method:"get"
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.priceList = res.data.data
        }
        else{
          this.$message.error('系统出错,请稍后再试')
        }
      })
    },
    //查询器材剩余量
    search(){
      if(this.formData.kind === ""||this.formData.price === ""||this.date === '' ||this.time === '') this.$message.error("请选择后查询！")
      else{
        let t = 0
        t = timeFormat(this.time)
        let token  = localStorage.getItem('Authorization')
        let param = ''
        param += "?kind="+this.formData.kind
        param += "&price="+this.formData.price
        // console.log(this.date);
        request({
          url:'tools/search/'+token+'/'+this.date+'/'+t+'/'+param,
          method:'get',
        }).then(res=>{
          let code = res.data.code
          // console.log(res);
          if(code === 200){
            this.count = res.data.data
            if(this.count>0){
              this.flag = true
              this.$message.success("器材有剩余")
            }
            else{
              this.$message.info("无剩余器材")
            }
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
    //预约处理
    handleborrow(){
      let token  = localStorage.getItem('Authorization')
      let usercode = localStorage.getItem('userid')
      let t = 0
      t = timeFormat(this.time)
      if(this.num === 0){
        this.$message.error("请选择器材数量")
      }
      else{
        this.$confirm("此操作为租用器材，是否继续？","提示",{type:"info"}).then(()=>{
        let param=''
        param += "?kind="+this.formData.kind
        param += "&price="+this.formData.price
        // console.log(param);
        request({
          url:'borrows/rent/'+token+'/'+usercode+'/'+this.num+'/'+this.date+'/'+t+'/'+param,
          method:'get',
          }).then(res=>{
            let code = res.data.code
            console.log(res);
            if(code === 200){
              this.$message.success("预约成功")
              for(let i = 0;i<res.data.data.length-1;i++){
                this.toolscode+=res.data.data[i]
                if(i !== res.data.data.length-2) this.toolscode+=","
              }
              this.ordercode = res.data.data[res.data.data.length-1]
              this.dialogTools = true
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
        })
      }
    },
    //查询用户已租用的器材
    getAll(){
      let token  = localStorage.getItem('Authorization')
      this.pagination.usercode = localStorage.getItem('userid')
      let records = []
      request({
        url:'/borrows/getAll/'+token+'/'+this.pagination.currentPage+'/'+this.pagination.pageSize+'/'+this.pagination.usercode,
        method:'get'
      }).then(res=>{
        let code = res.data.code
        // console.log(res);
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
    //切换页码
    handleCurrentChange(currentPage) {
      //修改页码值为当前选中的页码值
      this.pagination.currentPage = currentPage;
      //执行查询
      this.getAll();
    },
    //删除操作
    handleDelete(index, row) {
      // console.log(index, row);
      // console.log(row.id);
      let token  = localStorage.getItem('Authorization')

      this.$confirm("此操作为取消预约，是否继续？","提示",{type:"info"}).then(()=>{
        request({
          url:'borrows/'+token+'/'+row.id,
          method:'delete'
        }).then(res=>{
          // console.log(res);
          let code = res.data.code
          if(code === 200){
            this.$message.success("成功取消预约")
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
        }).finally(()=>{
          //重新加载数据
          this.getAll();
        })
      })
    }
  },
  created() {
    this.getAll()
    this.getKind()
  },
  computed:{
    userid(){
      let userid = localStorage.getItem('userid')
      return userid
    },
    username(){
      let username = localStorage.getItem('username')
      return username
    }
  }
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
      .add-form{
        .sp{
          display: inline-block;
          font-size: 14px;
          color: #606266;
          height: 40px;
          line-height: 40px;
          margin-left: 30px;
        }
        /deep/.search{
          margin-top: 12px;
          margin-left: 30px;
          margin-bottom: 10px;
        }
        /deep/.el-date-editor{
          width: 200px;
        }
        /deep/.el-input-number{
          margin-top: 12px;
          width: 200px;
        }
      }
      .show-table{
        margin-top: 50px;
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