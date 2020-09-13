package com.lzh.wms.system.vo;

import com.lzh.wms.system.domain.LeaveBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 请假单类子类
 *
 * @author lzh
 * @date 2020-03-21
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class LeaveBillVo extends LeaveBill {

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
