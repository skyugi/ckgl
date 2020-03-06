package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Kind;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzh
 * @date 2020-03-06 14:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KindVo extends Kind {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    /**
     * 接收多个id
     */
    private Integer[] ids;

}
