package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.DepotStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzh
 * @date 2020-04-02 23:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DepotStockVo extends DepotStock {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

}
