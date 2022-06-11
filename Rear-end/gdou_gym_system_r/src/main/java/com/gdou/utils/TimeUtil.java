package com.gdou.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeUtil {

    /**
     * 定义时间
     * @return
     */
    public static List<String> time(){
        List<String> list = new ArrayList<>();//把时间表存进list中
        //1
        list.add("08:30:00-10:00:00");
        //2
        list.add("10:00:00-11:30:00");
        //3
        list.add("14:30:00-16:00:00");
        //4
        list.add("16:00:00-17:30:00");
        //5
        list.add("17:30:00-19:00:00");
        //6
        list.add("19:00:00-20:30:00");
        //7
        list.add("20:30:00-22:00:00");

        return list;
    }

    /**
     * 对时间进行判断 判断现在时间是否达到预定日期 预定时间段
     * @param date 预定日期
     * @param id 预定时间段
     * @return 返回状态码
     */
    public static  String getTime(String date,Integer id){
//        对日期进行判断
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd");
        String now = formatterd.format(calendar.getTime());
        int res = now.compareTo(date);//现在日期与预约日期进行比较
//        System.out.println(now);

        if(res>0){
            System.out.println("超时领取");//超时领取
            return "02";
        }
        else if(res<0){
            System.out.println("未到时间");
            return "01";//未到时间
        }
        else{
            List<String> list = time();

            String[] str = list.get(id-1).split("-");//按照数据库的时间段对list里的数据进行切片

            //获取当前时间并且设置为"HH:mm:ss"日期格式
            SimpleDateFormat formatterh = new SimpleDateFormat("HH:mm:ss");

            String hour = formatterh.format(calendar.getTime());

            int begin = hour.compareTo(str[0]);//开始时间

            if(begin < 0){
            System.out.println("未到时间");
                return "01";
            }
            else{
                int end = hour.compareTo(str[1]);//截止时间
                if(end > 0){
                System.out.println("超时领取");
                    return "02";
                }
                else{
                System.out.println("正确");
                    return "00";
                }
            }
        }
    }


    public static List<String> chooseTime(String date){
        List<String> list = time();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd");
        String now = formatterd.format(calendar.getTime());
        //获取当前时间并且设置为"HH:mm:ss"日期格式
        SimpleDateFormat formatterh = new SimpleDateFormat("HH:mm:ss");

        int flag = now.compareTo(date);
//        System.out.println(now);
//        System.out.println(flag);
        List<String> time = new ArrayList<>();

        if(flag == 0){
            String hour = formatterh.format(calendar.getTime());
            for (int i = 0;i< list.size();i++){
                String[] str = list.get(i).split("-");//按照数据库的时间段对list里的数据进行切片
                int res = hour.compareTo(str[1]);
                if(res<0) time.add(list.get(i));
            }
            return time;
        }
        else{
            return list;
        }
    }
}
