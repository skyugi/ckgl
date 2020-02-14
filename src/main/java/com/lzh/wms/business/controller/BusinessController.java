package com.lzh.wms.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *业务功能的路由器
 *
 * @author lzh
 * @date 2020-02-14 21:47
 */
@Controller
@RequestMapping("/bus")
public class BusinessController {

    @RequestMapping("/toCustomerManager")
    public String toCustomer(){
        return "/business/customer/customerManager";
    }
}
