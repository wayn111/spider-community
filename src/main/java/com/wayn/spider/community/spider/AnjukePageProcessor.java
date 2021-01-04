package com.wayn.spider.community.spider;

import com.alibaba.fastjson.JSONObject;
import com.wayn.spider.community.core.entity.Community;
import com.wayn.spider.community.core.service.CommunityService;
import com.wayn.spider.community.util.TencentMapUtil;
import com.wayn.spider.community.util.spring.SpringContextUtil;
import lombok.SneakyThrows;
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
@Component
public class AnjukePageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000)
            .addHeader("cookie", "sessid=EC26D84C-525E-A4AB-E1B6-F340D726BCCC; aQQ_ajkguid=7CE62195-F606-2000-67CE-D770468EF2E4; twe=2; id58=e87rkF/zLKOZl9w7C9yVAg==; _ga=GA1.2.343527993.1609772195; _gid=GA1.2.1995531441.1609772195; 58tj_uuid=ce6ba613-b165-4006-b7db-b631914ba065; als=0; init_refer=https%253A%252F%252Fwww.anjuke.com%252F; new_uv=2; new_session=0; wmda_uuid=59461d7a76576a01f0dac28d5c284f0e; wmda_new_uuid=1; wmda_session_id_6289197098934=1609776435622-d0e0de14-df3e-e7e5; wmda_visited_projects=%3B6289197098934; ajk_member_verify=ogyMnTXfMI1rXIV5l79nDqUNoboBSt8sGYrTUF9qJH8%3D; ajk_member_verify2=MjEwNzE0NDQwfFVsTmQ2VFd8MQ%3D%3D; ctid=42; ajkAuthTicket=TT=40da82c520c87dc9d73798a3d4bbc849&TS=1609776651307&PBODY=EhWhLaN2z0TGFh6OHBmBWD4SYyzObozAncrN6mQJzjyuj-taV4JxXWu70yMEcCdbV9ZRxqAzw-TACcbtB_kacj5vyi5hn7KYBaE0LX64P05K3U5HMDVIJIZllmxXQo1QRzdjwQk0rt_SRZEzde7-LDJrz_4HdblLYY2EwTUM2MU&VER=2; obtain_by=1; xxzl_cid=97c47a78b41943b6ae229a425853c222; xzuid=759efd79-8861-4acb-a180-d9148bf5860c")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
            .addHeader(":authority", "zh.anjuke.com");

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

    @SneakyThrows
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
                int pageNum = (int) Math.ceil(dataNum / 30);
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
