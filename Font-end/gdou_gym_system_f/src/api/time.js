const timeFormat = function(time){
  let timeArr = ['8:30~10:00','10:00~11:30','14:30~16:00','16:00~17:30','17:30~19:00','19:00~20:30','20:30~22:00']
  for(var i = 0;i<timeArr.length;i++){
    if(time === timeArr[i]){
      return Number(++i)
    }
  }
}

export default timeFormat;