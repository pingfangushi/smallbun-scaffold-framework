
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/4
 */
public class RegexValidateUtil {

	private static final Pattern PATTERN = Pattern.compile("[0-9]+");

	/**
	 * 验证是否是邮箱
	 *
	 * @param email email
	 * @return boolean
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		String mark = "@";
		if (email.contains(mark)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检测是否为手机
	 *
	 * @param mobile mobile
	 * @return boolean
	 */
	public static boolean checkMobile(String mobile) {
		boolean flag = false;
		String check = "^1\\d{10}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(mobile);
		flag = matcher.matches();
		return flag;
	}

	/**
	 * 验证字符是否是数字
	 *
	 * @param str str
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		boolean flag = false;
		if (null != str) {
			Matcher match = PATTERN.matcher(str);
			if (match.matches()) {
				flag = true;
			}
		}
		return flag;
	}


}
