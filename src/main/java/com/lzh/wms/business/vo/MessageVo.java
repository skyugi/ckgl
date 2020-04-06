package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzh
 * @date 2020-04-07 1:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageVo extends Message {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

}
