package com.gdou.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.tools.domain.Tools;

import java.util.List;

public interface IToolsService extends IService<Tools> {
    IPage<Tools> getPage(int currentPage,int pageSize,Tools tools);
    List<Tools> getPrice(String kind);
    List<Integer> getTools(Tools tools,String date,int time);
}
