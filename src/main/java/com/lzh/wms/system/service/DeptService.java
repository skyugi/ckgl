package com.lzh.wms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.system.vo.DeptVo;

import java.util.List;

/**
 * <p>
 *  部门服务类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-06
 */
public interface DeptService extends IService<Dept> {

    /**
     *返回DataGridView，加载左边数据
     * @param deptVo
     * @param list
     * @return
     */
    DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo, List<Dept> list);

    /**
     * 构造queryWrapper对象
     * @param page
     * @param deptVo
     * @return
     */
    QueryWrapper loadAllDept(IPage<Dept> page, DeptVo deptVo);

    /**
     * 返回最大排序码
     * @return
     */
    QueryWrapper getDeptMaxOrderNum();
}
