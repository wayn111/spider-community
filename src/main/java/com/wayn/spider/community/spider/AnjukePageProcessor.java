package com.wayn.spider.community.spider;

import com.alibaba.fastjson.JSONObject;
import com.wayn.spider.community.core.entity.Community;
import com.wayn.spider.community.core.service.CommunityService;
import com.wayn.spider.community.util.TencentMapUtil;
import com.wayn.spider.community.util.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * github爬虫demo
 */
@Slf4j
@Component
public class AnjukePageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(600)
            .addHeader("cookie", "sessid=2159F266-CF2B-39FC-3533-210C3642B25B; aQQ_ajkguid=4BA9547C-273D-9088-F559-23F4A6991514; id58=e87rkF/tOPuT64v1INMnAg==; wmda_uuid=f75da04daa1039d0cb667d430d852b51; wmda_new_uuid=1; wmda_visited_projects=%3B6289197098934; _ga=GA1.2.1576678061.1609382138; 58tj_uuid=1d03e93f-c8ef-48d5-b288-ad8649ba82dc; als=0; __xsptplus8=8.1.1609382147.1609382250.4%234%7C%7C%7C%7C%7C%23%234qWjM5r_jI4xUz7CVw6lEheKztvA8lrt%23; cmctid=9617; twe=2; _gid=GA1.2.2096740622.1609731672; xxzl_cid=af8e8c604de44b3e8309343b5a623b26; xzuid=cc588bee-c3ef-4f5c-8067-a64e698d2326")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
            .addHeader(":authority", "document")
            .addHeader("sec-fetch-dest", "sec-fetch-dest")
            .addHeader("sec-fetch-user", "?1")
            .addHeader("referer", "https://login.anjuke.com/");

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("58.62.115.251", 36249, "", "")));
        Spider.create(new AnjukePageProcessor()).addUrl("https://www.anjuke.com/sy-city.html").setDownloader(httpClientDownloader).thread(5).run();
    }

    /**
     * 获取汉字拼音首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().get().startsWith("https://www.anjuke.com/")) {
            Selectable cssSelectable = page.getHtml().xpath("/html/body/div/div/div/ul/li/div/a/@href");
            List<String> all = cssSelectable.all();
            List<String> stringList = all.stream().map(item -> item + "/community/p1/")
                    .collect(Collectors.toList());
            page.addTargetRequests(stringList);
        } else {
            String url = page.getUrl().get();
            if (url.endsWith("/p1/")) {
                float dataNum = Float.parseFloat(page.getHtml().xpath("//*[@id=\"list-content\"]/div[1]/span/em[2]/text()").get());
                if (dataNum == 0) {
                    log.info(page.getUrl().get() + " 当前城市没有小区信息！");
                    return;
                }
                int pageNum = (int) Math.ceil(dataNum / 30);
                if (pageNum >= 50) {
                    pageNum = 50;
                }
                List<String> urlList = new ArrayList<>();
                String tempUrl;
                for (int i = 2; i <= pageNum; i++) {
                    tempUrl = url.replaceFirst("\\d", String.valueOf(i));
                    urlList.add(tempUrl);
                }
                page.addTargetRequests(urlList);
            }
            Selectable communityNameLonLat = page.getHtml().xpath("//*[@id=\"list-content\"]/div/div[1]/p[2]/a[1]/@href");
            // "//*[@id=\"list-content\"]/div[4]/div[1]/h3/a";
            List<String> communityNameLonLatList = communityNameLonLat.all();
            List<Community> list = new ArrayList<>();
            for (String href : communityNameLonLatList) {
                // /#l1=31.891564&l2=102.239196&l3=18&flag=1&commname=马江街300号院&commid=1428912
                String temp = href.substring(href.lastIndexOf("/"));
                String[] split = temp.split("&", -1);
                String communityName = split[4].split("=")[1];
                String letter = String.valueOf(getPinYinHeadChar(communityName).charAt(0)).toUpperCase();
                String lon = split[1].substring(3);
                String lat = split[0].substring(5);
                JSONObject jsonObject = TencentMapUtil.geocoder(lon, lat);
                String cityName = (String) jsonObject.getJSONObject("result").getJSONObject("ad_info").get("city");
                Integer adcode = Integer.parseInt((String) jsonObject.getJSONObject("result").getJSONObject("ad_info").get("adcode"));
                Integer cityCode = adcode / 100 * 100;
                Community community = Community.builder()
                        .communityName(communityName)
                        .cityCode(cityCode)
                        .cityName(cityName)
                        .areaId(adcode)
                        .communityLongitude(new BigDecimal(lon))
                        .communityLatitude(new BigDecimal(lat))
                        .communityLetter(letter)
                        .communityPid(0L)
                        .build();
                list.add(community);
            }
            CommunityService communityService = SpringContextUtil.getBean(CommunityService.class);
            communityService.saveBatch(list);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
