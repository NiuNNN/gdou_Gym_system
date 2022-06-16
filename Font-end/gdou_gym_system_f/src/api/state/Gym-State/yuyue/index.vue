<template>
  <div class="descriptions-container">
      <div class="descriptions-box">
        <el-page-header @back="goBack" content="场地预约" title="返回主页">
        </el-page-header>
        <div class="book-form">
        <el-form ref="form" :model="sizeForm" label-width="88px" :inline="true">
          <el-row>
            <el-col :span="8">
              <el-form-item label="预约场地">
              <el-select
                v-model="sizeForm.name"
                placeholder="请选择活动区域"
                @change="selectbyNames">
                <el-option
                  :label="st"
                  :value="st"
                  v-for="(st, index) in namelist"
                  :key="index">
                </el-option>
              </el-select>
          </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预约日期">
                <div class="block">
                  <el-date-picker
                    v-model="sizeForm.date"
                    type="date"
                    placeholder="选择日期"
                    @change="changedate">
                  </el-date-picker>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预约时间段">
                <el-select
                  v-model="sizeForm.time"
                  placeholder="请选择活动区域"
                  @change="tranfer">
                  <el-option
                    :label="tt"
                    :value="tt"
                    v-for="(tt, index) in times"
                    :key="index">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="价格">
                <el-select v-model="sizeForm.price" placeholder="请选择活动区域">
                  <el-option
                    :label="pp"
                    :value="pp"
                    v-for="(pp, index) in price"
                    :key="index">
                    </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item size="large">
              <el-button type="primary" @click="insertAppointment">立即预约</el-button>
             </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        </div>
        <div class="show">
         <h2 style="display: block; text-align: center">预约信息</h2>
      <!-- 展示数据 -->
        <el-table border style="width: 100%" :data="appointment">
          <el-table-column type="index" label="序号" width="80px" align="center">
          </el-table-column>
          <!-- <el-table-column label="数量"> </el-table-column> -->
          <el-table-column label="场地名称" prop="name"> </el-table-column>
          <el-table-column label="价格" prop="price"> </el-table-column>
          <el-table-column prop="date" label="日期"> </el-table-column>

          <el-table-column label="时间段" prop="time"> </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="{ row }">
              <el-button
                icon="el-icon-delete"
                type="danger"
               v-if="!JSON.parse(row.promise)"
             @click="deleteappid(row.id)"
              ></el-button>
                <h3 v-else>已判定失约</h3>
            </template>
          </el-table-column>
        </el-table>
        </div>
      </div>
  </div>
</template>

<script>
import dataFormat from "@/api/timeymd";
export default {
  name: "yuyue",
  data() {
    return {
      price: [],
      namelist: [],
      times: [],
      appointment: {},
      isshiyue:false,
      sizeForm: {
        userId: "",
        name: "",
        date: "",
        time: "",
        price: "",
        promise:false,
      },
             dialogFormVisible: false,
      value1: "",
    };
  },
  mounted() {
    this.getstate();
    this.seleteAll();
  },
  methods: {
    //获取所有预约信息
    async seleteAll() {
      try {
  
         let userId = localStorage.getItem("userid");
        let result = await this.$API.getuserapp(userId);
        console.log(result);
        if (result.data.code == 200) {
          this.appointment = result.data.data;
           console.log(this.appointment.promise);
               for (let i = 0; i <= this.appointment.length; i++) {
               
            switch (this.appointment[i].time) {
              case "1":
                this.appointment[i].time= "08:30:00-10:00:00";
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

    //获取所有场地名称
    async getstate() {
      try {
        let result = await this.$API.getListState();

        if (result.data.code == 200) {
          let list = result.data.data.map((item) => {
            return item.name;
          });

          this.namelist = this.unique(list);
        }
      } catch (error) {
        console.log(this.error);
      }
    },
    //通过名称查询时间段
    async selectbyNames(name) {
      try {
        let result = await this.$API.selectByName(name);
        let result1 = await this.$API.selectByNametop(name);
        if (result.data.code == 200 && result1.data.code == 200) {
          this.times = result.data.data;
          for (let i = 0; i <= this.times.length; i++) {
            switch (this.times[i]) {
              case "1":
                this.times[i] = "08:30:00-10:00:00";
                break;
              case "2":
                this.times[i] = "10:00:00-11:30:00";
                break;
              case "3":
                this.times[i] = "14:30:00-16:00:00";
                break;
              case "4":
                this.times[i] = "16:00:00-17:00:00";
                break;
              case "5":
                this.times[i] = "17:30:00-19:00:00";
                break;
              case "6":
                this.times[i] = "19:30:00-20:30:00";
                break;
                    case "7":
                this.times[i] = "20:30:00-22:00:00";
                break;
            }
          }
          this.price = this.unique(result1.data.data);
        }
      } catch (error) {}
    },

    tranfer(time) {
      switch (time) {
        case "08:30:00-10:00:00":
          console.log(this.sizeForm.time);
          this.sizeForm.time = "1";
          break;
        case "10:00:00-11:30:00":
          this.sizeForm.time = "2";
          break;
        case "14:30:00-16:00:00":
          this.sizeForm.time = "3";
          break;
        case "16:00:00-17:00:00":
          this.sizeForm.time = "4";
          break;
        case "17:30:00-19:00:00":
          this.sizeForm.time = "5";
          break;
        case "19:30:00-20:30:00":
          this.sizeForm.time = "6";
          break;
        case "20:30:00-22:00:00":
          this.sizeForm.time = "7";
          break;
      }
    },
    //预约
    async insertAppointment() {
      try {
 let userId = localStorage.getItem("userid");
 this.sizeForm.userId=userId;
        let result = await this.$API.insertAppointment(this.sizeForm);

        if (result.data.code == 200) {
          this.$message({
            type: "success",
            message: "添加成功",
          }),  
             this.getstate();
    this.seleteAll();
       this.price=[],
       this.namelist= [],
       this.times=[],
      this.sizeForm={}
        
        }
      } catch (error) {
        alert(error)
      }
    },

    //删除预约
    async deleteappid(id){

        let result=await this.$API.deleteById(id)
        if(result.data.code==200){
          this.$message({
            type:"success",
            message:'删除成功'
          })
           this.getstate();
            this.seleteAll();
        }
    },
  
    //设置特定格式
    changedate() {
      this.sizeForm.date = dataFormat("yyy-MM-dd", this.sizeForm.date);
    },

    //返回首页
    goBack() {
      this.$router.push("/home");
    },
    //数组去重
    unique(arr) {
      return Array.from(new Set(arr));
    },
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
    .book-form{
      margin: 50px auto;
    }
  }
}
</style>