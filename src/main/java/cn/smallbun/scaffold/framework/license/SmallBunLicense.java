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
package cn.smallbun.scaffold.framework.license;

import cn.smallbun.scaffold.framework.common.toolkit.AppVersionUtil;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * License
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/7/11 17:13
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20 + 1)
public class SmallBunLicense implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static AtomicBoolean processed = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(@NotBlank ApplicationEnvironmentPreparedEvent event) {

        // Skip if processed before, prevent duplicated execution in Hierarchical ApplicationContext
        if (processed.get()) {
            return;
        }
        String chineseText = "欢迎使用 smallbun 企业级开发脚手架，愿景：大明湖畔最好的企业级开发脚手架";
        String englishText = "Welcome to smallbun enterprise development scaffolding,Vision: The best enterprise-level development scaffolding by Daming Lake";

        System.err.println(chineseText);
        System.err.println(englishText);
        System.err.println(
            "smallbun-scaffold-framework Version: " + AppVersionUtil.getVersion(this.getClass()));

        // mark processed to be true
        processed.compareAndSet(false, true);
    }

}
