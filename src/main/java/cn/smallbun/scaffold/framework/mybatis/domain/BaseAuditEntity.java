/*
 * Copyright (c) 2018-2019.‭‭‭‭‭‭‭‭‭‭‭‭[zuoqinggang] www.pingfangushi.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

import java.time.LocalDateTime;

/**
 * 实体的基本抽象类，用于保存已创建，最后修改和创建的定义，最后修改日期。
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/9
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAuditEntity<PK> extends BaseEntity<PK> {

	/**
	 * 创建者
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	@ApiModelProperty(hidden = true)
	private String createBy;
	/**
	 * 创建日期
	 */
	@JsonIgnore
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@ApiModelProperty(hidden = true)
	private LocalDateTime createTime;
	/**
	 * 最后修改者
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	@TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(hidden = true)
	private String lastModifiedBy;
	/**
	 * 最后修改时间
	 */
	@JsonIgnore
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(hidden = true)
	private LocalDateTime lastModifiedTime;

}
