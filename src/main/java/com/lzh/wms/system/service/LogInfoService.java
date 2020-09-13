package com.lzh.wms.system.service;

import com.lzh.wms.statistics.domain.BaseEntity;
import com.lzh.wms.system.domain.LogInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-01
 */
public interface LogInfoService extends IService<LogInfo> {

    /**
     * 统计用户登录
     * @return
     */
    List<BaseEntity> queryLoginNameStatList();
}
