package com.lzh.wms.sys.mapper;

import com.lzh.wms.sys.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-01-30
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限或菜单id删除角色权限关系表sys_role_permission的数据
     * @param id
     */
    void deleteRolePermissionById(@Param("id") Serializable id);
}
