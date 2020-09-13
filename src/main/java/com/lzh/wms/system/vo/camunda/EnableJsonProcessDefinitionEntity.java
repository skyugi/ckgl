package com.lzh.wms.system.vo.camunda;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.impl.form.handler.StartFormHandler;
import org.camunda.bpm.engine.impl.persistence.entity.IdentityLinkEntity;
import org.camunda.bpm.engine.impl.persistence.entity.SuspensionState;
import org.camunda.bpm.engine.impl.task.TaskDefinition;

import java.util.*;

/**
 * 流程定义转换类
 * @author lzh
 * @date 2020-03-21 23:49
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnableJsonProcessDefinitionEntity {
    protected String id;
    protected String name;
    protected String key;
//    protected int version;
    protected Integer version;
//    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected String diagramResourceName;

}
