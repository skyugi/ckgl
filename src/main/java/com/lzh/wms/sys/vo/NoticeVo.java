package com.lzh.wms.sys.vo;

import com.lzh.wms.sys.domain.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 公告类子类
 *
 * @author lzh
 * @date 2020-02-02 21:36
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeVo extends Notice {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    private Integer[] ids;//接收多个id

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
