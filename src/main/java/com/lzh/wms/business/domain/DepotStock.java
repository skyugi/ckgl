package com.lzh.wms.business.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘样
 * @since 2020-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_depot_stock")
public class DepotStock implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String goodsName;

    private String depotName;

    private Integer goodsNum;

    private Integer depotId;

    private Integer goodsId;


}
