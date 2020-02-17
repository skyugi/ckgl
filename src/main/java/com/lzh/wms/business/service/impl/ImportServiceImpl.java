package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Import;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.mapper.ImportMapper;
import com.lzh.wms.business.service.ImportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public boolean save(Import entity) {
        //根据商品id查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
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
}
