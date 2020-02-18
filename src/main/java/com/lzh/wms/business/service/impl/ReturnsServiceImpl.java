package com.lzh.wms.business.service.impl;

import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Import;
import com.lzh.wms.business.domain.Returns;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.mapper.ImportMapper;
import com.lzh.wms.business.mapper.ReturnsMapper;
import com.lzh.wms.business.service.ReturnsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
@Service
@Transactional
public class ReturnsServiceImpl extends ServiceImpl<ReturnsMapper, Returns> implements ReturnsService {

    @Autowired
    private ImportMapper importMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void addReturns(Integer id, Integer number, String remark) {
        //1.根据进货单的id查询进货信息
        Import anImport = importMapper.selectById(id);
        //2.根据goodsid查询goods信息
        Goods goods = goodsMapper.selectById(anImport.getGoodsid());
        goods.setNumber(goods.getNumber()-number);
        goodsMapper.updateById(goods);
        //3.添加退货单的信息
        Returns returns = new Returns();
        returns.setProviderid(anImport.getProviderid());
        returns.setPaytype(anImport.getPaytype());
        returns.setReturntime(new Date());
        User user = (User) WebUtils.getSession().getAttribute("user");
        returns.setOperateperson(user.getName());
        returns.setReturnprice(anImport.getImportprice());
        returns.setNumber(number);
        returns.setRemark(remark);
        returns.setGoodsid(anImport.getGoodsid());
        this.getBaseMapper().insert(returns);
    }
}
