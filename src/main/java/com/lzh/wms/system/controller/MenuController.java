package com.lzh.wms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.*;
import com.lzh.wms.system.domain.Permission;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.PermissionService;
import com.lzh.wms.system.service.RoleService;
import com.lzh.wms.system.vo.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 菜单前端控制器
 *
 * @author lzh
 * @date 2020-01-30 21:57
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permisssionvo) {
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //设置只能查询菜单
        queryWrapper.eq("type", Constant.TYPE_MENU);
        queryWrapper.eq("available", Constant.AVAILABLE_TRUE);

        //fixme 根据用户角色查询菜单 表现层还能再简化吗
        User user = (User) WebUtils.getSession().getAttribute("user");
        List<Permission> list = null;
        if (user.getType() == Constant.USER_TYPE_SUPER) {
            list = permissionService.list(queryWrapper);
        } else {//todo 没用连表查询
            //根据用户id+角色+权限去查询
            Integer userId = user.getId();
            //根据用户id查询角色id
            List<Integer> currentUserRoleIds = roleService.queryIdsOfRoleBelongToUserByUid(userId);
            //根据角色id查询菜单、权限id
            Set<Integer> pids = new HashSet<>();
            pids = permissionService.queryPermissionIdsByRoleIds(currentUserRoleIds,roleService,pids);
            //根据菜单、权限id查询相应菜单、权限
            if (pids.size()>0) {
                queryWrapper.in("id",pids);
                list = permissionService.list(queryWrapper);
            }else {
                list = new ArrayList<>();
            }
        }
//        System.out.println(list);

         //构造层级关系,两级菜单
        List<TreeNode> assembledTreeNodeList = new ArrayList<>();
        /*List<TreeNode> list1 = new ArrayList<>();
        for (Permission permission : list) {
            if (permission.getPid() == 0 || permission.getPid() != 1) {
                continue;
            }
            //一级菜单节点
            TreeNode treeNode = new TreeNode(permission.getId(), permission.getPid(),
                    permission.getTitle(), permission.getIcon(), permission.getHref(), permission.getOpen() == Constast.OPEN_TRUE ? true : false);
            //创建children节点集合
            List<TreeNode> children = new ArrayList<>();
            for (Permission permission1 : list) {
                if (permission1.getPid() == permission.getId()) {
                    TreeNode childrenSingle = new TreeNode(permission1.getId(), permission1.getPid(),
                            permission1.getTitle(), permission1.getIcon(), permission1.getHref(), permission1.getOpen() == 1 ? true : false);
                    //添加children节点集合 元素
                    children.add(childrenSingle);
                }
            }
            treeNode.setChildren(children);
            list1.add(treeNode);
        }

        System.out.println(new DataGridView(list));
        return new DataGridView(list1);*/
        assembledTreeNodeList = TreeBuilderUtils.assembleTreeNode(list, assembledTreeNodeList);
        return new DataGridView(assembledTreeNodeList);
    }

    /**********************菜单管理开始******************************/
    /**
     * 加载菜单左边页面的树
     * @param permissionVo
     * @return
     */
    @RequestMapping("/loadMenuManagerLeftTreeJson")
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", Constant.TYPE_MENU);
        List<Permission> list = permissionService.list(queryWrapper);
        DataGridView dataGridView = permissionService.loadMenuManagerLeftTreeJson(permissionVo,list);
        return dataGridView;
    }

    /**
     * 查询菜单：全查询、模糊查询
     * @param permissionVo
     * @return
     */
    @RequestMapping("/loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper queryWrapper = permissionService.loadAllMenu(page,permissionVo);
        permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/getMenuMaxOrderNum")
    public Map<String,Object> getMenuMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = permissionService.getMenuMaxOrderNum();
        List<Permission> list = permissionService.list(queryWrapper);
        if (list != null && list.size()>0) {
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 添加菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("/addMenu")
    public ResultObj addMenu(PermissionVo permissionVo) {
        permissionVo.setType(Constant.TYPE_MENU);//设置添加类型为菜单
        try {
            permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("/updateMenu")
    public ResultObj updateMenu(PermissionVo permissionVo) {
        try {
            permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 判断当前菜单是否有子菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("/checkHasChildrenNode")
//    public Map<String,Object> checkHasChildrenNode(Integer id){
    public Map<String,Object> checkHasChildrenNode(PermissionVo permissionVo){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("pid",permissionVo.getId());
        List<Permission> list = permissionService.list(queryWrapper);
        if (list != null && list.size() > 0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /**
     * 删除菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("/deleteMenu")
//    public ResultObj deleteMenu(Integer id){
    public ResultObj deleteMenu(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    /**********************菜单管理结束******************************/
}
