<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="场地失约处理" title="返回主页">
      </el-page-header>
      <div class="search">
      <el-form label-width="100px" :inline="true">
        <el-row>
          <el-col :span="8">
            <el-form-item label="请输入用户ID">
              <el-input v-model="userId" @blur="kong"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item>
            <el-button type="primary"  @click="queryByid" >查询</el-button>
          </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="show">
      <h2 style="display: block; text-align: center">预约信息</h2>
    <!-- 展示数据 -->
    <el-table border style="width: 100%" :data="appointment" class="block" v-if="isblock">
      <el-table-column type="index" label="序号" width="80px" align="center">
      </el-table-column>
      <!-- <el-table-column label="数量"> </el-table-column> -->
      <el-table-column label="用户ID" prop="userId"> </el-table-column>
      <el-table-column label="场地名称" prop="name"> </el-table-column>
      <el-table-column label="价格" prop="price"> </el-table-column>
      <el-table-column prop="date" label="日期"> </el-table-column>
      <el-table-column prop="promise" label="是否失约"> </el-table-column>
      <el-table-column label="时间段" prop="time"> </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="{ row }">
          <el-button
            icon="el-icon-success"
            type="danger"
            @click="updapromise(row.id)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

      <el-table border style="width: 100%" :data="appointmentbyuserId" class="block" v-else>
      <el-table-column type="index" label="序号" width="80px" align="center">
      </el-table-column>
      <!-- <el-table-column label="数量"> </el-table-column> -->
      <el-table-column label="用户ID" prop="userId"> </el-table-column>
      <el-table-column label="场地名称" prop="name"> </el-table-column>
      <el-table-column label="价格" prop="price"> </el-table-column>
      <el-table-column prop="date" label="日期"> </el-table-column>
      <el-table-column prop="promise" label="是否失约"> </el-table-column>
      <el-table-column label="时间段" prop="time"> </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="{ row }">
          <el-button
            icon="el-icon-success"
            type="danger"
            @click="updapromise(row.id)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
    </div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      isblock:true,
      userId: "",
      appointment: [],
      appointmentbyuserId: [],
    };
  },
  mounted() {
    this.seleteAll();
  },
  methods: {
    //获取所有预约信息
    async seleteAll() {
      try {
        let result = await this.$API.getstateapp();
        if (result.data.code == 200) {
          this.appointment = result.data.data;
          console.log(appointment);
          for (let i = 0; i <= this.appointment.length; i++) {
            switch (this.appointment[i].time) {
              case "1":
                this.appointment[i].time = "08:30:00-10:00:00";
                break;
              case "2":
                this.appointment[i].time = "10:00:00-11:30:00";
                break;
              case "3":
                this.appointment[i].time = "14:30:00-16:00:00";
                break;
              case "4":
                this.appointment[i].time = "16:00:00-17:00:00";
                break;
              case "5":
                this.appointment[i].time = "17:30:00-19:00:00";
                break;
              case "6":
                this.appointment[i].time = "19:30:00-20:30:00";
                break;
              case "7":
                this.appointment[i].time = "20:30:00-22:00:00";
                break;
            }
          }
        }
      } catch (error) {}
    },

    async updapromise(id) {
      let result = await this.$API.updapromise(id);
      this.seleteAll();
    },
    async queryByid() {
    
      let result = await this.$API.getuserapp(this.userId);
      if (result.data.code == 200) {
        this.appointmentbyuserId = result.data.data;
        this.isblock=false
      }
    },
    goBack(){
      this.$router.push('/home')
    },
    kong(){
      if(this.userId==""){
         this.seleteAll();
        this.isblock=true
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
    .search{
      margin-top: 30px;
    }
  }
}
</style>