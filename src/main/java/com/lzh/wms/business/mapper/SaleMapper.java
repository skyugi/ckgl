package com.lzh.wms.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.wms.business.domain.Sale;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-03-30
 */
public interface SaleMapper extends BaseMapper<Sale> {

    /**
     * 汇总当月销售额
     * @return
     */
    BigDecimal statMonthSale();
}
