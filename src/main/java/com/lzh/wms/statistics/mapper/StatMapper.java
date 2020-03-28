package com.lzh.wms.statistics.mapper;

import com.lzh.wms.statistics.domain.BaseEntity;

import java.util.List;

/**
 * 统计分析mapper
 * @author lzh
 * @date 2020-03-28 17:12
 */
public interface StatMapper {
    /**
     * 统计客户地区数据
     * @return
     */
    List<BaseEntity> queryCustomerAreaStatList();

    /**
     * 统计客户名称
     * @return
     */
    List<BaseEntity> queryCustomerNameStatList();
}
