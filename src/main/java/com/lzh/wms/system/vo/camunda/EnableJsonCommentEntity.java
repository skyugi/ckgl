package com.lzh.wms.system.vo.camunda;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 批注类转换类
 * @author lzh
 * @date 2020-03-25 22:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnableJsonCommentEntity {

    protected String userId;
    protected Date time;
    protected String taskId;
    protected String message;
    protected String fullMessage;

    /**
     * 对应act_ru_task的_name字段
     * 自己扩展的字段，根据上面的taskId查出task后赋值，前端显示时显示任务节点的名称
     */
    protected String taskName;

}
