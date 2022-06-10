<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="器材租还" title="返回主页">
      </el-page-header>
    </div>
  </div>
</template>

<script>
export default {
  methods:{
    goBack () {
      this.$router.push('/home')
    },
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
  }
}
</style>