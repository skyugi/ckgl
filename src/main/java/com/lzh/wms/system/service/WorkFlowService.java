package com.lzh.wms.system.service;

import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.vo.WorkFlowVo;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
     * @param workFlowVo
     */
//    void startProcess(Integer leaveBillId, String type);
    void startProcess(WorkFlowVo workFlowVo);

    /**
     * 查询当前用户待办任务
     * @param workFlowVo
     * @return
     */
    DataGridView queryCurrentUserTask(WorkFlowVo workFlowVo);

    /**
     * 根据任务id查询不同类型单id
     * @param taskId
     * @return
     */
//    LeaveBill queryLeaveBillByTaskId(String taskId);
    Object queryObjectBillByTaskId(String taskId);

    /**
     * 根据任务id查询连线信息
     * @param taskId
     * @return
     */
    List<String> queryOutgoingNameByTaskId(String taskId);

    /**
     * 根据任务id查询任务批注信息
     * @param taskId
     * @return
     */
    DataGridView queryCommentByTaskId(String taskId);

    /**
     * 完成任务
     * @param workFlowVo
     */
    void completeTask(WorkFlowVo workFlowVo);

    /**
     * 根据任务id查流程定义
     * @param taskId
     * @return
     */
    ProcessDefinition queryProcessDefinitionByTaskId(String taskId);

    /**
     * 根据任务id查节点坐标
     * @param taskId
     * @return
     */
    Map<String, Object> queryTaskCoordinateByTaskId(String taskId);

    /**
     * 根据请假单或采购单id查询批注信息
     * @param workFlowVo
     * @return
     */
    DataGridView queryCommentByBillId(WorkFlowVo workFlowVo);

    /**
     * 查看我的审批记录
     * @param workFlowVo
     * @return
     */
    DataGridView queryCurrentUserHistoryTask(WorkFlowVo workFlowVo);

}
