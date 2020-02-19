package com.lzh.wms.system.common;

import com.alibaba.fastjson.JSON;

/**
 *
 *
 * @author lzh
 * @date 2020-02-19 22:32
 */
public class CacheBean {

    private String key;
    private Object value;

    public CacheBean() {
    }

    public CacheBean(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return JSON.toJSON(value).toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
