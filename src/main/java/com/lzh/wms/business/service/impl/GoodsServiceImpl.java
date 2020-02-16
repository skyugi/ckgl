package com.lzh.wms.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.service.GoodsService;
import com.lzh.wms.business.vo.GoodsVo;
import com.lzh.wms.system.common.Constast;
import com.lzh.wms.system.common.MyFileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-15
 */
@Service
@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public GoodsVo changeImageNameBeforeAddGoods(GoodsVo goodsVo) {
        if (goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().endsWith("_temp")){
            //将文件名后缀_temp去掉
            String replacedName = MyFileUtils.renameTempFile(goodsVo.getGoodsimg());
            goodsVo.setGoodsimg(replacedName);
        }
        return goodsVo;
    }

    @Override
    public GoodsVo judgeImageBeforeUpdateGoods(GoodsVo goodsVo, GoodsService goodsService) {
        //fixme 思考别的判段方法
        //修改过的话后缀一定有temp，修改过或者原来不是默认图片的进if
        if (!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))){
            //再判断是否是修改过的临时文件
            if (goodsVo.getGoodsimg().endsWith("_temp")){
                //是-->去掉后缀temp
                String replacedName = MyFileUtils.renameTempFile(goodsVo.getGoodsimg());
                goodsVo.setGoodsimg(replacedName);
                //删除原来的非默认图片
                String originalGoodsimg = goodsService.getById(goodsVo.getId()).getGoodsimg();
                MyFileUtils.removeFileByPath(originalGoodsimg);
            }
        }
        return goodsVo;
    }
}
