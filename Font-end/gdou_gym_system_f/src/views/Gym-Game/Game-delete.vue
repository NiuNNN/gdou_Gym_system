<template>
  <div class="descriptions-box">
    <el-page-header @back="goBack" content="赛事取消" title="返回主页">
    </el-page-header>
    <el-row :gutter="20" style="padding: 30px 60px 0px 60px">
      <el-col :span="12"
        ><div class="grid-content bg-purple">
          <div class="select">赛事名称:</div>
          <div class="select">
            <el-input
              placeholder="请输入内容"
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
              placeholder="请输入内容"
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
              placeholder="请输入内容"
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
    <el-table :data="tableData" style="width: 100%; padding: 0px 10px">
      <el-table-column prop="gameid" label="赛事ID" width="180">
      </el-table-column>
      <el-table-column prop="gametime" label="赛事时间" width="180">
      </el-table-column>
      <el-table-column prop="gamename" label="赛事名称" width="180">
      </el-table-column>
      <el-table-column prop="gametype" label="赛事类型" width="180">
      </el-table-column>
      <el-table-column
        prop="gamelocation"
        label="赛事地点"
        width="240"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
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
      checkdelete: '',
      options: [],
      tableData: [],
      toolid: '',
    }
  },
  methods: {
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
    deletegame(index) {
      this.toolid = this.tableData[index].toolid
      request({
        url: '/game/delete/' + this.tableData[index].gameid,

        method: 'delete',
      }).then((res) => {
        let code = res.data.code
        if (code === 200) {
          let id = this.tableData[index].toolid
          request({
            url: '/appointment/gametodelete/' + id,
            method: 'Post',
          }).then((res) => {
            let token = localStorage.getItem('Authorization')
            if (res.data.code === 200) {
              this.toolid = this.tableData[index].toolid
              request({
                url: '/price/cancel/' + token + '/' + this.toolid,
                method: 'Delete',
              }).then((res) => {
                if (res.data.code === 200) {
                  let id = this.tableData[index].judgeorderid
                  request({
                    url: '/judgestate/delete/' + id,
                    method: 'Delete',
                  }).then((res) => {
                    if (res.data.code === 200) {
                      this.$message.success('赛事删除成功')
                      this.getgameinfo()
                    } else {
                      request({
                        url: '/game/save',
                        method: 'Post',
                        data: {
                          userid: this.tableData[index].userid,
                          gamename: this.tableData[index].gamename,
                          gametime: this.tableData[index].gametime,
                          gametype: this.tableData[index].game_type,
                          gametool: this.tableData[index].gametool,
                          gamelocation: this.tableData[index].location,
                          gamenum: this.tableData[index].gamenum,
                          toolid: this.tableData[index].toolid,
                          locationid: this.tableData[index].locationid,
                          judgeorderid: this.tableData[index].judgeorderid,
                        },
                      }).then((res) => {
                        if (res.data.code === 200) {
                          this.$message.success('裁判预约订单恢复成功')
                        } else {
                          this.$message.error(
                            '赛事恢复失败，联系管理员处理器材'
                          )
                        }
                      })
                    }
                  })
                } else {
                  this.$message.error('器材删除失败，开始恢复')
                  request({
                    url: '/game/save',
                    method: 'Post',
                    data: {
                      userid: this.tableData[index].userid,
                      gamename: this.tableData[index].gamename,
                      gametime: this.tableData[index].gametime,
                      gametype: this.tableData[index].game_type,
                      gametool: this.tableData[index].gametool,
                      gamelocation: this.tableData[index].location,
                      gamenum: this.tableData[index].gamenum,
                      toolid: this.tableData[index].toolid,
                      locationid: this.tableData[index].locationid,
                    },
                  }).then((res) => {
                    if (res.data.code === 200) {
                      this.$message.success('赛事恢复成功')
                    } else {
                      this.$message.error('赛事恢复失败，联系管理员处理器材')
                    }
                  })
                }
              })
            } else {
              this.$message.error('场地删除失败')
              request({
                url: '/game/save',
                method: 'Post',
                data: {
                  userid: this.tableData[index].userid,
                  gamename: this.tableData[index].gamename,
                  gametime: this.tableData[index].gametime,
                  gametype: this.tableData[index].game_type,
                  gametool: this.tableData[index].gametool,
                  gamelocation: this.tableData[index].location,
                  gamenum: this.tableData[index].gamenum,
                  toolid: this.tableData[index].toolid,
                  locationid: this.tableData[index].locationid,
                },
              }).then((res) => {
                if (res.data.code === 200) {
                  this.$message.success('赛事恢复成功')
                } else {
                  this.$message.error('赛事恢复失败，联系管理员删除场地')
                }
              })
            }
          })
        } else {
          this.$message.error('系统出现错误，删除赛事失败')
        }
      })
    },
    getgameinfo() {
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
        }
      })
    },
    deleteRow(index, rows) {
      this.$confirm('将删除该赛事, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.deletegame(index)

          if (this.checkdelete === 1) {
            rows.splice(index, 1)
          }
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
