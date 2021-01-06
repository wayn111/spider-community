package com.wayn.spider.community.constant;

public class Constant {

    /**
     * 安居客城市列表页面url
     */
    public static final String ANJUKE_CITY_LIST_URL = "https://www.anjuke.com/sy-city.html";

    /**
     * 安居客域名url
     */
    public static final String ANJUKE_URL = "https://www.anjuke.com/";

    /**
     * 城市列表跳转属性xpath路径
     */
    public static final String CITY_LIST_HREF_XPATH = "/html/body/div/div/div/ul/li/div/a/@href";

    /**
     * 城市下小区数量xpath路径
     */
    public static final String CITY_NUMBER_XPATH = "//*[@id=\"list-content\"]/div[1]/span/em[2]/text()";
    /**
     * 城市下小区列表xpath路径
     */
    public static final String COMMUNITY_NAME_LON_LAT_XPATH = "//*[@id=\"list-content\"]/div/div[1]/p[2]/a[1]/@href";


}
