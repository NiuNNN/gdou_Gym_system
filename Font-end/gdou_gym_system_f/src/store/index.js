import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const store = new Vuex.Store({
  state:{
    //保存登陆时的用户名
    username:window.localStorage.getItem('username') ? window.localStorage.getItem('username'):'',

    //保存登陆时的用户号
    userid:window.localStorage.getItem('userid') ? window.localStorage.getItem('userid'):'',

    //保存登录时的用户级别
    userclass:window.localStorage.getItem('userclass') ? window.localStorage.getItem('userclass'):'',

    //保存登录时的token
    Authorization:window.localStorage.getItem('Authorization') ? window.localStorage.getItem('Authorization') : '',

    //保存token生成的时间
    signTime: window.localStorage.getItem('signTime') ? window.localStorage.getItem('signTime') : '',
  },
  mutations:{
    loginChange(state,user){
      state.username = user.username;
      state.Authorization = user.Authorization;
      state.userclass = user.userclass;
      state.signTime = user.signTime;
      state.userid = user.userid

      window.localStorage.setItem('username',state.username);
      window.localStorage.setItem('Authorization', state.Authorization);
      window.localStorage.setItem('userclass',state.userclass);
      window.localStorage.setItem('signTime', state.signTime);
      window.localStorage.setItem('userid', state.userid);
    }
  }
})

export default store;