package com.lzh.wms.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘样
 * @since 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_sale_report")
public class SaleReport implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //没有在页面接收不用加注解吧
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date month;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private Integer userid;

    private BigDecimal salemoney;

    @TableField(exist = false)
    private String username;


}
