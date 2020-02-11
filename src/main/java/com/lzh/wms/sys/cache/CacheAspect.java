package com.lzh.wms.sys.cache;

import com.lzh.wms.sys.domain.Dept;
import com.lzh.wms.sys.vo.DeptVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.CallbackHelper;
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
    private Map<String,Object> CACHE_CONTAINER = new HashMap<>();

    /**
     * 声明切面表达式
     */
    private static final String POINTCUT_DEPT_GET = "execution(* com.lzh.wms.sys.service.impl.DeptServiceImpl.getOne(..))";
    private static final String POINTCUT_DEPT_UPDATE = "execution(* com.lzh.wms.sys.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_DELETE = "execution(* com.lzh.wms.sys.service.impl.DeptServiceImpl.removeById(..))";

    private static final String CACHE_DEPT_PREFIX = "dept:";

    /**
     * 查询切入
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
            log.info("从缓存里找到部门对象：" + CACHE_DEPT_PREFIX + arg);
            return result1;
        }else {
            Dept result2 = (Dept) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+result2.getId(),result2);
            log.info("未从缓存里找到部门对象，查询数据库并放入缓存："+CACHE_DEPT_PREFIX+result2.getId());
            return result2;
        }
    }

    /**
     * 更新切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_DEPT_UPDATE)
    public Object cacheDeptUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        DeptVo deptVo = (DeptVo) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            Dept dept = (Dept) CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + deptVo.getId());
            if (dept == null){
                dept = new Dept();
            }
            BeanUtils.copyProperties(deptVo,dept);
            CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+dept.getId(),dept);
            log.info("部门对象缓存已更新" + CACHE_DEPT_PREFIX + deptVo.getId());
        }
        return isSuccess;
        /*Dept dept = (Dept) CACHE_CONTAINER.get(CACHE_DEPT_PREFIX + deptVo.getId());
        Boolean isSuccess = false;
        if (dept==null){
            isSuccess = (Boolean) joinPoint.proceed();
            if (isSuccess){
                dept = new Dept();
            }
             BeanUtils.copyProperties(deptVo,dept);
             CACHE_CONTAINER.put(CACHE_DEPT_PREFIX+dept.getId(),dept);
        }
        return isSuccess;*/
    }

    /**
     * 删除切入
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
            log.info("部门对象缓存已删除" + CACHE_DEPT_PREFIX + id);
        }
        return isSuccess;
    }
}

