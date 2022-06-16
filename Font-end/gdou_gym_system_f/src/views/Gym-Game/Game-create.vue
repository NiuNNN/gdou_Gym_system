<template>
  <div class="descriptions-box">
    <el-page-header @back="goBack" content="赛事创建" title="返回主页">
    </el-page-header>
    <el-row :gutter="20" style="padding: 30px 60px 0px 60px">
      <el-col :span="12">
        <div class="select">赛事名称：</div>
        <div class="select">
          <el-input
            placeholder="请输入赛事名称"
            style="width: 200px"
            v-model="game_name"
            clearable
          >
          </el-input></div
      ></el-col>
      <el-col :span="12">
        <div class="location select">
          <span>赛事场地：</span>
          <el-select
            v-model="locationvalue"
            placeholder="请选择赛事场地"
            style="width: 200px; padding-left: 5px"
            @change="getlocationprice"
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
    </el-row>
    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12">
        <div class="select">赛事类型：</div>
        <div class="select">
          <el-input
            placeholder="请输入赛事类型"
            style="width: 200px"
            v-model="game_type"
            clearable
          >
          </el-input></div
      ></el-col>
      <el-col :span="12">
        <div class="tool select">
          <span>赛事器材：</span>
          <el-select
            v-model="toolvalue"
            placeholder="请选择赛事器材"
            @change="getprice()"
          >
            <!-- toollist是ajax获取的时间列表 -->
            <el-option
              style="width: 200px"
              v-for="item in toollist"
              :key="item.value"
              :label="item.kind"
              :value="item.kind"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12">
        <div class="time select">
          <span>赛事日期：</span>
          <el-date-picker
            v-model="date"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
            value-format="yyyy-MM-dd"
            style="width: 200px"
          >
          </el-date-picker>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="time select">
          <span>时间段：</span>
          <el-select v-model="time" placeholder="选择时间段">
            <el-option
              v-for="(item, index) in timelist"
              :key="index"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12">
        <div class="tool select">
          <span>场地价格：</span>
          <el-select v-model="locationprice" placeholder="请选择器材价格">
            <el-option
              style="width: 200px"
              v-for="(item, index) in locationpricelist"
              :key="index"
              :label="item"
              :value="item"
              @click.native="toolclick(item.kind)"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="tool select">
          <span>器材价格：</span>
          <el-select v-model="game_price" placeholder="请选择器材价格">
            <el-option
              style="width: 200px"
              v-for="(item, index) in pricelist"
              :key="index"
              :label="item.price"
              :value="item.price"
              @click.native="toolclick(item.kind)"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12" style="padding: 0px 50px"
        ><div class="grid-content bg-purple">
          <div class="select">器材剩余数量：</div>
          <div class="select">
            <p>{{ game_renum }}</p>
          </div>
        </div></el-col
      >
      <el-col :span="12">
        <div class="select">器材数量：</div>
        <div class="select">
          <el-input
            placeholder="请输入器材数量"
            style="width: 90px"
            v-model="game_num"
            clearable
          >
          </el-input></div
      ></el-col>
    </el-row>
    <el-row :gutter="20" style="padding: 0px 60px">
      <el-col :span="12">
        <span><span class="ps">*请先查询赛事信息是否可用</span></span>
      </el-col>
      <el-col :span="3">
        <div class="select">
          <el-button
            type="primary"
            @click="getnum()"
            class="search"
            icon="el-icon-search"
            >查询</el-button
          >
        </div>
      </el-col>
      <el-col :span="3">
        <div class="select">
          <el-button type="primary" icon="el-icon-plus" @click="create"
            >创建</el-button
          >
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="tableData" style="width: 100%; padding: 0px 30px">
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
      </el-table>
    </el-row>
  </div>
</template>

<script>
import request from '@/utils/request.js'
import { timelist, timeFormat, tFormat } from '@/api/time.js'

