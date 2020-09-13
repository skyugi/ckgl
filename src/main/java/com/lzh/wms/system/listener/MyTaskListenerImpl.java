package com.lzh.wms.system.listener;

import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.apache.catalina.startup.WebAnnotationSet;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.List;

/**
 * @author lzh
 * @date 2020-03-22 15:04
 */
public class MyTaskListenerImpl implements TaskListener {

    UserService userService = SpringUtils.getBean(UserService.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        //得到当前用户
        User currentUser = (User) WebUtils.getSession().getAttribute("user");
        Integer mgr = currentUser.getMgr();

        //上级驳回，设置办理人为自己
        if (mgr==null||mgr==0){
//            delegateTask.setAssignee(currentUser.getName());
            List<User> list = userService.list();
            for (User user : list) {
//            if (user.getMgr()==null){
//                break;
//            }
                if (currentUser.getId().equals(user.getMgr())){
                    //设置自己
                    delegateTask.setAssignee(user.getName());
                    break;
                }
            }
        }else {




            //这里没法注入，好像是因为MyTaskListenerImpl不是由spring创建的，加component也不行
//            UserService userService = SpringUtils.getBean(UserService.class);
            User leaderOfCurrentUser = userService.getById(mgr);
            delegateTask.setAssignee(leaderOfCurrentUser.getName());
        }
    }
}
