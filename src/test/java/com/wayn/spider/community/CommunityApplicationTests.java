package com.wayn.spider.community;

import com.wayn.spider.community.constant.Constant;
import com.wayn.spider.community.spider.download.AbstractDynamicProxyDownload;
import com.wayn.spider.community.spider.download.AnjukeDynamicProxyDownload;
import com.wayn.spider.community.spider.processor.AnjukePageProcessor;
import com.wayn.spider.community.spider.proxy.WanbianIpProxy;
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
    private WanbianIpProxy ipProxy;

    @Test
    void contextLoads() {
        AbstractDynamicProxyDownload httpClientDownloader = new AnjukeDynamicProxyDownload(ipProxy);
        Spider.create(anjukePageProcessor).addUrl(Constant.ANJUKE_CITY_LIST_URL).setDownloader(httpClientDownloader).thread(3).run();
    }

}
