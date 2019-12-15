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

package cn.smallbun.scaffold.framework.configurer;

import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import cn.smallbun.scaffold.framework.initialize.ContextInitListener;
import cn.smallbun.scaffold.framework.security.jwt.JwtConfigurer;
import cn.smallbun.scaffold.framework.security.jwt.JwtFilter;
import cn.smallbun.scaffold.framework.security.jwt.TokenProvider;
import cn.smallbun.scaffold.framework.security.listener.SecurityListener;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.Serializable;

import static com.google.code.kaptcha.Constants.*;

/**
 * SmallBunConfigurer
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/11/30 21:07
 */
@EnableConfigurationProperties(value = SmallBunProperties.class)
@Configuration
public class SmallBunConfigurer implements Serializable {
	private final SmallBunProperties properties;

	public SmallBunConfigurer(SmallBunProperties properties) {
		this.properties = properties;
	}

	/**
	 * contextInitListener
	 * @return {@link ContextInitListener}
	 */
	@Bean
	@ConditionalOnMissingBean
	public ContextInitListener contextInitListener() {
		return new ContextInitListener();
	}

	/**
	 * securityListener
	 * @return {@link ContextInitListener}
	 */
	@Bean
	@ConditionalOnMissingBean
	public SecurityListener securityListener() {
		return new SecurityListener();
	}

	/**
	 * 配置验证码
	 *
	 * @return DefaultKaptcha
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = SmallBunDefaults.DEFAULT_PREFIX + ".captcha.enable", havingValue = "true")
	public DefaultKaptcha captchaProducer() {
		DefaultKaptcha captchaProducer = new DefaultKaptcha();
		SmallBunProperties.Captcha captcha = properties.getCaptcha();
		java.util.Properties properties = new java.util.Properties();
		properties.setProperty(KAPTCHA_BORDER, captcha.getBorder());
		properties.setProperty(KAPTCHA_IMAGE_WIDTH, captcha.getImageWidth());
		properties.setProperty(KAPTCHA_IMAGE_HEIGHT, captcha.getImageHeight());
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, captcha.getTextProducerFontColor());
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, captcha.getTextProducerFontSize());
		properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, captcha.getTextProducerImpl());
		properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, captcha.getTextProducerCharLength());
		properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, captcha.getTextProducerFontNames());
		Config config = new Config(properties);
		captchaProducer.setConfig(config);
		return captchaProducer;
	}

	/**
	 * DaoAuthenticationProvider
	 * @return  {@link DaoAuthenticationProvider}
	 */
	@Bean
	@ConditionalOnMissingBean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		//密码配置
		provider.setPasswordEncoder(passwordEncoder());
		//设置隐藏未找到用户
		provider.setHideUserNotFoundExceptions(properties.getSecurity().getHideUserNotFound());
		//设置用户详细信息服务
		provider.setUserDetailsService(ApplicationContextHelp.getBean(UserDetailsService.class));
		return provider;
	}

	/**
	 * JwtConfigurer
	 * @return {@link JwtFilter}
	 */
	@Bean
	@ConditionalOnMissingBean
	public JwtConfigurer jwtFilter() {
		return new JwtConfigurer(tokenProvider());
	}

	/**
	 * 密码，使用 BCryptPasswordEncoder
	 * @return  {@link PasswordEncoder}
	 */
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * token提供者
	 * @return {@link TokenProvider}
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenProvider tokenProvider() {
		return new TokenProvider(properties);
	}

	/**
	 * AuthenticationEntryPoint
	 * @return {@link AuthenticationEntryPoint}
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> ApplicationContextHelp.getBean(HandlerExceptionResolver.class)
				.resolveException(request, response, null, authException);
	}

	/**
	 * AccessDeineHandler
	 * @return {@link AccessDeniedHandler}
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> ApplicationContextHelp
				.getBean(HandlerExceptionResolver.class)
				.resolveException(request, response, null, accessDeniedException);
	}
}
