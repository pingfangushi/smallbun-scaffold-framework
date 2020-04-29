/*
 * scaffold-framework-core - smallbun企业级开发脚手架-核心框架
 * Copyright © 2018-2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.smallbun.scaffold.framework.log.aspect;

import cn.smallbun.scaffold.framework.common.address.Address;
import cn.smallbun.scaffold.framework.common.toolkit.IpUtil;
import cn.smallbun.scaffold.framework.common.toolkit.UserAgentUtil;
import cn.smallbun.scaffold.framework.log.GlobalLog;
import cn.smallbun.scaffold.framework.log.LogRecordHandler;
import cn.smallbun.scaffold.framework.log.annotation.Log;
import cn.smallbun.scaffold.framework.log.domain.LogModel;
import cn.smallbun.scaffold.framework.log.enmus.Status;
import cn.smallbun.scaffold.framework.security.domain.User;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * AOP 请求Controller打印基本信息，并添加MDC请求链ID
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/4/30
 */
@Order(value = 0)
@Aspect
public class LogAspect {

    public LogAspect(LogRecordHandler smallBunLogLogic) {
        this.logRecordHandler = smallBunLogLogic;
    }

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置切面
     */
    @Pointcut(value = "@annotation(log)")
    public void pointcut(Log log) {
    }

    /**
     * 请求Controller 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before(value = "pointcut(log)", argNames = "joinPoint,log")
    public void logDeBefore(JoinPoint joinPoint, Log log) {
        //在这里将数据放入线程
        GlobalLog.set(new LogModel());
        MDC.put("TRACE_ID", "" + IdWorker.getIdStr());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        assert attributes != null;
        Object parameter = Arrays.toString(joinPoint.getArgs());
        try {
            parameter = JSON.toJSONString(joinPoint.getArgs());
        } catch (Exception ignored) {
        }
        HttpServletRequest request = attributes.getRequest();
        if (!log.save()) {
            return;
        }
        logger.debug("----------------------------------------------------------");
        logger.debug("用户代理:{}", UserAgentUtil.getUserAgent(request));
        logger.debug("请求路径:{}", request.getRequestURL().toString());
        logger.debug("请求类型:{}", request.getMethod());
        logger.debug("客户端IP:{}", request.getRemoteAddr());
        logger.debug("请求方法:{}", joinPoint.getSignature().getDeclaringTypeName() + "."
                                + joinPoint.getSignature().getName());
        logger.debug("请求参数:{}", parameter);
    }

    /**
     * 之后操作
     *
     * @param returnValue {@link Object}
     */
    @AfterReturning(pointcut = "pointcut(logging)", returning = "returnValue", argNames = "returnValue,logging")
    public void logDoAfterReturning(Object returnValue, Log logging) {
        if (StringUtils.isEmpty(returnValue)) {
            returnValue = "";
        }
        Object result = returnValue;
        try {
            result = JSON.toJSONString(returnValue);
        } catch (Exception ignored) {
        }
        //是否打印
        if (!logging.save()) {
            return;
        }
        logger.debug("请求响应:{}", result);
        logger.debug("----------------------------------------------------------");
        MDC.remove("TRACE_ID");
    }

    /**
     * 后置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(value = "pointcut(annotation)", returning = "result", argNames = "joinPoint,annotation,result")
    public void doAfter(JoinPoint joinPoint, Log annotation, Object result) {
        // 保存的时候才会去做操作
        if (annotation.save()) {
            logRecordHandler
                .recording(getLog(joinPoint, annotation, result, Status.SUCCESS.getCode()));
        }
    }

    /**
     * LogAnnotation 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @AfterThrowing(value = "pointcut(annotation)", throwing = "e", argNames = "joinPoint,annotation,e")
    public void doAfter(JoinPoint joinPoint, Log annotation, Exception e) {
        // 保存的时候才会去做操作
        if (annotation.save()) {
            logRecordHandler.recording(
                getLog(joinPoint, annotation, e.getStackTrace(), Status.ERROR.getCode()),
                joinPoint);
        }
    }

    /**
     * 获取日志
     *
     * @param joinPoint  {@link JoinPoint}
     * @param annotation {@link Log}
     * @param result     {@link Object}
     * @return {@link Log}
     */
    private LogModel getLog(JoinPoint joinPoint, Log annotation, Object result, String status) {
        //获取请求类型
        HttpServletRequest request = ((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //记录日志
        LogModel logger = GlobalLog.get();
        //获取类注解
        Log byClass = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), Log.class);
        if (!Objects.isNull(byClass)) {
            logger.setModule(byClass.module());
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法注解
        Log byMethod = AnnotationUtils.findAnnotation(methodSignature.getMethod(), Log.class);
        if (!Objects.isNull(byMethod)) {
            logger.setFeature(byMethod.feature());
        }
        //请求地址
        logger.setUri(request.getRequestURI());
        //请求参数
        logger.setParams(JSON.toJSONString(request.getParameterMap()));
        //平台类型
        logger.setPlatform(annotation.platform());
        //操作类型
        logger.setAction(annotation.action());
        //方法名称
        logger.setMethod(joinPoint.getTarget().getClass().getName() + "."
                         + joinPoint.getSignature().getName() + "()");
        //操作用户
        logger.setUser(SecurityUtils.getCurrentUserLogin().isPresent()
            ? SecurityUtils.getCurrentUserLogin().get()
            : User.ANONYMOUS_USER);
        //操作IP
        logger.setIp(IpUtil.getIpAddr(request));
        //登录地点
        logger.setLocation(Address.getCityInfoByDb(IpUtil.getIpAddr(request)));
        //浏览器
        logger.setBrowser(UserAgentUtil.getUserAgent(request).getBrowser().getName());
        //操作系统
        logger.setOs(UserAgentUtil.getUserAgent(request).getOperatingSystem().getName());
        //操作时间
        logger.setTime(LocalDateTime.now());
        //返回数据
        logger.setResult(result);
        //状态
        logger.setStatus(status);
        return logger;
    }

    /**
     * 日志逻辑接口
     */
    private final LogRecordHandler logRecordHandler;
}
