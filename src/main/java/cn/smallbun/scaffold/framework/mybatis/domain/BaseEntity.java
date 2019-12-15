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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 基础实体类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/9 11:55
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseEntity<PK> extends IdEntity<PK> {
	/**
	 * 乐观锁
	 */
	@Version
	@JsonIgnore
	@JSONField(serialize = false)
	@TableField(value = "version_")
	@ApiModelProperty(hidden = true)
	private Integer version;
	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@JSONField(serialize = false)
	@TableField(value = "is_deleted", fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(hidden = true)
	private String isDeleted;
	/**
	 * 备注
	 */
	@TableField(value = "remarks_", fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(hidden = true)
	private String remarks;
}
