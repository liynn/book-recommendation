package com.wy.util;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by wy on 2017/3/25.
 */
public class BookSpider implements PageProcessor {

    //豆瓣图书标签页
    private static final String URL = "https://book.douban.com/tag/";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        //获取所有的标签连接
        List<String> tagLinks = page.getHtml().links().regex("https://book\\.douban\\.com/tag/.*").all();
        page.putField("tagLinks",tagLinks);
        page.addTargetRequests(tagLinks);
        String bookId = page.getHtml().links().regex("https://book.douban.com/subject/6082808/").get();
        page.putField("bookId",bookId);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BookSpider())
                //从"https://book.douban.com/tag/"开始抓
                .addUrl(URL)
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
