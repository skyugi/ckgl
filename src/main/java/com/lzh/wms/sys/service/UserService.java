package com.lzh.wms.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzh.wms.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.wms.sys.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘样
 * @since 2019-12-29
 */
public interface UserService extends IService<User> {

    /**
     * 构造queryWrapper对象
     * @param page
     * @param userVo
     * @return
     */
    QueryWrapper loadAllUser(IPage<User> page, UserVo userVo);

    /**
     * 返回最大排序码
     * @return
     */
    QueryWrapper getDeptMaxOrderNum();

    /**
     * 为用户分配角色，保存用户和角色的关系
     * @param uid
     * @param rids
     */
    void saveUserRole(Integer uid, Integer[] rids);
}
