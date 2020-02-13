package com.lzh.wms.sys.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzh.wms.sys.common.ActiverUser;
import com.lzh.wms.sys.common.Constast;
import com.lzh.wms.sys.common.WebUtils;
import com.lzh.wms.sys.domain.Permission;
import com.lzh.wms.sys.domain.User;
import com.lzh.wms.sys.service.PermissionService;
import com.lzh.wms.sys.service.RoleService;
import com.lzh.wms.sys.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//todo shiro加强
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy //只有使用的时候才会加载
    private UserService userService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    @Lazy
    private RoleService roleService;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname",authenticationToken.getPrincipal().toString());
        User user = userService.getOne(queryWrapper);
        if (user != null){
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);


            //查询所有菜单
            QueryWrapper<Permission> qw = new QueryWrapper<>();
            //设置只能查询菜单
            qw.eq("type", Constast.TYPE_PERMISSION);
            queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
            //todo 没用连表查询
            //根据用户id+角色+权限去查询
            Integer userId = user.getId();
            //根据用户id查询角色id
            List<Integer> currentUserRoleIds = roleService.queryIdsOfRoleBelongToUserByUid(userId);
            //根据角色id查询菜单、权限id
            Set<Integer> pids = new HashSet<>();
            pids = permissionService.queryPermissionIdsByRoleIds(currentUserRoleIds,roleService,pids);
            //根据菜单、权限id查询相应菜单、权限
            List<Permission> list = null;
            if (pids.size()>0) {
                qw.in("id",pids);
                list = permissionService.list(qw);
            }else {
                list = new ArrayList<>();
            }
            List<String> percodes = new ArrayList<>();
            for (Permission permission : list) {
                percodes.add(permission.getPercode());
            }
            activerUser.setPermissions(percodes);


            ByteSource credentialsSalt  = ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPwd(),credentialsSalt,this.getName());
            return info;
        }
        return null;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiverUser activerUser = (ActiverUser) principalCollection.getPrimaryPrincipal();
        User user = activerUser.getUser();
        List<String> permissions = activerUser.getPermissions();
        if (user.getType()==Constast.USER_TYPE_SUPER){
            authorizationInfo.addStringPermission("*:*");
        }else {
            if (permissions!=null&&permissions.size()>0){
                authorizationInfo.addStringPermissions(permissions);
            }
        }
        return authorizationInfo;
    }
}
