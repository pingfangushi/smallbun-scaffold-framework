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
package cn.smallbun.scaffold.framework.mybatis.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 页面模型，用于接收页面分页查询传值（不用于返回，只用于接收）
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/4/26
 */
@Data
@ApiModel(value = "分页参数")
public class PageModel implements Serializable {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页，默认第一页")
    private int     current  = 1;
    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty(value = "每页记录，默认十条")
    private int     pageSize = 10;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "需要排序的字段")
    private String  sorter;

    /**
     * 是否正序排列，默认 true
     */
    @ApiModelProperty(value = "是否正序排列，默认 true")
    private boolean asc      = true;
}
