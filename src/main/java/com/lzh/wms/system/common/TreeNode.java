package com.lzh.wms.system.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 左侧菜单构造节点
 *
 * @author lzh
 * @date 2020-01-31 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {

    private Integer id;
    @JsonProperty("parentId")
    private Integer pid;
    /**
     * 页面需要的json字段
     */
    private String title;
    private String icon;
    private String href;
    private Boolean spread;
    private List<TreeNode> children;

    /**
     * dtree开启复选框
     * 字符串格式，简洁写法，仅支持一级复选框，checked属性直接由checkArr表示
     * "0"代表不选中 "1"代表选中
     */
    private String checkArr = "0";

    /**
     * 左侧导航栏构造器--标准数据格式
     * @param id
     * @param pid
     * @param title
     * @param icon
     * @param href
     * @param spread
     */
    public TreeNode(Integer id, Integer pid, String title, String icon, String href, Boolean spread) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.icon = icon;
        this.href = href;
        this.spread = spread;
    }

    /**
     * dtree的数据格式--简单json类型
     * @param id
     * @param pid
     * @param title
     * @param spread
     */
    public TreeNode(Integer id, Integer pid, String title, Boolean spread) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.spread = spread;
    }

    /**
     * 兼容dtree开启复选框的构造器
     * @param id
     * @param pid
     * @param title
     * @param spread
     * @param checkArr
     */
    public TreeNode(Integer id, Integer pid, String title, Boolean spread, String checkArr) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.spread = spread;
        this.checkArr = checkArr;
    }
}
