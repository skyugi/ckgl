package com.lzh.wms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.system.common.Constant;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import com.lzh.wms.system.common.WebUtils;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.service.LeaveBillService;
import com.lzh.wms.system.vo.LeaveBillVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 *  请假单前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-03-21
 */
@RestController
@RequestMapping("/leaveBill")
public class LeaveBillController {
    @Autowired
    private LeaveBillService leaveBillService;

    /**
     * 查询请假单：全查询、模糊查询
     * @param leaveBillVo
     * @return
     */
    @RequestMapping("/loadAllLeaveBill")
    public DataGridView loadAllLeaveBill(LeaveBillVo leaveBillVo){
        User user = (User) WebUtils.getSession().getAttribute("user");
        //设置非超级管理员只能查看自己的请假单
        if (!user.getType().equals(Constant.USER_TYPE_SUPER)){
            leaveBillVo.setUserid(user.getId());
        }
        IPage<LeaveBill> page = new Page<>(leaveBillVo.getPage(),leaveBillVo.getLimit());
        QueryWrapper<LeaveBill> queryWrapper = new QueryWrapper<>();
        //组装查询条件
        queryWrapper.like(leaveBillVo.getUserid()!=null,"userid",leaveBillVo.getUserid());
        queryWrapper.like(StringUtils.isNotBlank(leaveBillVo.getTitle()),"title",leaveBillVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(leaveBillVo.getReason()),"reason",leaveBillVo.getReason());
        //数据库数据大于开始时间
        queryWrapper.ge(leaveBillVo.getStartTime()!=null,"leavetime",leaveBillVo.getStartTime());
        //小于
        queryWrapper.le(leaveBillVo.getEndTime()!=null,"leavetime",leaveBillVo.getEndTime());
        queryWrapper.orderByDesc("leavetime");
        leaveBillService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加请假单
     * @param leaveBillVo
     * @return
     */
    @RequestMapping("/addLeaveBill")
    public ResultObj addLeaveBill(LeaveBillVo leaveBillVo){
        try {
            leaveBillService.save(leaveBillVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改请假单
     * @param leaveBillVo
     * @return
     */
    @RequestMapping("/updateLeaveBill")
    public ResultObj updateLeaveBill(LeaveBillVo leaveBillVo){
        try {
            leaveBillService.updateById(leaveBillVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除请假单信息
     * @param leaveBillVo
     * @return
     */
    @RequestMapping("/batchDeleteLeaveBill")
    public ResultObj batchDeleteLeaveBill(LeaveBillVo leaveBillVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : leaveBillVo.getIds()) {
                idList.add(id);
            }
            leaveBillService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除请假单信息
     * @param id
     * @return
     */
    @RequestMapping("deleteLeaveBill")
    public ResultObj deleteLeaveBill(Integer id){
        try {
            leaveBillService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

