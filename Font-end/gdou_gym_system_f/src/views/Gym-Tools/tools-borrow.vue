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
                  <el-button type="primary" v-if="flag" @click="handleborrow()" class="search" icon="el-icon-s-claim">租用</el-button>
                  <el-button type="primary" v-else disabled class="search" icon="el-icon-s-claim">租用</el-button>
                </el-col>
              </el-row>
           </el-form>
        </div>
        <div class="show-table">
          123
        </div>
      </div>
      <!-- 租用回弹 -->

    </div>
  </div>  
</template>

<script>
import request from '@/utils/request.js'
import timeFormat from '@/api/time.js'
export default {
  data() {
    return {
      kindList:[],//用户存放种类
      formData:{
        kind:'',
        price:''
      },//预约用的表单
      priceList:[],//用于存放价格
      count:0,
      flag:false,
      date:'',
      timeList:[{
        lebel:'8:30~10:00',
        value:'8:30~10:00',
      },{
        lebel:'10:00~11:30',
        value:'10:00~11:30',
      },{
        lebel:'14:30~16:00',
        value:'14:30~16:00',
      },{
        lebel:'16:00~17:30',
        value:'16:00~17:30',
      },{
        lebel:'17:30~19:00',
        value:'17:30~19:00',
      },{
        lebel:'19:00~20:30',
        value:'19:00~20:30',
      },{
        lebel:'20:30~22:00',
        value:'20:30~22:00',
      }],//设置时间段
      time:'',
      num:0,
      pickerOptions: {
        disabledDate(time) {
          const date = new Date();
          date.setTime(date.getTime() - 3600 * 1000 * 24)
          return time.getTime() <= date;
        }
      }
    }
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    //获取器材类型
    getKind(){
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
        console.log(this.date);
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
    // 预约处理
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
        console.log(param);
        request({
          url:'borrows/rent/'+token+'/'+usercode+'/'+this.num+'/'+this.date+'/'+t+'/'+param,
          method:'get',
          }).then(res=>{
            console.log(res);
          })
        })
      }
    }
  },
  created() {
    this.getKind()
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
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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
        margin-top: 20px;
        height: 100px;
        background-color: pink;
      }
    }
  }
}
</style>