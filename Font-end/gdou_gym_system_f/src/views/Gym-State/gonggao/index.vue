<template>
<div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="公告添加" title="返回主页">
      </el-page-header>
      <div class="add">
        <el-button type="primary" style="margin: 20px 0" @click="dialogFormVisible = true">添加公告</el-button>
      </div>
      <div class="show">
        <el-table :data="txts" border >
          <el-table-column label="序号" type="index"
          >
          </el-table-column>
            <el-table-column label="公告内容" prop="content"
          >
          </el-table-column>
              <el-table-column label="操作" 
          >
          <template slot-scope="{row}">
          <el-button type="danger" size="default" @click="deletenotice(row.id)" icon="el-icon-delete"></el-button>

          </template>

          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="notice">
      <el-dialog
        title="场地公告"
        :visible.sync="dialogFormVisible"
        custom-class="dheight"
      >
        <div class="el-dialog__body">
            <el-input
              v-model="textarea"
              autocomplete="on"
              type="textarea"
              :rows="3">
              </el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="insertNotice"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>
</div>
</template>

<script>
export default {
  data() {
    return {
  
        textarea: "",
        txts:[],

      dialogFormVisible: false,
      formLabelWidth: "120px",
    };
  },
  name: "gonggao",
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
//删除公告
    async deletenotice(id){
     let result=await this.$API.deletenoticeById(id)
     if(result.data.code==200){
      this.$message({
        type:'success',
        message:'删除成功'
      }),
      this.getAllNotice()
     }
    }
    ,
    //添加公告
    async insertNotice(){
      this.dialogFormVisible = false
    // let tar =JSON.stringify(this.textarea)
   
      let result=await this.$API.insertNotice(this.textarea)
      if(result.data.code==200){
            this.$message({
        type:'success',
        message:'添加成功'
      }),
      this.getAllNotice()
     
      }
    },
    goBack(){
      this.$router.push("/home");
    }
  }
};
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
    .change-form{
      margin: 50px auto;
      margin-bottom: 100px;
      width: 35%;
    }
  }
}
</style>
<style lang="less">
.add{
  .el-dialog{
  height: 500px;
  .el-dialog__body{
    height: 300px;
    }
  }
}
</style>