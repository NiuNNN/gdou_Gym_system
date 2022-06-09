const timeArr = ['8:30~10:00','10:00~11:30','14:30~16:00','16:00~17:30','17:30~19:00','19:00~20:30','20:30~22:00']

const dataFormat = function(fmt,date){
  var ret;
  var opt =
   {
      "y+": date.getFullYear().toString(),        // 年
      "M+": (date.getMonth() + 1).toString(),     // 月
      "d+": date.getDate().toString(),            // 日
      "H+": date.getHours().toString(),           // 时
      "m+": date.getMinutes().toString(),         // 分
      "s+": date.getSeconds().toString()          // 秒
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
  for(var i = 0;i<timeArr.length;i++){
    if(time === timeArr[i]){
      return Number(++i)
    }
  }
}
const tFormat = function(t){
  return timeArr[Number(t)-1]
}

export {dataFormat,timeFormat,tFormat} ;