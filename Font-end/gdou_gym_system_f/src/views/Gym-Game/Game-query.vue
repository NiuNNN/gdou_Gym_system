<template>
  <div class="descriptions-box">
    <el-page-header @back="goBack" content="赛事查询" title="返回主页">
    </el-page-header>
    <el-row :gutter="20" style="padding: 30px 60px 0px 60px">
      <el-col :span="12"
        ><div class="grid-content bg-purple">
          <div class="select">赛事名称:</div>
          <div class="select">
            <el-input
              placeholder="请输入赛事名称"
              style="width: 180px"
              v-model="game_name"
              clearable
            >
            </el-input>
          </div></div
      ></el-col>
      <el-col :span="12"
        ><div class="grid-content bg-purple">
          <div class="select">赛事类型:</div>
          <div class="select">
            <el-input
              placeholder="请输入赛事类型"
              style="width: 180px"
              v-model="game_type"
              clearable
            >
            </el-input>
          </div></div
      ></el-col>
    </el-row>
    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12">
        <div class="location select">
          <span>赛事场地：</span>
          <el-select
            v-model="game_location"
            placeholder="请选择赛事场地"
            style="width: 180px"
          >
            <el-option
              v-for="(item, index) in options"
              :key="index"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="time select">
          <span>赛事日期：</span>
          <el-date-picker
            v-model="game_time"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            style="width: 200px"
          >
          </el-date-picker>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12"
        ><div class="grid-content bg-purple">
          <div class="select">赛事&nbsp;&nbsp;&nbsp;ID:</div>
          <div class="select">
            <el-input
              placeholder="请输入ID"
              style="width: 180px"
              v-model="game_id"
              clearable
            >
            </el-input>
          </div></div
      ></el-col>
      <el-col :span="12">
        <div class="select">
          <el-button type="primary" icon="el-icon-search" @click="query"
            >搜索</el-button
          >
        </div>
      </el-col>
    </el-row>
    <el-table :data="tableData" style="width: 100%; padding: 0px 30px">
      <el-table-column prop="gameid" label="赛事ID" width="100">
      </el-table-column>
      <el-table-column prop="gametime" label="赛事时间" width="180">
      </el-table-column>
      <el-table-column prop="gamename" label="赛事名称" width="180">
      </el-table-column>
      <el-table-column prop="gametype" label="赛事类型" width="100">
      </el-table-column>
      <el-table-column
        prop="gamelocation"
        label="赛事地点"
        width="240"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="look(scope.$index, tableData)"
            type="text"
            size="small"
          >
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="成功预约" :visible.sync="detail" width="30%">
      <el-descriptions class="margin-top" title="预约信息" :column="2">
        <el-descriptions-item label="赛事ID">{{
          detailData.gameid
        }}</el-descriptions-item>
        <el-descriptions-item label="该赛事用户ID">{{
          detailData.userid
        }}</el-descriptions-item>

        <el-descriptions-item label="赛事器材">{{
          detailData.gametool
        }}</el-descriptions-item>
        <el-descriptions-item label="裁判预约订单号">{{
          detailData.judgeorderid
        }}</el-descriptions-item>

        <el-descriptions-item label="器材订单号">{{
          detailData.toolid
        }}</el-descriptions-item>
        <el-descriptions-item label="场地订单号">{{
          detailData.locationid
        }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detail = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      game_name: '',
      game_type: '',
      game_location: '',
      game_time: '',
      game_id: '',
      detail: false,
      options: [],

      tableData: [],
      detailData: [],
    }
  },
  methods: {
    look(index, rows) {
      this.detailData = this.tableData[index]
      this.detail = true
    },
    unique(arr) {
      return Array.from(new Set(arr))
    },
    getlocation() {
      request({
        url: '/state/selectAll',
        method: 'Get',
      }).then((res) => {
        let code = res.data.code
        if (code === 200) {
          let list = res.data.data.map((item) => {
            return item.name
          })
          this.options = this.unique(list)
          console.log(this.options)
        }
      })
    },
    goBack() {
      this.$router.push('/home')
    },
    query() {
      this.getgameinfo()
    },
    getgameinfo() {
      if (
        this.game_id === '' ||
        this.game_time === '' ||
        this.game_location === '' ||
        this.game_type === '' ||
        this.game_name === ''
      ) {
        this.$message.error('请填写完整的查询信息')
      } else {
        request({
          url: '/game/get',
          params: {
            gameid: this.game_id,
            gametime: this.game_time,
            gamelocation: this.game_location,
            gametype: this.game_type,
            gamename: this.game_name,
          },
          method: 'Get',
        }).then((res) => {
          let code = res.data.code
          if (code === 200) {
            this.tableData = res.data.data
          } else {
            this.$message.error('查询的信息有误')
            this.tableData = []
          }
        })
      }
    },
    deleteRow(index, rows) {
      this.$confirm('将删除该赛事, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.deletegame(this.tableData[index].gameid)
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          rows.splice(index, 1)
          //在这去后台删除数据
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
  },
  created() {
    this.getlocation()
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
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
}
</style>
