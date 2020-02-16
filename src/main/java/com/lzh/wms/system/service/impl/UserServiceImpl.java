package com.lzh.wms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.mapper.RoleMapper;
import com.lzh.wms.system.mapper.UserMapper;
import com.lzh.wms.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.wms.system.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘样
 * @since 2019-12-29
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        //根据用户id删除用户角色中间表的数据
        roleMapper.deleteRoleUserByUid(id);
        //删除用户头[如果是默认头像不删除  否则删除]
        return super.removeById(id);
    }

    @Override
    public QueryWrapper loadAllUser(IPage<User> page, UserVo userVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()), "loginname", userVo.getName()).or().eq(StringUtils.isNotBlank(userVo.getName()), "name", userVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()), "address", userVo.getAddress());
        queryWrapper.eq("type", Constant.USER_TYPE_NORMAL);//查询系统用户
        queryWrapper.eq(userVo.getDeptid()!=null, "deptid",userVo.getDeptid());
        return queryWrapper;
    }

    @Override
    public QueryWrapper getDeptMaxOrderNum() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("ordernum");
        return queryWrapper;
    }

    @Override
    public void saveUserRole(Integer uid, Integer[] rids) {
        //先删除sys_role_user表的数据
        roleMapper.deleteRoleUserByUid(uid);
        if (rids!=null&&rids.length>0){
            for (Integer rid : rids) {
                roleMapper.saveUserRole(uid,rid);
            }
        }
    }
}
