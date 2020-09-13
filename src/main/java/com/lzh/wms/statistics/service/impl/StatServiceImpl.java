package com.lzh.wms.statistics.service.impl;

import com.lzh.wms.statistics.domain.BaseEntity;
import com.lzh.wms.statistics.mapper.StatMapper;
import com.lzh.wms.statistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lzh
 * @date 2020-03-28 17:10
 */
@Service
@Transactional
public class StatServiceImpl implements StatService {

    @Autowired
    private StatMapper statMapper;

    @Override
    public List<BaseEntity> queryCustomerAreaStatList() {
        return statMapper.queryCustomerAreaStatList();
    }

    @Override
    public List<BaseEntity> queryCustomerNameStatList() {
        return statMapper.queryCustomerNameStatList();
    }

    @Override
    public List<BaseEntity> queryImportPayTypeYearStat(String year) {
        return statMapper.queryImportPayTypeYearStat(year);
    }

    @Override
    public List<Double> queryImportPayTypeYearEachMonthStat(String year) {
        return statMapper.queryImportPayTypeYearEachMonthStat(year);
    }

    @Override
    public List<BaseEntity> queryRoleNameStatList() {
        return statMapper.queryRoleNameStatList();
    }

    @Override
    public List<BaseEntity> queryDeptEmployeeNumStat() {
        return statMapper.queryDeptEmployeeNumStat();
    }
}
