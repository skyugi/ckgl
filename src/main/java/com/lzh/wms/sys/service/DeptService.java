package com.lzh.wms.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.sys.vo.DeptVo;

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

}
