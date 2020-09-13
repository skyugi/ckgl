package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.SaleReport;
import com.lzh.wms.business.mapper.SaleReportMapper;
import com.lzh.wms.business.service.SaleReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-04-08
 */
@Service
@Transactional
public class SaleReportServiceImpl extends ServiceImpl<SaleReportMapper, SaleReport> implements SaleReportService {

    @Autowired
    private SaleReportMapper saleReportMapper;

    @Override
    public Boolean addSaleReportRecord(BigDecimal saleMoney, User user) {
        //必须从那边传进来,因为汇总期间如果执行到这一步时登录人不是汇报人则插入的是登录人
//        User user = (User) WebUtils.getSession().getAttribute("user");
        //当前时间作为汇报时间
        Date reportTime = new Date();
        //获得当前时间的字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reportTimeStr = simpleDateFormat.format(reportTime);
        String substring = reportTimeStr.substring(0, 10);
        //转换成yyyy-MM-dd
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date month = null;
        try {
            month = format.parse(substring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        另一种方式转换成yyyy-MM-dd
//        ParsePosition pos = new ParsePosition(8);
//        Date month = simpleDateFormat.parse(reportTimeStr, pos);

        SaleReport saleReport = new SaleReport();
        saleReport.setMonth(month);
        saleReport.setTime(reportTime);
        saleReport.setUserid(user.getId());
        saleReport.setSalemoney(saleMoney);

        try {
            saleReportMapper.insert(saleReport);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
