<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="器材维护" title="返回主页">
      </el-page-header>
      <div class="btn">
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
              <template slot-scope="scope">
                <el-button type="primary" size="mini" @click="handlefix(scope.$index, scope.row)" v-if="scope.row.safe === '否'">维护</el-button>
                <el-button type="primary" size="mini" @click="handleup(scope.$index, scope.row)" v-else>上架</el-button>
                <el-button type="danger" size="mini" @click="handledelete(scope.$index, scope.row)">删除</el-button>
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
      kindList:[],//用户存放种类
      selectList:[{
        select:"------"
      },{
        select:"是"
      },{
        select:"否"
      },],
      formData:{
        kind:'',
        price:''
      },//添加用的表单
      dialogFormVisibleSel:false,//查询表单是否可见
    }
  },
  created() {
    this.getAll()
  },
  methods:{
    goBack () {
      this.$router.push('/home')
    },
    //获取页面数据
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
        url:"/tools/getExistAll/"+token+"/"+this.pagination.currentPage+"/"+this.pagination.pageSize+param,
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
    //弹出搜索框
    handleSearch(){
      this.dialogFormVisibleSel = true;
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
    //对器材进行维护
    handlefix(index,row){
      this.$confirm("你确定要把该器材进行维护吗，是否继续？","提示",{type:"info"}).then(()=>{
        request({
          url:'tools/fix/'+row.id,
          method:'get',
        }).then(res=>{
          // console.log(res);
          let code =  res.data.code
          if(code === 200){
            this.$message.success('器材下架成功')
          }
          else{
            this.$message.error('系统出错,请稍后再试')
          }
        }).finally(res=>{
          this.getAll()
        })
      })
    },
    //器材上架
    handleup(index,row){
      this.$confirm("你确定要把该器材进行维护吗，是否继续？","提示",{type:"info"}).then(()=>{
        request({
          url:'tools/up/'+row.id,
          method:'get',
        }).then(res=>{
          // console.log(res);
          let code =  res.data.code
          if(code === 200){
            this.$message.success('器材上架成功')
          }
          else{
            this.$message.error('系统出错,请稍后再试')
          }
        }).finally(res=>{
          this.getAll()
        })
      })
    },
    //对器材进行删除
    handledelete(index,row){
      if(row.safe === '否'){
        this.$confirm("此器材不处于维护状态，你确定要把该器材删除吗，是否继续？","提示",{type:"danger"}).then(()=>{
          this.deleteTools(row);
        }).catch(()=>{
          this.$message.info("取消操作");
        });
      }
      else{
        this.$confirm("你确定要把该器材删除吗，是否继续？","提示",{type:"danger"}).then(()=>{
          this.deleteTools(row);
        }).catch(()=>{
          this.$message.info("取消操作");
        });
      }
    },
    //删除器材
    deleteTools(row){
      request({
        url:'tools/'+row.id,
        method:'delete'
      }).then(res=>{
        let code =  res.data.code
          if(code === 200){
            this.$message.success('器材删除成功')
          }
          else{
            this.$message.error('系统出错,请稍后再试')
          }
      }).finally(res=>{
          this.getAll()
        })
    }
  }
}
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