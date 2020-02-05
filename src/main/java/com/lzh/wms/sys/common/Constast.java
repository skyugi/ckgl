package com.lzh.wms.sys.common;

/**
 * 常量类接口
 *
 * @author lzh
 * @date 2020-01-04 22:53
 */
public interface Constast {

    /**
     * 状态码
     */
    public static final Integer OK=200;
    public static final Integer ERROR=-1;
    /**
     * 菜单权限类型
     */
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_PERMISSION = "permission";
    public static final Integer AVAILABLE_TRUE = 1;
    public static final Integer AVAILABLE_FALSE = 0;
    /**
     * 用户角色 类型
     */
    Integer USER_TYPE_SUPER = 0;
    Integer USER_TYPE_NORMAL = 1;
    /**
     * 菜单是否展开
     */
    Integer OPEN_TRUE = 1;
    Integer OPEN_FALSE = 0;
}
