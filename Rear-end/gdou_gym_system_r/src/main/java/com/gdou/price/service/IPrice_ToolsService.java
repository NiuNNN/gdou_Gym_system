package com.gdou.price.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.price.domain.Price_Tools;

import java.util.List;

public interface IPrice_ToolsService extends IService<Price_Tools> {
    IPage<Price_Tools> getPage(int currentPage, int pageSize,Price_Tools priceTools);
    List<Integer> handleRent(Integer id);
    String overTime(Integer id);
    boolean cancelRent(Integer id);
    String changeRent(Integer id);
    String continueRent(Integer id);
    List<String> getPrice(Integer id);
    boolean handlePay(Integer id,String price);
    List<Integer> getPriceTools(Integer id);
    
}
