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

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

/**
 * 3DES加密工具类
 *
 * @author SanLi
 * Created  by 2689170096@qq.com  on 2018/1/15
 */
public class Des3Util {
    /**
     * 密钥 长度不得小于24
     */
    public final static String  DES_KEY = "Klu&pVSlHSD@2M#p6NRRjv0Gz#nkGAMC#fUfxdpWuKnb3oXG%%zb^tywMN97bU2HxqHy&50dMQSOgP#mNvNIvfOg0z8u!BY2ZeL";
    /**
     * 向量 可有可无 终端后台也要约定
     */
    private final static String IV      = "01234567";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return String
     */
    public static String encode(String plainText, String key, String encoding) {
        try {
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.getUrlEncoder().encodeToString(encryptData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return String
     */
    public static String decode(String encryptText, String key, String encoding) {
        try {
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, desKey, ips);
            byte[] decryptData = cipher.doFinal(Base64.getUrlDecoder().decode(encryptText));
            return new String(decryptData, encoding);
        } catch (Exception e) {
            throw new RuntimeException("3DES 解密异常");
        }
    }
}
