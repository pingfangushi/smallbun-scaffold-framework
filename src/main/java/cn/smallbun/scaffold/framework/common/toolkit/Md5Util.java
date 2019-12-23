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
package cn.smallbun.scaffold.framework.common.toolkit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class Md5Util {

    /**
     * 16位加密
     *
     * @param plainText plainText
     * @return String
     */
    public static String md516(String plainText) {
        return md516(plainText, StandardCharsets.UTF_8.name());
    }

    /**
     * 16位加密
     *
     * @param plainText plainText
     * @return String
     */
    public static String md516(String plainText, String charSet) {
        String result = null;
        try {
            byte[] ptBytes = plainText.getBytes(charSet);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ptBytes);
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // result = buf.toString(); //md5 32bit
            // result = buf.toString().substring(8, 24))); //md5 16bit
            result = buf.toString().substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 32位加密
     *
     * @param plainText plainText
     * @return String
     */
    public static String md532(String plainText) {
        return md532(plainText, StandardCharsets.UTF_8.name());
    }

    /**
     * 32位加密
     *
     * @param plainText plainText
     * @return String
     */
    public static String md532(String plainText, String charSet) {
        String result = null;
        try {
            byte[] ptBytes = plainText.getBytes(charSet);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ptBytes);
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // result = buf.toString(); //md5 32bit
            // result = buf.toString().substring(8, 24))); //md5 16bit
            result = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj.getBytes(StandardCharsets.ISO_8859_1));
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = StringUtil.byteToHexString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
        return resultString;
    }

    public static String encode(String text) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0").append(hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
