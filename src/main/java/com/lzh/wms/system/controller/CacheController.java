package com.lzh.wms.system.controller;

import com.lzh.wms.system.cache.CachePool;
import com.lzh.wms.system.common.CacheBean;
import com.lzh.wms.system.common.DataGridView;
import com.lzh.wms.system.common.ResultObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存管理前端控制器
 *
 * @author lzh
 * @date 2020-02-19 22:26
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    private static volatile Map<String,Object> CACHE_CONTAINER = CachePool.CACHE_CONTAINER;

    /**
     * 查询所有缓存
     * @return
     */
    @RequestMapping("loadAllCache")
    public DataGridView loadAllCache(){
        List<CacheBean> list = new ArrayList<>();
        Set<Map.Entry<String, Object>> entrySet = CACHE_CONTAINER.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            list.add(new CacheBean(entry.getKey(),entry.getValue()));
        }
        return new DataGridView(list);
    }

    /**
     * 同步缓存
     * @return
     */
    @RequestMapping("syncCache")
    public ResultObj syncCache(){
        CachePool.syncDataToCache();
        return ResultObj.OPERATE_SUCCESS;
    }

    /**
     * 根据key删除缓存
     * @param key
     * @return
     */
    @RequestMapping("deleteCache")
    public ResultObj deleteCache(String key){
        //一般操作内存删除出现异常概率比操作数据库低，低比较多
        CachePool.removeCacheByKey(key);
        return ResultObj.DELETE_SUCCESS;
    }

    /**
     * 清空缓存
     * @return
     */
    @RequestMapping("removeAllCache")
    public ResultObj removeAllCache(){
        //一般操作内存删除出现异常概率比操作数据库低，低比较多
        CachePool.removeAll();
        return ResultObj.DELETE_SUCCESS;
    }

}
