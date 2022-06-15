<template>
  <div class="descriptions-container">
      <div class="descriptions-box">
        <el-page-header @back="goBack" content="场地公告" title="返回主页">
        </el-page-header>
        <el-card v-for="(txt,index) in txts" :key="index" style="margin:45px">
          <span>{{txt.content}}</span>
        </el-card>
      </div>
  </div>
</template>

<script>
export default {
    data(){
        return{
            txts:[]
        }
    },
  mounted(){
    this.getAllNotice()
  },
    methods:{
    //获取所有数据
  async  getAllNotice(){
      let result=await this.$API.getListNotice()
      if(result.data.code==200){
            this.txts=result.data.data
      }
    },
    }
}
</script>

<style>

</style>