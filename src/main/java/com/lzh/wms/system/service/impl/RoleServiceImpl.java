package com.lzh.wms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzh.wms.system.common.Constast;
import com.lzh.wms.system.common.TreeNode;
import com.lzh.wms.system.domain.Permission;
import com.lzh.wms.system.domain.Role;
import com.lzh.wms.system.mapper.RoleMapper;
import com.lzh.wms.system.service.PermissionService;
import com.lzh.wms.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-09
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

//    @Autowired
//    private RoleService roleService;//不能@Autowired

//    public void setRoleService(RoleService roleService) {
//        this.roleService = (RoleServiceImpl)roleService;
//    }

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean removeById(Serializable id) {
        //根据角色ID删除sys_role_permission中间表数据
        this.getBaseMapper().deleteRolePermissionByRid(id);
        //根据角色ID删除sys_role_user中间表数据
        this.getBaseMapper().deleteRoleUserByRid(id);
        return super.removeById(id);
    }

    @Override
    public List<TreeNode> initPermissionByRoleId(Integer roleId, RoleService roleService) {
        //查询所有可用的菜单和权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Permission> allAvailablePermissions = permissionService.list(queryWrapper);
        //根据角色ID查询当前角色拥有的权限和菜单//todo 目前不采用连表查询
        //1.根据角色id查询当前角色拥有的所有的权限或菜单id
        List<Integer> currentRolePermissionIds = roleService.queryIdsOfPermissionBelongToRoleByRid(roleId);//不能用this.roleService
        //2.根据查询出来的权限或菜单id查询权限和菜单数据
        List<Permission> currentRolePermissions = null;
        if (currentRolePermissionIds!=null && currentRolePermissionIds.size()>0){
            queryWrapper.in("id",currentRolePermissionIds);
            currentRolePermissions = permissionService.list(queryWrapper);
        }else {
            //不管是增强for循环还是普通for循环都是需要判断是否为null的
            currentRolePermissions = new ArrayList<>();
        }
        //构造返回DataGridView的data
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission allAvailablePermission : allAvailablePermissions) {
            String checkArr = "0";
            for (Permission currentRolePermission : currentRolePermissions) {
                if (currentRolePermission.equals(allAvailablePermission)){
                    checkArr = "1";
                    break;
                }
            }
            //应该返回全部的菜单和权限
            Boolean spread = (allAvailablePermission.getOpen()==null||allAvailablePermission.getOpen()==0)?false:true;
            treeNodeList.add(new TreeNode(allAvailablePermission.getId(), allAvailablePermission.getPid(),
                    allAvailablePermission.getTitle(), spread, checkArr));
        }
        return treeNodeList;
    }

    @Override
    public List<Integer> queryIdsOfPermissionBelongToRoleByRid(Integer roleId) {
        return this.getBaseMapper().queryIdsOfPermissionBelongToRoleByRid(roleId);
    }

    @Override
    public void saveRolePermission(Integer rid, Integer[] pids) {
        //由于sys_role_permission是双主键，先删除该角色所有菜单权限
        this.getBaseMapper().deleteRolePermissionByRid(rid);
        if (pids != null && pids.length > 0) {
            for (Integer pId : pids){
                this.getBaseMapper().saveRolePermission(rid,pId); //todo 了解mybatis xml批处理
            }
        }
    }

    @Override
    public List<Integer> queryIdsOfRoleBelongToUserByUid(Integer id) {
        return this.getBaseMapper().queryIdsOfRoleBelongToUserByUid(id);
    }

    @Override
    public List<Map<String, Object>> assembleListMaps(List<Map<String, Object>> listMaps, List<Integer> currentUserRoleIds) {
        //fixme
        if (listMaps!=null&&listMaps.size()>0){
            for (Map<String, Object> map : listMaps) {
                if (map != null && map.get("id") != null) {
                    if (currentUserRoleIds!=null&&currentUserRoleIds.size()>0){
                        for (Integer currentUserRoleId : currentUserRoleIds) {
                            if (currentUserRoleId != null) {
                                if (currentUserRoleId.equals(map.get("id"))){
                                    map.put("LAY_CHECKED",true);
                                }
                            }
                        }
                    }
                }
            }
        }
        return listMaps;
    }
}
