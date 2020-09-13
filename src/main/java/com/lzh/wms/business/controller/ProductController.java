package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Product;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.business.service.ProductService;
import com.lzh.wms.business.service.KindService;
import com.lzh.wms.business.vo.ProductVo;
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
 *  医药货品管理前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private KindService kindService;

    /**
     * 查询医药货品：全查询、模糊查询
     * @param productVo
     * @return
     */
    @RequestMapping("/loadAllProduct")
    public DataGridView loadAllProduct(ProductVo productVo){
        IPage<Product> page = new Page<>(productVo.getPage(),productVo.getLimit());
        //组装查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(productVo.getKindId()!=null&&productVo.getKindId()!=0,"kind_id",productVo.getKindId());
        queryWrapper.like(StringUtils.isNotBlank(productVo.getProductName()), "product_name", productVo.getProductName());
        queryWrapper.like(StringUtils.isNotBlank(productVo.getProductSn()),"product_sn",productVo.getProductSn());
        productService.page(page,queryWrapper);
        List<Product> records = page.getRecords();
        for (Product product : records) {
            Kind kind = kindService.getById(product.getKindId());
            if (null!=kind){
                product.setKindName(kind.getName());
            }
        }
        return new  DataGridView(page.getTotal(),records);
    }

    /**
     * 添加医药货品
     * @param productVo
     * @return
     */
    @RequestMapping("/addProduct")
    public ResultObj addProduct(ProductVo productVo){
        try {
            ProductVo productVo1 = productService.changeImageNameBeforeAddProduct(productVo);
            productService.save(productVo1);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改医药货品
     * @param productVo
     * @return
     */
    @RequestMapping("/updateProduct")
    public ResultObj updateProduct(ProductVo productVo){
        try {
            ProductVo productVo1 = productService.judgeImageBeforeUpdateProduct(productVo,productService);
            productService.updateById(productVo1);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除医药货品信息
     * @param id
     * @return
     */
    @RequestMapping("deleteProduct")
    public ResultObj deleteProduct(Integer id, String productImgPath){
        try {
            //如果医药货品图片不是默认图片则删除
            MyFileUtils.removeFileByPath(productImgPath);
            productService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 为医药货品进货查询提供查询所有医药货品
     * @return
     */
    @RequestMapping("/loadAllProductDropDownList")
    public DataGridView loadAllKindDropDownList(){
        //组装查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
        List<Product> list = productService.list(queryWrapper);
        //兼容前端显示，从缓存里面取，效率影响不大
        for (Product product : list) {
            Kind kind = kindService.getById(product.getKindId());
            product.setKindName(kind.getName());
        }
        return new  DataGridView(list);
    }

    /**
     * 为添加医药货品进货选定供货商后根据供货商id查询医药货品
     * @return
     */
    @RequestMapping("/loadProductDropDownListByKindId")
    public DataGridView loadProductDropDownListByKindId(Integer kindId){
        if (kindId!=null&&kindId!=0){
            //组装查询条件
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
            queryWrapper.eq("kindId",kindId);
            List<Product> list = productService.list(queryWrapper);
            //兼容前端显示，从缓存里面取，效率影响不大
            for (Product product : list) {
                Kind kind = kindService.getById(product.getKindId());
                product.setKindName(kind.getName());
            }
            return new  DataGridView(list);
        }else {
            return null;
        }

    }

}

