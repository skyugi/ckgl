package com.lzh.wms.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-15
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 添加商品前去掉图片名后缀_temp
     * @param goodsVo
     * @return
     */
    GoodsVo changeImageNameBeforeAddGoods(GoodsVo goodsVo);

    /**
     * 更新商品时判断图片的修改情况
     * @param goodsVo
     * @param goodsService
     * @return
     */
    GoodsVo judgeImageBeforeUpdateGoods(GoodsVo goodsVo, GoodsService goodsService);

}
