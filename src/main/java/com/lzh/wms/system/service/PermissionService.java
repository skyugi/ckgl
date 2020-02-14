package com.lzh.wms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.system.vo.PermissionVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-01-30
 */
public interface PermissionService extends IService<Permission> {

    /**
     *返回DataGridView，加载左边数据
     * @param permissionVo
     * @param list
     * @return
     */
    DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo, List<Permission> list);

    /**
     * 构造queryWrapper对象
     * @param page
     * @param permissionVo
     * @return
     */
    QueryWrapper loadAllMenu(IPage<Permission> page, PermissionVo permissionVo);

    /**
     * 返回最大排序码
     * @return
     */
    QueryWrapper getMenuMaxOrderNum();

    /********************权限***************************/

    /**
     * 构造queryWrapper对象
     * @param page
     * @param permissionVo
     * @return
     */
    QueryWrapper loadAllPermission(IPage<Permission> page, PermissionVo permissionVo);

    /**
     * 返回最大排序码
     * @return
     */
    QueryWrapper getPermissionMaxOrderNum();

    /**
     * 根据登录用户的角色查询其权限，返回权限id
     * @return
     * @param currentUserRoleIds
     * @param roleService
     * @param pids
     */
    Set<Integer> queryPermissionIdsByRoleIds(List<Integer> currentUserRoleIds, RoleService roleService, Set<Integer> pids);
}
