package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Message;
import com.lzh.wms.business.service.MessageService;
import com.lzh.wms.business.vo.MessageVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *   消息前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-04-06
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    /**
     * 加载全部消息
     * @param messageVo
     * @return
     */
    @RequestMapping("loadAllMessage")
    public DataGridView loadAllMessage(MessageVo messageVo){
        IPage<Message> page = new Page<Message>(messageVo.getPage(),messageVo.getLimit());
        //组装查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        messageService.page(page,queryWrapper);
        List<Message> records = page.getRecords();
        for (Message message : records) {
            User user = userService.getById(message.getUserid());
            if (null!=user){
                message.setUsername(user.getName());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

}

