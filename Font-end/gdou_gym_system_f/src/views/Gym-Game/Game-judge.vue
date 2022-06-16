<template>
  <div class="descriptions-box">
    <el-page-header @back="goBack" content="裁判管理" title="返回主页">
    </el-page-header>
    <el-row :gutter="20" style="padding: 30px 60px 0px 60px">
      <el-col :span="12">
        <div class="select">裁判姓名：</div>
        <div class="select">
          <el-input
            placeholder="请输入裁判姓名"
            style="width: 200px"
            v-model="judge_name"
            clearable
          >
          </el-input></div
      ></el-col>
      <el-col :span="12">
        <div class="select">裁判年龄：</div>
        <div class="select">
          <el-input
            placeholder="请输入裁判年龄"
            style="width: 200px"
            v-model="judge_age"
            clearable
          >
          </el-input></div
      ></el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="14">
        <div class="select text">工作经历：</div>
        <div class="select">
          <el-input
            type="textarea"
            placeholder="请输入工作经历"
            style="width: 300px"
            v-model="judge_detail"
            :autosize="{ minRows: 2, maxRows: 4 }"
            clearable
          >
          </el-input>
        </div>
      </el-col>
      <el-col :span="5"
        ><el-button type="primary" @click="addjudge"
          >添加裁判</el-button
        ></el-col
      >
    </el-row>
    <el-row>
      <el-table :data="tableData" style="width: 100%; padding: 0px 10px">
        <el-table-column prop="judgeid" label="裁判ID" width="200">
        </el-table-column>
        <el-table-column prop="name" label="裁判姓名" width="200">
        </el-table-column>
        <el-table-column prop="age" label="裁判年龄" width="200">
        </el-table-column>
        <el-table-column
          prop="details"
          label="工作经历"
          width="240"
        ></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click.native.prevent="look(scope.$index)"
            >
              查看
            </el-button>
            <el-button
              @click.native.prevent="deleteRow(scope.$index, tableData)"
              type="text"
              size="small"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-dialog title="查看裁判信息" width="30%" :visible.sync="detailvisible">
      <el-descriptions :column="2">
        <el-descriptions-item label="裁判名称">{{
          detailData.name
        }}</el-descriptions-item>
        <el-descriptions-item label="裁判ID">{{
          detailData.judgeid
        }}</el-descriptions-item>
        <el-descriptions-item label="工作经历">{{
          detailData.details
        }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      judge_name: '',
      judge_age: '',
      judge_detail: '',
      detailvisible: false,
      tableData: [],
      detailData: [],
      check: 0,
    }
  },
  methods: {
    look(index) {
      this.detailData = this.tableData[index]
      this.detailvisible = true
      console.log(this.tableData)
      console.log(this.detailData)
    },
    goBack() {
      this.$router.push('/home')
    },
    addjudge() {
      request({
        url: '/judge/save',
        data: {
          name: this.judge_name,
          age: this.judge_age,
          details: this.judge_detail,
          avatar: '1',
        },
        method: 'Post',
      }).then((res) => {
        let code = res.data.code
        if (code === 200) {
          this.$message.success('添加成功')
          this.getjudgeinfo()
        } else {
          this.$message.error('添加失败')
        }
      })
    },
    getjudgeinfo() {
      request({
        url: '/judge/getjudgeid',
        method: 'Get',
      }).then((res) => {
        let code1 = res.data.code
        if (code1 === 200) {
          this.tableData = res.data.data
        } else {
          this.$message.error('获取裁判信息失败')
        }
      })
    },
    deletejudge(judgeid) {
      request({
        url: '/judge/deletejudge/' + judgeid,
        method: 'Post',
      }).then((res) => {
        if (res.data.code === 200) {
          this.$message.success('删除裁判成功')
          this.check = 1
        } else {
          this.$message.error('删除裁判失败')
        }
      })
    },
    deleteRow(index, rows) {
      let a = this.tableData[index].judgeid
      this.deletejudge(a)
      if (this.check === 1) {
        rows.splice(index, 1)
      }
    },
  },
  created() {
    this.getjudgeinfo()
  },
}
</script>
<style lang="less" scoped>
.descriptions-box {
  position: relative;
  margin: 30px auto;
  width: 98%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0);
  border-radius: 4px;
  overflow: hidden;
  height: 800px;
}
.select {
  display: inline-block;
  padding-left: 5px;
}
.text {
  float: left;
  margin-left: 60px;
}
</style>
