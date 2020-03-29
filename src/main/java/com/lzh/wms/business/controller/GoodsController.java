package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.service.KindService;
import com.lzh.wms.business.service.ProviderService;
import com.lzh.wms.business.vo.GoodsVo;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.MyFileUtils;
import com.lzh.wms.system.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  商品管理前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private KindService kindService;

    /**
     * 查询商品：全查询、模糊查询
     * @param goodsVo
     * @return
     */
    @RequestMapping("/loadAllGoods")
    public DataGridView loadAllGoods(GoodsVo goodsVo){
        IPage<Goods> page = new Page<>(goodsVo.getPage(),goodsVo.getLimit());
        //组装查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
        queryWrapper.eq(goodsVo.getKindId()!=null&&goodsVo.getKindId()!=0,"kind_id",goodsVo.getKindId());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()),"productcode",goodsVo.getProductcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()),"promitcode",goodsVo.getPromitcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()),"description",goodsVo.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()),"size",goodsVo.getSize());
        goodsService.page(page,queryWrapper);
        List<Goods> records = page.getRecords();
        for (Goods goods : records) {
            Provider provider = providerService.getById(goods.getProviderid());
            Kind kind = kindService.getById(goods.getKindId());
            if (null!=provider){
                goods.setProvidername(provider.getProvidername());
            }
            if (kind!=null){
                goods.setKindName(kind.getName());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

    /**
     * 添加商品
     * @param goodsVo
     * @return
     */
    @RequestMapping("/addGoods")
    public ResultObj addGoods(GoodsVo goodsVo){
        try {
           GoodsVo goodsVo1 = goodsService.changeImageNameBeforeAddGoods(goodsVo);
            goodsService.save(goodsVo1);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改商品
     * @param goodsVo
     * @return
     */
    @RequestMapping("/updateGoods")
    public ResultObj updateGoods(GoodsVo goodsVo){
        try {
            GoodsVo goodsVo1 = goodsService.judgeImageBeforeUpdateGoods(goodsVo,goodsService);
            goodsService.updateById(goodsVo1);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    
    /**
     * 删除商品信息
     * @param id
     * @return
     */
    @RequestMapping("deleteGoods")
    public ResultObj deleteGoods(Integer id, String goodsimg){
        try {
            //如果商品图片不是默认图片则删除
            MyFileUtils.removeFileByPath(goodsimg);
            goodsService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 为商品进货查询提供查询所有商品
     * @return
     */
    @RequestMapping("/loadAllGoodsDropDownList")
    public DataGridView loadAllProviderDropDownList(){
        //组装查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Goods> list = goodsService.list(queryWrapper);
        //兼容前端显示，从缓存里面取，效率影响不大
        for (Goods goods : list) {
            Provider provider = providerService.getById(goods.getProviderid());
            goods.setProvidername(provider.getProvidername());
        }
        return new  DataGridView(list);
    }

    /**
     * 为添加商品进货选定供货商后根据供货商id查询商品
     * @return
     */
    @RequestMapping("/loadGoodsDropDownListByProviderId")
    public DataGridView loadGoodsDropDownListByProviderId(Integer providerid){
        if (providerid!=null&&providerid!=0){
            //组装查询条件
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
            queryWrapper.eq("providerid",providerid);
            List<Goods> list = goodsService.list(queryWrapper);
            //兼容前端显示，从缓存里面取，效率影响不大
            for (Goods goods : list) {
                Provider provider = providerService.getById(goods.getProviderid());
                goods.setProvidername(provider.getProvidername());
            }
            return new  DataGridView(list);
        }else {
            return null;
        }

    }

}

