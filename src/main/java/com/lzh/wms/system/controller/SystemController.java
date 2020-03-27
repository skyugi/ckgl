package com.lzh.wms.system.controller;

import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.service.LeaveBillService;
import com.lzh.wms.system.service.WorkFlowService;
import com.lzh.wms.system.vo.WorkFlowVo;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import java.util.List;
import java.util.Map;

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
    
    /**
     * 跳转到菜单管理
     * @return
     */
    @RequestMapping("toMenuManager")
    public String toMenuManager(){
        return "system/menu/menuManager";
    }

    /**
     * 跳转到菜单管理右边页面
     * @return
     */
    @RequestMapping("toMenuRight")
    public String toMenuRight(){
        return "system/menu/menuRight";
    }

    /**
     * 跳转到菜单管理左边页面
     * @return
     */
    @RequestMapping("toMenuLeft")
    public String toMenuLeft(){
        return "system/menu/menuLeft";
    }

    /**
     * 跳转到权限管理
     * @return
     */
    @RequestMapping("toPermissionManager")
    public String toPermissionManager(){
        return "system/permission/permissionManager";
    }

    /**
     * 跳转到权限管理右边页面
     * @return
     */
    @RequestMapping("toPermissionRight")
    public String toPermissionRight(){
        return "system/permission/permissionRight";
    }

    /**
     * 跳转到权限管理左边页面
     * @return
     */
    @RequestMapping("toPermissionLeft")
    public String toPermissionLeft(){
        return "system/permission/permissionLeft";
    }

    /**
     * 跳转到角色管理
     * @return
     */
    @RequestMapping("toRoleManager")
    public String toRoleManager(){
        return "system/role/roleManager";
    }

    /**
     * 跳转到用户管理
     * @return
     */
    @RequestMapping("toUserManager")
    public String toUserManager(){
        return "system/user/userManager";
    }

    /**
     * 跳转到缓存管理
     *
     */
    @RequestMapping("toCacheManager")
    public String toCacheManager() {
        return "system/cache/cacheManager";
    }

    /**
     * 跳转到请假单管理
     * @return
     */
    @RequestMapping("toLeaveBillManager")
    public String toLeavebillManager(){
        return "system/leaveBill/leaveBillManager";
    }

    /**
     * 跳转到流程管理
     * @return
     */
    @RequestMapping("toWorkFlowManager")
    public String toWorkFlowManager(){
        return "system/workFlow/workFlowManager";
    }

    /**
     * 跳转到查看流程图的界面
     * @param workFlowVo
     * @return
     */
    @RequestMapping("toViewFlowDiagram")
    public String toViewFlowDiagram(WorkFlowVo workFlowVo, Model model){
//        WebUtils.getSession().setAttribute("viewFlowDiagramPath","/workFlow/viewFlowDiagram?deploymentId="+workFlowVo.getDeploymentId());
        model.addAttribute("viewFlowDiagramPath","/workFlow/viewFlowDiagram?deploymentId="+workFlowVo.getDeploymentId());
        return "/system/workFlow/viewFlowDiagram";
    }

    /**
     * 跳转到我的待办任务
     * @return
     */
    @RequestMapping("toMyTaskManager")
    public String toMyTaskManager(){
        return "system/workFlow/MyTaskManager";
    }

    WorkFlowService workFlowService = SpringUtils.getBean(WorkFlowService.class);

    /**
     * 跳转到办理任务的界面
     * @param workFlowVo
     * @return
     */
    @RequestMapping("toDoTask")
    public String toDoTask(WorkFlowVo workFlowVo, Model model){
        //1、根据任务id查请假单
        LeaveBill leaveBill = workFlowService.queryLeaveBillByTaskId(workFlowVo.getTaskId());
        model.addAttribute("leaveBill",leaveBill);
        //2、根据任务id查询连线信息
        List<String> outgoingName = workFlowService.queryOutgoingNameByTaskId(workFlowVo.getTaskId());
        model.addAttribute("outgoingName",outgoingName);
        return "system/workFlow/doTaskManager";
    }


    /**
     * 根据任务id查看流程图进度
     * @param workFlowVo
     * @param model
     * @return
     */
    @RequestMapping("toViewProcessByTaskId")
    public String toViewProcessByTaskId(WorkFlowVo workFlowVo,Model model){
        ProcessDefinition processDefinition = workFlowService.queryProcessDefinitionByTaskId(workFlowVo.getTaskId());
        //取出流程部署id
        String deploymentId = processDefinition.getDeploymentId();
        workFlowVo.setDeploymentId(deploymentId);
        //根据任务id查询节点坐标
        Map<String,Object> coordinate = workFlowService.queryTaskCoordinateByTaskId(workFlowVo.getTaskId());
        model.addAttribute("viewFlowDiagramPath","/workFlow/viewFlowDiagram?deploymentId="+workFlowVo.getDeploymentId());
        model.addAttribute("coordinate",coordinate);
        return "/system/workFlow/viewFlowDiagram";
    }

    @Autowired
    private LeaveBillService leaveBillService;
    /**
     * 根据请假单id查询请假单和对应审批批注的信息
     * @param workFlowVo
     * @param model
     * @return
     */
    @RequestMapping("viewSpProcess")
    public String viewSpProcess(WorkFlowVo workFlowVo, Model model){
        LeaveBill leaveBill = leaveBillService.getById(workFlowVo.getLeaveBillId());
        model.addAttribute("leaveBill",leaveBill);
        return "/system/workFlow/viewSpProcess";
    }

    /**
     * 跳转到我的任务处理记录
     * @return
     */
    @RequestMapping("toMySpManager")
    public String toMySpManager(){
        return "system/workFlow/mySpManager";
    }
}
