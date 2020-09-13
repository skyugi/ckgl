package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.business.domain.Product;
import com.lzh.wms.business.mapper.ProductMapper;
import com.lzh.wms.business.service.ProductService;
import com.lzh.wms.business.service.ProductService;
import com.lzh.wms.business.vo.ProductVo;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.MyFileUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-03-07
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public ProductVo changeImageNameBeforeAddProduct(ProductVo productVo) {
        if (productVo.getProductImgPath()!=null&&productVo.getProductImgPath().endsWith("_temp")){
            //将文件名后缀_temp去掉
            String replacedName = MyFileUtils.renameTempFile(productVo.getProductImgPath());
            productVo.setProductImgPath(replacedName);
        }
        return productVo;
    }

    @Override
    public ProductVo judgeImageBeforeUpdateProduct(ProductVo productVo, ProductService productService) {
        //fixme 思考别的判段方法
        //修改过的话后缀一定有temp，修改过或者原来不是默认图片的进if
        if (!(productVo.getProductImgPath()!=null&&productVo.getProductImgPath().equals(Constant.IMAGES_DEFAULTGOODSIMG_PNG))){
            //再判断是否是修改过的临时文件
            if (productVo.getProductImgPath().endsWith("_temp")){
                //是-->去掉后缀temp
                String replacedName = MyFileUtils.renameTempFile(productVo.getProductImgPath());
                productVo.setProductImgPath(replacedName);
                //删除原来的非默认图片
                String originalProductImgPath = productService.getById(productVo.getId()).getProductImgPath();
                MyFileUtils.removeFileByPath(originalProductImgPath);
            }
        }
        return productVo;
    }

}
