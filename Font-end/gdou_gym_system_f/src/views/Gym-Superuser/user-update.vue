<template>
  <div class="descriptions-container">
    <div class="descriptions-box">
      <el-page-header @back="goBack" content="学生密码修改" title="返回主页">
      </el-page-header>
      <div class="change-form">
        <el-form
          :model="ruleForm"
          status-icon
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="输入账号" prop="userid">
            <el-input
              type="text"
              v-model="ruleForm.userid"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="输入旧密码" prop="password">
            <el-input
              type="password"
              v-model="ruleForm.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="输入新密码" prop="pass">
            <el-input
              type="password"
              v-model="ruleForm.pass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input
              type="password"
              v-model="ruleForm.checkPass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')"
              >修改</el-button
            >
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    const validate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入旧密码'))
      } else {
        callback()
      }
    }
    // 用户名文本框检索
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账号'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        userid: '',
        password: '',
        pass: '',
        checkPass: '',
      },
      rules: {
        password: [{ validator: validate, trigger: 'blur' }],
        pass: [{ validator: validatePass, trigger: 'blur' }],
        checkPass: [{ validator: validatePass2, trigger: 'blur' }],
        userid: [{ validator: validateCode, trigger: 'blur' }],
      },
    }
  },
  methods: {
    goBack() {
      this.$router.push('/home')
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url: '/users/getuserid',
            data: {
              code: this.ruleForm.userid,
            },
            method: 'Post',
          }).then((res) => {
            let code1 = res.data.code
            if (code1 === 200) {
              this.$message.success('用户账号正确')
            } else {
              this.$message.error('用户账号错误，请重新填写')
            }
          })

          let token = localStorage.getItem('Authorization')
          request({
            url:
              '/users/toUploadPwd/' +
              token +
              '/' +
              this.ruleForm.userid +
              '/' +
              this.ruleForm.password +
              '/' +
              this.ruleForm.pass,
            method: 'put',
          }).then((res) => {
            let code = res.data.code
            if (code === 200) {
              this.$message.success('密码修改成功')
              return
            } else if (code === 404) {
              this.$message.error('旧密码错误，请重新填写')
              this.resetForm('ruleForm')
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
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>

<style lang="less" scoped>
.descriptions-container {
  width: 100%;
  .descriptions-box {
    position: relative;
    margin: 0 auto;
    width: 98%;
    // box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    overflow: hidden;
    .change-form {
      margin: 50px auto;
      margin-bottom: 100px;
      width: 35%;
    }
  }
}
</style>
