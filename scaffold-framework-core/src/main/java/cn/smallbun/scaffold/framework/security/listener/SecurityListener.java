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
package cn.smallbun.scaffold.framework.security.listener;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cn.smallbun.scaffold.framework.common.toolkit.CreateFileUtil.createFile;

/**
 * SecurityListener
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/9/3
 */
@Slf4j
public class SecurityListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(SecurityListener.class);
    private static final String DIR_NAME = ".smallbun";
    private static final String USER_HOME = "user.home";

    public SecurityListener(SmallBunProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {
        if (properties.getSecurity().isPasswordGenerated()) {
            String path = addSeparator(System.getProperty(USER_HOME)) + DIR_NAME + File.separator
                    + "secrets" + File.separator;
            createFile(path + "password");
            try {
                BufferedWriter stream = new BufferedWriter(
                        new FileWriter(new File(path + "password")));
                //清空
                stream.write(properties.getSecurity().getNativePassword());
                stream.flush();
                stream.close();
                // log
                logger.info(String.format("%n%nUsing generated security password: %s%n",
                        properties.getSecurity().getNativePassword()));
            } catch (IOException e) {
                System.exit(5907);
            }
        }
    }

    public static String addSeparator(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * SmallBunProperties
     */
    private final SmallBunProperties properties;

}
