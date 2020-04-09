package com.lzh.wms.statistics.controller;

import com.lzh.wms.business.service.KindService;
import com.lzh.wms.statistics.domain.BaseEntity;
import com.lzh.wms.statistics.service.StatService;
import com.lzh.wms.system.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析
 * @author lzh
 * @date 2020-03-28 16:26
 */
@RestController
@RequestMapping("stat")
public class StatController {

    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private StatService statService;
    @Autowired
    private KindService kindService;

    /**
     * 登录日志中登录名统计
     * @return
     */
    @RequestMapping("queryLoginNameStatJson")
    public List<BaseEntity> queryLoginNameStatJson(){
        return logInfoService.queryLoginNameStatList();
    }

    /**
     * 医药类别统计
     * @return
     */
    @RequestMapping("queryKindNameStatJson")
    public List<BaseEntity> queryKindNameStatJson(){
        return kindService.queryKindNameStatList();
    }

    /**
     * 统计客户地区的数据
     * @return
     */
    @RequestMapping("queryCustomerAreaStatJson")
    public List<BaseEntity> queryCustomerAreaStatJson(){
        return statService.queryCustomerAreaStatList();
    }

    /**
     * 统计客户名称
     * @return
     */
    @RequestMapping("queryCustomerNameStatJson")
    public List<BaseEntity> queryCustomerNameStatJson(){
        return statService.queryCustomerNameStatList();
    }

    /**
     * 统计年采购各支付类型所占金额
     * @param year
     * @return
     */
    @RequestMapping("queryImportPayTypeYearStat")
    public Map<String,Object> queryImportPayTypeYearStat(String year){
        List<BaseEntity> list = statService.queryImportPayTypeYearStat(year);
        Map<String,Object> map = new HashMap<>(16);
        List<String> listName = new ArrayList<>();
        List<Double> listValue = new ArrayList<>();
        for (BaseEntity baseEntity : list) {
            listName.add(baseEntity.getName());
            listValue.add(baseEntity.getValue());
        }
        map.put("name",listName);
        map.put("value",listValue);
        return map;
    }

    /**
     * 年度采购各支付类型金额按月统计
     * @param year
     * @return
     */
    @RequestMapping("queryImportPayTypeYearEachMonthStat")
    public List<Double> queryImportPayTypeYearEachMonthStat(String year){
        List<Double> list = statService.queryImportPayTypeYearEachMonthStat(year);
        for (int i = 0; i < list.size() ; i++) {
            if (list.get(i)==null){
                //兼容echarts折线图不显示null的数据,这里Double是引用类型,不能用增强for然后赋值bean=0.0
                list.set(i,0.0);
            }
        }
        return list;
    }

    /**
     * 统计各角色/职位的员工人数占比
     * @return
     */
    @RequestMapping("queryRoleNameStatJson")
    public List<BaseEntity> queryRoleNameStatJson(){
        return statService.queryRoleNameStatList();
    }

    /**
     * 统计各部门人数
     * @param
     * @return
     */
    @RequestMapping("queryDeptEmployeeNumStatJson")
    public Map<String,Object> queryDeptEmployeeNumStatJson(){
        List<BaseEntity> list = statService.queryDeptEmployeeNumStat();
        Map<String,Object> map = new HashMap<>(16);
        List<String> listName = new ArrayList<>();
        List<Double> listValue = new ArrayList<>();
        for (BaseEntity baseEntity : list) {
            listName.add(baseEntity.getName());
            listValue.add(baseEntity.getValue());
        }
        map.put("name",listName);
        map.put("value",listValue);
        return map;
    }
}
