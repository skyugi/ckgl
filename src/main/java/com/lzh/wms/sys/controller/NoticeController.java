package com.lzh.wms.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.wms.sys.common.DataGridView;
import com.lzh.wms.sys.common.ResultObj;
import com.lzh.wms.sys.common.WebUtils;
import com.lzh.wms.sys.domain.LogInfo;
import com.lzh.wms.sys.domain.Notice;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.NoticeService;
import com.lzh.wms.sys.vo.LogInfoVo;
import com.lzh.wms.sys.vo.NoticeVo;
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
 *  公告前端控制器
 * </p>
 *
 * @author 刘样
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 查询公告：全查询、模糊查询
     * @param noticeVo
     * @return
     */
    @RequestMapping("/loadAllNotice")
    public DataGridView loadAllNotice(NoticeVo noticeVo){
        IPage<Notice> page = new Page<>(noticeVo.getPage(),noticeVo.getLimit());
        //组装查询条件
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        //数据库数据大于开始时间
        queryWrapper.ge(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        //小于
        queryWrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());

        queryWrapper.orderByDesc("createtime");
        noticeService.page(page,queryWrapper);
        return new  DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("/addNotice")
    public ResultObj addNotice(NoticeVo noticeVo){
        try {
            noticeVo.setCreatetime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            noticeVo.setOpername(user.getName());
            noticeService.save(noticeVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("/updateNotice")
    public ResultObj updateNotice(NoticeVo noticeVo){
        try {
            noticeService.updateById(noticeVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 批量删除公告信息
     * @param noticeVo
     * @return
     */
    @RequestMapping("/batchDeleteNotice")
    public ResultObj batchDeleteNotice(NoticeVo noticeVo){
        try {
            Collection<Serializable> idList = new ArrayList<>();
            for (Integer id : noticeVo.getIds()) {
                idList.add(id);
            }
            noticeService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 删除公告信息
     * @param id
     * @return
     */
    @RequestMapping("deleteNotice")
    public ResultObj deleteNotice(Integer id){
        try {
            noticeService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}

