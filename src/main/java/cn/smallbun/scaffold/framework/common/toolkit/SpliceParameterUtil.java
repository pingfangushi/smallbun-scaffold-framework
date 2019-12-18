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

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 拼接参数
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/12/1 21:48
 */
public class SpliceParameterUtil {
	/**
	 * 拼接get 请求参数
	 * @param url url
	 * @param params params
	 * @return String
	 */
	public static String urlBuilder(String url, List<NameValuePair> params) {
		return url + "?" + URLEncodedUtils.format(params, "UTF-8");
	}


	/**
	 * 拼接Post请求参数
	 * @param params params
	 * @return String
	 */
	public static String paramsToString(Map<String, String> params) {
		if (params != null && params.size() > 0) {
			String paramsEncoding = "UTF-8";
			StringBuilder encodedParams = new StringBuilder();
			try {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
					encodedParams.append('=');
					encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
					encodedParams.append('&');
				}
				return encodedParams.toString();
			} catch (UnsupportedEncodingException uee) {
				throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
			}
		}
		return "";
	}
}
