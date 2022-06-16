<template>
<div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="场地增删改查" title="返回主页">
      </el-page-header>
      <div class="search">
        <el-button type="primary" style="margin: 20px 0" @click="dialogFormVisible = true">添加场地</el-button>
      </div>
      <div class="show">
        <el-table border style="width: 100%" :data="listState">
          <el-table-column type="index" label="序号" width="80px" align="center">
          </el-table-column>
          <!-- <el-table-column label="数量"> </el-table-column> -->
          <el-table-column label="场地名称" prop="name"> </el-table-column>
          <el-table-column label="价格" prop="price"> </el-table-column>
          <el-table-column label="是否已选" prop="selected"> </el-table-column>
             <el-table-column label="时间段" prop="time"> </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="{row}">
            <el-button icon="el-icon-delete" type="danger" @click="deleteappid(row)"></el-button>
            </template>

          </el-table-column>
        </el-table>
      </div>
    </div>
    <!-- 弹窗 -->
    <div class="getmessage">
      <el-dialog
        title="场地信息"
        :visible.sync="dialogFormVisible"
        custom-class="dheight"
        width="35%">
        <div class="el-dialog__body">
          <el-form :model="state">
            <el-form-item label="场地名称">
              <el-input autocomplete="on" v-model="state.name"></el-input>
            </el-form-item>
            <el-form-item label="场地价格">
              <el-input autocomplete="on" v-model="state.price"></el-input>
            </el-form-item>
            <el-form-item label="时间段">
              <el-select
                placeholder="请选择时间段"
                clearable
                filterable
                v-model="state.time"
              >
                <el-option label="1" value="1"> </el-option>
                <el-option label="2" value="2"> </el-option>
                <el-option label="3" value="3"> </el-option>
                <el-option label="4" value="4"> </el-option>
                <el-option label="5" value="5"> </el-option>
                <el-option label="6" value="6"> </el-option>
                 <el-option label="7" value="7"> </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="insertState">确 定</el-button>
        </div>
      </el-dialog>
    </div>
</div>
</template>

<script>
export default {
  name: "State",
  data() {
    return {
      state: {
        name: "",
        price: "",
        time: "",
      },
      listState: [],
      dialogFormVisible: false,
      formLabelWidth: "120px",
    };
  },
  mounted() {
    this.getstate();
  },
  methods: {
    async getstate() {
      try {
        let result = await this.$API.getListStateall();

        if (result.data.code == 200) {
          this.listState = result.data.data;
        }
      } catch (error) {
        console.log(this.error);
      }
    },
    //返回首页
    goBack() {
      this.$router.push("/home");
    },
    //添加场地
    async insertState() {
      try {
        this.dialogFormVisible = false;
        let result = await this.$API.insertState(this.state);
        console.log(result);
        if (result.data.code == 200) {
          this.$message({
            type: "success",
            message: "添加成功",
          });
          this.getstate();
        }
      } catch (error) {
        this.$message({
          type: "error",
          message: error,
        });
      }
    },
    //删除场地
    async deleteappid(row){
           try {
            console.log(row.id,row.name,row.time);
        let result = await this.$API.deleteStateById(row.id,row.name,row.time);

        if (result.data.code == 200) {
          this.$message({
            type:'success',
            message:'删除成功'
          })
            this.getstate();
        }
      } catch (error) {
        
      }
    }
  },
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
.getmessage{
  .el-dialog{
  height: 500px;
  .el-dialog__body{
    height: 300px;
    }
  }
}
</style>