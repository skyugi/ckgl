package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.Sale;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 销售类子类
 * @author lzh
 * @date 2020-04-01 3:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SaleVo extends Sale {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
