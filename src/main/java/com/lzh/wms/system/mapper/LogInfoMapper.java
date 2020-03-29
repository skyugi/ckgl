package com.lzh.wms.system.mapper;

import com.lzh.wms.statistics.domain.BaseEntity;
import com.lzh.wms.system.domain.LogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-02-01
 */
public interface LogInfoMapper extends BaseMapper<LogInfo> {

    /**
     * 统计登录用户名
     * @return
     */
    List<BaseEntity> queryLoginNameStatList();
}
