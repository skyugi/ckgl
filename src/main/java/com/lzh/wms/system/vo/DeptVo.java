package com.lzh.wms.system.vo;

import com.lzh.wms.system.domain.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
