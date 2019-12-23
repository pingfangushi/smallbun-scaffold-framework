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
package cn.smallbun.scaffold.framework.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 基础树实体
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/20 15:56
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseNodeEntity<PK, E extends BaseNodeEntity> extends BaseEntity<PK> {
    /**
     * 父ID，由于是树表，父ID和子ID类型一致
     */
    @ApiModelProperty(value = "父节点编号")
    @TableField("parent_")
    private PK      parentId;
    /**
     * 全部父类ID，用,分隔
     */
    @ApiModelProperty(value = "所有父节点")
    @TableField("parents_")
    private String  parentIds;
    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    @TableField(value = "name_")
    private String  name;
    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<E> children;
    /**
     * 是否是子叶节点
     */
    @TableField(exist = false)
    private boolean isLeaf;
}
