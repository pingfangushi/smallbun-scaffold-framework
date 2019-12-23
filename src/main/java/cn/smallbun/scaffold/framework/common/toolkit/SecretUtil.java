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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class SecretUtil {

    /**
     * 定义加密算法，有DES、DESede(即3DES)、Blowfish
     */
    private static final String ALGORITHM = "DESede";

    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @param key key
     * @return byte[]
     */
    public static byte[] encryptMode(byte[] src, String key) {
        try {
            // 生成密钥
            SecretKey desKey = new SecretKeySpec(build3DesKey(key), ALGORITHM);
            // 实例化负责加密/解密的Cipher工具类
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            // 初始化为加密模式
            c1.init(Cipher.ENCRYPT_MODE, desKey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src 密文的字节数组
     * @return byte[]
     */
    public static byte[] decryptMode(byte[] src, String key) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(key), ALGORITHM);
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            // 初始化为解密模式
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     * @return byte[]
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        // 声明一个24位的字节数组，默认里面都是0

        byte[] key = new byte[24];
        // 将字符串转成字节数组
        byte[] temp = keyStr.getBytes(StandardCharsets.UTF_8);

        /*
         * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
