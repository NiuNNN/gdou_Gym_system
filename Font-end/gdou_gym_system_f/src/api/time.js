const timeArr = ['8:30:00-10:00:00','10:00:00-11:30:00','14:30:00-16:00:00','16:00:00-17:30:00','17:30:00-19:00:00','19:00:00-20:30:00','20:30:00-22:00:00']
const searchList=[{
  lebel:'------',
  value:'------',
},{
  lebel:'8:30:00-10:00:00',
  value:'8:30:00-10:00:00',
},{
  lebel:'10:00:00-11:30:00',
  value:'10:00:00-11:30:00',
},{
  lebel:'14:30:00-16:00:00',
  value:'14:30:00-16:00:00',
},{
  lebel:'16:00:00-17:30:00',
  value:'16:00:00-17:30:00',
},{
  lebel:'17:30:00-19:00:00',
  value:'17:30:00-19:00:00',
},{
  lebel:'19:00:00-20:30:00',
  value:'19:00:00-20:30:00',
},{
  lebel:'20:30:00-22:00:00',
  value:'20:30:00-22:00:00',
}]//设置时间段
const dataFormat = function(fmt,date){
  var ret;
  var opt =
   {
      "d+": date.getDate().toString(),            // 日
      "H+": date.getHours().toString(),           // 时
      "m+": date.getMinutes().toString(),         // 分
      // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (var k in opt)
  {
      ret = new RegExp("(" + k + ")").exec(fmt);
      if (ret)
      {
          fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
      };
  };
  return fmt;
}

const timeFormat = function(time){
  for(let i = 0;i<timeArr.length;i++){
    if(time === timeArr[i]){
      return Number(++i)
    }
  }
}
const tFormat = function(t){
  return timeArr[Number(t)-1]
}

export {searchList,dataFormat,timeFormat,tFormat} ;