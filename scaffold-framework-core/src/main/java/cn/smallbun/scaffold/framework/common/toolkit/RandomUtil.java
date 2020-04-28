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

import java.util.Random;

/**
 * 随机数
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/26
 */
public class RandomUtil {

    /**
     * 生成六位随机数
     *
     * @return String
     */
    public static String random() {

        StringBuilder buffer = new StringBuilder();
        //添加三位随机数
        //生成三个 0-9
        int num1, num2, num3, num4, num5, num6;
        Random rnd = new Random();
        num1 = rnd.nextInt(9);
        num2 = rnd.nextInt(9);
        num3 = rnd.nextInt(9);
        num4 = rnd.nextInt(9);
        num5 = rnd.nextInt(9);
        num6 = rnd.nextInt(9);

        String num = num1 + "" + num2 + "" + num3 + "" + num4 + "" + num5 + "" + num6;
        buffer.append(num);

        return buffer.toString();
    }

    /**
     * 获得指定长度随机数
     *
     * @param length length
     * @return String
     */
    public static String random(int length) {
        String result = "";
        if (length > 0) {
            StringBuffer sb = new StringBuffer();
            Random rnd = new Random();
            for (int i = 0; i < length; i++) {
                int num = rnd.nextInt(9);
                sb.append(num);
            }
            result = sb.toString();
        }
        return result;
    }

    public static int randomSet(int lastNumber, int number) {
        Random random = new Random();
        int num = random.nextInt(number);

        while (num == lastNumber) {
            num = random.nextInt(number);
        }

        return num;
    }

}
