package com.lzh.wms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.domain.Permission;
import com.lzh.wms.sys.mapper.PermissionMapper;
import com.lzh.wms.sys.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        queryWrapper.eq("type",Constast.TYPE_MENU);
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        //点击左边的tree时查询当前节点及其子节点的数据
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
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
}
