package com.lzh.wms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.TreeNode;
import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.mapper.DeptMapper;
import com.lzh.wms.sys.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.sys.vo.DeptVo;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-06
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public Dept getOne(Wrapper<Dept> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    public boolean update(Dept entity, Wrapper<Dept> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }


    @Override
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo, List<Dept> list) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dept dept : list) {
            treeNodeList.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),dept.getOpen()== Constast.OPEN_TRUE ?true:false));
        }
        return new DataGridView(treeNodeList);
    }
}
