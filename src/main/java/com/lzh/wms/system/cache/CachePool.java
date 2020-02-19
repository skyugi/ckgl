package com.lzh.wms.system.cache;

import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.domain.Goods;
import com.lzh.wms.business.domain.Provider;
import com.lzh.wms.business.mapper.CustomerMapper;
import com.lzh.wms.business.mapper.GoodsMapper;
import com.lzh.wms.business.mapper.ProviderMapper;
import com.lzh.wms.system.common.SpringUtils;
import com.lzh.wms.system.domain.Dept;
import com.lzh.wms.system.domain.User;
import com.lzh.wms.system.mapper.DeptMapper;
import com.lzh.wms.system.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义缓存池
 * 
 * @author lzh
 * @date 2020-02-19 21:56
 */
public class CachePool {

    /**
     * 将所有的缓存数据存放到CACHE_CONTAINER 类似于redis
     */
    public static volatile Map<String,Object> CACHE_CONTAINER = new HashMap<>();

    /**
     * 根据key删除缓存
     * @param key
     */
    public static void removeCacheByKey(String key){
        if (CACHE_CONTAINER.containsKey(key)){
            CACHE_CONTAINER.remove(key);
        }
    }

    /**
     * 清空所有缓存
     */
    public static void removeAll(){
        CACHE_CONTAINER.clear();
    }

    /**
     * 同步缓存
     */
    public static void syncDataToCache(){
        //同步部门数据
        DeptMapper deptMapper = SpringUtils.getBean(DeptMapper.class);
        List<Dept> deptList = deptMapper.selectList(null);
        for (Dept dept : deptList) {
            CACHE_CONTAINER.put("dept:" + dept.getId(),dept);
        }
        //同步用户数据
        UserMapper userMapper = SpringUtils.getBean(UserMapper.class);
        List<User> userList = userMapper.selectList(null);
        for (User user : userList) {
            CACHE_CONTAINER.put("user:" + user.getId(),user);
        }
        //同步客户数据
        CustomerMapper customerMapper = SpringUtils.getBean(CustomerMapper.class);
        List<Customer> customerList = customerMapper.selectList(null);
        for (Customer customer : customerList) {
            CACHE_CONTAINER.put("customer:" + customer.getId(),customer);
        }
        //同步供应商数据
        ProviderMapper providerMapper = SpringUtils.getBean(ProviderMapper.class);
        List<Provider> providerList = providerMapper.selectList(null);
        for (Provider provider : providerList) {
            CACHE_CONTAINER.put("provider:" + provider.getId(),provider);
        }
        //同步商品数据
        GoodsMapper goodsMapper = SpringUtils.getBean(GoodsMapper.class);
        List<Goods> goodsList = goodsMapper.selectList(null);
        for (Goods goods : goodsList) {
            CACHE_CONTAINER.put("goods:" + goods.getId(),goods);
        }
    }
}
