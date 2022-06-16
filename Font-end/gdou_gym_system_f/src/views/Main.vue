<template>
  <div class="container">
    <el-row :gutter="40">
      <el-col :span="12">
        <div class="notice">
          <div class="header">体育馆公告</div>
          <div class="content">
            <el-card class="box-card">
              <div v-for="(txt,index) in txts" :key="index" class="text item">
                {{txt.content}}
              </div>
            </el-card>
          </div>
          </div>
      </el-col>
      <el-col :span="12">
        <div class="notice">
          <div class="header">违规公告</div>
          <div class="content">
            <el-card class="box-card">
              <div v-for="(item,index) in breakContract" :key="index" class="text item">
                {{item}}
              </div>
            </el-card>
          </div>
          </div>
      </el-col>
    </el-row>
    <el-row :gutter="40">
      <el-col :span="12">
        <div class="notice">
          <div class="header">场地预约信息</div>
          <div class="content">
            <el-card class="box-card">
              <div v-for="(item,index) in state" :key="index" class="text item">
                {{'您已预约： '+item.name +' 预约时间： '+ item.date + ' '+ item.time}}
              </div>
            </el-card>
          </div>
          </div>
      </el-col>
      <el-col :span="12">
        <div class="notice">
          <div class="header">器材预约信息</div>
          <div class="content">
            <el-card class="box-card">
              <div v-for="(item,index) in tools" :key="index" class="text item">
                {{'您已预约： '+item.kind +' 预约时间： '+ item.date + ' '+ item.datetime}}
              </div>
            </el-card>
          </div>
          </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      txts:[],
      breakContract:[],
      tools:[],
      state:[]
    }
  },
  methods:{
    async getAllNotice(){
      let result=await this.$API.getListNotice()
      if(result.data.code==200){
        this.txts=result.data.data
      }
    },
    getBreakContract(){
      request({
        url:'break',
        method:'get'
      }).then(res=>{
        // console.log(res);
        this.breakContract = res.data.data
      })
    },
    getToolsRent(){
      let usercode = localStorage.getItem('userid')
      request({
        url:'borrows/'+usercode,
        method:'get'
      }).then(res=>{
        // console.log(res);
        this.tools = res.data.data
      })
    },
    getStateRent(){
      let usercode = localStorage.getItem('userid')
      request({
        url:'appointment/rent/'+usercode,
        method:'get'
      }).then(res=>{
        console.log(res);
        this.state = res.data.data
      })
    }
  },
  created(){
    this.getAllNotice()
    this.getBreakContract()
    this.getToolsRent()
    this.getStateRent()
  }
}
</script>

<style lang="less" scoped>
.container{
  .notice{
    // background-color: pink;
    margin-bottom: 50px;
    .header{
      color: #303133;
      height: 40px;
      font-size: 20px;
      font-weight: 700;
    }
    .content{
      padding-left: 15px;
      height: 310px;
      overflow: visible;
      // background-color: purple;
    }
  }
}
.text {
  font-size: 14px;
}
.item {
  padding: 18px 0;
}
/deep/.el-card{
  height: 300px;
}

</style>