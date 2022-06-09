package com.gdou.tools.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.IToolsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ToolsServiceImpl extends ServiceImpl<ToolsMapper, Tools> implements IToolsService {

    @Autowired
    private ToolsMapper toolsMapper;

    @Autowired
    private TStateMapper tStateMapper;

    @Override
    public IPage<Tools> getPage(int currentPage, int pageSize, Tools tools) {
        LambdaQueryWrapper<Tools> lambdaQueryWrapper = new LambdaQueryWrapper<Tools>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(tools.getKind()),Tools::getKind,tools.getKind());
        lambdaQueryWrapper.like(Strings.isNotEmpty(tools.getSafe()),Tools::getSafe,tools.getSafe());
        IPage page = new Page(currentPage,pageSize);
        toolsMapper.selectPage(page,lambdaQueryWrapper);
        return page;
    }

    @Override
    public List<Tools> getPrice(String kind) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("distinct kind,price").eq("kind",kind);
        List list = toolsMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<Integer> getTools(Tools tools,String date,int time) {
        //1、首先查询是否有符合条件的器材 获取其id
        QueryWrapper<Tools> queryWrapper = new QueryWrapper<Tools>();
        queryWrapper.eq("kind",tools.getKind())
                .eq("price",tools.getPrice())
                .eq("safe","否");
        List<Tools> l1 = toolsMapper.selectList(queryWrapper);

        //2.创建一个list用于过滤出l1里的id 器材编号
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<l1.size();i++){
            list.add(l1.get(i).getId());
        }
        System.out.println(list);

        //3、然后通过时间、时间段以及器材编号查找是否又符合的器材数据
        System.out.println(date);
        System.out.println(time);
        QueryWrapper<TState> queryWrapper1 = new QueryWrapper<TState>();
        queryWrapper1.eq("date",date)
                     .eq("time",time)
                     .in("toolscode",list);
        List<TState> l2 = tStateMapper.selectList(queryWrapper1);
        System.out.println(l2);

        //4、筛选出符合条件的器材 然后返回
        for (int i = 0;i<l2.size();i++){
            for (int j = 0;j<list.size();j++){
                if(list.get(j).equals(l2.get(i).getToolscode())){
                    list.remove(j);
                }
            }
        }
        System.out.println(list);
        return list;
    }

    @Override
    public Integer updateState(String price,String kind) {
//        首先查找出符合条件的器材
        QueryWrapper<Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kind",kind)
                    .eq("price",price)
                    .eq("state","否")
                    .eq("safe","否");
        List<Tools> list = toolsMapper.selectList(queryWrapper);

//        生成随机数 然后从中随机取出一个
        Random random = new Random();
        int number = random.nextInt(list.size());
        Tools tools = list.get(number);

//        把随机取出的器材 改变状态
        UpdateWrapper<Tools> updateWrapper = new UpdateWrapper<Tools>();
        updateWrapper.eq("id",tools.getId()).set("state","是");
        int update = toolsMapper.update(null,updateWrapper);
        if(update>0) return tools.getId();
        return 0;
    }
}
