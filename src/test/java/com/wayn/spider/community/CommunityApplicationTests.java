package com.wayn.spider.community;

import com.wayn.spider.community.spider.AnjukePageProcessor;
import com.wayn.spider.community.spider.GithubRepoPageProcessor;
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
    private GithubRepoPageProcessor githubRepoPageProcessor;
    @Autowired
    private AnjukePageProcessor anjukePageProcessor;


    @Test
    void contextLoads() {
        Spider.create(new AnjukePageProcessor()).addUrl("https://www.anjuke.com/sy-city.html").thread(5).run();
    }

}
