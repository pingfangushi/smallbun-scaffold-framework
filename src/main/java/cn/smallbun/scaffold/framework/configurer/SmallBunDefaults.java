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
package cn.smallbun.scaffold.framework.configurer;

/**
 * SmallBun 企业级脚手架配置
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/8
 */
public interface SmallBunDefaults {

    String DEFAULT_PREFIX = "cn.smallbun.scaffold";

    /**
     * 项目信息
     */
    interface Project {
        /**
         * 名称
         */
        String NAME       = "smallbun 企业级脚手架";
        /**
         *
         */
        String POWERED_BY = "http://www.smallbun.cn";
    }

    /**
     * 验证码
     */
    interface Captcha {
        /**
         * 是否启用
         */
        boolean ENABLE                    = false;
        /**
         * 是否有边框
         */
        String  BORDER                    = "no";
        /**
         * 图像宽度
         */
        String  IMAGE_WIDTH               = "240";
        /**
         * 图像高度
         */
        String  IMAGE_HEIGHT              = "80";
        /**
         * 文本生产者字体颜色
         */
        String  TEXT_PRODUCER_FONT_COLOR  = "black";
        /**
         * 文字制作者字体大小
         */
        String  TEXT_PRODUCER_FONT_SIZE   = "60";
        /**
         * 文字制片人实现
         */
        String  TEXT_PRODUCER_IMPL        = "com.google.code.kaptcha.impl.ShadowGimpy";
        /**
         * 文本生产者字符长度
         */
        String  TEXT_PRODUCER_CHAR_LENGTH = "5";
        /**
         * 文本生产者字体名称
         */
        String  TEXT_PRODUCER_FONT_NAMES  = "宋体,楷体,微软雅黑";
    }

    /**
     * 演示环境
     * @author SanLi
     * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/8
     */
    interface Demo {
        boolean OPEN = false;
    }

    /***
     * 用户相关
     */
    interface User {
        /**
         * 注册默认密码
         */
        String REGISTER_DEFAULT_PASSWORD = "$2a$10$SkPLa0RwRFrjyv1YterZtucAtjrPgYXi6zGXbjmEpolt10AcKZBqW";
    }

    /**
     * 安全配置
     */
    interface Web {
        /**
         * 启用手机访问
         */
        Boolean ENABLE_PHONE  = false;
        /**
         * 如果是手机访问重定向地址
         */
        String  IS_PHONE_PATH = "";
    }

    /**
     * 安全配置
     */
    interface Security {
        /**
         * 隐藏用户未找到异常
         */
        Boolean HIDE_USER_NOT_FOUND = true;
        /**
         * USERNAME
         */
        String  USERNAME            = "root";
        /**
         * ID
         */
        String  ID                  = "5dd89cff-7e69-443b-8e80-9b95e385eed8";

        /**
         * 认证方式
         */
        interface Authentication {
            /**
             * jwt
             */
            interface Jwt {
                /**
                 * SECRET
                 */
                String SECRET                                    = null;
                /**
                 * base64
                 */
                String BASE64_SECRET                             = "=OWYwZWYzMTk2YTc3Zjg2NmM3ZWE1Nzc2YzI2NDFkZDE2MmQ4MDk1YTAwNzk3ZjNiMTdlY2VhZjcxZmJkOGI0OGRhZDhkYWQ3MWU3ZjdmMzc3NTMwNTJiMGM0OThkZDg3YTA4MzFjMTA4MzAxN2E1NmJlYzQ4MTdjNWFkZmI1ZDU";
                /**
                 * 30分钟
                 */
                long   TOKEN_VALIDITY_IN_SECONDS                 = 1800;
                /**
                 * 记住我 30 天
                 */
                long   TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME = 2592000;
            }
        }
    }
}
