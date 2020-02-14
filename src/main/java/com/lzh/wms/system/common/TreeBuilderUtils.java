package com.lzh.wms.system.common;

import com.lzh.wms.system.domain.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造层级关系,两级菜单
 * 
 * @author lzh
 * @date 2020-02-14 0:16
 */
public class TreeBuilderUtils {
    
    public static List<TreeNode> assembleTreeNode(List<Permission> list,List<TreeNode> assembledTreeNodeList){
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
            assembledTreeNodeList.add(treeNode);
        }
        return assembledTreeNodeList;
    }
}
