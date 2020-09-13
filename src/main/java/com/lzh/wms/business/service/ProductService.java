package com.lzh.wms.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.business.domain.Product;
import com.lzh.wms.business.vo.ProductVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-03-07
 */
public interface ProductService extends IService<Product> {

    /**
     * 添加医药货品前去掉图片名后缀_temp
     * @param productVo
     * @return
     */
    ProductVo changeImageNameBeforeAddProduct(ProductVo productVo);

    /**
     * 更新医药货品时判断图片的修改情况
     * @param productVo
     * @param productService
     * @return
     */
    ProductVo judgeImageBeforeUpdateProduct(ProductVo productVo, ProductService productService);

}
