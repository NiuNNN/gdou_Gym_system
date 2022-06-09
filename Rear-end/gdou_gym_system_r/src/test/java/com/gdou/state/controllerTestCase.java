package com.gdou.state;

import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.domain.TState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private TStateMapper tStateMapper;
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
}
