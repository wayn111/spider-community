package com.wayn.spider.community.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.font.PrismFontStrike;
import com.wayn.spider.community.core.dto.GeocoderDTO;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Objects;

public class TentMapUtil {
    public static final String HOST = "https://apis.map.qq.com";
    public static final String KEY = "HMCBZ-NBLK3-I6J37-YV6XE-FA4FT-BSFTX";
    public static final String SECRET_KEY = "xx2TvMFRFYsEveDwUN57fnpfrnRvaTKq";

    public static JSONObject geocoder(String lon, String lat) {
        String params = "/ws/geocoder/v1/?key=" + KEY + "&location=39.984154,116.307490";
        String md5Hex = DigestUtils.md5DigestAsHex((params + SECRET_KEY).getBytes());
        String hash = Md5Utils.hash(params + SECRET_KEY);
        params += "&sig=" + md5Hex;
        HttpGet httpGet = new HttpGet(HOST + params);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                return JSONObject.parseObject(strResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
