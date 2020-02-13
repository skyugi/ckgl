package com.lzh.wms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Permission;
import com.lzh.wms.sys.mapper.PermissionMapper;
import com.lzh.wms.sys.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.sys.service.RoleService;
import com.lzh.wms.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-01-30
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo, List<Permission> list) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission permission : list) {
            treeNodeList.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),permission.getOpen()== Constast.OPEN_TRUE ?true:false));
        }
        return new DataGridView(treeNodeList);
    }

    @Override
    public QueryWrapper loadAllMenu(IPage<Permission> page, PermissionVo permissionVo) {
        //组装查询条件
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //点击左边的tree时查询当前节点及其子节点的数据
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        queryWrapper.eq("type",Constast.TYPE_MENU);//通过sql WHERE (id = ? OR pid = ? AND type = ?)知这里必须放上面判断的下面 解决点击左侧树权限也会显示的bug
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        return queryWrapper;
    }

    @Override
    public QueryWrapper getMenuMaxOrderNum() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("ordernum");
        return queryWrapper;
    }

    @Override
    public boolean removeById(Serializable id) {
        PermissionMapper permissionMapper = this.getBaseMapper();
        //根据权限或菜单id删除角色权限关系表sys_role_permission的数据
        permissionMapper.deleteRolePermissionById(id);
        //删除权限表sys_permission的数据
        return super.removeById(id);
    }
    
    /********************权限开始***************************/

    @Override
    public QueryWrapper loadAllPermission(IPage<Permission> page, PermissionVo permissionVo) {
        //组装查询条件
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",Constast.TYPE_PERMISSION);//只能查询权限
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "percode", permissionVo.getPercode());
        //点击左边的tree时查询当前节点及其子节点的数据
        queryWrapper.eq(permissionVo.getId()!=null,"pid",permissionVo.getId());//权限只有一级 通过判断其pid是否=页面传来的菜单id即可
        queryWrapper.orderByAsc("ordernum");
        return queryWrapper;
    }

    @Override
    public QueryWrapper getPermissionMaxOrderNum() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("ordernum");
        return queryWrapper;
    }

    @Override
    public Set<Integer> queryPermissionIdsByRoleIds(List<Integer> currentUserRoleIds, RoleService roleService, Set<Integer> pids) {
        if (currentUserRoleIds!=null&&currentUserRoleIds.size()>0){
            for (Integer currentUserRoleId : currentUserRoleIds) {
                if (currentUserRoleId!=null){
                    List<Integer> permissionIds = roleService.queryIdsOfPermissionBelongToRoleByRid(currentUserRoleId);
                    pids.addAll(permissionIds);
                }
            }
        }
        return pids;
    }
}
