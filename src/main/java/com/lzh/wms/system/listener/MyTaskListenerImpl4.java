package com.lzh.wms.system.listener;

import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.List;

/**
 * 第四节点驳回到第三节点
 * @author lzh
 * @date 2020-03-22 15:04
 */
public class MyTaskListenerImpl4 implements ExecutionListener,TaskListener {

    String assignee = null;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        //得到当前用户
        User currentUser = (User) WebUtils.getSession().getAttribute("user");
        //这里没法注入，好像是因为MyTaskListenerImpl不是由spring创建的，加component也不行
        UserService userService = SpringUtils.getBean(UserService.class);
//        TaskService taskService = SpringUtils.getBean(TaskService.class);
//        DelegateTask delegateTask = SpringUtils.getBean(DelegateTask.class);

//        User leaderOfCurrentUser = userService.getById(mgr);
        List<User> list = userService.list();
        for (User user : list) {
//            if (user.getMgr()==null){
//                break;
//            }
            if (currentUser.getId().equals(user.getMgr())){
//                String processInstanceId = execution.getProcessInstanceId();
//                taskService.createTaskQuery().processInstanceId(processInstanceId).
                assignee = user.getName();

                break;
            }
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee(assignee);
    }
//    @Override
//    public void notify(DelegateTask delegateTask) {
//
//    }
}
