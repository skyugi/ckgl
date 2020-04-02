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

    /**
     * 统计年采购各支付类型所占金额
     * @param year
     * @return
     */
    List<BaseEntity> queryImportPayTypeYearStat(String year);

    /**
     * 年度采购各支付类型金额按月统计
     * @param year
     * @return
     */
    List<Double> queryImportPayTypeYearEachMonthStat(String year);
}
