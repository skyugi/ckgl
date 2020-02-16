package com.lzh.wms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.domain.Permission;
import com.lzh.wms.system.service.PermissionService;
import com.lzh.wms.system.vo.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-01-30
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;

    /**
     * 查询权限：全查询、模糊查询
     * @param permissionVo
     * @return
     */
    @RequestMapping("/loadAllPermission")
    public DataGridView loadAllPermission(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper queryWrapper = permissionService.loadAllPermission(page,permissionVo);
        permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/getPermissionMaxOrderNum")
    public Map<String,Object> getPermissionMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = permissionService.getPermissionMaxOrderNum();
        List<Permission> list = permissionService.list(queryWrapper);
        if (list != null && list.size()>0) {
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 添加权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("/addPermission")
    public ResultObj addPermission(PermissionVo permissionVo) {
        permissionVo.setType(Constant.TYPE_PERMISSION);//设置添加类型为权限
        try {
            permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("/updatePermission")
    public ResultObj updatePermission(PermissionVo permissionVo) {
        try {
            permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("/deletePermission")
//    public ResultObj deletePermission(Integer id){
    public ResultObj deletePermission(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

