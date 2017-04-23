package com.wy.util;

import com.google.common.collect.Lists;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class BookSpider implements PageProcessor {

    //豆瓣图书标签页
    private static final String URL = "https://book.douban.com/tag/";

    //标签页匹配规则
    private static final String TAG_REGEX = "(https://book\\.douban\\.com/tag/[\\u4e00-\\u9fa5]+)";

    //图书列表页匹配规则
    private static final String BOOK_REGEX = "(https://book.douban.com/subject/[0-9]\\d{6})/";

    //图书分页匹配规则
    private static final String PAGE_REGEX = "(https://book\\.douban\\.com/tag/[\\u4e00-\\u9fa5]+\\?start=[2468]0\\&type=T)";

    private Site site = Site.me().setRetryTimes(5).setSleepTime(30000);

    /**
     * 爬取豆瓣图书编号
     *
     * @param page 豆瓣页面地址
     */
    @Override
    public void process(Page page) {
        //获取所有的标签连接
        if (page.getHtml().links().regex(TAG_REGEX).match()) {
            List<String> tagLinks = page.getHtml().links().regex(TAG_REGEX).all();
            List<String> tagList = Lists.newArrayList();
            tagLinks.forEach(link -> {
                if (!tagList.contains(link)) {
                    tagList.add(link);
                }
            });
            page.putField("tagLinks", tagList);
            page.addTargetRequests(tagList);
        }
        if (page.getHtml().links().regex(PAGE_REGEX).match()) {
            List<String> pageLinks = page.getHtml().links().regex(PAGE_REGEX).all();
            List<String> pageList = Lists.newArrayList();
            pageLinks.forEach(link -> {
                if (!pageList.contains(link)) {
                    pageList.add(link);
                }
            });
            page.putField("pageLinks", pageList);
            page.addTargetRequests(pageList);
        }
        if (page.getHtml().links().regex(BOOK_REGEX).match()) {
            List<String> bookIds = page.getHtml().links().regex(BOOK_REGEX)
                    .replace("https://book.douban.com/subject/", "")
                    .replace("/", "")
                    .all();
            List<String> ids = Lists.newArrayList();
            bookIds.forEach(id -> {
                if (!ids.contains(id)) {
                    ids.add(id);
                }
            });
            page.putField("ids", ids);
            FileUtil.write("/Users/wy/books.dat", ids);
        }
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
