package com.lzh.wms.system.service.impl;

import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.mapper.LeaveBillMapper;
import com.lzh.wms.system.service.WorkFlowService;
import com.lzh.wms.system.vo.WorkFlowVo;
import com.lzh.wms.system.vo.camunda.EnableJsonDeploymentEntity;
import com.lzh.wms.system.vo.camunda.EnableJsonProcessDefinitionEntity;
import com.lzh.wms.system.vo.camunda.EnableJsonTaskEntity;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author lzh
 * @date 2020-03-18 21:59
 */
@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private LeaveBillMapper leaveBillMapper;

    @Override
    public DataGridView queryAllProcessDeployment(WorkFlowVo workFlowVo) {
        if (workFlowVo.getDeploymentName()==null){
            workFlowVo.setDeploymentName("");
        }
        String deploymentName = workFlowVo.getDeploymentName();
        //查询流程部署总条数
        long count = repositoryService.createDeploymentQuery().deploymentNameLike("%" + deploymentName + "%").count();
        int firstResult = (workFlowVo.getPage()-1)*workFlowVo.getLimit();
        int maxResults = workFlowVo.getLimit();
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .deploymentNameLike("%" + deploymentName + "%").listPage(firstResult, maxResults);
        List<EnableJsonDeploymentEntity> data = new ArrayList<>();
        for (Deployment deployment : list) {
            EnableJsonDeploymentEntity enableJsonDeploymentEntity = new EnableJsonDeploymentEntity();
            //解决无法write json
            BeanUtils.copyProperties(deployment,enableJsonDeploymentEntity);
            data.add(enableJsonDeploymentEntity);
        }
        return new DataGridView(count,data);
    }

    @Override
    public DataGridView queryAllProcessDefinition(WorkFlowVo workFlowVo) {
        if (workFlowVo.getDeploymentName()==null){
            workFlowVo.setDeploymentName("");
        }
        String deploymentName = workFlowVo.getDeploymentName();
        //根据部署的名称查询所有的部署id
        List<Deployment> list = repositoryService.createDeploymentQuery().deploymentNameLike("%" + deploymentName + "%").list();
        Set<String> deploymentIdsSet = new HashSet<>();
        for (Deployment deployment : list) {
            deploymentIdsSet.add(deployment.getId());
        }
        long count = 0;
        List<EnableJsonProcessDefinitionEntity> data = new ArrayList<>();
        if (deploymentIdsSet.size()>0){
            //activiti的API是通过部署id的set集合去查的repositoryService.createProcessDefinitionQuery().deploymentIds(Set<String> deploymentIds)
            //camunda没有这个api。。
            //将set转成String数组
            String[] deploymentIds = deploymentIdsSet.toArray(new String[deploymentIdsSet.size()]);
            //先只考虑一次只部署一个流程，一个部署id对应一个流程定义id
            String[] processDefinitionIds = new String[deploymentIdsSet.size()];
            for (String deploymentId : deploymentIds) {
                long count1 = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).count();
                if (count1!=0){
                    count++;
                }
                List<ProcessDefinition> list1 = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).list();
                processDefinitionIds[(int) count-1]=list1.get(0).getId();
            }
            int firstResult = (workFlowVo.getPage()-1)*workFlowVo.getLimit();
            int maxResults = workFlowVo.getLimit();
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionIdIn(processDefinitionIds).listPage(firstResult, maxResults);

            for (ProcessDefinition processDefinition : processDefinitionList) {
                EnableJsonProcessDefinitionEntity enableJsonProcessDefinitionEntity = new EnableJsonProcessDefinitionEntity();
                BeanUtils.copyProperties(processDefinition,enableJsonProcessDefinitionEntity);
                data.add(enableJsonProcessDefinitionEntity);
            }
        }
        return new DataGridView(count,data);
    }

    @Override
    public void addWorkFlow(InputStream inputStream, String deploymentName) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        repositoryService.createDeployment().name(deploymentName).addZipInputStream(zipInputStream).deploy();
        zipInputStream.close();
        inputStream.close();
    }

    @Override
    public void deleteWorkFlow(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId,true);
    }

    @Override
    public InputStream queryFlowDiagram(String deploymentId) {
        //1、根据部署id查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        //2、根据流程定义对象得到图片的名称
        String diagramResourceName = processDefinition.getDiagramResourceName();
        //3、根据部署id和流程图片名查询图片流
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
        return inputStream;
    }

    @Override
    public void startProcess(Integer leaveBillId, String type) {
//        String processDefinitionKey = type.replace("","").getClass().getSimpleName();
        String processDefinitionKey = type;
        //LeaveBill:1
        String businessKey = processDefinitionKey + ":" +leaveBillId;
        Map<String, Object> variables = new HashMap<>();
        User user = (User) WebUtils.getSession().getAttribute("user");
        //设置流程变量，下个任务的办理人
        variables.put("username",user.getName());
        runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variables);
        //更新请假单的状态
        LeaveBill leaveBill = leaveBillMapper.selectByPrimaryKey(leaveBillId);
        //审批中
        leaveBill.setState(Constant.STATE_LEAVEBILL_ONE);
        leaveBillMapper.updateByPrimaryKeySelective(leaveBill);
    }

    @Override
    public DataGridView queryCurrentUserTask(WorkFlowVo workFlowVo) {
        User user = (User) WebUtils.getSession().getAttribute("user");
        long count = taskService.createTaskQuery().taskAssignee(user.getName()).count();
        int firstResult = (workFlowVo.getPage()-1)*workFlowVo.getLimit();
        int maxResults = workFlowVo.getLimit();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(user.getName()).listPage(firstResult, maxResults);
        List<EnableJsonTaskEntity> data = new ArrayList<>();
        for (Task task : tasks) {
            EnableJsonTaskEntity enableJsonTaskEntity = new EnableJsonTaskEntity();
            BeanUtils.copyProperties(task,enableJsonTaskEntity);
            data.add(enableJsonTaskEntity);
        }
        return new DataGridView(count,data);
    }

    public List<String> queryOutComeByTaskId(String taskId) {
        List<String> names = new ArrayList<>();
        // 1,根据任务ID查询任务实例
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2,取出流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        // 3,取出流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        // 4,根据流程实例ID查询流程实例
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 5,根据流程定义ID查询流程定义的XML信息
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) this.repositoryService
                .getProcessDefinition(processDefinitionId);
        // 6,从流程实例对象里面取出当前活动节点ID
//        String activityId = processInstance.getActivityId();// usertask1
//        --------------------------------------------------------------------------------------------------------------------------------------------
        String activityId = runtimeService.getActivityInstance(processInstanceId).getActivityId();
//        --------------------------------------------------------------------------------------------------------------------------------------------
        // 7,使用活动ID取出xml和当前活动ID相关节点数据
        ActivityImpl activityImpl = processDefinition.findActivity(activityId);
        // 8,从activityImpl取出连线信息
        List<PvmTransition> transitions = activityImpl.getOutgoingTransitions();
        if (null != transitions && transitions.size() > 0) {
            // PvmTransition就是连接对象
            for (PvmTransition pvmTransition : transitions) {
                String name = pvmTransition.getProperty("name").toString();
                names.add(name);
            }
        }
        return names;
    }
}
