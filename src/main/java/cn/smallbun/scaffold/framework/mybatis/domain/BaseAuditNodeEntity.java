/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 zuoqinggang (qinggang.zuo@gmail.com / 2689170096@qq.com)
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
package cn.smallbun.scaffold.framework.mybatis.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 基础树实体，继承基础审计实体
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/20 15:56
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAuditNodeEntity<PK, E extends BaseAuditNodeEntity> extends BaseNodeEntity<PK, E> {

    public static final String CREATE_BY          = "createBy";
    public static final String CREATE_TIME        = "createTime";
    public static final String LAST_MODIFIED_BY   = "lastModifiedBy";
    public static final String LAST_MODIFIED_TIME = "lastModifiedTime";
    /**
     * 创建者
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private String             createBy;
    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private LocalDateTime      createTime;
    /**
     * 最后修改者
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    private String             lastModifiedBy;
    /**
     * 最后修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    private LocalDateTime      lastModifiedTime;

}
