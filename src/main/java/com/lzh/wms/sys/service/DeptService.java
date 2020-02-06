package com.lzh.wms.sys.service;

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
     *返回DataGridView
     * @param deptVo
     * @param list
     * @return
     */
    DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo, List<Dept> list);
}
