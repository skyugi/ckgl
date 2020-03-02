package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Depot;
import com.lzh.wms.business.service.DepotService;
import com.lzh.wms.business.vo.DepotVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;

    /**
     * 查询仓库：全查询、模糊查询
     * @param depotVo
     * @return
     */
    @RequestMapping("/loadAllDepot")
    public DataGridView loadAllDepot(DepotVo depotVo){
        IPage<Depot> page = new Page<>(depotVo.getPage(),depotVo.getLimit());
        //组装查询条件
        QueryWrapper<Depot> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(depotVo.getName()),"name",depotVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(depotVo.getLocation()),"location",depotVo.getLocation());
        depotService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加仓库
     * @param depotVo
     * @return
     */
    @RequestMapping("/addDepot")
    public ResultObj addDepot(DepotVo depotVo){
        try {
            depotService.save(depotVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改仓库
     * @param depotVo
     * @return
     */
    @RequestMapping("/updateDepot")
    public ResultObj updateDepot(DepotVo depotVo){
        try {
            depotService.updateById(depotVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除仓库信息
     * @param depotVo
     * @return
     */
//    @RequestMapping("/batchDeleteDepot")
//    public ResultObj batchDeleteDepot(DepotVo depotVo){
//        try {
//            Collection<Serializable> idList = new ArrayList<>();
//            for (Integer id : depotVo.getIds()) {
//                idList.add(id);
//            }
//            depotService.removeByIds(idList);
//            return ResultObj.DELETE_SUCCESS;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResultObj.DELETE_ERROR;
//        }
//    }

    /**
     * 删除仓库信息
     * @param id
     * @return
     */
    @RequestMapping("deleteDepot")
    public ResultObj deleteDepot(Integer id){
        try {
            depotService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}

