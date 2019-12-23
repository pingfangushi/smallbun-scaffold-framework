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
package cn.smallbun.scaffold.framework.trace;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 日志追踪加入MDC
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/4/23
 */
@Data
@Aspect
@Component
public class TraceAspect {

    /**
     * 返回结果之前
     */
    @Before(value = "@annotation(trace)")
    public void logBefore(final JoinPoint joinPoint, Trace trace) {
        //添加MDC
        MDC.put("TRACE_ID", "" + IdWorker.getIdStr());
    }

    /**
     * 之后操作
     *
     * @param trace Trace
     */
    @After(value = "@annotation(trace)")
    public void logDoAfterReturning(Trace trace) {
        //清除MDC
        MDC.clear();
    }

}
