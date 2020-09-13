package com.lzh.wms.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.wms.business.domain.DepotStock;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-03-29
 */
public interface DepotStockMapper extends BaseMapper<DepotStock> {

    /**
     * 销售出库时判断销售商品所在仓库库存
     * @return
     * @param goodsId
     * @param depotId
     */
    Integer getDepotStockNum(@Param("goodsId") Integer goodsId, @Param("depotId") Integer depotId);
}
