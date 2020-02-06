package com.lzh.wms.sys.vo;

import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.domain.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门类子类
 *
 * @author lzh
 * @date 2020-02-02 21:36
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends Dept {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;


}
