package com.lzh.wms.system.vo;

import com.lzh.wms.system.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色类子类
 *
 * @author lzh
 * @date 2020-02-02 21:36
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends User {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;


}
