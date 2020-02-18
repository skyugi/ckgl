package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Import;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.service.ImportService;
import com.lzh.wms.business.service.ProviderService;
import com.lzh.wms.business.vo.GoodsVo;
import com.lzh.wms.business.vo.ImportVo;
import com.lzh.wms.business.vo.ProviderVo;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  进货前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportService importService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询进货信息：全查询、模糊查询
     * @param importVo
     * @return
     */
    @RequestMapping("/loadAllImport")
    public DataGridView loadAllImport(ImportVo importVo){
        IPage<Import> page = new Page<>(importVo.getPage(),importVo.getLimit());
        //组装查询条件
        QueryWrapper<Import> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(importVo.getProviderid()!=null&&importVo.getProviderid()!=0, "providerid", importVo.getProviderid());
        queryWrapper.like(importVo.getGoodsid()!=null&&importVo.getGoodsid()!=0, "goodsid", importVo.getGoodsid());
        queryWrapper.ge(importVo.getStartTime()!=null, "importtime", importVo.getStartTime());
        queryWrapper.le(importVo.getEndTime()!=null, "importtime", importVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(importVo.getOperateperson()),"operateperson",importVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(importVo.getRemark()),"remark",importVo.getRemark());
        queryWrapper.orderByDesc("importtime");
        importService.page(page,queryWrapper);
        List<Import> records = page.getRecords();
        for (Import import1 : records) {
            Provider provider = providerService.getById(import1.getProviderid());
            if (null!=provider){
                import1.setProvidername(provider.getProvidername());
            }
            Goods goods = goodsService.getById(import1.getGoodsid());
            if (goods!=null){
                import1.setGoodsname(goods.getGoodsname());
                import1.setSize(goods.getSize());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

    /**
     * 添加商品进货
     * @param importVo
     * @return
     */
    @RequestMapping("/addImport")
    public ResultObj addImport(ImportVo importVo){
        try {
            importVo.setImporttime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            importVo.setOperateperson(user.getName());
            importService.save(importVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改商品进货
     * @param importVo
     * @return
     */
    @RequestMapping("/updateImport")
    public ResultObj updateImport(ImportVo importVo){
        try {
            importService.updateById(importVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除商品进货
     * @param id
     * @return
     */
    @RequestMapping("/deleteImport")
    public ResultObj deleteImport(Integer id){
        try {
            importService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    
}

