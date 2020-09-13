package com.lzh.wms.system.listener;

import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

/**
 * 支持请假网关,设置二级领导为待办人
 * @author lzh
 * @date 2020-04-06
 */
public class MyTaskListenerImpl2 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        //得到当前用户
        User currentUser = (User) WebUtils.getSession().getAttribute("user");
        Integer mgr = currentUser.getMgr();
        //这里没法注入，好像是因为MyTaskListenerImpl不是由spring创建的，加component也不行
        UserService userService = SpringUtils.getBean(UserService.class);
        User leaderOfCurrentUser = userService.getById(mgr);

        User leaderOfCurrentUserLeader = userService.getById(leaderOfCurrentUser.getMgr());

//        delegateTask.setAssignee(leaderOfCurrentUser.getName());
        delegateTask.setAssignee(leaderOfCurrentUserLeader.getName());
    }
}
