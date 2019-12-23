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
package cn.smallbun.scaffold.framework.mybatis.utils;

import cn.smallbun.scaffold.framework.mybatis.domain.BaseNodeEntity;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 节点工具类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/10/11 20:28
 */
public class NodeHelp implements Serializable {
    /**
     * 获取树
     * @param list list
     * @param <T> 类型
     */
    public static <T extends BaseNodeEntity<?, T>> List<T> getNodeList(List<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (T li : list) {
                List<T> ts = Lists.newArrayList();
                //设置子菜单
                for (T entity : list) {
                    //子节点和父节点不一致
                    if (!li.getId().equals(entity.getId())
                        //父节点等于子节点
                        && li.getId().equals(entity.getParentId())) {
                        ts.add(entity);
                        li.setChildren(ts);
                        //如果已经成为了子节点,设置状态
                        entity.setLeaf(true);
                    }
                }
            }
            list.removeIf(T::isLeaf);
            return list;
        }
        return Lists.newArrayList();
    }
}
