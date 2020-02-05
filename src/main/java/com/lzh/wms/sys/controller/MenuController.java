package com.lzh.wms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.common.WebUtils;
import com.lzh.wms.sys.domain.Permission;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.PermissionService;
import com.lzh.wms.sys.vo.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permisssionvo) {
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //设置只能查询菜单
        queryWrapper.eq("type", Constast.TYPE_MENU);
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);

        //根据用户角色查询菜单
        User user = (User) WebUtils.getSession().getAttribute("user");
        List<Permission> list = null;
        if (user.getType() == Constast.USER_TYPE_SUPER) {
            list = permissionService.list(queryWrapper);
        } else {
            //根据用户id+角色+权限去查询
            list = permissionService.list(queryWrapper);
        }
//        System.out.println(list);
        /**
         * 构造层级关系,三级菜单
         */
        List<TreeNode> list1 = new ArrayList<>();

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


        return new DataGridView(list1);
    }
}
