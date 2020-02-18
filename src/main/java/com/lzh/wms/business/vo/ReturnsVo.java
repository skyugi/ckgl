package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Returns;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 退货类子类
 *
 * @author lzh
 * @date 2020-02-19 0:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReturnsVo extends Returns {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
