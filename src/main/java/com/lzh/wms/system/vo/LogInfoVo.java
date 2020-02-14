package com.lzh.wms.system.vo;

import com.lzh.wms.system.domain.LogInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 登录日志类子类
 *
 * @author lzh
 * @date 2020-02-01 15:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogInfoVo extends LogInfo {

    private static final long serialVersionUID = 1L;

//    private Integer page = 1;
    private Integer page;
//    private Integer limit = 10;
    private Integer limit;

    private Integer[] ids;//接收多个id

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
