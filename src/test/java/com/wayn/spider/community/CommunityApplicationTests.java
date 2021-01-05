package com.wayn.spider.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wayn.spider.community.download.DynamicProxyDownload;
import com.wayn.spider.community.proxy.IpProxy;
import com.wayn.spider.community.spider.AnjukePageProcessor;
import com.wayn.spider.community.spider.GithubRepoPageProcessor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunityApplication.class)
@EnableAutoConfiguration
class CommunityApplicationTests {

    @Autowired
    private GithubRepoPageProcessor githubRepoPageProcessor;
    @Autowired
    private AnjukePageProcessor anjukePageProcessor;
    @Autowired
    private IpProxy ipProxy;

    @Test
    void contextLoads() {
        JSONArray wanbianProxyPool = ipProxy.getWanbianProxyPool();
        List<Proxy> proxyList = new ArrayList<>();
        for (Object o : wanbianProxyPool) {
            JSONObject jsonObject = (JSONObject) o;
            Proxy proxy = new Proxy((String) jsonObject.get("IP"),
                    (Integer) jsonObject.get("Port"));
            proxyList.add(proxy);
        }
        DynamicProxyDownload httpClientDownloader = new DynamicProxyDownload(ipProxy);
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxyList.toArray(new Proxy[]{})));

        Spider.create(new AnjukePageProcessor()).addUrl("https://www.anjuke.com/sy-city.html").setDownloader(httpClientDownloader).thread(15).run();
    }

}
