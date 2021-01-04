package com.wayn.spider.community.spider;

import com.alibaba.fastjson.JSONObject;
import com.wayn.spider.community.util.Md5Utils;
import com.wayn.spider.community.util.TentMapUtil;
import lombok.SneakyThrows;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * github爬虫demo
 */
@Component
public class AnjukePageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000)
            .addHeader("cookie", "sessid=2159F266-CF2B-39FC-3533-210C3642B25B; aQQ_ajkguid=4BA9547C-273D-9088-F559-23F4A6991514; " +
                    "id58=e87rkF/tOPuT64v1INMnAg==; _ga=GA1.2.1576678061.1609382138; 58tj_uuid=1d03e93f-c8ef-48d5-b288-ad8649ba82dc; " +
                    "als=0; __xsptplus8=8.1.1609382147.1609382250.4%234%7C%7C%7C%7C%7C%23%234qWjM5r_jI4xUz7CVw6lEheKztvA8lrt%23; " +
                    "cmctid=9617; twe=2; _gid=GA1.2.2096740622.1609731672; new_uv=7; new_session=0; utm_source=; spm=; lp_lt_ut=326590a40d0516e4460a31589e08a6cf; " +
                    "init_refer=https%253A%252F%252Fluoyang.anjuke.com%252F; ctid=293; wmda_uuid=c6c4b6f0b444e20f29a9e84587e8a5d8; wmda_new_uuid=1; " +
                    "wmda_session_id_6289197098934=1609743128571-9c707836-edf5-e01e; wmda_visited_projects=%3B6289197098934; obtain_by=1; " +
                    "xxzl_cid=af8e8c604de44b3e8309343b5a623b26; xzuid=cc588bee-c3ef-4f5c-8067-a64e698d2326")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

    @SneakyThrows
    @Override
    public void process(Page page) {
        if (page.getUrl().get().startsWith("https://www.anjuke.com/")) {
            Selectable cssSelectable = page.getHtml().xpath("/html/body/div/div/div/ul/li/div/a/@href");
            List<String> all = cssSelectable.all();
            List<String> stringList = all.stream().map(item -> item + "/community/?from=navigation")
                    .collect(Collectors.toList());
            page.addTargetRequests(stringList);
        } else {
            Thread.sleep(1000);
            Selectable communityNameLonLat = page.getHtml().xpath("//*[@id=\"list-content\"]/div/div[1]/p[2]/a[1]/@href");
            // "//*[@id=\"list-content\"]/div[4]/div[1]/h3/a";
            List<String> communityNameLonLatList = communityNameLonLat.all();
            for (String href : communityNameLonLatList) {
                // /#l1=31.891564&l2=102.239196&l3=18&flag=1&commname=马江街300号院&commid=1428912
                String temp = href.substring(href.lastIndexOf("/"));
                String[] split = temp.split("&", -1);
                String communityName = split[4].split("=")[1];
                String letter = String.valueOf(getPinYinHeadChar(communityName).charAt(0)).toUpperCase();
                String lon = split[0].substring(5);
                String lat = split[1].substring(2);
                JSONObject jsonObject = TentMapUtil.geocoder("116.307490", "39.984154");
                Integer adcode = Integer.parseInt((String) jsonObject.getJSONObject("result").getJSONObject("ad_info").get("adcode"));
                Integer cityCode = adcode / 100 * 100;
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws IOException {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("27.190.81.155", 251157, "", "")));
        Spider.create(new AnjukePageProcessor()).addUrl("https://www.anjuke.com/sy-city.html").thread(5).run();
        // JSONObject jsonObject = TentMapUtil.geocoder("116.307490", "39.984154");
        // System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("adcode"));
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
}
