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
package cn.smallbun.scaffold.framework.common.address;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

import static cn.smallbun.scaffold.framework.common.toolkit.HttpClientUtil.client;
import static cn.smallbun.scaffold.framework.common.toolkit.IpUtil.internalIp;

/**
 * 城市工具服务
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/11/5 11:55
 */
public class Address implements Serializable {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Address.class);
    /**
     * 淘宝IP地址库
     */
    private static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    /**
     * 根据IP获取区域信息 淘宝库
     * @param ip ip 地址
     * @return 例: 中国 山东 济南 联通
     */
    public static String getCityInfoByTaoBao(String ip) {
        try {
            //如果不是内部IP
            if (!internalIp(ip)) {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("ip", ip);
                String client = client(IP_URL, HttpMethod.POST, params);
                if (StringUtils.isNotBlank(client)) {
                    Result result = JSONObject.parseObject(client, Result.class);
                    if (result.getCode() == 0) {
                        return result.getData().getCountry() + " " + result.getData().getRegion()
                               + " " + result.getData().getCity() + " " + result.getData().getIsp();
                    }
                }
                return "";
            }
            return "内网地址";
        } catch (Exception e) {
            return "";
        }
    }

    @Data
    public static class Result {
        /**
         * code
         */
        private int      code;
        /**
         * data
         */
        private DataBean data;

        @Data
        static class DataBean {
            private String ip;
            private String country;
            private String area;
            private String region;
            private String city;
            private String county;
            private String isp;
            @JSONField(name = "country_id")
            private String countryId;
            @JSONField(name = "area_id")
            private String areaId;
            @JSONField(name = "region_id")
            private String regionId;
            @JSONField(name = "city_id")
            private String cityId;
            @JSONField(name = "county_id")
            private String countyId;
            @JSONField(name = "isp_id")
            private String ispId;
        }
    }

    /**
     * 根据IP获取地址，本地库
     * @param ip ip 地址
     * @return 地址
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String getCityInfoByDb(String ip) {
        if (!internalIp(ip)) {
            DbSearcher searcher = null;
            try {
                String dbPath = Address.class.getResource("/ip2region/ip2region.db").getPath();
                File file = new File(dbPath);
                if (!file.exists()) {
                    String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                    dbPath = tmpDir + "ip.db";
                    file = new File(dbPath);
                    FileUtils.copyInputStreamToFile(Objects.requireNonNull(Address.class
                        .getClassLoader().getResourceAsStream("ip2region/ip2region.db")), file);
                }
                DbConfig config = new DbConfig();
                searcher = new DbSearcher(config, file.getPath());
                Method method;
                method = searcher.getClass().getMethod("memorySearch", String.class);
                DataBlock dataBlock;
                if (!Util.isIpAddress(ip)) {
                    LOGGER.error("Error: Invalid ip address");
                }
                dataBlock = (DataBlock) method.invoke(searcher, ip);
                String address = dataBlock.getRegion().replace("0|", "");
                if (address.charAt(address.length() - 1) == '|') {
                    address = address.substring(0, address.length() - 1);
                }
                return address;
            } catch (Exception e) {
                LOGGER.error("获取地址信息异常", e);
            } finally {
                if (searcher != null) {
                    try {
                        searcher.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "";
        }
        return "内网地址";
    }
}
