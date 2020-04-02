package com.lzh.wms.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘样
 * @since 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_sale")
public class Sale implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerid;

    private String paytype;

    private Date saletime;

    private String operateperson;

    private Integer number;

    private String remark;

    private BigDecimal saleprice;

    private Integer goodsid;

    private Integer depotId;

    /**
     * 默认为null,1代表逻辑删除，2代表已退货
     */
    private Integer state;

    @TableField(exist = false)
    private String customername;

    @TableField(exist = false)
    private String goodsname;

    @TableField(exist = false)
    private String size;

    @TableField(exist = false)
    private String depotName;
}
