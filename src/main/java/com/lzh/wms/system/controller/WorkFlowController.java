package com.lzh.wms.system.controller;

import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.service.WorkFlowService;
import com.lzh.wms.system.vo.WorkFlowVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> addWorkFlow(MultipartFile mf, WorkFlowVo workFlowVo){
        Map<String,Object> map=new HashMap<String, Object>(16);
        try {
            this.workFlowService.addWorkFlow(mf.getInputStream(),workFlowVo.getDeploymentName());
            map.put("msg", "部署成功");
        } catch (Exception e) {
            map.put("msg", "部署失败");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 删除流程
     * @param workFlowVo
     * @return
     */
    @RequestMapping("deleteWorkFlow")
    public ResultObj deleteWorkFlow(WorkFlowVo workFlowVo){
        try {
            workFlowService.deleteWorkFlow(workFlowVo.getDeploymentId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除流程
     * @param workFlowVo
     * @return
     */
    @RequestMapping("batchDeleteWorkFlow")
    public ResultObj batchDeleteWorkFlow(WorkFlowVo workFlowVo){
        try {
            String[] deploymentIds = workFlowVo.getIds();
            for (String deploymentId : deploymentIds) {
                workFlowService.deleteWorkFlow(deploymentId);
            }
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }



    /**
     * 将流程图输出在页面上
     * @param deploymentId
     * @param response
     */
    @RequestMapping("viewFlowDiagram")
    public void viewFlowDiagram(String deploymentId, HttpServletResponse response){
        InputStream inputStream = workFlowService.queryFlowDiagram(deploymentId);
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage,"PNG",outputStream);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动流程
     * @param leaveBillId
     * @param type
     * @return
     */
    @RequestMapping("startProcess")
    public ResultObj startProcess(Integer leaveBillId,String type){
        try {
            workFlowService.startProcess(leaveBillId,type);
            return ResultObj.START_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.START_ERROR;
        }
    }

}
