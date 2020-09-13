package com.lzh.wms.system.serviceTask;

import com.lzh.wms.business.domain.Message;
import com.lzh.wms.business.domain.PurchaseBill;
import com.lzh.wms.business.service.MessageService;
import com.lzh.wms.business.service.PurchaseBillService;
import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzh
 * @date 2020-04-06 22:15
 */
public class SmsServiceTask implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(SmsServiceTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String eventName = execution.getEventName();
        log.info("eventName is {}",eventName);

        String processBusinessKey = execution.getProcessBusinessKey();
        String[] split = processBusinessKey.split(":");
        String purchaseBillId = split[1];

        //得到当前用户
//        User currentUser = (User) WebUtils.getSession().getAttribute("user");
//        Integer mgr = currentUser.getMgr();
        //这里没法注入，好像是因为MyTaskListenerImpl不是由spring创建的，加component也不行
//        UserService userService = SpringUtils.getBean(UserService.class);
        PurchaseBillService purchaseBillService = SpringUtils.getBean(PurchaseBillService.class);
        PurchaseBill purchaseBill = purchaseBillService.getById(purchaseBillId);
        Message message = new Message();
        message.setTitle(purchaseBill.getTitle());
        message.setGoods(purchaseBill.getGoods());
        message.setNum(purchaseBill.getNum());
        message.setReason(purchaseBill.getReason());
        message.setTime(purchaseBill.getPurchasetime());
        message.setUserid(purchaseBill.getUserid());

        MessageService messageService = SpringUtils.getBean(MessageService.class);
        messageService.saveOrUpdate(message);
    }
}
