package com.lzh.wms.system.cache;

import com.lzh.wms.system.domain.Dept;
import com.lzh.wms.system.domain.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存
 *
 * @author lzh
 * @date 2020-02-07 21:48
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {

    /**
     * 日志出处
     */
    private Log log = LogFactory.getLog(CacheAspect.class);

    /**
     * 声明一个缓存容器
     */
    private Map<String,Object> CACHE_CONTAINER = CachePool.CACHE_CONTAINER;

    /**
     * 声明切面表达式
     */
    private static final String POINTCUT_DEPT_ADD = "execution(* com.lzh.wms.system.service.impl.DeptServiceImpl.save(..))";
    private static final String POINTCUT_DEPT_GET = "execution(* com.lzh.wms.system.service.impl.DeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_UPDATE = "execution(* com.lzh.wms.system.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_DELETE = "execution(* com.lzh.wms.system.service.impl.DeptServiceImpl.removeById(..))";

    private static final String CACHE_DEPT_PREFIX = "dept:";

    /**
     * 部门添加迁入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_ADD)
    public Object cacheDeptAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Dept dept = (Dept) joinPoint.getArgs()[0];
        Boolean result = (Boolean) joinPoint.proceed();
        if (result!=null && result){
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+dept.getId(),dept);
            log.info("部门添加成功，将部门对象------：" + CACHE_DEPT_PREFIX+dept.getId() + "------放入缓存中");
        }
        return result;
    }

    /**
     * 部门查询切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_GET)
    public Object cacheDeptGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer arg = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object result1 = CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + arg);
        if (result1 != null) {
            log.info("从缓存里找到部门对象------：" + CACHE_DEPT_PREFIX + arg + "------");
            return result1;
        }else {
            Dept result2 = (Dept) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+result2.getId(),result2);
            log.info("未从缓存里找到部门对象，查询数据库并放入缓存：------"+CACHE_DEPT_PREFIX+result2.getId() + "------");
            return result2;
        }
    }

    /**
     * 部门更新切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_UPDATE)
    public Object cacheDeptUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Dept deptVo = (Dept) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            Dept dept = (Dept) CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + deptVo.getId());
            if (dept == null){
                dept = new Dept();
            }
            BeanUtils.copyProperties(deptVo,dept);
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+dept.getId(),dept);
            log.info("部门对象缓存已更新：------" + CACHE_DEPT_PREFIX + deptVo.getId() + "------");
        }
        return isSuccess;
    }

    /**
     * 部门删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_DELETE)
    public Object cacheDeptDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_DEPT_PREFIX+id);
            log.info("部门对象缓存已删除:------" + CACHE_DEPT_PREFIX + id + "------");
        }
        return isSuccess;
    }

    /**
     * 声明切面表达式
     */
    private static final String POINTCUT_USER_ADD = "execution(* com.lzh.wms.system.service.impl.UserServiceImpl.save(..))";
    private static final String POINTCUT_USER_GET = "execution(* com.lzh.wms.system.service.impl.UserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_UPDATE = "execution(* com.lzh.wms.system.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_DELETE = "execution(* com.lzh.wms.system.service.impl.UserServiceImpl.removeById(..))";

    private static final String CACHE_USER_PREFIX = "user:";

    /**
     * 用户添加切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_ADD)
    public Object cacheUserAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        User user = (User) joinPoint.getArgs()[0];
        Boolean result = (Boolean) joinPoint.proceed();
        if (result!=null && result){
            CACHE_CONTAINER.put(CACHE_USER_PREFIX+user.getId(),user);
            log.info("用户添加成功，将用户对象------：" + CACHE_USER_PREFIX+user.getId() + "------放入缓存中");
        }
        return result;
    }

    /**
     * 用户查询切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_GET)
    public Object cacheUserGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer arg = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object result1 = CACHE_CONTAINER.get(CACHE_USER_PREFIX + arg);
        if (result1 != null) {
            log.info("从缓存里找到用户对象------：" + CACHE_USER_PREFIX + arg + "------");
            return result1;
        }else {
            User result2 = (User) joinPoint.proceed();

            if (result2==null){
                return null;
            }


            CACHE_CONTAINER.put(CACHE_USER_PREFIX+result2.getId(),result2);
            log.info("未从缓存里找到用户对象，查询数据库并放入缓存：------"+CACHE_USER_PREFIX+result2.getId() + "------");
            return result2;
        }
    }

    /**
     * 用户更新切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_UPDATE)
    public Object cacheUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        User userVo = (User) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            User user = (User) CACHE_CONTAINER.get(CACHE_USER_PREFIX + userVo.getId());
            if (user == null){
                user = new User();
            }
            BeanUtils.copyProperties(userVo,user);
            CACHE_CONTAINER.put(CACHE_USER_PREFIX+user.getId(),user);
            log.info("用户对象缓存已更新：------" + CACHE_USER_PREFIX + userVo.getId() + "------");
        }
        return isSuccess;
    }

    /**
     * 用户删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_USER_DELETE)
    public Object cacheUserDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_USER_PREFIX+id);
            log.info("用户对象缓存已删除:------" + CACHE_USER_PREFIX + id + "------");
        }
        return isSuccess;
    }
    
}

