package com.lzh.wms.business.controller;


import com.lzh.wms.business.service.ReturnsService;
import com.lzh.wms.system.common.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  退货前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/returns")
public class ReturnsController {

    @Autowired
    private ReturnsService returnsService;

    @RequestMapping("/addReturns")
    public ResultObj addReturns(Integer id, Integer number, String remark){
        try {
            returnsService.addReturns(id,number,remark);
            return ResultObj.OPERATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

}

