package com.lzh.wms.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.business.domain.Depot;
import com.lzh.wms.business.domain.DepotStock;
import com.lzh.wms.business.service.DepotStockService;
import com.lzh.wms.business.vo.DepotStockVo;
import com.lzh.wms.system.common.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *   仓库库存 前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-29
 */
@RestController
@RequestMapping("/depotStock")
public class DepotStockController {

    @Autowired
    private DepotStockService depotStockService;

    /**
     * 查询库存
     * @param depotStockVo
     * @return
     */
    @RequestMapping("/loadAllDepotStock")
    public DataGridView loadAllDepotStock(DepotStockVo depotStockVo){
        IPage<DepotStock> page = new Page<>(depotStockVo.getPage(),depotStockVo.getLimit());
        //组装查询条件
        QueryWrapper<DepotStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(depotStockVo.getGoodsName()),"goods_name",depotStockVo.getGoodsName());
        queryWrapper.like(StringUtils.isNotBlank(depotStockVo.getDepotName()),"depot_name",depotStockVo.getDepotName());
        queryWrapper.groupBy("goods_id","depot_id");
        depotStockService.page(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

}

