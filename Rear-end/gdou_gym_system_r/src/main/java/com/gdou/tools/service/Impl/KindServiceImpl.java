package com.gdou.tools.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.tools.dao.KindMapper;
import com.gdou.tools.domain.Kind;
import com.gdou.tools.service.IKindService;
import org.springframework.stereotype.Service;

@Service
public class KindServiceImpl extends ServiceImpl<KindMapper, Kind> implements IKindService {
}
