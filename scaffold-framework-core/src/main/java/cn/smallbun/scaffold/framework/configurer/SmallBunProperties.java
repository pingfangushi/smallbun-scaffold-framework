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
package cn.smallbun.scaffold.framework.configurer;

import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static cn.smallbun.scaffold.framework.configurer.SmallBunDefaults.Exception.SHOW_STACK;

/**
 * smallbun 特定的配置属性
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/10/25 20:43
 */
@Data
@ConfigurationProperties(value = SmallBunDefaults.DEFAULT_PREFIX, ignoreUnknownFields = false)
public class SmallBunProperties {
    /**
     * 验证码
     */
    @NestedConfigurationProperty
    private final Captcha   captcha   = new Captcha();
    /**
     * 演示环境
     */
    @NestedConfigurationProperty
    private final Demo      demo      = new Demo();
    /**
     * 用户相关
     */
    @NestedConfigurationProperty
    private final User      user      = new User();
    /**
     * 安全配置
     */
    @NestedConfigurationProperty
    private final Web       web       = new Web();
    /**
     * 安全配置
     */
    @NestedConfigurationProperty
    private final Security  security  = new Security();
    /**
     * 异常配置
     */
    @NestedConfigurationProperty
    private final Exception exception = new Exception();

    /**
     * 异常配置
     *
     * @author SanLi
     * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2020/4/17
     */
    @Data
    public static class Exception {
        /**
         * 显示堆栈
         */
        private boolean showStack = SHOW_STACK;
    }

    /**
     * 验证码
     *
     * @author SanLi
     * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/8
     */
    @Getter
    @Setter
    public static class Captcha {
        boolean enable                 = SmallBunDefaults.Captcha.ENABLE;
        String  border                 = SmallBunDefaults.Captcha.BORDER;
        String  imageWidth             = SmallBunDefaults.Captcha.IMAGE_WIDTH;
        String  imageHeight            = SmallBunDefaults.Captcha.IMAGE_HEIGHT;
        String  textProducerFontColor  = SmallBunDefaults.Captcha.TEXT_PRODUCER_FONT_COLOR;
        String  textProducerFontSize   = SmallBunDefaults.Captcha.TEXT_PRODUCER_FONT_SIZE;
        String  textProducerImpl       = SmallBunDefaults.Captcha.TEXT_PRODUCER_IMPL;
        String  textProducerCharLength = SmallBunDefaults.Captcha.TEXT_PRODUCER_CHAR_LENGTH;
        String  textProducerFontNames  = SmallBunDefaults.Captcha.TEXT_PRODUCER_FONT_NAMES;
    }

    /**
     * 演示环境
     */
    @Getter
    @Setter
    public static class Demo {
        /**
         * 是否开启
         */
        boolean open = SmallBunDefaults.Demo.OPEN;
    }

    /**
     * 用户相关
     */
    @Getter
    @Setter
    public static class User {
        /**
         * 默认密码
         */
        String registerDefaultPassword = SmallBunDefaults.User.REGISTER_DEFAULT_PASSWORD;
    }

    /**
     * Web配置
     */
    @Getter
    @Setter
    public static class Web {
        /**
         * 手机访问
         */
        Boolean enablePhone = SmallBunDefaults.Web.ENABLE_PHONE;
        /**
         * 重定向地址
         */
        String  isPhonePath = SmallBunDefaults.Web.IS_PHONE_PATH;
    }

    /**
     * 安全配置
     */
    @Getter
    @Setter
    public static class Security {
        /**
         * ID
         */
        private String id                = SmallBunDefaults.Security.ID;
        /**
         * 全局用户名，超级管理员
         */
        private String username          = SmallBunDefaults.Security.USERNAME;
        /**
         * 密码
         */
        private String password          = UUID.randomUUID().toString();
        /**
         * passwordGenerated
         */
        boolean        passwordGenerated = true;

        public void setPassword(String password) {
            if (!StringUtils.hasLength(password)) {
                return;
            }
            this.passwordGenerated = false;
            this.password = ApplicationContextHelp.getBean(PasswordEncoder.class).encode(password);
        }

        public String getPassword() {
            if (isPasswordGenerated()) {
                return ApplicationContextHelp.getBean(PasswordEncoder.class).encode(this.password);
            }
            return password;
        }

        public String getNativePassword() {
            return password;
        }

        /**
         * 隐藏用户不存在
         */
        Boolean                      hideUserNotFound = SmallBunDefaults.Security.HIDE_USER_NOT_FOUND;
        /**
         * 认证方式
         */
        @NestedConfigurationProperty
        private final Authentication authentication   = new Authentication();

        /**
         * 认证方式
         */
        @Getter
        @Setter
        public static class Authentication {
            /**
             * jwt
             */
            @NestedConfigurationProperty
            private final Jwt jwt = new Jwt();

            @Getter
            @Setter
            public static class Jwt {
                /**
                 * 秘钥
                 */
                private String secret                              = SmallBunDefaults.Security.Authentication.Jwt.SECRET;
                /**
                 * base64秘钥
                 */
                private String base64Secret                        = SmallBunDefaults.Security.Authentication.Jwt.BASE64_SECRET;
                /**
                 * 默认令牌有效期
                 */
                private long   tokenValidityInSeconds              = SmallBunDefaults.Security.Authentication.Jwt.TOKEN_VALIDITY_IN_SECONDS;
                /**
                 * 记住我令牌有效期
                 */
                private long   tokenValidityInSecondsForRememberMe = SmallBunDefaults.Security.Authentication.Jwt.TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME;
            }
        }
    }
}
