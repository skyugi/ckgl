package com.lzh.wms.business.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzh
 * @date 2020-04-08 22:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SaleReportVo {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

}
