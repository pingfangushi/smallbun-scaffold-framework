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


package cn.smallbun.scaffold.framework.common.toolkit;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DesUtil
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class DesUtil {
	private final static String DES = "DES/CBC/PKCS5Padding";
	private final static String ENCODING = "UTF-8";

	/**
	 * sha1 安全加密算法
	 *
	 * @param decrypt
	 * @param upper   结果是否大写
	 * @return String
	 * @throws DigestException DigestException
	 */
	public static String sha1(String decrypt, boolean upper) throws DigestException {
		try {
			//指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			//获取字节数组
			byte[] bytes = digest.digest();
			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			// 字节数组转换为 十六进制 数
			for (byte aByte : bytes) {
				String shaHex = Integer.toHexString(aByte & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			if (upper) {
				return hexString.toString().toUpperCase();
			} else {
				return hexString.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new DigestException("签名错误！");
		}
	}

	/**
	 * DES加密字符串
	 *
	 * @param encryptString 待加密的字符串
	 * @param encryptKey    加密密钥,要求为8位
	 * @return 加密成功返回加密后的字符串，失败返回源串
	 */
	public static String encryptdes(String encryptString, String encryptKey, byte[] ivP) {
		byte[] ret;

		try {
			// 从原始密匙数据创建DESKeySpec对象
			byte[] key = encryptKey.getBytes(ENCODING);
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secureKey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES);
			// 用密匙初始化Cipher对象
			IvParameterSpec iv = new IvParameterSpec(ivP);
			cipher.init(Cipher.ENCRYPT_MODE, secureKey, iv);
			// 获取数据并加密
			byte[] src = encryptString.getBytes(ENCODING);
			src = cipher.doFinal(src);
			Base64 enc = new Base64();
			ret = enc.encode(src);
		} catch (Exception ex) {
			ret = encryptString.getBytes();
		}
		return new String(ret);
	}

	/**
	 * DES解密字符串
	 *
	 * @param decryptString 待解密的字符串
	 * @param decryptKey    解密密钥,要求为8位,和加密密钥相同
	 * @return 解密成功返回解密后的字符串，失败返源串
	 */
	public static String decryptdes(String decryptString, String decryptKey, byte[] ivP) {
		String ret;
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			byte[] key = decryptKey.getBytes(ENCODING);
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DES);
			// 用密匙初始化Cipher对象
			IvParameterSpec iv = new IvParameterSpec(ivP);
			cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
			// 获取数据并解密
			Base64 dnc = new Base64();
			byte[] src = dnc.decode(decryptString.getBytes());
			src = cipher.doFinal(src);
			ret = new String(src, 0, src.length, ENCODING);
		} catch (Exception ex) {
			ret = decryptString;
		}
		return ret;
	}

	/**
	 * DES加密字符串
	 *
	 * @param encryptString 待加密的字符串
	 * @param encryptKey    加密密钥,要求为8位
	 * @return 加密成功返回加密后的字符串，失败返回源串
	 */
	public static byte[] encryptdes(byte[] encryptString, String encryptKey, byte[] ivP) {
		byte[] ret;

		try {
			// 从原始密匙数据创建DESKeySpec对象
			byte[] key = encryptKey.getBytes(ENCODING);
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secureKey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES);
			// 用密匙初始化Cipher对象
			IvParameterSpec iv = new IvParameterSpec(ivP);
			cipher.init(Cipher.ENCRYPT_MODE, secureKey, iv);
			// 获取数据并加密
			ret = cipher.doFinal(encryptString);
		} catch (Exception ex) {
			ret = encryptString;
		}
		return ret;
	}

	/**
	 * DES解密字符串
	 *
	 * @param decryptString 待解密的字符串
	 * @param decryptKey    解密密钥,要求为8位,和加密密钥相同
	 * @return 解密成功返回解密后的字符串，失败返源串
	 */
	public static byte[] decryptDES(byte[] decryptString, String decryptKey, byte[] ivP) {
		byte[] ret;
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			byte[] key = decryptKey.getBytes(ENCODING);
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secureKey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DES);
			// 用密匙初始化Cipher对象
			IvParameterSpec iv = new IvParameterSpec(ivP);
			cipher.init(Cipher.DECRYPT_MODE, secureKey, iv);
			// 获取数据并解密
			ret = cipher.doFinal(decryptString);
		} catch (Exception ex) {
			ret = decryptString;
		}
		return ret;
	}
}
