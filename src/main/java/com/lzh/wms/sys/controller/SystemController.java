package com.lzh.wms.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统前端控制器 路由
 * 从登陆前端控制器分离
 *
 * @author lzh
 * @date 2020-01-04 22:39
 */
@Controller
@RequestMapping("/sys")
public class SystemController {

    /**
     * 跳转到登录界面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/system/index/login";
    }

    /**
     * 跳转到首页
     * @return
     */
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "system/index/index";
    }

    /**
     * 跳转到工作台
     * @return
     */
    @RequestMapping("/toDeskManager")
    public String toDeskManager(){
        return "system/index/deskManager";
    }

    /**
     * 跳转到登录日志管理
     * @return
     */
    @RequestMapping("/toLogInfoManager")
    public String toLogInfoManager(){
        return "system/logInfo/logInfoManager";
    }

    /**
     * 跳转到公告管理
     * @return
     */
    @RequestMapping("/toNoticeManager")
    public String toNoticeManager(){
        return "/system/notice/noticeManager";
    }

    /**
     * 跳转到部门管理
     * @return
     */
    @RequestMapping("toDeptManager")
    public String toDeptManager(){
        return "system/dept/deptManager";
    }

    /**
     * 跳转到部门管理右边页面
     * @return
     */
    @RequestMapping("toDeptRight")
    public String toDeptRight(){
        return "system/dept/deptRight";
    }

    /**
     * 跳转到部门管理左边页面
     * @return
     */
    @RequestMapping("toDeptLeft")
    public String toDeptLeft(){
        return "system/dept/deptLeft";
    }

}
