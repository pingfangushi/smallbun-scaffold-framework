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
package cn.smallbun.scaffold.framework.web;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseResource
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/9 11:56
 */
public abstract class BaseResource {
    private static final String NULL = "null";
    /**
     * 日志
     */
    public final Logger         log  = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化，这里用来对参数进行过滤
     *
     * @param binder {@link WebDataBinder}
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value == null ? "" : value.toString();
            }
        });
    }

    /**
     * 把浏览器参数转化放到Map集合中
     *
     * @param request {@link HttpServletRequest}
     * @return {@link Map}
     */
    public Map<String, Object> getParam(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>(16);
        String method = request.getMethod();
        Enumeration<?> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key != null) {
                if (key instanceof String) {
                    String value = request.getParameter(key.toString());
                    //前台encodeURIComponent('我们');转码后到后台还是ISO-8859-1，所以还需要转码
                    if ("GET".equals(method)) {
                        value = new String(value.getBytes(StandardCharsets.ISO_8859_1),
                            StandardCharsets.UTF_8);
                    }
                    paramMap.put(key.toString(), value);
                }
            }
        }
        return paramMap;
    }

    /**
     * 将数据刷新写回web端
     *
     * @param response        {@link HttpServletResponse}  response对象
     * @param responseContent {@link String}  返回的数据
     */
    public void flushResponse(HttpServletResponse response, String responseContent) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("GBK");
            // 针对ajax中页面编码为GBK的情况，一定要加上以下两句
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("text/html;charset=UTF-8");
            writer = response.getWriter();
            if (responseContent == null || "".equals(responseContent)
                || NULL.equals(responseContent)) {
                writer.write("");
            } else {
                writer.write(responseContent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
