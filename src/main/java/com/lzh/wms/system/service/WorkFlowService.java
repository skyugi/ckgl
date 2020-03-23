package com.lzh.wms.system.service;

import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.vo.WorkFlowVo;

import java.io.IOException;
import java.io.InputStream;

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

    /**
     * 添加流程部署
     * @param inputStream
     * @param deploymentName
     */
    void addWorkFlow(InputStream inputStream, String deploymentName) throws IOException;

    /**
     * 根据流程部署id删除流程
     * @param deploymentId
     */
    void deleteWorkFlow(String deploymentId);

    /**
     * 根据流程部署id查询流程图
     * @param deploymentId
     * @return
     */
    InputStream queryFlowDiagram(String deploymentId);

    /**
     * 启动流程
     * @param leaveBillId
     * @param type
     */
    void startProcess(Integer leaveBillId, String type);

    /**
     * 查询当前用户待办任务
     * @param workFlowVo
     * @return
     */
    DataGridView queryCurrentUserTask(WorkFlowVo workFlowVo);
}
