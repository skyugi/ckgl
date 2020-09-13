package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.business.mapper.KindMapper;
import com.lzh.wms.business.service.KindService;
import com.lzh.wms.statistics.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-03-06
 */
@Service
@Transactional
public class KindServiceImpl extends ServiceImpl<KindMapper, Kind> implements KindService {

    @Autowired
    private KindMapper kindMapper;

    @Override
    public List<BaseEntity> queryKindNameStatList() {
        return kindMapper.queryKindNameStatList();
    }
}
