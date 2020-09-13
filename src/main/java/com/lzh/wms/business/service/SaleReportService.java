package com.lzh.wms.business.service;

import com.lzh.wms.business.domain.SaleReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.system.domain.User;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-04-08
 */
public interface SaleReportService extends IService<SaleReport> {

    /**
     * 将当月销售额插入相应的表做记录
     * @param saleMoney
     * @param user
     * @return
     */
    Boolean addSaleReportRecord(BigDecimal saleMoney, User user);
}
