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
        DynamicProxyDownload httpClientDownloader = new DynamicProxyDownload(ipProxy);
        Spider.create(new AnjukePageProcessor()).addUrl("https://www.anjuke.com/sy-city.html").setDownloader(httpClientDownloader).thread(10).run();
        // Spider.create(new AnjukePageProcessor()).addUrl("https://ali.anjuke.com/community/p1/").setDownloader(httpClientDownloader).thread(1).run();
    }

}
