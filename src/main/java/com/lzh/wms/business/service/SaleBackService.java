package com.lzh.wms.business.service;

import com.lzh.wms.business.domain.SaleBack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-04-02
 */
public interface SaleBackService extends IService<SaleBack> {

    /**
     * 退货入库
     * @param id
     * @param number
     * @param remark
     */
    void addSaleBack(Integer id, Integer number, String remark);
}
