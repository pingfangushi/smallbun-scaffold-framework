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
package cn.smallbun.scaffold.framework.exception.handler;

import cn.smallbun.scaffold.framework.common.result.ApiRestResult;
import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.exception.enums.ExceptionStatus;
import cn.smallbun.scaffold.framework.security.exception.HaveNotAuthorityException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.Set;

import static cn.smallbun.scaffold.framework.exception.Exceptions.getStackTraceAsString;
import static cn.smallbun.scaffold.framework.exception.enums.ExceptionStatus.EX900000;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 全局异常处理 matchIfMissing = true
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/10/9
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements Serializable {

    /**
     * SmallBunProperties
     */
    private final SmallBunProperties properties;

    public GlobalExceptionHandler(SmallBunProperties properties) {
        this.properties = properties;
    }

    /**
     * 默认异常处理
     *
     * @param e {@link ExceptionStatus}
     * @return {@link Object}
     */
    @ExceptionHandler(value = Exception.class)
    public ApiRestResult<?> defaultErrorHandler(Exception e) {
        //返回Rest错误
        return ApiRestResult.err(getErrorMsg(e)).build();
    }

    /**
     * 参数验证异常
     *
     * @param exception {@link MethodArgumentNotValidException}
     * @return {@link Object}
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiRestResult<?> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        return baseBindException(exception.getBindingResult());
    }

    /**
     * ConstraintViolationException
     *
     * @param e {@link ConstraintViolationException}
     * @return {@link Object}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuffer errorMsg = new StringBuffer();
        constraintViolations.forEach(ex -> errorMsg.append(ex.getMessage()));
        return ApiRestResult.err(errorMsg.substring(0, errorMsg.length() - 1), EX900000.getCode());

    }

    /**
     * 参数验证异常
     *
     * @param exception {@link BindException}
     * @return {@link Object}
     */
    @ExceptionHandler(value = BindException.class)
    public ApiRestResult<?> bindExceptionValidHandler(BindException exception) {
        return baseBindException(exception.getBindingResult());
    }

    /**
     * 参数绑定异常公共处理方法
     *
     * @param bindingResult {@link BindingResult}
     * @return {@link Object}
     */
    private ApiRestResult<?> baseBindException(BindingResult bindingResult) {
        StringBuilder buffer = new StringBuilder();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : bindingResult.getFieldErrors()) {
            buffer.append(error.getDefaultMessage()).append(",");
        }
        return ApiRestResult.err(buffer.substring(0, buffer.length() - 1), EX900000.getCode())
            .build();
    }

    /**
     * 用户名或密码错误 BadCredentialsException
     *
     * @param e {@link BadCredentialsException}
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ApiRestResult<?> badCredentialsException(BadCredentialsException e) {
        //返回Rest错误
        return new ApiRestResult<>().message(ExceptionStatus.EX000101.getMessage())
            .status(ExceptionStatus.EX000101.getCode()).result(getErrorMsg(e)).build();
    }

    /**
     * 用户不存在
     *
     * @param e {@link UsernameNotFoundException}
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ApiRestResult<?> usernameNotFoundException(UsernameNotFoundException e) {
        //返回Rest错误
        return new ApiRestResult<>().message(ExceptionStatus.EX000107.getMessage())
            .status(ExceptionStatus.EX000107.getCode()).result(getErrorMsg(e)).build();
    }

    /**
     * 用户被锁定
     *
     * @param e {@link LockedException}
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = LockedException.class)
    public ApiRestResult<?> lockedException(LockedException e) {
        //返回Rest错误
        return new ApiRestResult<>().message(ExceptionStatus.EX000103.getMessage())
            .status(getErrorMsg(e)).result(ExceptionStatus.EX000103.getMessage()).build();
    }

    /**
     * 如果由于没有足够信任凭据而拒绝身份验证请求，则抛出该异常。
     *
     * @param e {@link InsufficientAuthenticationException} e
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = InsufficientAuthenticationException.class)
    public ResponseEntity<ApiRestResult<?>> insufficientAuthenticationException(InsufficientAuthenticationException e) {
        return new ResponseEntity<>(new ApiRestResult<>().message(e.getLocalizedMessage())
            .result(UNAUTHORIZED.getReasonPhrase()).status(String.valueOf(UNAUTHORIZED.value())),
            UNAUTHORIZED);
    }

    /**
     * 没有访问权限
     *
     * @param e {@link AccessDeniedException}
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiRestResult<?>> methodAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(
            new ApiRestResult<>().message(e.getLocalizedMessage())
                .result(FORBIDDEN.getReasonPhrase()).status(String.valueOf(FORBIDDEN.value())),
            FORBIDDEN);
    }

    /**
     * InternalAuthenticationServiceException
     *
     * @param e {@link InternalAuthenticationServiceException}
     * @return {@link ApiRestResult}
     */
    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ApiRestResult<?> internalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        //没有权限
        if (e.getCause() instanceof HaveNotAuthorityException) {
            return new ApiRestResult<>().message(ExceptionStatus.EX000105.getMessage())
                .result(getErrorMsg(e)).status(String.valueOf(ExceptionStatus.EX000105.getCode()));
        }
        //禁用
        if (e.getCause() instanceof DisabledException) {
            return new ApiRestResult<>().message(ExceptionStatus.EX000104.getMessage())
                .status(ExceptionStatus.EX000104.getCode()).result(getErrorMsg(e)).build();
        }
        return new ApiRestResult<>().message(ExceptionStatus.EX000106.getMessage())
            .result(getErrorMsg(e)).status(String.valueOf(ExceptionStatus.EX000106.getCode()));
    }

    /**
     * 获取异常信息
     *
     * @param e {@link Exception}
     * @return {@link String}
     */
    private String getErrorMsg(Exception e) {
        //是否显示堆栈异常信息
        if (properties.getException().isShowStack()) {
            return getStackTraceAsString(e);
        }
        return e.getMessage();
    }
}
