package com.gdou.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.price.domain.Unreceive_Tools;

public interface IUnreceive_Tools extends IService<Unreceive_Tools> {
    boolean register(Integer id);
}
