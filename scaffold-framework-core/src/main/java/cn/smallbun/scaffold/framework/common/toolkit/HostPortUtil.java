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
package cn.smallbun.scaffold.framework.common.toolkit;

import org.springframework.util.StringUtils;

import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * HostPortUtil
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/10/31
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class HostPortUtil {

    /**
     * 0~255
     */
    private static final String  SEGMENT_REG = "((2[0-4]\\d)|(25[0-5])|(1\\d{2})|([1-9]\\d)|(\\d))";

    private static final Pattern IP_PATTERN  = getIpv4Reg();

    /**
     * 工具类隐藏构造方法
     */
    private HostPortUtil() {
    }

    /**
     * ipv4正则
     *
     * @return 正则对象
     */
    private static Pattern getIpv4Reg() {
        StringJoiner sj = new StringJoiner("\\.");
        sj.add(SEGMENT_REG);
        sj.add(SEGMENT_REG);
        sj.add(SEGMENT_REG);
        sj.add(SEGMENT_REG);
        return Pattern.compile(sj.toString());
    }

    /**
     * 非法ipv4抛异常
     *
     * @param ipv4 ipv4
     */
    private static void checkIPv4(String ipv4) {
        if (!isLegalV4(ipv4)) {
            throw new IllegalArgumentException("Illegal ipv4 value " + ipv4);
        }
    }

    /**
     * ip转换为对应32bit数字
     *
     * @param ipv4 点分十进制ipv4
     * @return ipv4 对应数字
     */
    private static long toDigital(String ipv4) {
        checkIPv4(ipv4);
        String[] segments = ipv4.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result += Long.parseLong(segments[3 - i]) << (8 * i);
        }
        return result;
    }

    /**
     * 检查是否合法ipv4
     *
     * @param ipv4 ipv4地址
     * @return 是否匹配
     */
    private static boolean isLegalV4(String ipv4) {
        return !StringUtils.isEmpty(ipv4) && IP_PATTERN.matcher(ipv4).matches();
    }

    /**
     * 数字转换为string ip格式
     *
     * @param ipv4 32位数字ip
     * @return 点分十进制ip
     */
    private static String fromDigital(Long ipv4) {
        if (ipv4 > 0xFFFFFFFFL || ipv4 < 0) {
            throw new IllegalArgumentException("Illegal ipv4 digital " + ipv4);
        }
        int[] segments = new int[4];
        for (int i = 0; i < 4; i++) {
            segments[3 - i] = ipv4.intValue() & 0xFF;
            ipv4 >>= 8;
        }
        StringJoiner sj = new StringJoiner(".");
        for (int segment : segments) {
            sj.add(String.valueOf(segment));
        }
        return sj.toString();
    }

}
