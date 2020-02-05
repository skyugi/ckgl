package com.lzh.wms.sys.common;

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
     * 左侧导航栏构造器
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
}
