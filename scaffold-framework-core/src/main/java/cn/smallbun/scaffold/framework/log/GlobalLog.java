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
package cn.smallbun.scaffold.framework.log;

import com.alibaba.fastjson.JSONObject;
import cn.smallbun.scaffold.framework.json.JSON;
import cn.smallbun.scaffold.framework.log.domain.LogModel;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * 全局日志记录接口
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/21 11:34
 */
public class GlobalLog {
    /**
     * LOGGER
     */
    public static String LOGGER = "SMALLBUN_LOG";

    /**
     * put
     *
     * @param logger {@link LogModel}
     */
    public static void set(LogModel logger) {
        MDC.put(LOGGER, JSON.toJSONString(logger));
    }

    /**
     * put
     *
     * @return {@link LogModel}
     */
    public static LogModel get() {
        try {
            LogModel model = JSONObject.parseObject(MDC.get(LOGGER), LogModel.class);
            if (!Objects.isNull(model)) {
                return model;
            }
            throw new NullPointerException("No log object exists for the current thread");
        } catch (IllegalArgumentException e) {
            throw new NullPointerException("No log object exists for the current thread");
        }
    }

    /**
     * 存放日志
     *
     * @param before 修改前
     * @param after  修改后
     */
    public static void set(String before, String after) {
        //封装日志
        LogModel logger = GlobalLog.get();
        //修改前
        logger.setBefore(before);
        //修改后
        logger.setAfter(after);
        //设置
        set(logger);
    }
}
