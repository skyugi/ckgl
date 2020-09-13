package com.lzh.wms.system.common;

/**
 * 常量类接口
 *
 * @author lzh
 * @date 2020-01-04 22:53
 */
public interface Constant {

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
    /**
     * 用户默认密码
     */
    String USER_DEFAULT_PWD = "123456";
    /**
     * 商品默认图片
     */
    String IMAGES_DEFAULTGOODSIMG_PNG = "images/defaultProductImgPath.png";
    /**
     * 临时图片后缀
     */
    String TEMP_IMAGE_SUFFIX = "_temp";
    /**
     * 请假单的状态
     */
    //未提交
    public static final String STATE_LEAVEBILL_ZERO = "0";
    //审批中
    public static final String STATE_LEAVEBILL_ONE = "1";
    //审批完成
    public static final String STATE_LEAVEBILL_TWO = "2";
    //取消
    public static final String STATE_LEAVEBILL_THREE = "3";
}
