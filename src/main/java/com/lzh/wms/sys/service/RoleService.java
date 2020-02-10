package com.lzh.wms.sys.service;

import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-09
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色ID加载rolePermissionTree的json数据
     * @param roleId
     * @param roleService
     * @return
     */
    List<TreeNode> initPermissionByRoleId(Integer roleId, RoleService roleService);

    /**
     * 根据角色id查询当前角色拥有的所有的权限或菜单id
     * @param roleId
     * @return
     */
    List<Integer> queryIdsOfPermissionBelongToRoleByRid(Integer roleId);

    /**
     * 为角色分配菜单权限
     * @param rid
     * @param pids
     */
    void saveRolePermission(Integer rid, Integer[] pids);
}
