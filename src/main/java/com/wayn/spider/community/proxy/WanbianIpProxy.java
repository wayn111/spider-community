package com.wayn.spider.community.proxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;

@Component
public class WanbianIpProxy implements IpProxyInterface {


    @Value("${ipProxy.wanbian.api}")
    private String proxyApi;

    @Override
    public Proxy getProxy() {
        HttpGet httpGet = new HttpGet(proxyApi);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        Proxy proxy = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求代理IP接口失败");
            }
            String strResult = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = JSONObject.parseObject(strResult).getJSONArray("data");
            // 接口返回异常时，重新获取
            if (jsonArray.size() == 0) {
                getProxy();
            }
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            proxy = new Proxy((String) jsonObject.get("IP"),
                    (Integer) jsonObject.get("Port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
