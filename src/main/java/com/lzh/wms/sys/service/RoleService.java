package com.lzh.wms.sys.service;

import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据用户id查询当前用户拥有的所有的角色id
     * @param id
     * @return
     */
    List<Integer> queryIdsOfRoleBelongToUserByUid(Integer id);

    /**
     * 组装map对象，提供前端数据表格选中所需字段LAY_CHECKED,true代表选中
     * @param listMaps
     * @param currentUserRoleIds
     * @return
     */
    List<Map<String, Object>> assembleListMaps(List<Map<String, Object>> listMaps, List<Integer> currentUserRoleIds);
}
