import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Vuex from 'vuex'
import store from './store'

// 导入Element
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 导入基本样式
import '@/assets/css/normalize.css'
//同意接口api文件里面的全部请求函数
//统一引入
import  * as API from '@/api/state'

Vue.use(ElementUI);
Vue.use(Vuex);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
  //全局事件总线
  beforeCreate(){
    Vue.prototype.$API=API;
  },

}).$mount('#app')
