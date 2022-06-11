<template>
    <div class="descriptions-container">
      <div class="descriptions-box">
        <el-page-header @back="goBack" content="器材管理" title="返回主页">
        </el-page-header>
        <div class="btn">
          <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleCreate()">添加</el-button>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch()">搜索</el-button>
        </div>
        <!-- 表格组件 -->
        <div class="table-container">
          <el-table
            :data="tableData"
            stripe
            style="width: 100%">
            <el-table-column
              prop="id"
              label="编号">
            </el-table-column>
            <el-table-column
              prop="kind"
              label="种类">
            </el-table-column>
            <el-table-column
              prop="price"
              label="价格">
            </el-table-column>
            <el-table-column
              prop="safe"
              label="是否需要维修">
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template :slot-scope="scope">
                <el-button type="primary" size="mini">查看</el-button>
                <el-button type="danger" size="mini">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <!-- 分页组件 -->
      <div class="pagination-container">
        <div class="block">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="pagination.currentPage"
            :page-size="pagination.pageSize"
            layout="prev, pager, next, jumper"
            :total="pagination.total">
          </el-pagination>
        </div>
      </div>
      <!-- 新增标签弹层 -->
      <div class="add-form">
       <el-dialog title="新增器材" :visible.sync="dialogFormVisible" width="45%">
           <el-form ref="formData" :model="formData"  label-position="right" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="器材类型" prop="kind">
                    <el-select v-model="formData.kind" placeholder="请选择器材类型" @change="getPrice()">
                      <el-option v-for="(item,index) in kindList" :key="index" :label="item.kind" :value="item.kind">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="器材价格" prop="price">
                    <el-select v-model="formData.price" placeholder="请选择价格">
                      <el-option v-for="(item,index) in priceList" :key="index" :label="item.price" :value="item.price">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
           </el-form>
           <div slot="footer" class="dialog-footer">
             <el-button @click="cancel()">取消</el-button>
             <el-button type="primary" @click="handleAdd()">确定</el-button>
           </div>
       </el-dialog>
      </div>
      <!-- 搜索标签弹层 -->
      <div class="add-form">
       <el-dialog title="搜索器材" :visible.sync="dialogFormVisibleSel" width="45%">
           <el-form ref="pagination" :model="pagination"  label-position="right" label-width="100px">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="器材类型" prop="kind">
                    <el-select v-model="pagination.kind" placeholder="------">
                      <el-option v-for="(item,index) in kindList" :key="index" :label="item.kind" :value="item.kind">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="器材是否损坏" prop="price">
                    <el-select v-model="pagination.safe" placeholder="------">
                      <el-option v-for="(item,index) in selectList" :key="index" :label="item.select" :value="item.select">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
                  <span class="ps">*不选中则不进行进行按条件查询</span>
                </el-col>
              </el-row>
           </el-form>
           <div slot="footer" class="dialog-footer">
             <el-button @click="cancel()">取消</el-button>
             <el-button type="primary" @click="getAll()">确定</el-button>
           </div>
       </el-dialog>
      </div>
      <!-- 查看标签弹层 -->
      <div class="show-form">
        <el-dialog title="器材信息" :visible.sync="dialogFormVisible4Edit" width="45%">
          <el-form ref="formData" :model="formData"  label-position="right" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <span class="sp">器材编号：</span>
                  <span class="sp">1</span>
                </el-col>
                <el-col :span="12">
                  <span class="sp">器材种类：</span>
                  <span class="sp">篮球</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <span class="sp">是否借出：</span>
                  <span class="sp">是</span>
                </el-col>
                <el-row :span="12">
                  <span class="sp">租借人：</span>
                  <span class="sp">苏浩杰</span>
                </el-row>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <span class="sp">租借时间：</span>
                  <span class="sp">2022/6/7 12:00:00</span>
                </el-col>
                <el-col :span="12">
                  <span class="sp">单价</span>
                  <span class="sp">￥0.50/小时</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <span class="sp">是否损坏：</span>
                  <span class="sp" v-if="false">无</span>
                  <span v-else style="color:#F56c6c" class="sp">有</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col >
                  <span class="sp">损坏描述：</span>
                  <span class="sp">123456789</span>
                </el-col>
              </el-row>
           </el-form>
           <div slot="footer" class="dialog-footer">
             <el-button @click="cancel()">取消</el-button>
             <el-button type="primary" @click="handleAdd()">确定</el-button>
           </div>
       </el-dialog>
      </div>
    </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    return {
      tableData:[],//当前页要展示的列表数据
      pagination: {//分页相关模型数据
          currentPage: 1,//当前页码
          pageSize:8,//每页显示的记录数
          total:0,//总记录数
          kind:'',
          safe:''
      },
      formData:{
        kind:'',
        price:''
      },//添加用的表单
      kindList:[],//用户存放种类
      priceList:[],//用于存放价格
      dialogFormVisible: false,//添加表单是否可见
      dialogFormVisibleSel:false,//查询表单是否可见
      dialogFormVisible4Edit:false,//详情表单是否可见
      selectList:[{
        select:"------"
      },{
        select:"是"
      },{
        select:"否"
      },]
    }
  },
  created() {
    this.getAll()
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    //获取表格数据
    getAll(){
      if(this.pagination.kind==="------") this.pagination =''
      if(this.pagination.safe==="------") this.pagination =''
      //组织参数，拼接url请求地址
      let param = ''
      let token = localStorage.getItem('Authorization')
      // param = "?id="+this.pagination.id;
      param +="?kind="+this.pagination.kind;
      param +="&safe="+this.pagination.safe;

      request({
        url:"/tools/getAll/"+token+"/"+this.pagination.currentPage+"/"+this.pagination.pageSize+param,
        method:"get"
      }).then(res=>{
        // console.log(res);
        let code = res.data.code
        if(code === 200){
          this.pagination.pageSize = res.data.data.size//表格大小
          this.pagination.currentPage = res.data.data.current//当前页数
          this.pagination.total = res.data.data.total;//总页数
          this.tableData= res.data.data.records
        }
        else if(code === 401){
          localStorage.removeItem('username');
          localStorage.removeItem('Authorization');
          localStorage.removeItem('signTime');
          localStorage.removeItem('userclass')
          this.$router.push('/user_login');
        }
        else{
          this.$message.error('系统出错,请稍后再试')
        }
      }).finally(()=>{
        this.dialogFormVisibleSel = false
      })
    },
    //切换页码
    handleCurrentChange(currentPage) {
      //修改页码值为当前选中的页码值
      this.pagination.currentPage = currentPage;
      //执行查询
      this.getAll();
    },
    //添加器材
    handleAdd(){
      if(this.formData.kind===''|| this.formData.price === ''){
        this.$message.error("请正确添加器材")
      }
      else{
        request({
        url:"/tools",
        method:"post",
        data:this.formData
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.dialogFormVisible = false;
          this.$message.success("器材添加成功")
        }
        else{
          this.$message.error("系统故障，请稍后再试")
        }
      }).finally(()=>{
        this.getAll()
        this.resetForm()
      })
      }
    },
    //取消
    cancel(){
      this.dialogFormVisible = false;
      this.dialogFormVisibleSel =false;
      this.dialogFormVisible4Edit = false;
      this.$message.info("当前操作取消")
    },
    //弹出添加窗口
    handleCreate() {
      this.dialogFormVisible = true;
      this.resetForm();
      this.getKind();
    },
    //重置表单
    resetForm() {
        this.formData = {};
        this.pagination.kind='',
        this.pagination.safe=''
    },
    //获取器材类型
    getKind(){
      request({
        url:"/kinds",
        method:"get"
      }).then(res=>{
        // console.log(res);
        this.kindList = res.data.data
      })
    },
    //获取选中器材类型对应的价格
    getPrice(){
      request({
        url:"/tools/getprice?kind="+this.formData.kind,
        method:"get"
      }).then(res=>{
        let code = res.data.code
        if(code === 200){
          this.priceList = res.data.data
        }
        else{
          this.$message.error('系统出错,请稍后再试')
        }
      })
    },
    //弹出搜索框
    handleSearch(){
      this.dialogFormVisibleSel = true;
      this.resetForm();
      this.getKind();
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
    // box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    overflow: hidden;
    height: 800px;
    .btn{
      width: 90%;
      margin: 50px auto 30px;
    }
    .table-container{
      margin: 0 auto;
      width: 90%;
      border: 1px solid #EBEEF5;
    }
  }
  .pagination-container{
    top: -130px;
    position: relative;
    width: 50%;
    margin: 30px auto;
    .block{
      margin: 0 auto;
    }
  }
}
</style>
<style>
.add-form .el-dialog{
  height: 320px;
}
.add-form .el-dialog__body{
  height: 130px;
}
.ps{
  padding-left: 5%;
  color: #F56c6c;
  font-size: 10px;
  font-weight: 700;
}
.show-form .sp{
  display: inline-block;
  font-size: 14px;
  font-weight: 600;
  height: 20px;
  line-height: 20px;
  padding: 5px;
}
</style>