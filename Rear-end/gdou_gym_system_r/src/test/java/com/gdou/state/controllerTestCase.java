package com.gdou.state;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.ITStateService;
import com.gdou.utils.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private ITStateService itStateService;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private ToolsMapper toolsMapper;
    @Test
    void testSave(){
        int num = 4;

        List<Integer> toolsList = new ArrayList<>();
        toolsList.add(1);
        toolsList.add(2);
        toolsList.add(3);
        toolsList.add(4);

        Random random = new Random();
        int number = 0;

//        把随机抽取出来的器材存进list中
        List<Integer> toolsCode = new ArrayList<>();
        for(int i=0;i<num;i++){
            number = random.nextInt(toolsList.size());
            toolsCode.add(toolsList.get(number));
            toolsList.remove(number);
        }

        System.out.println(toolsCode);
        TState tState = new TState();
        tState.setUsercode("201911701310");
        tState.setToolscode(3);
        tState.setDate("2022-06-07");
        tState.setTime(3);
        int insert = tStateMapper.insert(tState);
        System.out.println(tState);

    }

    @Test
    void testDelete(){
        TState tState = tStateMapper.selectById(4);
//        System.out.println(tState);
        QueryWrapper<Price_Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usercode",tState.getUsercode())
                .eq("date",tState.getDate())
                .eq("time",tState.getTime());
        List<Price_Tools> price_tools = price_toolsMapper.selectList(queryWrapper);
//        System.out.println(price_tools);

        for (int i = 0;i<price_tools.size();i++){
            String[] str = price_tools.get(i).getToolslist().split(",");
            List<String> list1= Arrays.asList(str);
            List<String> arrList = new ArrayList<String>(list1);
            System.out.println(arrList);
            for(int j=0;j<arrList.size();j++){
                if(arrList.get(j).equals("4")){
                    arrList.remove("4");
                    String s ="";
                    for (int z = 0;z<arrList.size();z++){
                        s+=arrList.get(z);
                        if(z != arrList.size()-1) s+=",";
                    }
                    UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
                    System.out.println(s);
                    updateWrapper.eq("id",price_tools.get(i).getId()).set("toolslist",s);
                    price_toolsMapper.update(null,updateWrapper);
                }
            }
        }
    }

    @Test
    void testHandleReceive(){
        //把toolslist转换成list
        Price_Tools price_tools = price_toolsMapper.selectById(2);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号
        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }

        //遍历tools表 根据exist项来确认器材是否存在
        List<Integer> list = new ArrayList<>();
        for(int i =0;i<tList.size();i++){
            Tools tools = toolsMapper.selectById(tList.get(i));
            if(tools.getExist().equals("否")) {
                list.add(tools.getId());
            }
        }

        System.out.println(list);
    }

    @Test
    void testSubtractDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        System.out.println(now);


        String renTime = "2022-06-10 10:00:00";

        Date rt = formatter.parse(renTime);

        System.out.println(rt);

        long nowTime = now.getTime()-rt.getTime();

        String format = formatter.format(nowTime);


        System.out.println(nowTime/1000/(60*60));//转小时

        System.out.println(format);


    }

    @Test
    void testGetDateTime() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = itStateService.getDateTime(8).get(1);
        System.out.println(dateTime);
        Date rt = formatter.parse(dateTime);
        System.out.println(rt);


    }

    @Test
    void testEndTime(){
        System.out.println(TimeUtil.endTime(1));
    }
}
