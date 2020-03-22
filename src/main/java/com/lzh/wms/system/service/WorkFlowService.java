package com.lzh.wms.system.service;

import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.vo.WorkFlowVo;

/**
 * @author lzh
 * @date 2020-03-18 21:58
 */
public interface WorkFlowService {
    /**
     * 查询流程部署
     * @param workFlowVo
     * @return
     */
    DataGridView queryAllProcessDeployment(WorkFlowVo workFlowVo);

    /**
     * 查询流程定义
     * @param workFlowVo
     * @return
     */
    DataGridView queryAllProcessDefinition(WorkFlowVo workFlowVo);

    void addWorkFlow();
}
