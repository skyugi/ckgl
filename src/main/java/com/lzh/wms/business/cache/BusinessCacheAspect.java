package com.lzh.wms.business.cache;

import com.lzh.wms.business.domain.Customer;
import com.lzh.wms.business.domain.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存
 *
 * @author lzh
 * @date 2020-02-15 1:22
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class BusinessCacheAspect {

    /**
     * 日志出处
     */
    private Log log = LogFactory.getLog(BusinessCacheAspect.class);

    /**
     * 声明一个缓存容器
     */
    private Map<String,Object> CACHE_CONTAINER = new HashMap<>();

    /**
     * 声明切面表达式
     */
    private static final String POINTCUT_CUSTOMER_ADD = "execution(* com.lzh.wms.business.service.impl.CustomerServiceImpl.save(..))";
    private static final String POINTCUT_CUSTOMER_GET = "execution(* com.lzh.wms.business.service.impl.CustomerServiceImpl.getById(..))";
    private static final String POINTCUT_CUSTOMER_UPDATE = "execution(* com.lzh.wms.business.service.impl.CustomerServiceImpl.updateById(..))";
    private static final String POINTCUT_CUSTOMER_DELETE = "execution(* com.lzh.wms.business.service.impl.CustomerServiceImpl.removeById(..))";
    private static final String POINTCUT_CUSTOMER_BATCH_DELETE = "execution(* com.lzh.wms.business.service.impl.CustomerServiceImpl.removeByIds(..))";

    private static final String CACHE_CUSTOMER_PREFIX = "customer:";

    /**
     * 客户添加迁入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_ADD)
    public Object cacheCustomerAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Customer customer = (Customer) joinPoint.getArgs()[0];
        Boolean result = (Boolean) joinPoint.proceed();
        if (result!=null && result){
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX+customer.getId(),customer);
            log.info("客户添加成功，将客户对象------：" + CACHE_CUSTOMER_PREFIX+customer.getId() + "------放入缓存中");
        }
        return result;
    }

    /**
     * 客户查询切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_GET)
    public Object cacheCustomerGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer arg = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object result1 = CACHE_CONTAINER.get(CACHE_CUSTOMER_PREFIX + arg);
        if (result1 != null) {
            log.info("从缓存里找到客户对象------：" + CACHE_CUSTOMER_PREFIX + arg + "------");
            return result1;
        }else {
            Customer result2 = (Customer) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX+result2.getId(),result2);
            log.info("未从缓存里找到客户对象，查询数据库并放入缓存：------"+CACHE_CUSTOMER_PREFIX+result2.getId() + "------");
            return result2;
        }
    }

    /**
     * 客户更新切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_UPDATE)
    public Object cacheCustomerUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Customer customerVo = (Customer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            Customer customer = (Customer) CACHE_CONTAINER.get(CACHE_CUSTOMER_PREFIX + customerVo.getId());
            if (customer == null){
                customer = new Customer();
            }
            BeanUtils.copyProperties(customerVo,customer);
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PREFIX+customer.getId(),customer);
            log.info("客户对象缓存已更新：------" + CACHE_CUSTOMER_PREFIX + customerVo.getId() + "------");
        }
        return isSuccess;
    }

    /**
     * 客户删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_DELETE)
    public Object cacheCustomerDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_CUSTOMER_PREFIX+id);
            log.info("客户对象缓存已删除:------" + CACHE_CUSTOMER_PREFIX + id + "------");
        }
        return isSuccess;
    }

    /**
     * 客户批量删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_BATCH_DELETE)
    public Object cacheCustomerBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            for (Serializable id : idList) {
                log.info("---------------------批量删除客户对象缓存开始--------------------------------------------");
                //删除缓存
                CACHE_CONTAINER.remove(CACHE_CUSTOMER_PREFIX+id);
                log.info("客户对象缓存已删除:------" + CACHE_CUSTOMER_PREFIX + id + "------");
            }
            log.info("---------------------批量删除客户对象缓存结束--------------------------------------------");
        }
        return isSuccess;
    }

    /**
     * 声明切面表达式
     */
    private static final String POINTCUT_PROVIDER_ADD = "execution(* com.lzh.wms.business.service.impl.ProviderServiceImpl.save(..))";
    private static final String POINTCUT_PROVIDER_GET = "execution(* com.lzh.wms.business.service.impl.ProviderServiceImpl.getById(..))";
    private static final String POINTCUT_PROVIDER_UPDATE = "execution(* com.lzh.wms.business.service.impl.ProviderServiceImpl.updateById(..))";
    private static final String POINTCUT_PROVIDER_DELETE = "execution(* com.lzh.wms.business.service.impl.ProviderServiceImpl.removeById(..))";
    private static final String POINTCUT_PROVIDER_BATCH_DELETE = "execution(* com.lzh.wms.business.service.impl.ProviderServiceImpl.removeByIds(..))";

    private static final String CACHE_PROVIDER_PREFIX = "provider:";

    /**
     * 供应商添加迁入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_ADD)
    public Object cacheProviderAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Provider provider = (Provider) joinPoint.getArgs()[0];
        Boolean result = (Boolean) joinPoint.proceed();
        if (result!=null && result){
            CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX+provider.getId(),provider);
            log.info("供应商添加成功，将供应商对象------：" + CACHE_PROVIDER_PREFIX+provider.getId() + "------放入缓存中");
        }
        return result;
    }

    /**
     * 供应商查询切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_GET)
    public Object cacheProviderGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer arg = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object result1 = CACHE_CONTAINER.get(CACHE_PROVIDER_PREFIX + arg);
        if (result1 != null) {
            log.info("从缓存里找到供应商对象------：" + CACHE_PROVIDER_PREFIX + arg + "------");
            return result1;
        }else {
            Provider result2 = (Provider) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX+result2.getId(),result2);
            log.info("未从缓存里找到供应商对象，查询数据库并放入缓存：------"+CACHE_PROVIDER_PREFIX+result2.getId() + "------");
            return result2;
        }
    }

    /**
     * 供应商更新切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_UPDATE)
    public Object cacheProviderUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Provider providerVo = (Provider) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            Provider provider = (Provider) CACHE_CONTAINER.get(CACHE_PROVIDER_PREFIX + providerVo.getId());
            if (provider == null){
                provider = new Provider();
            }
            BeanUtils.copyProperties(providerVo,provider);
            CACHE_CONTAINER.put(CACHE_PROVIDER_PREFIX+provider.getId(),provider);
            log.info("供应商对象缓存已更新：------" + CACHE_PROVIDER_PREFIX + providerVo.getId() + "------");
        }
        return isSuccess;
    }

    /**
     * 供应商删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_DELETE)
    public Object cacheProviderDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_PROVIDER_PREFIX+id);
            log.info("供应商对象缓存已删除:------" + CACHE_PROVIDER_PREFIX + id + "------");
        }
        return isSuccess;
    }

    /**
     * 供应商批量删除切入
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_BATCH_DELETE)
    public Object cacheProviderBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            for (Serializable id : idList) {
                log.info("---------------------批量删除供应商对象缓存开始--------------------------------------------");
                //删除缓存
                CACHE_CONTAINER.remove(CACHE_PROVIDER_PREFIX+id);
                log.info("供应商对象缓存已删除:------" + CACHE_PROVIDER_PREFIX + id + "------");
            }
            log.info("---------------------批量删除供应商对象缓存结束--------------------------------------------");
        }
        return isSuccess;
    }
    
}
