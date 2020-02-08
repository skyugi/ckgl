package com.lzh.wms.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.sys.vo.PermissionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-01-30
 */
public interface PermissionService extends IService<Permission> {

    /**
     *返回DataGridView，加载左边数据
     * @param permissionVo
     * @param list
     * @return
     */
    DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo, List<Permission> list);

    /**
     * 构造queryWrapper对象
     * @param page
     * @param permissionVo
     * @return
     */
    QueryWrapper loadAllMenu(IPage<Permission> page, PermissionVo permissionVo);

    /**
     * 返回最大排序码
     * @return
     */
    QueryWrapper getMenuMaxOrderNum();

}
