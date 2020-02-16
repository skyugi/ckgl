package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.mapper.ProviderMapper;
import com.lzh.wms.business.service.ProviderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-15
 */
@Service
@Transactional
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {

    @Override
    public boolean save(Provider entity) {
        return super.save(entity);
    }

    @Override
    public Provider getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean updateById(Provider entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }
}
