<template>
  <div class="descriptions-box">
    <el-page-header @back="goBack" content="赛事公告" title="返回主页">
    </el-page-header>
    <el-divider></el-divider>
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="(item, index) in playlist" :key="index">
        <img :src="item.url" alt="" style="width: 100%" />
      </el-carousel-item>
    </el-carousel>
    <el-collapse
      v-model="activeNames"
      style="padding-left: 30px; margin-bottom: 30px"
    >
      <el-collapse-item title="我校举办第二届“海大杯”龙舟赛" name="1">
        <div style="width: 800px">
          11月7日，广东海洋大学第二届“海大杯”龙舟赛在湖光校区西湖举行。来自17个
          学院的238名龙舟健儿激情挥桨，上演“蛟龙”破浪的视觉盛宴，诠释了“同舟共济、
          奋发有为”的新时代龙舟精神。本次比赛的竞赛项目为男女混合12人龙舟200米直
          道赛，执行中国龙舟协会审订的《龙舟竞赛规则》。经过一上午的激烈竞争，
          体育与休闲学院、机械与动力工程动学院、研究生院获得比赛前三名，获得第四至
          第八名的代表队分别是滨海农业学院、海运学院、经济学院、管理学院、食品科技学院。；
        </div>
      </el-collapse-item>
      <el-collapse-item title="我校游泳队在广东省游泳冠军赛中再获突破" name="2">
        <div style="width: 600px">
          11月12-14日，第一届广东省游泳冠军赛在华南师范大学大学城校区举行，我校游泳
          队引领校园游泳运动进入新时代，实现了该项目赛事上的历史性突破，也是继今年广东省大
          学生游泳竞标赛后的又一次重大突破。
        </div>
      </el-collapse-item>
      <el-collapse-item
        title="我校在全省大学生跆拳道锦标赛中斩获两金四银六铜"
        name="3"
      >
        <div style="width: 600px">
          10月27-31日，广东省大学生跆拳道锦标赛在中山大学广州校区东校园举行，我校杨天天
          、刘雅萱同学凭借精湛的技术和拼搏的精神，分获乙A组男子个人竞技70kg、乙A组女子个
          人竞技58kg第一名，实现了我校历史上该项目金牌零的突破。跆拳道代表队成员还获得四银
          六铜，并斩获乙组团体总分第四名。
        </div>
      </el-collapse-item>
    </el-collapse>
    <span style="padding-left: 30px; font-size: 13px">今天赛事安排</span>
    <el-divider></el-divider>
    <el-empty description="今天没赛事喔" v-if="Gamelist.length == 0"></el-empty>
    <el-table :data="Gamelist" style="width: 100%" v-else>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="赛事日期">
              <span>{{ props.row.gametime }}</span>
            </el-form-item>
            <el-form-item label="赛事名称">
              <span>{{ props.row.gamename }}</span>
            </el-form-item>
            <el-form-item label="赛事类型">
              <span>{{ props.row.gametype }}</span>
            </el-form-item>
            <el-form-item label="赛事器材">
              <span>{{ props.row.gametool }}</span>
            </el-form-item>
            <el-form-item label="赛事场地">
              <span>{{ props.row.gamelocation }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="赛事日期" prop="gametime"> </el-table-column>
      <el-table-column label="赛事名称" prop="gamename"> </el-table-column>
      <el-table-column label="今日赛事"> 欢迎来观看赛事</el-table-column>
    </el-table>
  </div>
</template>
<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      nowDate: '', // 当前日期
      Gamelist: [],
      url: '/game/getdaygameinfo',
      activeNames: ['0'],

      playlist: [
        {
          url: require('../Gym-Game/img/banner1.jpg'),
        },
        { url: require('../Gym-Game/img/banner2.jpg') },
      ],
    }
  },
  methods: {
    goBack() {
      this.$router.push('/home')
    },

    getdaygameinfo() {
     
      request({
        url: this.url,
        method: 'Get',
      }).then((res) => {
        if (res.data.code === 200) {
          this.Gamelist = res.data.data
        }
      }).finally(()=>{
        console.log('ok');
      })
    },
  },
  created() {
    this.getdaygameinfo()
  },
}
</script>
<style lang="less" scoped>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  padding-left: 30px;
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>
