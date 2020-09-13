package com.lzh.wms.system.vo.camunda;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity;

import java.util.Date;
import java.util.Map;

/**
 * 流程部署转换类
 * 解决 HttpMessageNotWritableException: Could not write JSON
 * @author lzh
 * @date 2020-03-21 23:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnableJsonDeploymentEntity {
    protected String id;
    protected String name;
    protected Date deploymentTime;
    protected String source;
}
