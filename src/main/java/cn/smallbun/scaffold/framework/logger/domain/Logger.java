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
package cn.smallbun.scaffold.framework.logger.domain;

import cn.smallbun.scaffold.framework.logger.enmus.Operate;
import cn.smallbun.scaffold.framework.logger.enmus.Platform;
import cn.smallbun.scaffold.framework.logger.enmus.Status;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志记录实体类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/11/2 15:18
 */
@Data
public class Logger implements Serializable {
    /**
     * 模块名称
     */
    private String        module;
    /**
     * 请求参数
     */
    private String        params;
    /**
     * 功能名称
     */
    private String        feature;

    /**
     * 操作类型
     */
    private Operate       action;

    /**
     * 平台类型
     */
    private Platform      platform;
    /**
     * 请求方法
     */
    private String        method;

    /**
     * 操作用户(关联用户表)
     */
    private String        user;

    /**
     * 操作时间
     */
    private LocalDateTime time;

    /**
     * URI
     */
    private String        uri;

    /**
     * 地点
     */
    private String        location;

    /**
     * 浏览器
     */
    private String        browser;

    /**
     * 操作系统
     */
    private String        os;

    /**
     * ip
     */
    private String        ip;
    /**
     * 结果
     */
    private Object        result;
    /**
     * 状态
     */
    private Status        status;
}
