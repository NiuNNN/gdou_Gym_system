import axios from 'axios' // 引入 axios

// 创建axios实例 后端采用8088端口
const service = axios.create({
  baseURL: 'http://localhost:80/',
  timeout: 15000
})

export default service