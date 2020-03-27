package com.lzh.wms.system.vo;

import com.lzh.wms.system.domain.LeaveBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 *
 * @author lzh
 * @date 2020-03-21
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkFlowVo {

    private static final long serialVersionUID=1L;

    private Integer page;
    private Integer limit;

    /**
     * 接收多个id
     * 这里是多个流程部署的id，故用String
     */
    private String[] ids;
    /**
     * 流程部署名称
     */
    private String deploymentName;
    /**
     * 流程部署id
     */
    private String deploymentId;
    /**
     * 请假单id
     */
    private Integer leaveBillId;
    /**
     * 任务id
      */
    private String taskId;

    /**
     * 连线名
     */
    private String outgoingName;

    /**
     * 批注
     */
    private String comment;
}
