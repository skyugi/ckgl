package com.lzh.wms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.domain.Dept;
import com.lzh.wms.system.service.DeptService;
import com.lzh.wms.system.vo.DeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    /**
     * 加载部门左边页面的树
     * @param deptVo
     * @return
     */
    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo){
        List<Dept> list = deptService.list();
        DataGridView dataGridView = deptService.loadDeptManagerLeftTreeJson(deptVo,list);
        return dataGridView;
    }

    /**
     * 查询部门：全查询、模糊查询
     * @param deptVo
     * @return
     */
    @RequestMapping("/loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo){
        IPage<Dept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());
        QueryWrapper queryWrapper = deptService.loadAllDept(page,deptVo);
        deptService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/getDeptMaxOrderNum")
    public Map<String,Object> getDeptMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = deptService.getDeptMaxOrderNum();
        List<Dept> list = deptService.list(queryWrapper);
        if (list != null && list.size()>0) {
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 添加部门
     * @param deptVo
     * @return
     */
    @RequestMapping("/addDept")
    public ResultObj addDept(DeptVo deptVo) {
        deptVo.setCreatetime(new Date());
        try {
            deptService.save(deptVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改部门
     * @param deptVo
     * @return
     */
    @RequestMapping("/updateDept")
    public ResultObj updateDept(DeptVo deptVo) {
        try {
            deptService.updateById(deptVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 判断当前部门是否有子部门
     * @param deptVo
     * @return
     */
    @RequestMapping("/checkHasChildrenNode")
//    public Map<String,Object> checkHasChildrenNode(Integer id){
    public Map<String,Object> checkHasChildrenNode(DeptVo deptVo){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("pid",deptVo.getId());
        List<Dept> list = deptService.list(queryWrapper);
        if (list != null && list.size() > 0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /**
     * 删除部门
     * @param deptVo
     * @return
     */
    @RequestMapping("/deleteDept")
//    public ResultObj deleteDept(Integer id){
    public ResultObj deleteDept(DeptVo deptVo){
        try {
            deptService.removeById(deptVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

