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
     * 跳转到采购退货查询
     * @return
     */
    @RequestMapping("/toReturnsManager")
    public String toReturnsManager(){
        return "/business/returns/returnsManager";
    }

    /**
     * 跳转到采购退货
     * @return
     */
    @RequestMapping("/toImportReturnManager")
    public String toImportReturnManager(){
        return "/business/import/importReturnManager";
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

    /**
     * 跳转到医药货品管理
     * @return
     */
    @RequestMapping("/toProductManager")
    public String toProductManager(){
        return "/business/product/productManager";
    }

    /**
     * 跳转到客户地区统计
     * @return
     */
    @RequestMapping("toCustomerInfoStat")
    public String toCustomerAreaStat(){
        return "statistics/customerInfoStat";
    }

    /**
     * 跳转到采购统计
     * @return
     */
    @RequestMapping("toImportInfoStat")
    public String toImportInfoStat(){
        return "statistics/importInfoStat";
    }

    /**
     * 跳转到销售出库
     * @return
     */
    @RequestMapping("toSaleManager")
    public String toSaleManager(){
        return "business/sale/saleManager";
    }

    /**
     * 跳转到退货入库
     * @return
     */
    @RequestMapping("toSaleReturnManager")
    public String toSaleReturnManager(){
        return "business/sale/saleReturnManager";
    }

    /**
     * 跳转到退货入库记录
     * @return
     */
    @RequestMapping("toSaleBackRecordManager")
    public String toSaleBackRecordManager(){
        return "business/sale/saleBackRecordManager";
    }

    /**
     * 跳转到库存查询
     * @return
     */
    @RequestMapping("toDepotStockManager")
    public String toDepotStockManager(){
        return "business/depotStock/depotStockManager";
    }

    /**
     * 跳转到库存报警
     * @return
     */
    @RequestMapping("toStockAlarmManager")
    public String toStockAlarmManager(){
        return "business/depotStock/stockAlarmManager";
    }

    /**
     * 跳转到采购单申请
     * @return
     */
    @RequestMapping("toPurchaseBillManager")
    public String toPurchaseBillManager(){
        return "business/purchaseBill/purchaseBillManager";
    }

    /**
     * 跳转到我的消息
     * @return
     */
    @RequestMapping("toMyMessageManager")
    public String toMessageManager(){
        return "business/message/myMessageManager";
    }

}
