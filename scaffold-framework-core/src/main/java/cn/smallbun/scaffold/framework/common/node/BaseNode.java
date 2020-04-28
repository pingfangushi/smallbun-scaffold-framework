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
package cn.smallbun.scaffold.framework.common.node;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基础树
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/21 19:56
 */
@Data
public class BaseNode implements Serializable {
    /**
     * ID
     */
    private String                   id;
    /**
     * 父ID
     */
    private String                   parentId;
    /**
     * 是否子叶节点
     */
    private boolean                  leaf;
    /**
     * 子节点
     */
    private List<? extends BaseNode> children;
}
