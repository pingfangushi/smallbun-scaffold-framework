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
package cn.smallbun.scaffold.framework.mybatis.datascope.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/11/14 16:49
 */
public enum DataScopeEnum {
                           /**
                            * 所有数据
                            */
                           DATA_SCOPE_ALL(1, "所有数据"),
                           /**
                            * 所在公司及以下数据
                            */
                           DATA_SCOPE_COMPANY_AND_CHILD(2, "所在公司及以下数据"),
                           /**
                            *所在公司数据
                            */
                           DATA_SCOPE_COMPANY(3, "所在公司数据"),
                           /**
                            * 所在部门及以下数据
                            */
                           DATA_SCOPE_ORG_AND_CHILD(4, "所在部门及以下数据"),
                           /**
                            * 所在部门数据
                            */
                           DATA_SCOPE_ORG(5, "所在部门数据"),
                           /**
                            * 仅本人数据
                            */
                           DATA_SCOPE_SELF(6, "仅本人数据"),
                           /**
                            * 按明细设置
                            */
                           DATA_SCOPE_CUSTOM(7, "按明细设置");

    DataScopeEnum(Integer value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    @EnumValue
    private final int    value;
    private final String describe;

    public String getDescribe() {
        return describe;
    }

    public Integer getValue() {
        return value;
    }
}
