package com.wayn.spider.community;

import com.wayn.spider.community.spider.AnjukePageProcessor;
import com.wayn.spider.community.spider.GithubRepoPageProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private GithubRepoPageProcessor githubRepoPageProcessor;
    @Autowired
    private AnjukePageProcessor anjukePageProcessor;


    @Test
    void contextLoads() {
        Spider.create(anjukePageProcessor).addUrl("https://www.anjuke.com/sy-city.html").thread(5).run();
    }

}
