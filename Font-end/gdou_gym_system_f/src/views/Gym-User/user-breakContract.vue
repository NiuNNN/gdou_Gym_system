<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="失约处理" title="返回主页">
      </el-page-header>
      <div class="show">
        <div class="header">场地待缴费记录</div>
         <el-table
         :data="StateData"
         stripe
         style="width: 100%"
         height="250">
         <el-table-column
           prop="name"
           label="场地">
         </el-table-column>
         <el-table-column
           prop="date"
           label="日期">
         </el-table-column>
         <el-table-column
           prop="time"
           label="时间">
         </el-table-column>
         <el-table-column
           prop="price"
           label="金额">
         </el-table-column>
      </el-table>
      </div>
      <div class="show">
        <div class="header">场地待缴费记录</div>
          <el-table
          :data="ToolsData"
          stripe
          style="width: 100%"
          height="250">
          <el-table-column
           prop="kind"
           label="器材">
         </el-table-column>
         <el-table-column
           prop="date"
           label="日期">
         </el-table-column>
         <el-table-column
           prop="datetime"
           label="时间">
         </el-table-column>
         <el-table-column
           prop="price"
           label="金额">
         </el-table-column>
      </el-table>
      </div>
      
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      StateData:[],
      ToolsData:[]
    }
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    getState(){
      let userocde = localStorage.getItem('userid')
      request({
        url:'appointment/getPromise/'+userocde,
        method:'get'
      }).then(res=>{
        this.StateData = res.data.data
      })
    },
    getTools(){
      let userocde = localStorage.getItem('userid')
      request({
        url:'break/'+userocde,
        method:'get'
      }).then(res=>{
        this.ToolsData = res.data.data
      })
    }
  },
  created(){
    this.getState()
    this.getTools()
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
      margin-top: 30px;
      span{
        font-weight: 700;
      }
    }
  }
}
</style>