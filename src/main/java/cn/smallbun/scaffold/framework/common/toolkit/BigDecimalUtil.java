
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
	 * @return
	 */
	public static int compare(BigDecimal first, BigDecimal second) {
		return first.compareTo(second);
	}

	/**
	 * 加法
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static BigDecimal plus(BigDecimal first, BigDecimal second) {
		return first.add(second);
	}

	/**
	 * 减法
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal first, BigDecimal second) {
		return first.subtract(second);
	}

	/**
	 * 乘法
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
		return first.multiply(second);
	}

	/**
	 * 减法
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static BigDecimal divide(BigDecimal first, BigDecimal second) {
		return first.divide(second);
	}

}
