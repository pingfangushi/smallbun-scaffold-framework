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
package cn.smallbun.scaffold.framework.mybatis.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/10/9 18:45
 */
@Data
public class Page<T> implements Serializable {
    /**
     * list
     */
    @ApiModelProperty(value = "数据集合")
    private List<T>    list;
    /**
     * 分页数据
     */
    @ApiModelProperty(value = "页数数据")
    private Pagination pagination;

    /**
     * 分页参数数据
     */
    @Data
    public static class Pagination implements Serializable {
        @ApiModelProperty(value = "总条数")
        private long total;
        @ApiModelProperty(value = "总页数")
        private long pageSize;
        @ApiModelProperty(value = "当前页")
        private long current;
    }
}
