package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.business.domain.PurchaseBill;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.business.service.PurchaseBillService;
import com.lzh.wms.business.vo.PurchaseBillVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  采购单 前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-04-03
 */
@RestController
@RequestMapping("/purchaseBill")
public class PurchaseBillController {
    @Autowired
    private PurchaseBillService purchaseBillService;

    /**
     * 查询采购单：全查询、模糊查询
     * @param purchaseBillVo
     * @return
     */
    @RequestMapping("/loadAllPurchaseBill")
    public DataGridView loadAllPurchaseBill(PurchaseBillVo purchaseBillVo){
        User user = (User) WebUtils.getSession().getAttribute("user");
        //设置非超级管理员只能查看自己的采购单
        if (!user.getType().equals(Constant.USER_TYPE_SUPER)){
            purchaseBillVo.setUserid(user.getId());
        }
        IPage<PurchaseBill> page = new Page<>(purchaseBillVo.getPage(),purchaseBillVo.getLimit());
        QueryWrapper<PurchaseBill> queryWrapper = new QueryWrapper<>();
        //组装查询条件
        queryWrapper.like(purchaseBillVo.getUserid()!=null,"userid",purchaseBillVo.getUserid());
        queryWrapper.like(StringUtils.isNotBlank(purchaseBillVo.getTitle()),"title",purchaseBillVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(purchaseBillVo.getReason()),"reason",purchaseBillVo.getReason());
        //数据库数据大于开始时间
        queryWrapper.ge(purchaseBillVo.getStartTime()!=null,"purchasetime",purchaseBillVo.getStartTime());
        //小于
        queryWrapper.le(purchaseBillVo.getEndTime()!=null,"purchasetime",purchaseBillVo.getEndTime());
        queryWrapper.orderByDesc("purchasetime");
        purchaseBillService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加采购单
     * @param purchaseBillVo
     * @return
     */
    @RequestMapping("addPurchaseBill")
    public ResultObj addPurchaseBill(PurchaseBillVo purchaseBillVo){
        try {
            purchaseBillService.save(purchaseBillVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改采购单
     * @param purchaseBillVo
     * @return
     */
    @RequestMapping("/updatePurchaseBill")
    public ResultObj updatePurchaseBill(PurchaseBillVo purchaseBillVo){
        try {
            purchaseBillService.updateById(purchaseBillVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除采购单信息
     * @param purchaseBillVo
     * @return
     */
    @RequestMapping("/batchDeletePurchaseBill")
    public ResultObj batchDeletePurchaseBill(PurchaseBillVo purchaseBillVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : purchaseBillVo.getIds()) {
                idList.add(id);
            }
            purchaseBillService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除采购单信息
     * @param id
     * @return
     */
    @RequestMapping("deletePurchaseBill")
    public ResultObj deletePurchaseBill(Integer id){
        try {
            purchaseBillService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

