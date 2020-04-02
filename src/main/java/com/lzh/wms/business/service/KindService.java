package com.lzh.wms.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.statistics.domain.BaseEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-03-06
 */
public interface KindService extends IService<Kind> {

    /**
     * 医药类别统计
     * @return
     */
    List<BaseEntity> queryKindNameStatList();
}
