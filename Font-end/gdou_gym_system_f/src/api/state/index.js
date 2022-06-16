import request from '@/utils/request'
//获取selected为否的数据
export const getListState = () => request({
    url: "/state/selectAll",
    method: 'get',
})

//获取全部数据
export const getListStateall = () => request({
    url: "/state/selectAllItem",
    method: 'get',
})

export const insertState = (state) => request({
    url: "/state/insertState",
    method: 'post',
    data:state
})

//获取所有预约
export const getstateapp = () => request({
    url: "/appointment/selectAll",
    method: 'get',
    
})

//根据场地名查询时间段
export const selectByName = (name) => request({
    url: '/state/selectByName',
    method: 'get',
    params:{name}
})

export const selectByNametop = (name) => request({
    url: '/state/selectByNametop',
    method: 'get',
    params:{name}
})
//确认预约
export const insertAppointment = (stateAppointment) => request({
    url: "/appointment/insertAppointment",
    method: 'post',
    data:stateAppointment
})

//删除预约
export const deleteById = (id) => request({
    url: '/appointment/deleteById',
    method: 'get',
    params:{id}
})

//删除场地
export const deleteStateById = (id,name,time) => request({
    url: '/state/deleteById',
    method: 'get',
    params:{id,name,time}
})


//公告获取
export const getListNotice = () => request({
    url: "/notice/selectAll",
    method: 'get',
})

//删除公告
export const deletenoticeById = (id) => request({
    url: '/notice/deleteById',
    method: 'get',
    params:{id}
})
//添加公告

export const insertNotice = (content) => request({
    url: "/notice/insertNotice",
    method: 'post',
   params:{content}
})

//修改promise
export const updapromise=(id,name,time)=>request({
    url: "/appointment/updatePById",
    method: 'get',
   params:{id,name,time}
})

//根据userid获取预约名单
export const getuserapp=(userId)=>request({
    url: "/appointment/getInfoByUserId",
    method: 'get',
   params:{userId}
})

//开始使用

export const kaishiuse=(id,name,time)=>request({
    url: "/appointment/updateUById",
    method: 'get',
   params:{id,name,time}
})

//结束使用

export const jieshuuse=(id,name,time)=>request({
    url: "/appointment/updateUfById",
    method: 'get',
   params:{id,name,time}
})
