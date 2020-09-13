package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzh.wms.business.domain.Depot;
import com.lzh.wms.business.domain.DepotStock;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Import;
import com.lzh.wms.business.mapper.DepotMapper;
import com.lzh.wms.business.mapper.DepotStockMapper;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.mapper.ImportMapper;
import com.lzh.wms.business.service.ImportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
@Service
@Transactional
public class ImportServiceImpl extends ServiceImpl<ImportMapper, Import> implements ImportService {

    /**
     * 添加进货前更新商品库存
     * 用mapper不会更新到缓存，这里就不用goodService把number的变化更新到缓存，
     * 因为import类只用到了goods的goodsname和size
     */
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DepotStockMapper depotStockMapper;

    @Override
    public boolean save(Import entity) {
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
        //设置此次存入某库的货品数量
        depotStock.setGoodsNum(entity.getNumber());
        depotStockMapper.insert(depotStock);
        //---------------------------------------------

        //更新库存
        goods.setNumber(entity.getNumber()+goods.getNumber());
        goodsMapper.updateById(goods);
        //保存进货
        return super.save(entity);
    }

    /**
     * 更新进货前更新商品库存
     * 原来500，第一次+500，现商品库存1000，修改为+300，+600
     * 现商品库存为800，1100
     * @param entity
     * @return
     */
    @Override
    public boolean updateById(Import entity) {
        //进货量
        Integer number = this.getBaseMapper().selectById(entity.getId()).getNumber();
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        //库存量
        Integer number1 = goods.getNumber();
        Integer number2 = number1 - number +entity.getNumber();
        goods.setNumber(number2);
        goodsMapper.updateById(goods);
        //更新进货
        return super.updateById(entity);
    }

//    @Override
//    public boolean removeById(Serializable id) {
//        //进货量
//        Import anImport = this.getBaseMapper().selectById(id);
//        Goods goods = goodsMapper.selectById(anImport.getGoodsid());
//        //库存量
//        Integer number1 = goods.getNumber();
//        Integer number2 = number1 - anImport.getNumber();
//        goods.setNumber(number2);
//        goodsMapper.updateById(goods);
//        //删除进货
//        return super.removeById(id);
//    }
}
