package com.wayn.spider.community.spider.download;

import com.wayn.spider.community.constant.Constant;
import com.wayn.spider.community.spider.proxy.IpProxyInterface;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;

/**
 * 安居客动态代理下载器
 */
@Slf4j
public class AnjukeDynamicProxyDownload extends AbstractDynamicProxyDownload {

    @Override
    protected boolean ipProxyValidate(Page page) {
        if (!page.getUrl().get().startsWith(Constant.ANJUKE_URL)) {
            if (!"0".equals(page.getHtml().xpath(Constant.CITY_NUMBER_XPATH).get())) {
                return null != page.getHtml().xpath(Constant.COMMUNITY_NAME_LON_LAT_XPATH).get()
                        && null != page.getHtml().xpath(Constant.CITY_NUMBER_XPATH).get();
            }
        }
        return true;
    }

    public AnjukeDynamicProxyDownload(IpProxyInterface ipProxyInterface) {
        super(ipProxyInterface);
    }

}
