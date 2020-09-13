package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.Message;
import com.lzh.wms.business.mapper.MessageMapper;
import com.lzh.wms.business.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-04-06
 */
@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
