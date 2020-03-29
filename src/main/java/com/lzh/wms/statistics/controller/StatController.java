package com.lzh.wms.statistics.controller;

import com.lzh.wms.statistics.domain.BaseEntity;
import com.lzh.wms.statistics.service.StatService;
import com.lzh.wms.system.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping("queryLoginNameStatJson")
    public List<BaseEntity> queryLoginNameStatJson(){
        return logInfoService.queryLoginNameStatList();
    }

    @RequestMapping("queryCustomerAreaStatJson")
    public List<BaseEntity> queryCustomerAreaStatJson(){
        return statService.queryCustomerAreaStatList();
    }

    @RequestMapping("queryCustomerNameStatJson")
    public List<BaseEntity> queryCustomerNameStatJson(){
        return statService.queryCustomerNameStatList();
    }

}
