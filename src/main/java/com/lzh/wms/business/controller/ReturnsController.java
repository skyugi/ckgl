package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Import;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.domain.Returns;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.service.ProviderService;
import com.lzh.wms.business.service.ReturnsService;
import com.lzh.wms.business.vo.ImportVo;
import com.lzh.wms.business.vo.ReturnsVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private ProviderService providerService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询退货信息：全查询、模糊查询
     * @param returnsVo
     * @return
     */
    @RequestMapping("/loadAllReturns")
    public DataGridView loadAllReturns(ReturnsVo returnsVo){
        IPage<Returns> page = new Page<>(returnsVo.getPage(),returnsVo.getLimit());
        //组装查询条件
        QueryWrapper<Returns> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(returnsVo.getProviderid()!=null&&returnsVo.getProviderid()!=0, "providerid", returnsVo.getProviderid());
        queryWrapper.like(returnsVo.getGoodsid()!=null&&returnsVo.getGoodsid()!=0, "goodsid", returnsVo.getGoodsid());
        queryWrapper.ge(returnsVo.getStartTime()!=null, "returntime", returnsVo.getStartTime());
        queryWrapper.le(returnsVo.getEndTime()!=null, "returntime", returnsVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(returnsVo.getOperateperson()),"operateperson",returnsVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(returnsVo.getRemark()),"remark",returnsVo.getRemark());
        queryWrapper.orderByDesc("returntime");
        returnsService.page(page,queryWrapper);
        List<Returns> records = page.getRecords();
        for (Returns returns1 : records) {
            Provider provider = providerService.getById(returns1.getProviderid());
            if (null!=provider){
                returns1.setProvidername(provider.getProvidername());
            }
            Goods goods = goodsService.getById(returns1.getGoodsid());
            if (goods!=null){
                returns1.setGoodsname(goods.getGoodsname());
                returns1.setSize(goods.getSize());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }
    
    /**
     * 退货
     * @param id
     * @param number
     * @param remark
     * @return
     */
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

