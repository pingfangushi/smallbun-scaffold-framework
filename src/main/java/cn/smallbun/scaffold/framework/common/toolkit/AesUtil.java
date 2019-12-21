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
package cn.smallbun.scaffold.framework.common.toolkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

/**
 * AES加解密工具类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class AesUtil {
    private static Logger log = LoggerFactory.getLogger(AesUtil.class);

    public static String encryptAES(String content, String key) {
        try {
            return bytesToHexString(encryptAes(key.getBytes(), content.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param key     密钥信息
     * @param content 待加密信息
     */
    public static byte[] encryptAes(byte[] key, byte[] content) throws Exception {
        // 不是16的倍数的，补足
        int base = 16;
        key = getBytes(key, base);
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    public static String decryptAes(String content, String key) {
        try {
            byte[] contentByte = hexStringToByte(content);
            return new String(decryptAes(key.getBytes(), contentByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param key     密钥信息
     * @param content 待加密信息
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decryptAes(byte[] key, byte[] content) throws Exception {
        // 不是16的倍数的，补足
        int base = 16;
        key = getBytes(key, base);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    private static byte[] getBytes(byte[] key, int base) {
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        return key;
    }

    /**
     * 生成16位随机密钥
     *
     * @return 16位随机秘钥
     */
    public static String randomAesKey() {
        StringBuilder key = new StringBuilder();
        int length = 16;
        for (int i = 0; i < length; i++) {
            key.append(makeOneKey());
        }
        return key.toString();
    }

    private static String makeOneKey() {
        Random random = new Random();
        int randomInt = random.nextInt(16);
        int length = 10;
        if (randomInt > -1 && randomInt < length) {
            return String.valueOf(randomInt);
        } else {
            switch (randomInt) {
                case 10:
                    return "A";
                case 11:
                    return "B";
                case 12:
                    return "C";
                case 13:
                    return "D";
                case 14:
                    return "E";
                case 15:
                    return "F";
                default:
                    return "0";
            }
        }
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return byte[]
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] aChar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(aChar[pos]) << 4 | toByte(aChar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
