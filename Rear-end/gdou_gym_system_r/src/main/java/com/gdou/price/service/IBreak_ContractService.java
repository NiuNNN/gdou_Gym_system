package com.gdou.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.price.domain.Break_Contract;

public interface IBreak_ContractService extends IService<Break_Contract> {
    Boolean takeNotes(Integer id);
}
