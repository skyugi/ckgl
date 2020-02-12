package com.lzh.wms.sys.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.PinyinUtils;
import com.lzh.wms.sys.common.ResultObj;
import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.DeptService;
import com.lzh.wms.sys.service.UserService;
import com.lzh.wms.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (mgr != null && mgr != 0) {//若前端没选直属领导则mgr默认传进去的值是0
                User userManager = userService.getById(mgr);
                user.setLeadername(userManager.getName());
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/getUserMaxOrderNum")
    public Map<String,Object> getUserMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = userService.getDeptMaxOrderNum();
        List<User> list = userService.list(queryWrapper);
        if (list != null && list.size()>0) {
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 根据部门id查询当前部门下面的领导用户
     * @param deptid
     * @return
     */
    @RequestMapping("/loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(deptid!=null,"deptid",deptid);
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        queryWrapper.eq("type",Constast.USER_TYPE_NORMAL);
        List<User> list = userService.list(queryWrapper);
        return new DataGridView(list);
    }

    /**
     * 将汉字转为拼音，首字母不大写
     * @param username
     * @return
     */
    @RequestMapping("/changChineseToPinYin")
    public Map<String,Object> changChineseToPinYin(String username){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(username)){
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value","");
        }
        return map;
    }

    /**
     * 添加用户
     * @param userVo
     * @return
     */
    @RequestMapping("/addUser")
    public ResultObj addUser(UserVo userVo){
        try {
            userVo.setType(Constast.USER_TYPE_NORMAL);
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);//设置盐
            userVo.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());//设置密码
            userService.save(userVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 根据用户领导id查询当前部门下面的领导用户
     * @param id
     * @return
     */
    @RequestMapping("/loadUserById")
    public DataGridView loadUserById(Integer id){
        User user = userService.getById(id);
        return new DataGridView(user);
    }

    /**
     * 修改用户
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public ResultObj updateUser(UserVo userVo){
        try {
            userService.updateById(userVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

}

