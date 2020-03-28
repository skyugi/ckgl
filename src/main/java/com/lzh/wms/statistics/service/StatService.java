package com.lzh.wms.statistics.service;

import com.lzh.wms.statistics.domain.BaseEntity;

import java.util.List;

/**
 * 统计分析数据服务接口
 * @author lzh
 * @date 2020-03-28 17:09
 */
public interface StatService {

    /**
     * 统计客户地区的数据
     * @return
     */
    List<BaseEntity> queryCustomerAreaStatList();

    /**
     * 统计客户名称
     * @return
     */
    List<BaseEntity> queryCustomerNameStatList();
}
