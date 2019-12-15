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


package cn.smallbun.scaffold.framework.web.configurer;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.web.interceptor.UserAgentInterceptor;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * web配置
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/11 13:58
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer, WebMvcRegistrations {
	private final SmallBunProperties smallBunProperties;
	private final Logger logger = LoggerFactory.getLogger(WebConfigurer.class);

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	public WebConfigurer(SmallBunProperties smallBunProperties) {
		this.smallBunProperties = smallBunProperties;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (!smallBunProperties.getWeb().getEnablePhone()) {
			registry.addInterceptor(new UserAgentInterceptor(smallBunProperties)).addPathPatterns("/**");
		}
	}

	/**
	 * 消息转换
	 * @param converters {@link List<HttpMessageConverter>}
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//1、定义一个convert转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		//2、添加fastJson的配置信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteDateUseDateFormat);
		//解决Long太长精度问题
		SerializeConfig serializeConfig = SerializeConfig.globalInstance;
		serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
		serializeConfig.put(Long.class, ToStringSerializer.instance);
		serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
		fastJsonConfig.setSerializeConfig(serializeConfig);
		//设置UTF-8
		fastConverter.setDefaultCharset(StandardCharsets.UTF_8);
		// 处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		//3、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		//4、将convert添加到converters中
		converters.add(0, fastConverter);
	}


	/**
	 * FormContentFilter
	 * @return {@link FormContentFilter}
	 */
	@Bean
	public FormContentFilter formContentFilter() {
		return new FormContentFilter();
	}
}