export default {
  data() {
    return {
      options: [],
      timelist: [
        {
          lebel: '08:30:00-10:00:00',
          value: '08:30:00-10:00:00',
        },
        {
          lebel: '10:00:00-11:30:00',
          value: '10:00:00-11:30:00',
        },
        {
          lebel: '14:30:00-16:00:00',
          value: '14:30:00-16:00:00',
        },
        {
          lebel: '16:00:00-17:30:00',
          value: '16:00:00-17:30:00',
        },
        {
          lebel: '17:30:00-19:00:00',
          value: '17:30:00-19:00:00',
        },
        {
          lebel: '19:00:00-20:30:00',
          value: '19:00:00-20:30:00',
        },
        {
          lebel: '20:30:00-22:00:00',
          value: '20:30:00-22:00:00',
        },
      ],
      toollist: [],
      pricelist: [],
      locationpricelist: [],
      tableData: [],
      pickerOptions: {
        disabledDate(time) {
          const date = new Date()
          date.setTime(date.getTime() - 3600 * 1000 * 24)
          return time.getTime() <= date
        },
      },
      date: '',
      time: '',
      // 用来上传赛事信息 创建赛事
      game_name: '',
      game_type: '',
      game_renum: '',
      game_num: '',
      game_id: '',
      game_price: '',
      locationid: '',
      timevalue: '',
      toolvalue: '',
      toolid: '',
      locationvalue: '',
      locationprice: '',
      locationtime: '',
      orderid: '',
    }
  },
  methods: {
    getgameinfo() {
      let usercode = localStorage.getItem('userid')
      let t = 0
      t = timeFormat(this.time)
      request({
        url: '/game/getgameinfo',
        method: 'Get',
      }).then((res) => {
        console.log(res);
        let code = res.data.code
        if (code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    getorderid() {
      let usercode = localStorage.getItem('userid')
      let t = 0
      t = timeFormat(this.time)
      request({
        url: '/judgestate/rent/' + this.data + '/' + t + '/' + usercode,
        method: 'Get',
      }).then((res) => {
        if (res.data.code === 200) {
          this.orderid = res.data.data[1]
        } else {
          this.$message.error('该事件段没有裁判')
        }
      })
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
    getlocationprice() {
      request({
        url: '/state/selectByNametop',
        method: 'Get',
        params: { name: this.locationvalue },
      }).then((res) => {
        let code = res.data.code
        if (code === 200) {
          this.locationpricelist = this.unique(res.data.data)
          console.log(this.locationpricelist)
        }
      })
    },
    goBack() {
      this.$router.push('/home')
    },
    getprice() {
      request({
        url: '/tools/getprice?kind=' + this.toolvalue,
        method: 'get',
      }).then((res) => {
        let code = res.data.code
        if (code === 200) {
          this.pricelist = res.data.data
        } else {
          this.$message.error('系统出错,请稍后再试')
        }
      })
    },

    timeclick() {
      // 获取该场地时间的订单号
      console.log()
    },
    toolclick(e) {},
    getkind() {
      request({
        url: '/kinds',
        method: 'Get',
      }).then((res) => {
        this.toollist = res.data.data
      })
    },
    getnum() {
      if (
        this.toolvalue === '' ||
        this.game_price === '' ||
        this.date === '' ||
        this.time === ''
      )
        this.$message.error('请选择后查询！')
      else {
        let t = 0
        t = timeFormat(this.time)
        let token = localStorage.getItem('Authorization')
        console.log(t)
        let param = ''
        param += '?kind=' + this.toolvalue
        param += '&price=' + this.game_price
        console.log(param)
        // console.log(this.date);
        request({
          url:
            'tools/search/' + token + '/' + this.date + '/' + t + '/' + param,
          method: 'get',
        }).then((res) => {
          let code = res.data.code
          // console.log(res);
          if (code === 200) {
            this.game_renum = res.data.data
            if (this.game_renum > 0) {
              this.flag = true
              this.$message.success('器材有剩余')
            } else {
              this.$message.info('无剩余器材')
            }
          } else if (code === 401) {
            localStorage.removeItem('username')
            localStorage.removeItem('Authorization')
            localStorage.removeItem('signTime')
            localStorage.removeItem('userclass')
            this.$router.push('/user_login')
          } else {
            this.$message.error('系统出错,请稍后再试')
          }
        })
      }
    },
    create() {
      if (
        this.toolvalue === '' ||
        this.game_price === '' ||
        this.date === '' ||
        this.time === '' ||
        this.locationvalue === '' ||
        this.locationprice === ''
      )
        this.$message.error('请选择完整后再创建！')
      else {
        let t = 0
        t = timeFormat(this.time)
        let token = localStorage.getItem('Authorization')

        let param = ''
        param += '?kind=' + this.toolvalue
        param += '&price=' + this.game_price
        console.log(param)
        // console.log(this.date);
        request({
          url:
            'tools/search/' + token + '/' + this.date + '/' + t + '/' + param,
          method: 'get',
        }).then((res) => {
          let code = res.data.code
          // console.log(res);
          if (code === 200) {
            this.game_renum = res.data.data
            if (this.game_renum > 0) {
              if (this.num === 0) {
                this.$message.error('请选择器材数量')
              } else {
                let t = 0
                t = timeFormat(this.time)
                let token = localStorage.getItem('Authorization')
                let usercode = localStorage.getItem('userid')
                let param = ''
                param += '?kind=' + this.toolvalue
                param += '&price=' + this.game_price
                request({
                  url:
                    'borrows/rent/' +
                    token +
                    '/' +
                    usercode +
                    '/' +
                    this.game_num +
                    '/' +
                    this.date +
                    '/' +
                    t +
                    '/' +
                    param,
                  method: 'get',
                }).then((res) => {
                  let code = res.data.code
                  console.log('器材租用')
                  console.log(res.data)
                  if (code === 200) {
                    this.toolid = res.data.data[res.data.data.length - 1]
                    console.log(this.toolid)
                    let usercode1 = localStorage.getItem('userid')
                    let t = 0
                    t = timeFormat(this.time)

                    request({
                      url: '/appointment/rent',
                      method: 'Post',
                      data: {
                        userId: usercode1,
                        name: this.locationvalue,
                        date: this.date,
                        time: t,
                        price: this.locationprice,
                        usec: 'false',
                        promise: 'false',
                      },
                    }).then((res) => {
                      let usercode = localStorage.getItem('userid')
                      let code = res.data.code
                      if (code === 200) {
                        this.locationid = res.data.data
                        let usercode = localStorage.getItem('userid')
                        let t = 0
                        t = timeFormat(this.time)
                        request({
                          url:
                            '/judgestate/rent/' +
                            this.date +
                            '/' +
                            t +
                            '/' +
                            usercode,
                          method: 'Get',
                        }).then((res) => {
                          if (res.data.code === 200) {
                            this.orderid = res.data.data[1]
                            request({
                              url: '/game/save',
                              method: 'Post',
                              data: {
                                userid: usercode,
                                gamename: this.game_name,
                                gametime: this.date,
                                gametype: this.game_type,
                                gametool: this.toolvalue,
                                gamelocation: this.locationvalue,
                                gamenum: this.game_num,
                                toolid: this.toolid,
                                locationid: this.locationid,
                                judgeorderid: this.orderid,
                              },
                            }).then((res) => {
                              let code = res.data.code
                              console.log('开始创建')
                              if (code === 200) {
                                this.$message.success('赛事创建成功')
                                this.getgameinfo()
                              } else {
                                this.$message.error('赛事创建失败')
                              }
                            })
                          } else {
                            this.$message.error('该事件段没有裁判')
                          }
                        })
                      }
                    })
                  } else {
                    this.$message.error('场地无可用')
                  }
                })
              }
            } else {
              this.$message.info('无剩余器材')
            }
          } else if (code === 401) {
            localStorage.removeItem('username')
            localStorage.removeItem('Authorization')
            localStorage.removeItem('signTime')
            localStorage.removeItem('userclass')
            this.$router.push('/user_login')
          } else {
            this.$message.error('系统出错,请稍后再试')
          }
        })
      }
    },
  },
  created() {
    this.getkind(), this.getlocation(), this.getgameinfo()
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
