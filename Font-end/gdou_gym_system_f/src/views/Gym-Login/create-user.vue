<template>
  <div class="forget-container">
    <div class="forget-box">
      <div class="forget-header">
        <i class="el-icon-back" @click="goBack"></i>
        <span>新建用户</span>
      </div>
      <el-form :model="userForm" status-icon :rules="rules" ref="userForm" label-width="100px" class="userForm">
        <el-form-item label="账号" prop="code">
          <el-input type="text" v-model="userForm.code" autocomplete="off" class="code"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input type="text" v-model="userForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="userForm.sex" label="男">男</el-radio>
          <el-radio v-model="userForm.sex" label="女">女</el-radio>
          </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="userForm.age"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input type="text" v-model="userForm.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input type="text" v-model="userForm.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
            <el-input type="password" v-model="userForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="userForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
        <el-form-item style="margin-left:120px">
          <el-button type="primary" @click="submitForm('userForm')">确认</el-button>
          <el-button @click="resetForm('userForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
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
    // 用户名文本框检索
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账号'))
      } else {
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
    const validateipt = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请正确填写，此处不能为空'))
      } else {
        callback()
      }
    }
    //密码校验
    const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.userForm.checkPass !== '') {
            this.$refs.userForm.validateField('checkPass');
          }
          callback();
        }
      };
      const validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.userForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
    return {
      userForm:{
        code:'',
        name:'',
        sex:'男',
        age:'',
        phone:'',
        email:'',
        pass:'',
        checkPass:'',
      },
      rules: {
        code:[
          { validator: validateCode, trigger: 'blur',required:true}
        ],
        name:[
          { validator: validateipt, trigger: 'blur',required:true}
        ],
        sex:[
          { validator: validateSex, trigger: 'blur' ,required:true}
        ],
        age:[
          { validator: checkAge, trigger: 'blur' ,required:true}
        ],
        phone:[
          { validator: validatePhone, trigger: 'blur' ,required:true}
        ],
        email:[
          { validator: validateEmail, trigger: 'blur' ,required:true}
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' ,required:true}
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' ,required:true}
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url:'/logins/save',
            method:'post',
            data:{
              code:this.userForm.code,
              name:this.userForm.name,
              phone:this.userForm.phone,
              password:this.userForm.checkPass,
              email:this.userForm.email,
              age:this.userForm.age,
              sex:this.userForm.sex
            }
          }).then(res=>{
            let code = res.data.code
            //判断是否更新成功
            if(code === 200){
              //3秒后跳转到登录页面
              this.$message.success('用户创建成功,3秒后返回登陆页面...')
              setTimeout(()=>{
                this.$router.push('/user_login')
              },3000)
            }
            else{
              if(res.data.data === 'code'){
                this.userForm.code =''
              }
              else if(res.data.data === 'phone'){
                this.userForm.phone =''
              }
              else{
                this.userForm.email =''
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
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    goBack(){
      this.$router.push('/user_login')
    }
  }
}
</script>

<style lang="less" scoped>
.forget-container{
  height: 100%;
  .forget-box{
    width: 600px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    .forget-header{
      width: 100%;
      background-color: #409eff;
      span,i{
        display: inline-block;
        text-align: center;
        height: 50px;
        line-height: 50px;
        font-size: 30px;
        color: #fff;
        font-weight: 600;
      }
      span{
        width: 90%;
      }
      i{
        position: relative;
        padding-left:5px ;
      }
      i:hover{
        cursor: pointer;
      }
    }
    /deep/.el-input{
      width: 50%;
    }
    .userForm{
      margin-top: 30px;
    }
    /deep/.el-form-item__label{
      margin-left: 100px;
    }
    /deep/.el-form-item__content{
      margin-left: 220px;
    }
    /deep/.el-form-item__error{
      margin-left:100px ;
    }
  }
}
</style>