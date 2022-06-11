<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="修改用户资料" title="返回主页">
      </el-page-header>
      <!-- 用户资料 -->
      <div class="left">
        <el-descriptions class="margin-top" title="用户信息" :column="3" direction="vertical" border>
          <el-descriptions-item label="学号">{{user.code}}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{user.name}}</el-descriptions-item>
          <el-descriptions-item label="性别">{{user.sex}}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{user.phone}}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{user.age}}</el-descriptions-item>
          <el-descriptions-item label="邮编">{{user.zip}}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{user.email}}</el-descriptions-item>
          <el-descriptions-item label="联系地址">{{user.address}}</el-descriptions-item>
        </el-descriptions>
      </div>
      <!-- 用户头像 -->
      <div class="right">
        <img :src="user.avatar" alt="">
        <span>学生头像</span>
      </div>
      <div class="btn">
        <el-button type="primary" @click="edit"><i class="el-icon-edit"></i>修改资料</el-button>
      </div>
    </div>
    <!-- 编辑弹窗层 -->
    <div class="edit-form">
      <el-dialog title="修改资料" :visible.sync="dialogFormVisibleEdit">
        <h1 class="usercode">学号:{{user.code}}</h1>
        <div class="upload">
          <el-upload
          :action=avatarUrl
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <span>(图片为jpg格式、且不能超过2M)</span>
        </div>
        <div class="form">
          <el-form 
        :model="user" 
        status-icon 
        :rules="rules" 
        ref="user" 
        label-width="100px" 
        label-position="right"  
        :hide-required-asterisk="false">
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input type="text" v-model="user.name" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年龄" prop="age">
               <el-input v-model.number="user.age"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item>
                <el-radio v-model="user.sex" label="男">男</el-radio>
                <el-radio v-model="user.sex" label="女">女</el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input type="text" v-model="user.email" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="phone">
                <el-input type="text" v-model="user.phone" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮编" prop="zip">
                <el-input type="text" v-model="user.zip" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="详细住址" prop="address">
                <el-input type="textarea" :rows="3" v-model="user.address" autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel()">取消</el-button>
          <el-button type="primary" @click="submitForm('user')">确定</el-button>
        </div> 
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  inject:['reload'],
  data() {
    // 年龄文本框检索
    const checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入年龄'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }, 1000)
    }
    // 性别单选框检索
    const validateSex = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请选择性别'))
      } else {
        callback()
      }
    }
    // 手机文本框检索
    const validatePhone = (rule, value, callback) => {
      const phone = /^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/
      if (value === '') {
        return callback(new Error('请输入手机号'))
      }
      setTimeout(() => {
        if (!phone.test(value)) {
          callback(new Error('无效手机号'))
        } else {
          callback()
        }
      }, 1000)
    }
    // 邮箱文本框检索
    const validateEmail = (rule, value, callback) => {
      const email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
      if (value === '') {
        callback(new Error('请输入邮箱地址'))
      } setTimeout(() => {
        if (!email.test(value)) {
          callback(new Error('无效邮箱地址'))
        } else {
          callback()
        }
      }, 1000)
    }
    // 地址选择器检索
    const validateAddress = (rule, value, callback) => {
      if (value !== '') {
        callback()
      } else {
        callback()
      }
    }
    // 邮编文本框检索
    const validateZip = (rule, value, callback) => {
      const zip = /[1-9]\d{5}(?!\d)/
      if(value !== ''){
        setTimeout(() => {
          if (!zip.test(value)) {
            callback(new Error('无效邮编'))
          } else {
            callback()
          }
        }, 1000)
      }
      else{
        callback()
      }
    }
    //姓名检索
    const validate = (rule, value, callback)=>{
      if (value === '') {
        callback(new Error('请输入姓名'))
      } else {
        callback()
      }
    }
    return {
      dialogFormVisibleEdit:false,//编辑表单是否可见
      user:{
        code:'',
        name:'',
        sex:'',
        phone:'',
        age:'',
        zip:'',
        email:'',
        address:'',
        avatar:''
      },
      //form检索规则
      rules: {
        name:[{
          validator: validate, trigger: 'blur',required:true
        }],
        sex: [
          { validator: validateSex, trigger: 'blur' ,required:true}
        ],
        age: [
          { validator: checkAge, trigger: 'blur' ,required:true}
        ],
        address: [
          { validator: validateAddress, trigger: 'blur'}
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' ,required:true}
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' ,required:true}
        ],
        zip: [
          { validator: validateZip, trigger: 'blur' }
        ]
      },
      imageUrl:'',
    }
  },
  created(){
    this.getuserinfo(),
    this.getImgUrl()
  },
  methods: {
    goBack () {
      this.$router.push('/home')
    },
    //获取用户信息
    getuserinfo(){
      let userid = localStorage.getItem('userid')
      request({
        url:'/users/toGetUserInfo/'+userid,
        methods:'Get',
      }).then((res)=>{
        let code = res.data.code
        if(code === 200){
          this.user.code = res.data.data.code
          this.user.name =res.data.data.name
          this.user.sex = res.data.data.sex
          this.user.age = res.data.data.age
          this.user.phone = res.data.data.phone
          this.user.email = res.data.data.email
          this.user.zip = res.data.data.zip
          this.user.address = res.data.data.address
        }
      })
    },
    //确定修改
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url:'/users/toUploadUser',
            method:'put',
            data:{
              code:this.user.code,
              name:this.user.name,
              phone:this.user.phone,
              password:this.user.checkPass,
              email:this.user.email,
              age:this.user.age,
              sex:this.user.sex,
              zip:this.user.zip,
              address:this.user.address
            }
          }).then(res=>{
            let code = res.data.code
            if(code === 200){
              this.$message.success("修改成功")
              this.dialogFormVisibleEdit = false;
              this.getImgUrl();
              this.reload();//刷新
            }
            else{
              if(res.data.data === 'code'){
                this.user.code =''
              }
              else if(res.data.data === 'phone'){
                this.user.phone =''
              }
              else{
                this.user.email =''
              }
              this.$message.error(res.data.message)
            }
          })
        } else {
          this.$message.error('请正确填写文本框')
          return false
        }
      })
    },
    handleAvatarSuccess(res, file) {
      this.$message.success("修改头像成功")
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    getImgUrl(){
      let userid = localStorage.getItem('userid')
      this.user.avatar="http://localhost/avatar/"+userid+".jpg"+"?"+Math.random()*10
      console.log(this.user.avatar);
    },
    //修改按钮
    edit(){
      this.dialogFormVisibleEdit = true;
    },
    //取消按钮
    cancel(){
        this.dialogFormVisibleEdit = false;
        this.$message.info("当前操作取消");
    }
  },
  computed:{
    avatarUrl(){
      let userid = localStorage.getItem('userid')
      let avatarUrl ="http://localhost:80/users/toUploadUserAvatar?code="+userid
      return avatarUrl
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
    height: 700px;
    border-radius: 4px;
    overflow: hidden;
    .left{
      margin-left: 10%;
      width: 60%;
      float: left;
      margin-top: 130px;
    }
    .right{
      margin-top:220px ;
      margin-left: 5%;
      float: left;
      width: 178px;
      img{
        width: 178px;
        height: 178px;
        border-radius: 15px;
      }
      span{
        margin-top: 5px;
        display: block;
        width: 178px;
        font-size: 14px;
        color: #909399;
        text-align: center;
      }
    }
    .btn{
      position: absolute;
      left: 50%;
      bottom: 158px;
      margin-right: 40px;
    }
  }
}
</style>
<style lang="less">
.edit-form{
.el-dialog{
  height: 600px;
  .el-dialog__body{
    height: 400px;
  }
  .form{
    float: left;
    width: 60%;
    height: 400px;
  }
  .upload{
    width: 30%;
    float: right;
     .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
      border-color: #409EFF;
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
    .span{
      margin-top: 5px;
      display: block;
      width: 178px;
      font-size: 10px;
      color: #606266;
      text-align: center;
    }
  }
  .usercode{
    padding-left:20px ;
    margin-bottom:20px ;
  }
}
}

</style>