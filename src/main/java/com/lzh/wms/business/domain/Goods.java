package com.lzh.wms.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String goodsname;

    private String produceplace;

    private String size;

    private String goodspackage;

    private String productcode;

    private String promitcode;

    private String description;

    private Double price;

    private Integer number;

    private Integer dangernum;

    private String goodsimg;

    private Integer available;

    private Integer providerid;

    private Integer kindId;

    @TableField(exist = false)
    private String providername;

    @TableField(exist = false)
    private String kindName;

}
