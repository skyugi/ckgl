package com.lzh.wms.system.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.Constast;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.PinyinUtils;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.domain.Dept;
import com.lzh.wms.system.domain.Role;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.DeptService;
import com.lzh.wms.system.service.RoleService;
import com.lzh.wms.system.service.UserService;
import com.lzh.wms.system.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private RoleService roleService;

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

    /**
     * 删除用户--restful风格
     * @param id
     * @return
     */
    @RequestMapping("deleteUser/{id}")
    public ResultObj deleteUser(@PathVariable("id") Integer id){
        try {
            userService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 重置密码--restful风格
     * @param id
     * @return
     */
    @RequestMapping("resetPwd/{id}")
    public ResultObj resetPwd(@PathVariable("id") Integer id){
        try {
            User user = new User();
            user.setId(id);
            String salt = IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);//设置盐
            user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());//设置密码
            userService.updateById(user);
            return ResultObj.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.RESET_ERROR;
        }
    }

    /**
     * 根据用户id查询其角色并选中
     * @param id
     * @return
     */
    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id){
        //1.查询所有可用的角色
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
        List<Role> testList = roleService.list(queryWrapper);
        System.out.println("-----------------------------------------------------------------list里role对象-------------------------------------------------------------------------");
        System.out.println(testList);
        System.out.println("-----------------------------------------------------------------list里role对象-------------------------------------------------------------------------");
        List<Map<String, Object>> listMaps = roleService.listMaps(queryWrapper);
        System.out.println("-----------------------------------------------------------------list里map对象-------------------------------------------------------------------------");
        System.out.println(listMaps);
        System.out.println("-----------------------------------------------------------------list里map对象-------------------------------------------------------------------------");
        //2.查询当前用户拥有的角色id集合
        List<Integer> currentUserRoleIds = roleService.queryIdsOfRoleBelongToUserByUid(id);
//        "LAY_CHECKED": true 则数据表格选中
        List<Map<String,Object>> assembledListMaps = roleService.assembleListMaps(listMaps,currentUserRoleIds);
        return new DataGridView((long) assembledListMaps.size(),assembledListMaps);
    }

    /**
     * 为用户分配角色，保存用户和角色的关系
     * @param uid
     * @param rids
     * @return
     */
    @RequestMapping("saveUserRole")
    public ResultObj saveUserRole(Integer uid, Integer[] rids){
        try {
            userService.saveUserRole(uid,rids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }
    }

}

