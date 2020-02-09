package com.lzh.wms.sys.service.impl;

import com.lzh.wms.sys.domain.Role;
import com.lzh.wms.sys.mapper.RoleMapper;
import com.lzh.wms.sys.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2020-02-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public boolean removeById(Serializable id) {
        //根据角色ID删除sys_role_permission中间表数据
        this.getBaseMapper().deleteRolePermissionByRid(id);
        //根据角色ID删除sys_role_user中间表数据
        this.getBaseMapper().deleteRoleUserByRid(id);
        return super.removeById(id);
    }
}
