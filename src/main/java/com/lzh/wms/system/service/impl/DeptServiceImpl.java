package com.lzh.wms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.TreeNode;
import com.lzh.wms.system.domain.Dept;
import com.lzh.wms.system.mapper.DeptMapper;
import com.lzh.wms.system.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.system.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public boolean save(Dept entity) {
        return super.save(entity);
    }

    @Override
    public Dept getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean updateById(Dept entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }


    @Override
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo, List<Dept> list) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dept dept : list) {
            treeNodeList.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(), dept.getOpen().equals(Constant.OPEN_TRUE) ?true:false));
        }
        return new DataGridView(treeNodeList);
    }

    @Override
    public QueryWrapper loadAllDept(IPage<Dept> page, DeptVo deptVo) {
        //组装查询条件
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()), "title", deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()), "address", deptVo.getAddress());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()), "remark", deptVo.getRemark());
        //点击左边的tree时查询当前节点及其子节点的数据
        queryWrapper.eq(deptVo.getId()!=null,"id",deptVo.getId()).or().eq(deptVo.getId()!=null,"pid",deptVo.getId());
        queryWrapper.orderByAsc("ordernum");
        return queryWrapper;
    }

    @Override
    public QueryWrapper getDeptMaxOrderNum() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("ordernum");
        return queryWrapper;
    }

}
