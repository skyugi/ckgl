package com.lzh.wms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.domain.LogInfo;
import com.lzh.wms.system.service.LogInfoService;
import com.lzh.wms.system.vo.LogInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  登录日志前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-01
 */
@RestController
@RequestMapping("/logInfo")
public class LogInfoController {

    @Autowired
    private LogInfoService logInfoService;

    /**
     * 查询登录日志：全查询、模糊查询
     * @param logInfoVo
     * @return
     */
    @RequestMapping("/loadAllLogInfo")
    public DataGridView loadAllLogInfo(LogInfoVo logInfoVo){
        IPage<LogInfo> page = new Page<>(logInfoVo.getPage(),logInfoVo.getLimit());
        //组装查询条件
        QueryWrapper<LogInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(logInfoVo.getLoginname()),"loginname",logInfoVo.getLoginname());
        queryWrapper.like(StringUtils.isNotBlank(logInfoVo.getLoginip()),"loginip",logInfoVo.getLoginip());
        //数据库数据大于开始时间
        queryWrapper.ge(logInfoVo.getStartTime()!=null,"logintime",logInfoVo.getStartTime());
        //小于
        queryWrapper.le(logInfoVo.getEndTime()!=null,"logintime",logInfoVo.getEndTime());

        queryWrapper.orderByDesc("logintime");
        logInfoService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 批量删除登录日志信息
     * @param logInfoVo
     * @return
     */
    @RequestMapping("/batchDeleteLogInfo")
    public ResultObj batchDeleteLogInfo(LogInfoVo logInfoVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : logInfoVo.getIds()) {
                idList.add(id);
            }
            logInfoService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除登录日志信息
     * @param id
     * @return
     */
    @RequestMapping("deleteLogInfo")
    public ResultObj deleteLogInfo(Integer id){
        try {
            logInfoService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

