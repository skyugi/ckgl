package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品类子类
 *
 * @author lzh
 * @date 2020-02-15 15:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsVo extends Goods {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

}
