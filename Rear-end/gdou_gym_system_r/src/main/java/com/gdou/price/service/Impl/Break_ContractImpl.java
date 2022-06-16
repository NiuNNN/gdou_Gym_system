package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Break_ContractMapper;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Break_Contract;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.service.IBreak_ContractService;
import com.gdou.user.dao.UserMapper;
import com.gdou.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class Break_ContractImpl extends ServiceImpl<Break_ContractMapper, Break_Contract> implements IBreak_ContractService {
    @Autowired
    private Break_ContractMapper break_contractMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 对用户的超时归还器材进行登记
     * @param id
     * @return
     */
    @Override
    public Boolean takeNotes(Integer id) {
        //只记录用户的违约记录 管理员违约不进行记录
        int res = 0;

        Price_Tools price_tools = price_toolsMapper.selectById(id);//获取用户信息
        User user = userMapper.selectById(price_tools.getUsercode());
        String description = "用户 "+user.getCode()+" "+user.getName()+" 于 "+price_tools.getDate()+" 预约的器材没有按预约时间及时归还，特此警告！！！";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(calendar.getTime());

        Break_Contract break_contract = new Break_Contract();
        break_contract.setUsercode(user.getCode());
        break_contract.setDescription(description);
        break_contract.setDate(date);

        QueryWrapper<Break_Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usercode",user.getCode());
        Long count = break_contractMapper.selectCount(queryWrapper);
        if(count>0){
            //用户存在记录 对记录更新
            UpdateWrapper<Break_Contract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("usercode",user.getCode());
            res = break_contractMapper.update(break_contract, updateWrapper);
        }
        else {
            //不存在用户 添加记录
            res = break_contractMapper.insert(break_contract);
        }
        return res>0;
    }

    /**
     * 获取违规信息
     * @return
     */
    @Override
    public List<String> getDescription() {
        List<String> description = new ArrayList<>();
        QueryWrapper<Break_Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("description","");
        List<Break_Contract> break_contracts = break_contractMapper.selectList(queryWrapper);
        for(int i =0;i<break_contracts.size();i++){
            description.add(break_contracts.get(i).getDescription());
        }
        return description;
    }

}
