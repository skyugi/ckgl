package com.lzh.wms.business.controller;

import com.lzh.wms.business.domain.Returns;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *业务功能的路由器
 *
 * @author lzh
 * @date 2020-02-14 21:47
 */
@Controller
@RequestMapping("/bus")
public class BusinessController {

    /**
     * 跳转到客户管理
     * @return
     */
    @RequestMapping("/toCustomerManager")
    public String toCustomer(){
        return "/business/customer/customerManager";
    }

    /**
     * 跳转到供应商管理
     * @return
     */
    @RequestMapping("/toProviderManager")
    public String toProvider(){
        return "/business/provider/providerManager";
    }

    /**
     * 跳转到商品管理
     * @return
     */
    @RequestMapping("/toGoodsManager")
    public String toGoods(){
        return "/business/goods/goodsManager";
    }

    /**
     * 跳转到进货管理
     * @return
     */
    @RequestMapping("/toImportManager")
    public String toImportManager(){
        return "/business/import/importManager";
    }

    /**
     * 跳转到退货查询
     * @return
     */
    @RequestMapping("/toReturnsManager")
    public String toReturnsManager(){
        return "/business/returns/returnsManager";
    }

    /**
     * 跳转到仓库管理
     * @return
     */
    @RequestMapping("/toDepotManager")
    public String toDepotManager(){
        return "/business/depot/depotManager";
    }

    /**
     * 跳转到医药分类管理
     * @return
     */
    @RequestMapping("/toKindManager")
    public String toKindManager(){
        return "/business/kind/kindManager";
    }
}
