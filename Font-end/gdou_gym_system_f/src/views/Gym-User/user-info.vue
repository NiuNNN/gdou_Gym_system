<template>
  <div class="add-container">
    <el-page-header @back="goBack" content="修改用户资料" title="返回主页">
    </el-page-header>
    <div class="input-box">
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
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-form-inline">
        <el-form-item label="学号" prop="code">
          <el-input type="text" v-model="ruleForm.code" autocomplete="off" readonly="readonly"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input type="text" v-model="ruleForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio v-model="ruleForm.sex" label="1">男</el-radio>
          <el-radio v-model="ruleForm.sex" label="2">女</el-radio>
          </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="ruleForm.age"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input type="text" v-model="ruleForm.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮编" prop="zip">
          <el-input type="text" v-model="ruleForm.zip" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input type="text" v-model="ruleForm.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="详细住址" prop="address">
          <el-input type="textarea" :rows="3" v-model="ruleForm.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data () {
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
    // 用户名文本框检索
    const validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        if (this.ruleForm.name !== '') {
          this.$refs.ruleForm.validateField('name')
        }
        callback()
      }
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
      if (!value) {
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
      if (value === '') {
        callback(new Error('请输入详细地址'))
      } else {
        callback()
      }
    }
    // 邮编文本框检索
    const validateZip = (rule, value, callback) => {
      const zip = /[1-9]\d{5}(?!\d)/
      if (value === '') {
        callback(new Error('请输入有编号'))
      } setTimeout(() => {
        if (!zip.test(value)) {
          callback(new Error('无效邮编'))
        } else {
          callback()
        }
      }, 1000)
    }
    return {
      ruleForm: {
        code:'',
        name: '',
        sex: '1',
        age: '',
        address: '',
        email: '',
        phone: '',
        zip: ''
      },
      imageUrl:'',
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        sex: [
          { validator: validateSex, trigger: 'blur' }
        ],
        age: [
          { validator: checkAge, trigger: 'blur' }
        ],
        address: [
          { validator: validateAddress, trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        zip: [
          { validator: validateZip, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    goBack () {
      this.$router.push('/home')
    },
    handleAvatarSuccess(res, file) {
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
    }
  },
  computed:{
    userid(){
      let userid = localStorage.getItem('userid')
      return userid
    }
  },
  computed:{
    avatarUrl(){
      let userid = localStorage.getItem('userid')
      let avatarUrl ="http://localhost:80/users/toUploadUserAvatar?code="+userid
      return avatarUrl
    }
  }
}
</script>

<style lang="less" scoped>
.add-container{
  height: 100%;
  width: 100%;
  .input-box{
    margin: 30px auto;
    width: 90%;
    // height: 700px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    overflow: hidden;
  }
  .demo-form-inline{
    float: left;
    width: 50%;
    margin: 30px 0 30px 50px;
  }
  /deep/.el-textarea__inner{
    resize: none
  }
  .el-button{
    margin-left: 37%;
    margin-top: 10px;
  }
  /deep/.el-upload {
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
  .upload{
    margin-top: 30px;
    margin-right: 10%;
    float: right;
    span{
      margin-top: 5px;
      display: block;
      width: 178px;
      font-size: 10px;
      color: #606266;
      text-align: center;
    }
  }
}
</style>
