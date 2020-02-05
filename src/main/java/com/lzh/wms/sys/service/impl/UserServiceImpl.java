package com.lzh.wms.sys.service.impl;

import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.mapper.UserMapper;
import com.lzh.wms.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2019-12-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
