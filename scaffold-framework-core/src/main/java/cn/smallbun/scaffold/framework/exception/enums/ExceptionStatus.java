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
package cn.smallbun.scaffold.framework.exception.enums;

/**
 * 异常状态码
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/9 20:44
 */
public enum ExceptionStatus {

                             /**
                              * 未知错误
                              */
                             UNKNOWN_ERROR("-1", "未知错误"),
                             /**
                              * 参数不合法
                              */
                             PARAM_ERROR("201", "参数不合法"),
                             /**
                              * 数据库异常
                              */
                             DATABASE_ERROR("202", "数据库异常"),
                             /**
                              * 拦截器不通过
                              */
                             EX999999("999999", "拦截器不通过"),
                             /**
                              * 参数校验失败(为空、错值提示)
                              */
                             EX900000("900000", "参数校验失败(为空、错值提示)"),
                             /**
                              * 系统异常
                              */
                             EX900001("900001", "系统异常，请稍后重试"),
                             /**
                              * 获取配置信息错误
                              */
                             EX900002("900002", "获取配置信息错误"),
                             /**
                              * 参数校验错误
                              */
                             EX900003("900003", "参数校验错误"),
                             /**
                              * 未定义错误消息
                              */
                             EX900004("900004", "未定义错误消息"),
                             /**
                              * 数字签名校验错误
                              */
                             EX900005("900005", "数字签名校验错误"),
                             /**
                              * 参数类型不对
                              */
                             EX900006("900006", "参数类型不对"),

                             /**
                              * 演示模式，不允许操作
                              */
                             EX900007("900007", "演示模式，不允许操作！"),
                             /**
                              * 调用微服务失败
                              */
                             EX900008("900008", "调用微服务异常！"),

                             /**
                              * 用户名或密码错误!
                              */
                             EX000101("000101", "用户名或密码错误!"),
                             /**
                              * 验证码错误
                              */
                             EX000102("000102", "验证码错误!"),
                             /**
                              * 用户被锁定
                              */
                             EX000103("000103", "用户被锁定,请联系管理员!"),
                             /**
                              * 用户被禁用
                              */
                             EX000104("000104", "用户被禁用,请联系管理员!"),
                             /**
                              * 没有用户权限
                              */
                             EX000105("000105", "没有可用权限,请联系管理员!"),
                             /**
                              * 内部身份验证服务异常
                              */
                             EX000106("000106", "内部身份验证服务异常,请联系管理员!"),
                             /**
                              * 用户不存在
                              */
                             EX000107("000107", "用户不存在!");

    private String code;
    private String message;

    ExceptionStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
