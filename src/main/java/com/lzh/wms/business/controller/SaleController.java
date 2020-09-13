package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Depot;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.domain.Sale;
import com.lzh.wms.business.service.DepotService;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.service.CustomerService;
import com.lzh.wms.business.service.SaleService;
import com.lzh.wms.business.vo.SaleVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private DepotService depotService;

    /**
     * 查询销售信息：全查询、模糊查询
     * @param saleVo
     * @return
     */
    @RequestMapping("/loadAllSale")
    public DataGridView loadAllSale(SaleVo saleVo){
        IPage<Sale> page = new Page<>(saleVo.getPage(),saleVo.getLimit());
        //组装查询条件
        QueryWrapper<Sale> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(saleVo.getCustomerid()!=null&&saleVo.getCustomerid()!=0, "customerid", saleVo.getCustomerid());
        queryWrapper.like(saleVo.getGoodsid()!=null&&saleVo.getGoodsid()!=0, "goodsid", saleVo.getGoodsid());
        queryWrapper.ge(saleVo.getStartTime()!=null, "saletime", saleVo.getStartTime());
        queryWrapper.le(saleVo.getEndTime()!=null, "saletime", saleVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(saleVo.getOperateperson()),"operateperson",saleVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(saleVo.getRemark()),"remark",saleVo.getRemark());
        //null为未逻辑删除,1为已逻辑删除,2为已退货入库
        queryWrapper.isNull("state");
        queryWrapper.orderByDesc("saletime");
        saleService.page(page,queryWrapper);
        List<Sale> records = page.getRecords();
        for (Sale sale1 : records) {
            Customer customer = customerService.getById(sale1.getCustomerid());
            if (null!=customer){
                sale1.setCustomername(customer.getCustomername());
            }
            Goods goods = goodsService.getById(sale1.getGoodsid());
            if (goods!=null){
                sale1.setGoodsname(goods.getGoodsname());
                sale1.setSize(goods.getSize());
            }
            Depot depot = depotService.getById(sale1.getDepotId());
            if (depot!=null){
                sale1.setDepotName(depot.getName());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

    /**
     * 添加商品销售出库
     * @param saleVo
     * @return
     */
    @RequestMapping("/addSale")
    public ResultObj addSale(SaleVo saleVo){
        try {
            saleVo.setSaletime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            saleVo.setOperateperson(user.getName());
            saleService.save(saleVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改商品销售
     * @param saleVo
     * @return
     */
    @RequestMapping("/updateSale")
    public ResultObj updateSale(SaleVo saleVo){
        try {
            saleService.updateById(saleVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除商品销售出库,将状态改为1，表示记录删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteSale")
    public ResultObj deleteSale(Integer id){
        try {
            Sale anSale = saleService.getById(id).setState(1);
            saleService.updateById(anSale);
//            saleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    
}

