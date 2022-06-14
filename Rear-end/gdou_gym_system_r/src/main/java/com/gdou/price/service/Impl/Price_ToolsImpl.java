package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_OverTime;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.service.IPrice_OverTimeService;
import com.gdou.price.service.IPrice_ToolsService;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.ITStateService;
import com.gdou.tools.service.IToolsService;
import com.gdou.utils.PriceUtil;
import com.gdou.utils.TimeUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Price_ToolsImpl extends ServiceImpl<Price_ToolsMapper, Price_Tools> implements IPrice_ToolsService {
    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private ToolsMapper toolsMapper;

    @Autowired
    private IToolsService iToolsService;

    @Autowired
    private ITStateService itStateService;

    @Autowired
    private IPrice_OverTimeService iPrice_overTimeService;


    /**
     * 获取订单信息
     * @param currentPage
     * @param pageSize
     * @param priceTools
     * @return
     */
    @Override
    public IPage<Price_Tools> getPage(int currentPage, int pageSize,Price_Tools priceTools) {
        //进行条件查询
        LambdaQueryWrapper<Price_Tools> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(priceTools.getUsercode()),Price_Tools::getUsercode,priceTools.getUsercode());
        lambdaQueryWrapper.like(Strings.isNotEmpty(priceTools.getDate()),Price_Tools::getDate,priceTools.getDate());
        lambdaQueryWrapper.like(priceTools.getTime()>0,Price_Tools::getTime,priceTools.getTime());
        IPage<Price_Tools> page = new Page(currentPage,pageSize);
        price_toolsMapper.selectPage(page,lambdaQueryWrapper);
        return page;
    }

    /**
     * 领取器材 判断器材是否存在 存在则把器材改成领取 只要有一个不存在则不成功返回不存在的器材
     * @param id 订单编号
     * @return 存放不存在器材的编号
     */
    @Override
    public List<Integer> handleRent(Integer id) {
        //把toolslist转换成list
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号
//        System.out.println(arrList);

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

        if(list.size()>0){
            //有器材不在 返回不在的器材
            return list;
        }
        else{
            //器材都在
            // 对tools表进行更新
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            String now = formatterd.format(calendar.getTime());

            for (int i = 0;i<tList.size();i++){
                UpdateWrapper<Tools> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",tList.get(i)).set("exist","否");
                toolsMapper.update(null,updateWrapper);
            }
            //对tools_state表进行更新
            for (int i = 0;i<arrList.size();i++){
                UpdateWrapper<TState> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",arrList.get(i)).set("receive","已领取").set("actually",now);
                tStateMapper.update(null,updateWrapper);
            }
            //对price_tools表进行更改
            UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("state","是");
            price_toolsMapper.update(null,updateWrapper);
            list.clear();
            return list;
        }
    }

    /**
     * 判断预约时间是否超时
     * @param id 订单编号
     * @return 返回状态码
     */
    @Override
    public String overTime(Integer id) {
       //从数据库中获取预约日期以及预约时间

        Price_Tools price_tools = price_toolsMapper.selectById(id);
        System.out.println(price_tools);
        return TimeUtil.getTime(price_tools.getDate(), price_tools.getTime());
    }

    /**
     * 取消预约
     * @param id 订单编号
     * @return
     */
    @Override
    public boolean cancelRent(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

        int flag;
        int res = 0;

        for(int i = 0;i<arrList.size();i++){
            res = tStateMapper.deleteById(arrList.get(i));
        }
        flag = price_toolsMapper.deleteById(id);
        return flag>0 && res>0;
    }

    /**
     * 替换器材如果有器材就给予替换 返回替换后的toolslist 没有就返回"01"
     * @param id 订单编号
     * @return
     */
    @Override
    public String changeRent(Integer id) {
        String newTools = "";

        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

//        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }

        //遍历tools表 根据exist项来确认器材是否存在
        List<Integer> existList = new ArrayList<>();
        for(int i =0;i<tList.size();i++){
            Tools tools = toolsMapper.selectById(tList.get(i));
            if(tools.getExist().equals("否")) {
                existList.add(tools.getId());
            }
        }

        System.out.println("不存在仓库的器材"+existList);//不存在仓库的器材号

        Tools tools = iToolsService.getById(existList.get(0));
//        System.out.println(tools);

        QueryWrapper<Tools> queryWrapper = new QueryWrapper<Tools>();
        queryWrapper.eq("kind",tools.getKind())
                .eq("price",tools.getPrice())
                .eq("safe","否")
                .eq("exist","是")
                .notIn("id",existList);
        List<Tools> l1 = toolsMapper.selectList(queryWrapper);

        System.out.println("符合条件存在仓库的器材号"+l1);//存在仓库的器材号

        List<Integer> list = new ArrayList<>();
        if(l1.size()>0){
            //创建一个list用于过滤出l1里的id 器材编号

            for(int i=0;i<l1.size();i++){
                list.add(l1.get(i).getId());
            }

            //然后通过时间、时间段以及器材编号查找是否有符合的器材数据

            QueryWrapper<TState> queryWrapper1 = new QueryWrapper<TState>();
            queryWrapper1.eq("date",price_tools.getDate())
                    .eq("time",price_tools.getTime())
                    .in("toolscode",list);
            List<TState> l2 = tStateMapper.selectList(queryWrapper1);

            System.out.println("原来存在订单的器材"+l2);//与仓库中的器材有冲突的器材数据

            //筛选出符合条件的器材 然后返回
            for (int i = 0;i<l2.size();i++){
                for (int j = 0;j<list.size();j++){
                    if(list.get(j).equals(l2.get(i).getToolscode())){
                        list.remove(j);
                    }
                }
            }

            System.out.println(list);
        }


        if(list.size() >= existList.size()){
            System.out.println("库中有充足的器材给予更换");
            List<Integer> l3 = new ArrayList<>();//用户返回给用户器材id
            List<Integer> l4 = new ArrayList<>();//用于存进price_tools
            //生成随机数 然后从可选器材里中随机取出一个
            Random random = new Random();
            int number = 0;

            TState tState = new TState();
            tState.setUsercode(price_tools.getUsercode());
            tState.setTime(price_tools.getTime());
            tState.setDate(price_tools.getDate());

            for(int i=0;i<existList.size();i++){//添加新的器材
                number = random.nextInt(list.size());//随机获取一个器材号
                tState.setToolscode(list.get(number));
                tStateMapper.insert(tState);
                list.remove(number);

                l3.add(tState.getToolscode());
                l4.add(tState.getId());

                tState.setId(null);
            }
            for(int i= 0;i<existList.size();i++){//删除尚未归还的器材对应订单
                QueryWrapper<TState> tStateQueryWrapper = new QueryWrapper<>();
                tStateQueryWrapper.eq("toolscode",existList.get(i))
                                .eq("usercode",price_tools.getUsercode())
                                .eq("date",price_tools.getDate())
                                .eq("time",price_tools.getTime());
                tStateMapper.delete(tStateQueryWrapper);
            }
//            System.out.println(tState);
            QueryWrapper<TState> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("usercode",tState.getUsercode())
                        .eq("date",tState.getDate())
                        .eq("time",tState.getTime());
            List<TState> tStateList = tStateMapper.selectList(queryWrapper2);
//            System.out.println(tStateList);
            //对price_tools表的toolslist进行修改
            String toolslist = "";
            for(int i = 0;i<tStateList.size();i++){
                newTools += tStateList.get(i).getToolscode();
                toolslist += tStateList.get(i).getId();
                if(i != tStateList.size()-1) {
                    newTools += ",";
                    toolslist +=",";
                }
            }
//            System.out.println(newTools);
            UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id)
                        .set("toolslist",toolslist);
            price_toolsMapper.update(null,updateWrapper);
        }
        else{
            System.out.println("库中没有充足的器材给予更换");
            newTools = "01";
        }

        return newTools;
    }

    /**
     * 对不存在的器材进行删除
     * @param id 订单编号
     * @return
     */
    @Override
    public String continueRent(Integer id) {
        String newTools ="";
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

//        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }

        //遍历tools表 根据exist项来确认器材是否存在
        List<Integer> existList = new ArrayList<>();
        for(int i =0;i<tList.size();i++){
            Tools tools = toolsMapper.selectById(tList.get(i));
            if(tools.getExist().equals("否")) {
                existList.add(tools.getId());
            }
        }
        for(int i= 0;i<existList.size();i++){//把不存在的器材进行删除
            QueryWrapper<TState> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("usercode",price_tools.getUsercode())
                    .eq("date",price_tools.getDate())
                    .eq("time",price_tools.getTime())
                    .eq("toolscode",existList.get(i));
            tStateMapper.delete(queryWrapper);
        }

        String toolslist = "";
        QueryWrapper<TState> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("usercode",price_tools.getUsercode())
                .eq("date",price_tools.getDate())
                .eq("time",price_tools.getTime());
        List<TState> tStateList = tStateMapper.selectList(queryWrapper2);

        for(int i = 0;i<tStateList.size();i++){
            newTools += tStateList.get(i).getToolscode();
            toolslist += tStateList.get(i).getId();
            if(i != tStateList.size()-1) {
                newTools +=",";
                toolslist +=",";
            }
        }
//            System.out.println(newTools);
        if(!toolslist.equals("")){
            System.out.println(toolslist);
            UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id)
                    .set("toolslist",toolslist);
            price_toolsMapper.update(null,updateWrapper);
        }
        else{
            price_toolsMapper.deleteById(id);//如果一件器材都没有的话 直接删除订单
        }
        return newTools;
    }


    /**
     * 计算收费
     * @param id
     * @return 第一个数据是时间 第二个数据是价格 第三个是回收时间 第四个数是判断有没有被记录失约
     */
    @Override
    public List<String> getPrice(Integer id) {
        List<String> list = new ArrayList<>();
        String finalPrice="";
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

//        System.out.println(arrList);

        TState tState = tStateMapper.selectById(arrList.get(0));

        Tools tools = toolsMapper.selectById(tState.getToolscode());

        Float price = PriceUtil.strFormatDouble(tools.getPrice());//获取价格
        System.out.println(price);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();//获取当前时间
        String rentTime = itStateService.getDateTime(id).get(0);
        try {
            Date rt = formatter.parse(rentTime);
            long nowTime = now.getTime()-rt.getTime();//获取现在时间与租借时间的时间戳
            long minute = nowTime/1000/60;//把时间戳转换为分钟
//            System.out.println(minute);
            if(minute <= 0){
                //没有超过预约归还时间
                finalPrice = price.toString();
                list.add("0");
            }
            else if(minute>0 && minute<=720){
                Price_OverTime overtime = iPrice_overTimeService.getById(1);
                System.out.println(overtime.getMultiple());
                Float allPrice= price + price*(minute/60)*overtime.getMultiple();
                finalPrice = allPrice.toString();
                list.add(String.valueOf(minute));
            }
            else{
                Price_OverTime overtime = iPrice_overTimeService.getById(2);
                Float allPrice = price*13+price*((minute-720)/60)*overtime.getMultiple();
                finalPrice = allPrice.toString();
                list.add(String.valueOf(minute));
            }
            list.add(finalPrice);
            String format = formatter.format(now.getTime());
            list.add(format);

            for(int i =0;i<arrList.size();i++){
                UpdateWrapper<TState> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",arrList.get(i)).set("ractually",format);
                tStateMapper.update(null,updateWrapper);
            }
            if(minute>360){
                //归还时间大于6小时记录失约
                list.add("01");
            }
            else{
                list.add("00");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 收费处理
     * @param id 订单号
     * @return
     */
    @Override
    public boolean handlePay(Integer id,String price) {
        int res = 0;
        UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("price",price);

        res = price_toolsMapper.update(null,updateWrapper);

        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

//        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }

        for(int i=0;i<tList.size();i++){
            UpdateWrapper<Tools> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("id",tList.get(i))
                    .set("exist","是");
            res = toolsMapper.update(null,updateWrapper1);
        }
        return res>0;
    }


    /**
     * 获取用户所选的器材信息
     * @param id
     * @return
     */
    @Override
    public List<Integer> getPriceTools(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号
//        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }
        return tList;
    }
}
