package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医药货品vo
 *
 * @author lzh
 * @date 2020-03-07 1:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductVo extends Product {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

}
