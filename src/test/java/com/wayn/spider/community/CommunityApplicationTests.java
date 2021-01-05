package com.wayn.spider.community;

import com.wayn.spider.community.download.DynamicProxyDownload;
import com.wayn.spider.community.proxy.IpProxy;
import com.wayn.spider.community.spider.AnjukePageProcessor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommunityApplication.class)
@EnableAutoConfiguration
class CommunityApplicationTests {
    @Autowired
    private AnjukePageProcessor anjukePageProcessor;
    @Autowired
    private IpProxy ipProxy;

    @Test
    void contextLoads() {
        DynamicProxyDownload httpClientDownloader = new DynamicProxyDownload(ipProxy);
        Spider.create(anjukePageProcessor).addUrl("https://www.anjuke.com/sy-city.html").setDownloader(httpClientDownloader).thread(30).run();
        // Spider.create(new AnjukePageProcessor()).addUrl("https://ali.anjuke.com/community/p1/").setDownloader(httpClientDownloader).thread(1).run();
    }

}
