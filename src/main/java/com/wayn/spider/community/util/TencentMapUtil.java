package com.wayn.spider.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.IOException;

@Component
public class TencentMapUtil {

    private static String host;
    private static String key;
    private static String secretKey;

    public static String getHost() {
        return host;
    }

    @Value("${tencent.map.host}")
    public void setHost(String host) {
        TencentMapUtil.host = host;
    }

    public static String getKey() {
        return key;
    }

    @Value("${tencent.map.key}")
    public void setKey(String key) {
        TencentMapUtil.key = key;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    @Value("${tencent.map.secretKey}")
    public void setSecretKey(String secretKey) {
        TencentMapUtil.secretKey = secretKey;
    }

    public static JSONObject geocoder(String lon, String lat) {
        String params = "/ws/geocoder/v1/?key=" + key + "&location=" + lat + "," + lon;
        String md5Hex = DigestUtils.md5DigestAsHex((params + secretKey).getBytes());
        params += "&sig=" + md5Hex;
        HttpGet httpGet = new HttpGet(host + params);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                return JSONObject.parseObject(strResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
