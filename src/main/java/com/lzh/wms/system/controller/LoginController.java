package com.lzh.wms.system.controller;

import com.lzh.wms.system.common.ActiverUser;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.LogInfo;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.LogInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 登录前端控制器
 *
 * @author lzh
 * @date 2020-01-04 22:39
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LogInfoService logInfoService;

    @RequestMapping("/login")
    public ResultObj login(String loginname, String pwd){
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user",activerUser.getUser());

            //记录登录日志
            LogInfo entity = new LogInfo();
            entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
            entity.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
            entity.setLogintime(new Date());
            logInfoService.save(entity);

            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }

    /**
     * 退出系统
     * @return
     */
    @RequestMapping("/logout")
    public ResultObj logout(){
        try {
            //注销session，跳转到登录界面
            User user = (User) WebUtils.getSession().getAttribute("user");
            if (user != null) {
                WebUtils.getSession().removeAttribute("user");
                System.out.println("------------------------------------session已注销，用户已退出------------------------------------");
            }
            return ResultObj.LOGOUT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.LOGOUT_ERROR;
        }
    }
}
