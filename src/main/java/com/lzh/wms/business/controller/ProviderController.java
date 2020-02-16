package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.service.ProviderService;
import com.lzh.wms.business.vo.ProviderVo;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  供应商管理前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    /**
     * 查询供应商：全查询、模糊查询
     * @param providerVo
     * @return
     */
    @RequestMapping("/loadAllProvider")
    public DataGridView loadAllProvider(ProviderVo providerVo){
        IPage<Provider> page = new Page<>(providerVo.getPage(),providerVo.getLimit());
        //组装查询条件
        QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()), "providername", providerVo.getProvidername());
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getTelephone()), "telephone", providerVo.getTelephone());
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()), "connectionperson", providerVo.getConnectionperson());
        providerService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加供应商
     * @param providerVo
     * @return
     */
    @RequestMapping("/addProvider")
    public ResultObj addProvider(ProviderVo providerVo){
        try {
            providerService.save(providerVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改供应商
     * @param providerVo
     * @return
     */
    @RequestMapping("/updateProvider")
    public ResultObj updateProvider(ProviderVo providerVo){
        try {
            providerService.updateById(providerVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除供应商信息
     * @param providerVo
     * @return
     */
    @RequestMapping("/batchDeleteProvider")
    public ResultObj batchDeleteProvider(ProviderVo providerVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : providerVo.getIds()) {
                idList.add(id);
            }
            providerService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除供应商信息
     * @param id
     * @return
     */
    @RequestMapping("deleteProvider")
    public ResultObj deleteProvider(Integer id){
        try {
            providerService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 为商品查询提供查询所有有效的供应商
     * @return
     */
    @RequestMapping("/loadAllProviderDropDownList")
    public DataGridView loadAllProviderDropDownList(){
        //组装查询条件
        QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Provider> list = providerService.list(queryWrapper);
        return new  DataGridView(list);
    }
    
}

