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
package cn.smallbun.scaffold.framework.common.toolkit;

import java.math.BigDecimal;

/**
 * BigdecimalUtil
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class BigDecimalUtil {

    /**
     * 比较
     *
     * @param first
     * @param second
     * @return int
     */
    public static int compare(BigDecimal first, BigDecimal second) {
        return first.compareTo(second);
    }

    /**
     * 加法
     *
     * @param first
     * @param second
     * @return BigDecimal
     */
    public static BigDecimal plus(BigDecimal first, BigDecimal second) {
        return first.add(second);
    }

    /**
     * 减法
     *
     * @param first
     * @param second
     * @return BigDecimal
     */
    public static BigDecimal subtract(BigDecimal first, BigDecimal second) {
        return first.subtract(second);
    }

    /**
     * 乘法
     *
     * @param first
     * @param second
     * @return BigDecimal
     */
    public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second);
    }

    /**
     * 减法
     *
     * @param first
     * @param second
     * @return BigDecimal
     */
    public static BigDecimal divide(BigDecimal first, BigDecimal second) {
        return first.divide(second);
    }

}
