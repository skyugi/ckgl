package com.lzh.wms.system.controller;

import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.service.WorkFlowService;
import com.lzh.wms.system.vo.WorkFlowVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzh
 * @date 2020-03-18 21:57
 */
@RestController
@RequestMapping("/workFlow")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;

    /**
     * 查询流程部署
     * @param workFlowVo
     * @return
     */
    @RequestMapping("loadAllDeployment")
    public DataGridView loadAllDeployment(WorkFlowVo workFlowVo){
        return workFlowService.queryAllProcessDeployment(workFlowVo);
    }

    /**
     * 查询流程定义
     * @param workFlowVo
     * @return
     */
    @RequestMapping("loadAllProcessDefinition")
    public DataGridView loadAllProcessDefinition(WorkFlowVo workFlowVo){
        return workFlowService.queryAllProcessDefinition(workFlowVo);
    }

    /**
     * 添加流程部署
     */
    @RequestMapping("addWorkFlow")
    public void addWorkFlow(){
//        Map<String,Object> map=new HashMap<String, Object>();
        try {
            this.workFlowService.addWorkFlow();
//            map.put("msg", "部署成功");
        } catch (Exception e) {
//            map.put("msg", "部署失败");
            e.printStackTrace();
        }
//        return map;
    }

}
