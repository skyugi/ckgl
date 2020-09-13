package com.lzh.wms.system.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端的状态码结果对象
 *
 * @author lzh
 * @date 2020-01-04 22:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {

    private Integer code;
    private String msg;

    public static final ResultObj LOGIN_SUCCESS = new ResultObj(Constant.OK, "登录成功");
    public static final ResultObj LOGIN_ERROR_PASS = new ResultObj(Constant.ERROR, "登录失败,用户名或密码错误");
    public static final ResultObj LOGIN_ERROR_CODE = new ResultObj(Constant.ERROR, "登录失败,验证码错误");
    public static final ResultObj LOGOUT_SUCCESS = new ResultObj(Constant.OK, "退出成功");
    public static final ResultObj LOGOUT_ERROR = new ResultObj(Constant.ERROR, "退出失败");

    public static final ResultObj DELETE_SUCCESS = new ResultObj(Constant.OK, "删除成功");
    public static final ResultObj DELETE_ERROR = new ResultObj(Constant.ERROR, "删除失败");

    public static final ResultObj ADD_SUCCESS = new ResultObj(Constant.OK, "添加成功");
    public static final ResultObj ADD_ERROR = new ResultObj(Constant.ERROR, "添加失败");

    public static final ResultObj UPDATE_SUCCESS = new ResultObj(Constant.OK, "修改成功");
    public static final ResultObj UPDATE_ERROR = new ResultObj(Constant.ERROR, "修改失败");

    public static final ResultObj RESET_SUCCESS = new ResultObj(Constant.OK, "重置成功");
    public static final ResultObj RESET_ERROR = new ResultObj(Constant.ERROR, "重置失败");

    public static final ResultObj DISPATCH_SUCCESS = new ResultObj(Constant.OK, "分配成功");
    public static final ResultObj DISPATCH_ERROR = new ResultObj(Constant.ERROR, "分配失败");

    public static final ResultObj  OPERATE_SUCCESS=new ResultObj(Constant.OK, "操作成功");
    public static final ResultObj  OPERATE_ERROR=new ResultObj(Constant.ERROR, "操作失败");

    public static final ResultObj  START_SUCCESS=new ResultObj(Constant.OK, "启动成功");
    public static final ResultObj  START_ERROR=new ResultObj(Constant.ERROR, "启动失败");

}
