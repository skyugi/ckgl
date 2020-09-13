package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.domain.Returns;
import com.lzh.wms.business.domain.SaleBack;
import com.lzh.wms.business.service.CustomerService;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.service.SaleBackService;
import com.lzh.wms.business.vo.SaleBackVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/saleBack")
public class SaleBackController {
    
    @Autowired
    private SaleBackService saleBackService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询退货信息：全查询、模糊查询
     * @param saleBackVo
     * @return
     */
    @RequestMapping("/loadAllSaleBack")
    public DataGridView loadAllSaleBack(SaleBackVo saleBackVo){
        IPage<SaleBack> page = new Page<>(saleBackVo.getPage(),saleBackVo.getLimit());
        //组装查询条件
        QueryWrapper<SaleBack> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(saleBackVo.getCustomerid()!=null&&saleBackVo.getCustomerid()!=0, "customerid", saleBackVo.getCustomerid());
        queryWrapper.like(saleBackVo.getGoodsid()!=null&&saleBackVo.getGoodsid()!=0, "goodsid", saleBackVo.getGoodsid());
        queryWrapper.ge(saleBackVo.getStartTime()!=null, "salebacktime", saleBackVo.getStartTime());
        queryWrapper.le(saleBackVo.getEndTime()!=null, "salebacktime", saleBackVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(saleBackVo.getOperateperson()),"operateperson",saleBackVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(saleBackVo.getRemark()),"remark",saleBackVo.getRemark());
        queryWrapper.orderByDesc("salebacktime");
        saleBackService.page(page,queryWrapper);
        List<SaleBack> records = page.getRecords();
        for (SaleBack saleBack : records) {
            Customer customer = customerService.getById(saleBack.getCustomerid());
            if (null!=customer){
                saleBack.setCustomername(customer.getCustomername());
            }
            Goods goods = goodsService.getById(saleBack.getGoodsid());
            if (goods!=null){
                saleBack.setGoodsname(goods.getGoodsname());
                saleBack.setSize(goods.getSize());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

    /**
     * 退货入库
     * @param id
     * @param number
     * @param remark
     * @return
     */
    @RequestMapping("/addSaleBack")
    public ResultObj addSaleBack(Integer id, Integer number, String remark){
        try {
            saleBackService.addSaleBack(id,number,remark);
            return ResultObj.OPERATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }
    
}

