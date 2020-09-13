package com.lzh.wms.system.vo.camunda;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 任务类转换类
 * @author lzh
 * @date 2020-03-23 22:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnableJsonTaskEntity {
    protected String id;
    protected String assignee;
    protected String name;
    protected Date createTime; // The time when the task has been created
}
