package com.lzh.wms.system.vo.camunda;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * historicTaskInstance类的扩展类
 * 增加任务对应的唯一批注信息
 * @author lzh
 * @date 2020-03-28 1:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExtendHistoricTaskInstance {
    private String name;
    private Date startTime;
    private Date endTime;
    private String assignee;

    /**
     * 任务对应的唯一批注信息
     */
    private String message;
}
