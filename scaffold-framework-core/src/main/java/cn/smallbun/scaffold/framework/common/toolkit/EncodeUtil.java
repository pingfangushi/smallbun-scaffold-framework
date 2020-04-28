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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/10/25 20:14
 */
public class EncodeUtil {
    private static Logger log = LoggerFactory.getLogger(EncodeUtil.class);

    /**
     * HEX编码
     *
     * @param dataByteAry 待编码数据-byte数组
     * @return char[]
     */
    public static char[] enByHEX(byte[] dataByteAry) {
        return Hex.encodeHex(dataByteAry, false);
    }

    /**
     * HEX解码
     *
     * @param dataCharAry 待解码数据-char数组
     * @return byte[]
     */
    public static byte[] deByHEX(char[] dataCharAry) {
        try {
            return Hex.decodeHex(dataCharAry);
        } catch (DecoderException e) {
            log.error("HEX解码发生异常", e);
            return null;
        }
    }

    /**
     * BASE64编码（数组->数组）
     *
     * @param dataByteAry 待编码数据-byte数组
     * @return 数组
     */
    public static byte[] enByBASE64(byte[] dataByteAry) {
        return Base64.encodeBase64(dataByteAry);
    }

    /**
     * BASE64解码（数组->数组）
     *
     * @param dataByteAry 待解码数据-byte数组
     * @return byte[]
     */
    public static byte[] deByBASE64(byte[] dataByteAry) {
        return Base64.decodeBase64(dataByteAry);
    }

    /**
     * byte字节数组 -> 二进制字符串 ,eg:([96, 0, 4, 0, 0] -> 000100011001...) 注：转换位图时使用
     *
     * @param byteAry byteAry
     * @return String
     */
    public static String byteAryToBinary(byte[] byteAry) {
        StringBuilder sbf = new StringBuilder(byteAry.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * byteAry.length; i++) {
            sbf.append((byteAry[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sbf.toString();
    }

    /**
     * 二进制字符串 -> 字节数组
     *
     * @param binaryStr binaryStr
     * @return byte[]
     */
    public static byte[] binaryToByteAry(String binaryStr) {
        int byteSize = (binaryStr.length() + Byte.SIZE - 1) / Byte.SIZE;
        int strLen = byteSize * Byte.SIZE;
        binaryStr = String.format("%" + strLen + "s", binaryStr).replace(' ', '0');
        byte[] byteAry = new byte[byteSize];
        char c;
        for (int i = 0; i < strLen; i++) {
            if ((c = binaryStr.charAt(i)) == '1') {
                byteAry[i / Byte.SIZE] = (byte) (byteAry[i / Byte.SIZE] | 0x80 >>> (i % Byte.SIZE));
            } else if (c != '0') {
                log.error("传入的二进制字符串必须为0或1，第" + i + "位=" + c);
                return null;
            }
        }
        return byteAry;
    }

    /**
     * 字节数组转换为十六进制-（数组->字符串）
     *
     * @param byteAry binaryStr
     * @return String
     */
    public static String byteAryToHex(byte[] byteAry) {
        return new String(enByHEX(byteAry));
    }

    /**
     * 十六进制转换为字节数组-（字符串->数组）
     *
     * @param hex hex
     * @return byte[]
     */
    public static byte[] hexToByteAry(String hex) {
        return deByHEX(hex.toCharArray());
    }

    /**
     * 十六进制转换为字节数组（指定转换后的长度）
     *
     * @param hex         待转换数据
     * @param byteArySize 转换后的字节长度，前面补0
     * @return byte[]
     */
    public static byte[] hexToByteAry(String hex, int byteArySize) {
        if (byteArySize <= 0) {
            return new byte[0];
        }
        hex = String.format("%" + byteArySize * 2 + "s", hex).replace(' ', '0');
        return hexToByteAry(hex);
    }
}
