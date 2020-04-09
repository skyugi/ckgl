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

    /**
     * 统计各角色/职位的员工人数占比
     * @return
     */
    List<BaseEntity> queryRoleNameStatList();

    /**
     * 统计部门人数
     * @return
     */
    List<BaseEntity> queryDeptEmployeeNumStat();
}
