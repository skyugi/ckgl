package com.lzh.wms.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.sys.common.*;
import com.lzh.wms.sys.domain.Permission;
import com.lzh.wms.sys.domain.Role;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.PermissionService;
import com.lzh.wms.sys.service.RoleService;
import com.lzh.wms.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  角色管理前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-09
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private PermissionService permissionService;

    /**
     * 查询角色：全查询、模糊查询
     * @param roleVo
     * @return
     */
    @RequestMapping("/loadAllRole")
    public DataGridView loadAllRole(RoleVo roleVo){
        IPage<Role> page = new Page<>(roleVo.getPage(),roleVo.getLimit());
        //组装查询条件
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark());
//        queryWrapper.eq(roleVo.getAvailable()!=null,"available",roleVo.getAvailable());
        queryWrapper.orderByDesc("createtime");
        roleService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加角色
     * @param roleVo
     * @return
     */
    @RequestMapping("/addRole")
    public ResultObj addRole(RoleVo roleVo){
        try {
            roleVo.setCreatetime(new Date());
            roleService.save(roleVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改角色
     * @param roleVo
     * @return
     */
    @RequestMapping("/updateRole")
    public ResultObj updateRole(RoleVo roleVo){
        try {
            roleService.updateById(roleVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除角色信息
     * @param id
     * @return
     */
    @RequestMapping("deleteRole")
    public ResultObj deleteRole(Integer id){
        try {
            roleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据角色ID加载rolePermissionTree的json数据
     * @param roleId
     * @return
     */
    @RequestMapping("/initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer roleId){
        List<TreeNode> treeNodeList = roleService.initPermissionByRoleId(roleId,roleService);
        return new DataGridView(treeNodeList);
    }

    /**
     * 为角色分配菜单权限
     * @param rid
     * @param pids
     * @return
     */
    @RequestMapping("saveRolePermission")
    public ResultObj saveRolePermission(Integer rid, Integer[] pids){
        try {
            roleService.saveRolePermission(rid,pids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }
    }
}

