package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.*;
import com.lzh.wms.business.mapper.*;
import com.lzh.wms.business.service.SaleBackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-04-02
 */
@Service
@Transactional
public class SaleBackServiceImpl extends ServiceImpl<SaleBackMapper, SaleBack> implements SaleBackService {

    @Autowired
    private SaleMapper saleMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DepotStockMapper depotStockMapper;
    
    @Override
    public void addSaleBack(Integer id, Integer number, String remark) {
        //1.根据销售单的id查询销售信息
        Sale sale = saleMapper.selectById(id);
        //2.根据goodsid查询goods信息
        Goods goods = goodsMapper.selectById(sale.getGoodsid());

        //bus_depot_stock-------------------------------------------------
        Depot depot = depotMapper.selectById(sale.getDepotId());
        DepotStock depotStock = new DepotStock();
        depotStock.setDepotId(sale.getDepotId());
        depotStock.setDepotName(depot.getName());
        depotStock.setGoodsId(sale.getGoodsid());
        depotStock.setGoodsName(goods.getGoodsname());
        //将退货入库数量用正数插进数据表
        depotStock.setGoodsNum(number);
        depotStockMapper.insert(depotStock);
        //-------------------------------------------------------------------

        goods.setNumber(goods.getNumber()+number);
        goodsMapper.updateById(goods);
        //3.添加退货入库单的信息
        SaleBack saleBack = new SaleBack();
        saleBack.setCustomerid(sale.getCustomerid());
        saleBack.setPaytype(sale.getPaytype());
        saleBack.setSalebacktime(new Date());
        User user = (User) WebUtils.getSession().getAttribute("user");
        saleBack.setOperateperson(user.getName());
        saleBack.setSalebackprice(sale.getSaleprice());
        saleBack.setNumber(number);
        saleBack.setRemark(remark);
        saleBack.setGoodsid(sale.getGoodsid());

        //把销售单的状态改为已退货--------------------------------------------------------------------
        sale.setState(2);
        saleMapper.updateById(sale);


        this.getBaseMapper().insert(saleBack);
    }
}
