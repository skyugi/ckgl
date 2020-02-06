package com.lzh.wms.sys.controller;


import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.service.DeptService;
import com.lzh.wms.sys.vo.DeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  部门管理前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-06
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo){
        List<Dept> list = deptService.list();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dept dept : list) {
            treeNodeList.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),dept.getOpen()== Constast.OPEN_TRUE ?true:false));
        }
        return new DataGridView(treeNodeList);
    }
}
