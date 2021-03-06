package com.lzh.wms.system.mapper;

import com.lzh.wms.system.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 根据角色id查询当前角色拥有的所有的权限或菜单id
     * @param roleId
     * @return
     */
    List<Integer> queryIdsOfPermissionBelongToRoleByRid(Integer roleId);

    /**
     * 为角色分配菜单权限
     * @param rid
     * @param pid
     */
    void saveRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    /**
     * 根据用户id删除用户角色中间表的数据
     * @param id
     */
    void deleteRoleUserByUid(Serializable id);

    /**
     * 根据用户id查询当前用户拥有的所有的角色id
     * @param id
     * @return
     */
    List<Integer> queryIdsOfRoleBelongToUserByUid(Integer id);

    /**
     * 为用户分配角色
     * @param uid
     * @param rid
     */
    void saveUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);
}
