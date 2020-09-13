package com.lzh.wms.business.vo;

import com.lzh.wms.business.domain.PurchaseBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 采购单
 * @author lzh
 * @date 2020-04-03 23:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurchaseBillVo extends PurchaseBill {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    /**
     * 接收多个id
     */
    private Integer[] ids;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
