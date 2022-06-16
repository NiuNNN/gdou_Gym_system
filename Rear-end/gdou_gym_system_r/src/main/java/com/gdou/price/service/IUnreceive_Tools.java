package com.gdou.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.price.domain.Unreceive_Tools;
import com.gdou.tools.domain.UserToolsVO;

import java.util.List;

public interface IUnreceive_Tools extends IService<Unreceive_Tools> {
    boolean register(Integer id);
    boolean cancelRegister(Integer id);
    List<UserToolsVO> getUnreceive(String usercode);
}
