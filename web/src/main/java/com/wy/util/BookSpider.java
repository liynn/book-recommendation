package com.wy.util;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by wy on 2017/3/25.
 */
public class BookSpider implements PageProcessor{

    //static List<String[]> poolHosts = new ArrayList<String[]>();


    private Site site = Site.me().setRetryTimes(3).setSleepTime(3000);


    @Override
    public void process(Page page) {
        //获取标签页地址
        List<String> tagLinks = page.getHtml().links().regex("(https://book\\.douban\\.com/tag/[\\u4e00-\\u9fa5]+)").all();
        page.addTargetRequests(tagLinks);
        //获取所有的页数
        List<String> pageLinks = page.getHtml().css("div.pagination").links().regex(".*/tag/.*\\?l=start.*").all();
        page.putField("pageLinks",pageLinks);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BookSpider())
                .addUrl("https://book.douban.com/tag/")
                .thread(5)
                .run();
    }
}
