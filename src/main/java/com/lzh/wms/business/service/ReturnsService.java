package com.lzh.wms.business.service;

import com.lzh.wms.business.domain.Returns;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
public interface ReturnsService extends IService<Returns> {

    /**
     * 退货
     * @param id 进货单id
     * @param number 退货数量
     * @param remark 备注
     */
    void addReturns(Integer id, Integer number, String remark);
}
