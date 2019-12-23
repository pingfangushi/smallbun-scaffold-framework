/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 SanLi (qinggang.zuo@gmail.com) ${company}
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
package cn.smallbun.scaffold.framework.logger.aspect;

import cn.smallbun.scaffold.framework.common.toolkit.IpUtil;
import cn.smallbun.scaffold.framework.logger.ILoggingRecord;
import cn.smallbun.scaffold.framework.logger.domain.Logger;
import cn.smallbun.scaffold.framework.logger.enmus.Status;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
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

import static cn.smallbun.scaffold.framework.common.address.Address.getCityInfoByDb;
import static cn.smallbun.scaffold.framework.common.toolkit.UserAgentUtil.getUserAgent;
import static cn.smallbun.scaffold.framework.security.domain.User.ANONYMOUS_USER;

/**
 * AOP 请求Controller打印基本信息，并添加MDC请求链ID
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/4/30
 */
@Slf4j
@Order(value = 0)
@Aspect
public class LoggingAspect {

    public LoggingAspect(ILoggingRecord smallBunLogLogic) {
        this.loggingRecord = smallBunLogLogic;
    }

    /**
     * 配置切面
     */
    @Pointcut(value = "@annotation(logging)")
    public void log(cn.smallbun.scaffold.framework.logger.annotation.Logger logging) {
    }

    /**
     * 请求Controller 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before(value = "log(logging)", argNames = "joinPoint,logging")
    public void logDeBefore(JoinPoint joinPoint,
                            cn.smallbun.scaffold.framework.logger.annotation.Logger logging) {
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
        if (!logging.save()) {
            return;
        }
        log.debug("----------------------------------------------------------");
        log.debug("用户代理:{}", getUserAgent(request));
        log.debug("请求路径:{}", request.getRequestURL().toString());
        log.debug("请求类型:{}", request.getMethod());
        log.debug("客户端IP:{}", request.getRemoteAddr());
        log.debug("请求方法:{}", joinPoint.getSignature().getDeclaringTypeName() + "."
                             + joinPoint.getSignature().getName());
        log.debug("请求参数:{}", parameter);
    }

    /**
     * 之后操作
     *
     * @param returnValue {@link Object}
     */
    @AfterReturning(pointcut = "log(logging)", returning = "returnValue", argNames = "returnValue,logging")
    public void logDoAfterReturning(Object returnValue,
                                    cn.smallbun.scaffold.framework.logger.annotation.Logger logging) {
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
        log.debug("请求响应:{}", result);
        log.debug("----------------------------------------------------------");
        MDC.remove("TRACE_ID");
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(value = "@annotation(annotation))", returning = "result")
    public void doAfter(JoinPoint joinPoint,
                        cn.smallbun.scaffold.framework.logger.annotation.Logger annotation,
                        Object result) {
        Logger logging = getLog(joinPoint, annotation, result);
        logging.setStatus(Status.SUCCESS);
        loggingRecord.recording(logging, joinPoint);
    }

    /**
     * LogAnnotation 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @AfterThrowing(value = "@annotation(annotation)", throwing = "e")
    public void doAfter(JoinPoint joinPoint,
                        cn.smallbun.scaffold.framework.logger.annotation.Logger annotation,
                        Exception e) {
        Logger logging = getLog(joinPoint, annotation, e.getStackTrace());
        logging.setStatus(Status.ERROR);
        if (annotation.save()) {
            loggingRecord.recording(logging, joinPoint);
        }
    }

    /**
     * 获取日志
     * @param joinPoint {@link JoinPoint}
     * @param annotation {@link cn.smallbun.scaffold.framework.logger.annotation.Logger}
     * @param result {@link Object}
     * @return {@link Logger}
     */
    private Logger getLog(JoinPoint joinPoint,
                          cn.smallbun.scaffold.framework.logger.annotation.Logger annotation,
                          Object result) {
        //获取请求类型
        HttpServletRequest request = ((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //记录日志
        Logger logging = new Logger();
        //获取类注解
        cn.smallbun.scaffold.framework.logger.annotation.Logger byClass = AnnotationUtils
            .findAnnotation(joinPoint.getTarget().getClass(),
                cn.smallbun.scaffold.framework.logger.annotation.Logger.class);
        if (!Objects.isNull(byClass)) {
            logging.setModule(byClass.module());
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法注解
        cn.smallbun.scaffold.framework.logger.annotation.Logger byMethod = AnnotationUtils
            .findAnnotation(methodSignature.getMethod(),
                cn.smallbun.scaffold.framework.logger.annotation.Logger.class);
        if (!Objects.isNull(byMethod)) {
            logging.setFeature(byMethod.feature());
        }
        //请求地址
        logging.setUri(request.getRequestURI());
        //请求参数
        logging.setParams(JSON.toJSONString(request.getParameterMap()));
        //平台类型
        logging.setPlatform(annotation.platform());
        //操作类型
        logging.setAction(annotation.action());
        //方法名称
        logging.setMethod(joinPoint.getTarget().getClass().getName() + "."
                          + joinPoint.getSignature().getName() + "()");
        //操作用户
        logging.setUser(SecurityUtils.getCurrentUserLogin().isPresent()
            ? SecurityUtils.getCurrentUserLogin().get()
            : ANONYMOUS_USER);
        //操作IP
        logging.setIp(IpUtil.getIpAddr(request));
        //登录地点
        logging.setLocation(getCityInfoByDb(IpUtil.getIpAddr(request)));
        //浏览器
        logging.setBrowser(getUserAgent(request).getBrowser().getName());
        //操作系统
        logging.setOs(getUserAgent(request).getOperatingSystem().getName());
        //操作时间
        logging.setTime(LocalDateTime.now());
        //返回数据
        logging.setResult(result);
        return logging;
    }

    /**
     * 日志逻辑接口
     */
    private final ILoggingRecord loggingRecord;
}
