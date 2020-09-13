package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.business.domain.Depot;
import com.lzh.wms.business.domain.DepotStock;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Sale;
import com.lzh.wms.business.mapper.DepotMapper;
import com.lzh.wms.business.mapper.DepotStockMapper;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.mapper.SaleMapper;
import com.lzh.wms.business.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-03-30
 */
@Service
@Transactional
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements SaleService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DepotStockMapper depotStockMapper;

    @Override
    public boolean save(Sale entity) {
        //根据商品id查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());

        //更新某个仓库的库存bus_depot_stock---------------------------------------------
//        SELECT goods_name,depot_name,sum(goods_num) FROM `bus_depot_stock` GROUP BY goods_name,depot_name
        Depot depot = depotMapper.selectById(entity.getDepotId());
        DepotStock depotStock = new DepotStock();
        //设置仓库的id
        depotStock.setDepotId(entity.getDepotId());
        //设置仓库名
        depotStock.setDepotName(depot.getName());
        //设置货品id
        depotStock.setGoodsId(entity.getGoodsid());
        //设置货品名
        depotStock.setGoodsName(goods.getGoodsname());
        //设置此次存入出库的货品数量
        depotStock.setGoodsNum(-entity.getNumber());
        Integer depotStockNum = depotStockMapper.getDepotStockNum(goods.getId(),depot.getId());
        if (depotStockNum==null||depotStockNum-entity.getNumber()<0){
            throw new RuntimeException("库存不足");
        }
        depotStockMapper.insert(depotStock);
        //---------------------------------------------

        //更新库存
        goods.setNumber(goods.getNumber()-entity.getNumber());
        goodsMapper.updateById(goods);
        //保存销售
        return super.save(entity);
    }
}
