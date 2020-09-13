package com.lzh.wms.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.wms.business.domain.Kind;
import com.lzh.wms.statistics.domain.BaseEntity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-03-06
 */
public interface KindMapper extends BaseMapper<Kind> {

    /**
     * 医药类别统计
     * @return
     */
    List<BaseEntity> queryKindNameStatList();
}
