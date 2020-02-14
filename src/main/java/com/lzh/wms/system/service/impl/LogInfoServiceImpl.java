package com.lzh.wms.system.service.impl;

import com.lzh.wms.system.domain.LogInfo;
import com.lzh.wms.system.mapper.LogInfoMapper;
import com.lzh.wms.system.service.LogInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-01
 */
@Service
@Transactional
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

}
