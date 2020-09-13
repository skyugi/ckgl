package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Message;
import com.lzh.wms.business.domain.SaleReport;
import com.lzh.wms.business.service.SaleReportService;
import com.lzh.wms.business.vo.MessageVo;
import com.lzh.wms.business.vo.SaleReportVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  每月销售汇报前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-04-08
 */
@RestController
@RequestMapping("/saleReport")
public class SaleReportController {

    @Autowired
    private SaleReportService saleReportService;
    @Autowired
    private UserService userService;

    /**
     * 加载全部消息
     * @param saleReportVo
     * @return
     */
    @RequestMapping("loadAllSaleReport")
    public DataGridView loadAllSaleReport(SaleReportVo saleReportVo){
        IPage<SaleReport> page = new Page<>(saleReportVo.getPage(),saleReportVo.getLimit());
        //组装查询条件
        QueryWrapper<SaleReport> queryWrapper = new QueryWrapper<>();
        saleReportService.page(page,queryWrapper);
        List<SaleReport> records = page.getRecords();
        for (SaleReport saleReport : records) {
            User user = userService.getById(saleReport.getUserid());
            if (null!=user){
                saleReport.setUsername(user.getName());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

}

