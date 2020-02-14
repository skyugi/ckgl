package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.domain.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商类子类
 *
 * @author lzh
 * @date 2020-02-14 22:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderVo extends Provider {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    /**
     * 接收多个id
     */
    private Integer[] ids;
}
