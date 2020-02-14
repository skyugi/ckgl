package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.mapper.CustomerMapper;
import com.lzh.wms.business.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-14
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
