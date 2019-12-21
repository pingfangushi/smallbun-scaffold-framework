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
package cn.smallbun.scaffold.framework.security.jwt;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.security.domain.User;
import com.google.common.collect.Lists;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 令牌提供者
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/7
 */
public class TokenProvider implements InitializingBean {

    private final Logger        logger          = LoggerFactory.getLogger(TokenProvider.class);
    /**
     * authorities
     */
    private static final String AUTHORITIES_KEY = "AUTHORITIES";
    /**
     * 用户ID
     */
    private static final String USER_ID         = "USER_ID";
    /**
     * 加密key
     */
    private Key                 key;
    /**
     * 令牌有效性（以毫秒为单位）
     */
    private long                tokenValidityInMilliseconds;
    /**
     * 记住我的令牌有效性（以毫秒为单位）
     */
    private long                tokenValidityInMillisecondsForRememberMe;

    public TokenProvider(SmallBunProperties properties) {
        this.security = properties.getSecurity();
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes;
        String secret = security.getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            logger
                .warn("警告：使用的JWT密钥不是Base64编码的. "
                      + "我们建议使用`cn.smallbun.scaffold.security.authentication.jwt.base64-secret`密钥以获得最佳安全性.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            logger.debug("使用Base64编码的JWT密钥");
            keyBytes = Decoders.BASE64
                .decode(security.getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * security.getAuthentication().getJwt()
            .getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe = 1000 * security.getAuthentication().getJwt()
            .getTokenValidityInSecondsForRememberMe();
    }

    /**
     * 创建token
     * @param authentication {@link Authentication} authentication 授权
     * @param rememberMe {@link Boolean} rememberMe 是否记住我
     * @return token
     */
    public String createToken(Authentication authentication, boolean rememberMe) {
        //权限
        String auth = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity;
        //是否记住我
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        //封装JWT
        JwtBuilder builder = Jwts.builder().setSubject(authentication.getName())
            //设置自定义JWT Claims参数值。这里放置权限集合
            .claim(AUTHORITIES_KEY, auth)
            //用户ID
            .claim(USER_ID, ((User) authentication.getPrincipal()).getId())
            //签名
            .signWith(key, SignatureAlgorithm.HS512)
            //过期时间
            .setExpiration(validity);
        return builder.compact();
    }

    /**
     * 获得身份验证
     * @param token token
     * @return  {@link Authentication}
     */
    Authentication getAuthentication(String token) {
        //获取Claims
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        //获取权限
        Collection<? extends GrantedAuthority> authorities = Lists.newArrayList();
        //有权限的时候处理权限、没有不处理
        if (!StringUtils.isEmpty(claims.get(AUTHORITIES_KEY).toString())) {
            authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        //封装用户
        User user = new User(claims.getSubject(), "", claims.get(USER_ID).toString(), authorities);
        //返回认证
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    /**
     * 验证token
     * @param authToken token
     * @return 是否通过
     */
    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
            logger.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            logger.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
            logger.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
            logger.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    private final SmallBunProperties.Security security;
}
