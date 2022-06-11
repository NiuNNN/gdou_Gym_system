package com.gdou.price;

import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.service.IPrice_ToolsService;
import com.gdou.utils.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootTest
public class controllerTestCase {

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private IPrice_ToolsService iPrice_toolsService;

    @Test
    void getTime() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(calendar.getTime()));

        String now = formatter.format(calendar.getTime());


        SimpleDateFormat formatter0 = new SimpleDateFormat("yyyy-MM-dd");

        String s0 = "2022-06-10";
        /*Date date0 = formatter0.parse(s0);
        System.out.println(date0);

        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");

        Date hour = new Date();
        String str = "14:25:39";*/

        int res = now.compareTo(s0);

        if(res>0){
            System.out.println("now>str");
        }
        else if(res == 0){
            System.out.println("now == str");
        }
        else{
            System.out.println("now<str");
        }

    }

    @Test
    void test(){
        List<String> list = new ArrayList<>();
        //1
        list.add("08:30:00-10:00:00");//0
        //2
        list.add("10:00:00-11:30:00");//1
        //3
        list.add("14:00:00-16:00:00");//2
        //4
        list.add("16:00:00-17:30:00");//3
        //5
        list.add("17:30:00-19:00:00");//4
        //6
        list.add("19:00:00-20:30:00");//5
        //7
        list.add("20:30:00-22:00:00");//6

        String[] str = list.get(2).split("-");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String hour = formatter.format(calendar.getTime());

        int begin = hour.compareTo(str[0]);

        if(begin < 0) System.out.println("还没到时间");
        else{
            int end = hour.compareTo(str[1]);
            if(end > 0) System.out.println("超时");
            else{
                System.out.println("正确");
            }
        }


        System.out.println(str[0]);
    }

    @Test
    void testTimeUtil(){
        String time = TimeUtil.getTime("2022-06-12",4);
        System.out.println(time);

    }

    @Test
    void testOverTime(){
        String s = iPrice_toolsService.overTime(8);
//        System.out.println(s);

    }
}
