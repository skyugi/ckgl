package com.lzh.wms.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.DeptService;
import com.lzh.wms.sys.service.UserService;
import com.lzh.wms.sys.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2019-12-29
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 用户查询：全查询、模糊查询
     * @param userVo
     * @return
     */
    @RequestMapping("/loadAllUsers")
    public DataGridView loadAllUsers(UserVo userVo){
        IPage<User> page = new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper queryWrapper = userService.loadAllUser(page,userVo);
        userService.page(page, queryWrapper);
        //为user额外属性赋值
        List<User> records = page.getRecords();
        for (User user : records){
            Integer deptid = user.getDeptid();
            if (deptid != null) {
                Dept dept = deptService.getById(deptid);
                user.setDeptname(dept.getTitle());
            }
            Integer mgr = user.getMgr();
            if (mgr != null) {
                User userManager = userService.getById(mgr);
                user.setLeadername(userManager.getName());
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

}

