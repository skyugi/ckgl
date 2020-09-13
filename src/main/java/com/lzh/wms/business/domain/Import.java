package com.lzh.wms.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘样
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_import")
public class Import implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String paytype;

    private Date importtime;

    private String operateperson;

    private Integer number;

    private String remark;

    private Double importprice;

    private Integer providerid;

    private Integer goodsid;

    private Integer depotId;

//    /**
//     * 进货数量-退货数量
//     * 方便统计某个仓库的库存量
//     */
//    private Integer stocknum;

    /**
     * 默认为null,1代表逻辑删除,2代表已退货
     */
    private Integer state;

    @TableField(exist = false)
    private String providername;

    @TableField(exist = false)
    private String goodsname;

    @TableField(exist = false)
    private String size;

    @TableField(exist = false)
    private String depotName;

}
