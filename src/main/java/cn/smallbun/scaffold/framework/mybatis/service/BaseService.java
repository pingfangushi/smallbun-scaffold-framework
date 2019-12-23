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
package cn.smallbun.scaffold.framework.mybatis.service;

import cn.smallbun.scaffold.framework.mybatis.page.PageModel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/9 12:00
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 唯一验证
     * @param t T
     * @return boolean
     */
    boolean unique(T t);

    /**
     * 分页查询
     * @param page {@link PageModel}
     * @return {@link IPage}
     */
    IPage<T> page(PageModel page);

    /**
     * 分页查询
     * @param page  {@link PageModel}
     * @param wrapper {@link QueryWrapper}
     * @return  {@link IPage}
     */
    IPage<T> page(PageModel page, Wrapper<T> wrapper);
}
