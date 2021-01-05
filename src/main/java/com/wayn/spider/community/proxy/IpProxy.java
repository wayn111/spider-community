package com.wayn.spider.community.proxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;

@Component
public class IpProxy {

    public JSONArray getWanbianProxyPool() {
        HttpGet httpGet = new HttpGet("http://ip.ipjldl.com/index.php/api/entry?method=proxyServer.tiqu_api_url&packid=0&fa=0&dt=0&groupid=0&fetch_key=&qty=1&time=1&port=1&format=json&ss=5&css=&dt=0&pro=&city=&usertype=6");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                return JSONObject.parseObject(strResult).getJSONArray("data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Proxy wanbianProxy() {
        HttpGet httpGet = new HttpGet("http://ip.ipjldl.com/index.php/api/entry?method=proxyServer.tiqu_api_url&packid=0&fa=0&dt=0&groupid=0&fetch_key=&qty=1&time=1&port=1&format=json&ss=5&css=&dt=0&pro=&city=&usertype=6");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = JSONObject.parseObject(strResult).getJSONArray("data");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                return new Proxy((String) jsonObject.get("IP"),
                        (Integer) jsonObject.get("Port"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
