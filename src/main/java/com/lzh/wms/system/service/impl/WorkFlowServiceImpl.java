package com.lzh.wms.system.service.impl;

import com.lzh.wms.business.domain.PurchaseBill;
import com.lzh.wms.business.mapper.PurchaseBillMapper;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.mapper.LeaveBillMapper;
import com.lzh.wms.system.service.WorkFlowService;
import com.lzh.wms.system.vo.WorkFlowVo;
import com.lzh.wms.system.vo.camunda.*;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.CommentEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
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
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private PurchaseBillMapper purchaseBillMapper;

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
            //todo 先只考虑一次只部署一个流程，一个部署id对应一个流程定义id
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
//    public void startProcess(Integer leaveBillId, String type) {
    public void startProcess(WorkFlowVo workFlowVo) {
//        String processDefinitionKey = type.replace("","").getClass().getSimpleName();
        //LeaveBill PurchaseBill
        String processDefinitionKey = workFlowVo.getType();
        String businessKey = null;
        if (workFlowVo.getLeaveBillId()!=null) {
            //LeaveBill:1
            businessKey = processDefinitionKey + ":" +workFlowVo.getLeaveBillId();
        }else if (workFlowVo.getPurchaseBillId()!=null){
            businessKey = processDefinitionKey + ":" +workFlowVo.getPurchaseBillId();
        }

        Map<String, Object> variables = new HashMap<>();
        User user = (User) WebUtils.getSession().getAttribute("user");
        //设置流程变量，下个任务的办理人
        variables.put("username",user.getName());
        runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variables);

        if (workFlowVo.getLeaveBillId()!=null){
            //更新请假单的状态
            LeaveBill leaveBill = leaveBillMapper.selectByPrimaryKey(workFlowVo.getLeaveBillId());
            //审批中
            leaveBill.setState(Constant.STATE_LEAVEBILL_ONE);
            leaveBillMapper.updateByPrimaryKeySelective(leaveBill);
        }else if (workFlowVo.getPurchaseBillId()!=null){
            //更新采购单的状态
            PurchaseBill purchaseBill = purchaseBillMapper.selectById(workFlowVo.getPurchaseBillId());
            //审批中
            purchaseBill.setState(Constant.STATE_LEAVEBILL_ONE);
            purchaseBillMapper.updateById(purchaseBill);
        }
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

    @Override
    public Object queryObjectBillByTaskId(String taskId) {
        //1、根据任务id查询任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2、取流程实例id或执行实例id,执行实例没有查business的api
        String processInstanceId = task.getProcessInstanceId();
//        String executionId = task.getExecutionId();
        //3、通过执行实例id查执行实例
//        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //4、取   LeaveBill:16
        String businessKey = processInstance.getBusinessKey();
        String[] split = businessKey.split(":");
//        String leaveBillId = split[1];
        String billId = split[1];
        if (businessKey.contains("LeaveBill")){
            return leaveBillMapper.selectByPrimaryKey(Integer.valueOf(billId));
        }else if (businessKey.contains("PurchaseBill")){
            return purchaseBillMapper.selectById(billId);
        }
        return null;
    }

    @Override
    public List<String> queryOutgoingNameByTaskId(String taskId) {
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
        String taskDefinitionKey = task.getTaskDefinitionKey();
        // 7,使用活动ID取出xml和当前活动ID相关节点数据
//        ActivityImpl activityImpl = processDefinition.findActivity(activityId);
        ActivityImpl activityImpl = processDefinition.findActivity(taskDefinitionKey);
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

    @Override
    public DataGridView queryCommentByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //todo 这里好像得判断task非空才进行下面操作比较保险
        if (task != null) {

            String processInstanceId = task.getProcessInstanceId();
            List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
            List<EnableJsonCommentEntity> data = new ArrayList<>();
            if (processInstanceComments!=null&&processInstanceComments.size()>0){
                for (Comment processInstanceComment : processInstanceComments) {
                    EnableJsonCommentEntity enableJsonCommentEntity = new EnableJsonCommentEntity();
                    BeanUtils.copyProperties(processInstanceComment,enableJsonCommentEntity);

                    String userId = processInstanceComment.getUserId();
                    //已完成的不能通过taskService去查
                    //观察act_hi_taskinst表,通过办理人和执行实例id或流程实例id锁定唯一的历史任务名
                    //!!!!!!!!!!!!这里注意查出来的历史实例不一定是singleResult(),驳回一次，就会有两个，驳回两次，就会有三个，但任务名是一样的
                    List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                            .taskAssignee(userId).executionId(task.getExecutionId()).list();
                    //这里可以不用非空判断,还是写上吧
                    if (historicTaskInstanceList!=null&&historicTaskInstanceList.size()>0){
                        enableJsonCommentEntity.setTaskName(historicTaskInstanceList.get(0).getName());
                    }

                    data.add(enableJsonCommentEntity);
                }
            }
            return new DataGridView(Long.valueOf(data.size()),data);
        }
        return null;
    }

    @Override
    public void completeTask(WorkFlowVo workFlowVo) {
        //任务id
        String taskId = workFlowVo.getTaskId();
        //连线变量名
        String outgoingName = workFlowVo.getOutgoingName();
        //请假单id
        Integer leaveBillId = workFlowVo.getLeaveBillId();
        //采购单id
        Integer purchaseBillId = workFlowVo.getPurchaseBillId();
        //批注
        String comment = workFlowVo.getComment();
        //1、根据任务id查任务实例
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2、流程实例id
        String processInstanceId = task.getProcessInstanceId();
        String username = ((User) WebUtils.getSession().getAttribute("user")).getName();
        /*
         * 因为批注人是org.activiti.engine.impl.cmd.AddCommentCmd 80代码使用 String userId =
         * Authentication.getAuthenticatedUserId(); CommentEntity comment = new
         * CommentEntity(); comment.setUserId(userId);
         * Authentication这类里面使用了一个ThreadLocal的线程局部变量
         */
//        Authentication.setAuthenticatedUserId(userName);
        // 添加批注信息
//        this.taskService.addComment(taskId, processInstanceId, "[" + outcome + "]" + comment);
        //camunda没用局部线程变量，看CommandContext里getAuthenticatedUserId()的源码是通过identityService取的
        identityService.setAuthenticatedUserId(username);
        Comment comment1 = taskService.createComment(taskId, processInstanceId, "[" + outgoingName + "]" + comment);

        String userId = comment1.getUserId();

        //完成任务
        Map<String,Object> variables = new HashMap<>(16);
        //!!!!!!!!!!注意这里key写outgo,流程图里这样定义了
        variables.put("outgo",outgoingName);

        Double days = leaveBillMapper.selectByPrimaryKey(leaveBillId).getDays();
        variables.put("days",days);

        taskService.complete(taskId,variables);


        //暴力查询，因为camunda的runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 还会关联ACT_RU_AUTHORIZATION表但是已经完成任务了，ACT_RU_AUTHORIZATION这条数据已经没了，所以查到的流程实例为空
        //activiti的ACT_RU_AUTHORIZATION表关联了流程实例id,camunda没有，关联的resourcesID是act_hi_taskinst表的id，暂时想不到怎么关联查
        identityService.setAuthenticatedUserId(null);

        //判断流程是否结束
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

//        identityService.setAuthenticatedUserId(username);好像只涉及ACT_RU_AUTHORIZATION的表，这里好像可以不用再设回去因为完成任务后这条数据就没了,ACT_HI_AUTHORIZATION不知道有没有影响反正暂时不用查到这个表
//        但是如果是驳回呢，测试一下,应该没问题

        if (processInstance == null&&leaveBillId!=null) {
            //说明流程结束
            LeaveBill leaveBill = new LeaveBill();
            leaveBill.setId(leaveBillId);
            if (outgoingName.equals("取消")){
                leaveBill.setState(Constant.STATE_LEAVEBILL_THREE);
            }else {
                //审批完成
                leaveBill.setState(Constant.STATE_LEAVEBILL_TWO);
            }
            leaveBillMapper.updateByPrimaryKeySelective(leaveBill);
        }else if (processInstance == null&&purchaseBillId!=null){
            PurchaseBill purchaseBill = new PurchaseBill();
            purchaseBill.setId(purchaseBillId);
            if (outgoingName.equals("取消")){
                purchaseBill.setState(Constant.STATE_LEAVEBILL_THREE);
            }else {
                //审批完成
                purchaseBill.setState(Constant.STATE_LEAVEBILL_TWO);
            }
            purchaseBillMapper.updateById(purchaseBill);
        }
    }

    @Override
    public ProcessDefinition queryProcessDefinitionByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = processInstance.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinition;
    }

    @Override
    public Map<String, Object> queryTaskCoordinateByTaskId(String taskId) {
        Map<String,Object> coordinate = new HashMap<>();
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
        String taskDefinitionKey = task.getTaskDefinitionKey();
        // 7,使用活动ID取出xml和当前活动ID相关节点数据
//        ActivityImpl activityImpl = processDefinition.findActivity(activityId);
        ActivityImpl activityImpl = processDefinition.findActivity(taskDefinitionKey);
        // 8,从activityImpl取出坐标信息
        coordinate.put("x",activityImpl.getX());
        coordinate.put("y",activityImpl.getY());
        coordinate.put("width",activityImpl.getWidth());
        coordinate.put("height",activityImpl.getHeight());
        return coordinate;
    }

    @Override
    public DataGridView queryCommentByBillId(WorkFlowVo workFlowVo) {
        Integer leaveBillId = workFlowVo.getLeaveBillId();
        Integer purchaseBillId = workFlowVo.getPurchaseBillId();
        String businessKey = null;
        //组装businessKey
        if (leaveBillId!=null){
            businessKey = LeaveBill.class.getSimpleName()+":"+leaveBillId;
        }else if (purchaseBillId!=null){
           businessKey = PurchaseBill.class.getSimpleName()+":"+purchaseBillId;
        }
        //如果流程结束task为null
        //Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();//因为leaveBillId是唯一的
        //String taskId = task.getId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey).singleResult();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey).list().get(0);
        //通过流程实例id查
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(historicProcessInstance.getId());
        List<EnableJsonCommentEntity> data = new ArrayList<>();
        if (processInstanceComments!=null&&processInstanceComments.size()>0){
            for (Comment processInstanceComment : processInstanceComments) {
                EnableJsonCommentEntity enableJsonCommentEntity = new EnableJsonCommentEntity();
                BeanUtils.copyProperties(processInstanceComment,enableJsonCommentEntity);

                String userId = processInstanceComment.getUserId();
                //已完成的不能通过taskService去查
                //观察act_hi_taskinst表,通过办理人和执行实例id或流程实例id锁定唯一的历史任务名
                //!!!!!!!!!!!!这里注意查出来的历史实例不一定是singleResult(),驳回一次，就会有两个，驳回两次，就会有三个，但任务名是一样的
                List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                        //这里没有网关其实也可以用流程实例id查
                        .taskAssignee(userId).executionId(historicTaskInstance.getExecutionId()).list();
                //这里可以不用非空判断,还是写上吧
                if (historicTaskInstanceList!=null&&historicTaskInstanceList.size()>0){
                    enableJsonCommentEntity.setTaskName(historicTaskInstanceList.get(0).getName());
                }

                data.add(enableJsonCommentEntity);
            }
        }
        return new DataGridView(Long.valueOf(data.size()),data);
    }

    @Override
    public DataGridView queryCurrentUserHistoryTask(WorkFlowVo workFlowVo) {
        User user = (User) WebUtils.getSession().getAttribute("user");
        String assigne = user.getName();
        long count = historyService.createHistoricTaskInstanceQuery().taskAssignee(assigne).count();
        int firstResult = (workFlowVo.getPage()-1)*workFlowVo.getLimit();
        int maxResults = workFlowVo.getLimit();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskAssignee(assigne).listPage(firstResult, maxResults);
        List<ExtendHistoricTaskInstance> data = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            ExtendHistoricTaskInstance extendHistoricTaskInstance = new ExtendHistoricTaskInstance();
            BeanUtils.copyProperties(historicTaskInstance,extendHistoricTaskInstance);
            //查询对应批注

            //act_hi_comment表的task_id_关联的是act_hi_taskinst表的id
            String id = historicTaskInstance.getId();
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
            for (Comment processInstanceComment : processInstanceComments) {
                String taskId = processInstanceComment.getTaskId();
                if (taskId.equals(id)){
                    //将接口转为实现类
                    CommentEntity commentEntity = (CommentEntity) processInstanceComment;
                    extendHistoricTaskInstance.setMessage(commentEntity.getMessage());
                    break;
                }
            }

            data.add(extendHistoricTaskInstance);
        }
        return new DataGridView(count,data);
    }

}
