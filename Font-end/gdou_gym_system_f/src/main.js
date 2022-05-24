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

Vue.use(ElementUI);
Vue.use(Vuex);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
