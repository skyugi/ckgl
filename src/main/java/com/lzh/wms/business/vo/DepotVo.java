package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Depot;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzh
 * @date 2020-03-03 1:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DepotVo extends Depot {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;
}
