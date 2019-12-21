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
package cn.smallbun.scaffold.framework.common.toolkit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * MapUtils
 *
 * @author SanLi
 * Created  by 2689170096@qq.com  on 2018/1/11
 */
public class MapUtils {
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, String> returnMap = new HashMap<>(16);
        Iterator<Map.Entry<String, String[]>> entries = properties.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        String name;
        String value = "";
        while (entries.hasNext()) {
            entry = entries.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else {
                String[] values = (String[]) valueObj;
                for (String value1 : values) {
                    value = value1 + ",";
                }
                value = value.substring(0, value.length() - 1);
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * Bean 转Map
     * @param o o
     * @return {@link Map} result
     */
    public static Map<String, String> beanToMap(Object o) {
        return JSONObject.parseObject(JSON.toJSONString(o),
            new TypeReference<Map<String, String>>() {
            });
    }
}
