package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.business.service.KindService;
import com.lzh.wms.business.vo.KindVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  医药分类前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/kind")
public class KindController {

    @Autowired
    private KindService kindService;

    /**
     * 查询医药分类：全查询、模糊查询
     * @param kindVo
     * @return
     */
    @RequestMapping("/loadAllKind")
    public DataGridView loadAllKind(KindVo kindVo){
        IPage<Kind> page = new Page<>(kindVo.getPage(),kindVo.getLimit());
        //组装查询条件
        QueryWrapper<Kind> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(kindVo.getName()), "name", kindVo.getName());
        kindService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加医药分类
     * @param kindVo
     * @return
     */
    @RequestMapping("/addKind")
    public ResultObj addKind(KindVo kindVo){
        try {
            kindService.save(kindVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改医药分类
     * @param kindVo
     * @return
     */
    @RequestMapping("/updateKind")
    public ResultObj updateKind(KindVo kindVo){
        try {
            kindService.updateById(kindVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除医药分类信息
     * @param kindVo
     * @return
     */
    @RequestMapping("/batchDeleteKind")
    public ResultObj batchDeleteKind(KindVo kindVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : kindVo.getIds()) {
                idList.add(id);
            }
            kindService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除医药分类信息
     * @param id
     * @return
     */
    @RequestMapping("deleteKind")
    public ResultObj deleteKind(Integer id){
        try {
            kindService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}

