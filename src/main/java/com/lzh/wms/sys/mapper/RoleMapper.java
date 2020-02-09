package com.lzh.wms.sys.mapper;

import com.lzh.wms.sys.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-02-09
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色ID删除sys_role_permission中间表数据
     * @param id
     */
    void deleteRolePermissionByRid(Serializable id);

    /**
     * 根据角色ID删除sys_role_user中间表数据
     * @param id
     */
    void deleteRoleUserByRid(Serializable id);
}
