package com.lzh.wms.sys.vo;

import com.lzh.wms.sys.domain.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;

/**
 * 菜单权限类子类
 *
 * @author lzh
 * @date 2020-01-30 23:06
 */
@Data
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends Permission {

    private static final long serialVersionUID=1L;






    /*private String price;

    public PermissionVo(Integer id, Integer pid, String type, String title, String percode, String icon, String href, String target, Integer open, Integer ordernum, Integer available, String price) {
        super(id, pid, type, title, percode, icon, href, target, open, ordernum, available);
        this.price = price;
    }


    public static void main(String[] args) {
        PermissionVo p1 = new PermissionVo(1,1,"1","1","1","1","1","1",1,1,1,"1");
        PermissionVo p2 = new PermissionVo(1,1,"1","1","1","1","1","1",1,1,2,"1");

        System.out.println(p1.equals(p2));//true就不会放进set集合中
        HashSet hashSet = new HashSet();
        hashSet.add(p1);
        hashSet.add(p2);
        System.out.println(hashSet.size());
    }*/

}
