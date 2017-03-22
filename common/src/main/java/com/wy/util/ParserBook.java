package com.wy.util;

/**
 * Created by wy on 2017/3/15.
 */

import com.wy.domain.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ParserBook extends Thread{
    /**
     * 豆瓣图书标签页地址
     */
    private final static String URL = "https://book.douban.com/tag/";

    /**
     * 请求头常量
     */
    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";

    /**
     * 请求COOKIE常量
     */
    private final static String UTMA = "81379588.1625906329.1478780180.1478780180.1478780180.1";
    private final static String UTMB = "81379588.1.10.1478780180";
    private final static String UTMC = "81379588";
    private final static String UTMZ = "81379588.1478780180.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
    private final static String PK_ID_ONE = "b8e7b1931da4acd1.1478780181.1.1478780181.1478780181.";
    private final static String BID = "MvEsSVNL_Nc";
    private final static String GA = "GA1.3.117318709.1478747468";
    private final static String PK_ID_TWO = "ce6e6ea717cbd043.1478769904.1.1478769904.1478769904.";
    private final static String PK_REF_ONE = "%5B%22%22%2C%22%22%2C1478769904%2C%22https%3A%2F%2Fbook.douban.com%2"
            + "Fsubject_search%3Fsearch_text%3D%25E6%258E%25A8%25E8%258D%2590%25E7%25B3%25BB%25E7%25BB%259F%25"
            + "E5%25AE%259E%25E8%25B7%25B5%26cat%3D1001%22%5D";
    private final static String PK_ID_THREE = "237bb6b49215ebbc.1478749116.2.1478774039.1478749120.";
    private final static String PK_REF_TWO = "%5B%22%22%2C%22%22%2C1478773525%2C%22https%3A%2F%2Fwww.baidu."
            + "com%2Flink%3Furl%3DlQ4OMngm1b6fAWeomMO7xq6PNbBlxyhdnHqz9mIYN9-ycRbjZvFb1NQyQ7hqzvI46-WThP"
            + "6A_Qo7oTQNP-98pa%26wd%3D%26eqid%3Da24e155f0000e9610000000258244a0c%22%5D";


    private static Map<String,String> cookies = new HashMap<>();

    private String tag = null;

    static {
        cookies.put("__utma", UTMA);
        cookies.put("__utmb", UTMB);
        cookies.put("__utmc", UTMC);
        cookies.put("__utmz", UTMZ);
        cookies.put("_pk_id.100001.3ac3", PK_ID_ONE);
        cookies.put("_pk_ses.100001.3ac3", "*");
        cookies.put("bid", BID);
        cookies.put("_ga", GA);
        cookies.put("_pk_id.100001.a7dd", PK_ID_TWO);
        cookies.put("_pk_ref.100001.a7dd", PK_REF_ONE);
        cookies.put("_pk_id.100001.8cb4", PK_ID_THREE);
        cookies.put("_pk_ref.100001.8cb4", PK_REF_TWO);
    }

    public ParserBook(String tag){
        this.tag = tag;
    }

    public static void main(String[] args) {
          getProxyIp();
//        BlockingQueue<String> tags = getAllTag();
//        System.out.println("标签总数:"+tags.size());
//        ExecutorService exec = Executors.newFixedThreadPool(5);
//        for (String tag : tags){
//            exec.execute(new ParserBook(tag));
//        }
//        exec.shutdown();
    }

    @Override
    public void run() {
        getBookUrl(tag);
    }
    /**
     * 得到图书地址链接
     * @param tag 标签
     * @return
     */
    public static synchronized void getBookUrl(String tag) {
        int index = 0;
        int count = 0;
        try {
            outer:while (true) {
                Document doc = Jsoup.connect(URL + tag + "?start=" + index + "&type=T")
                        .proxy(HttpUtil.setProxyIp())
                        .header("User-Agent", USER_AGENT)
                        .cookies(cookies)
                        .timeout(15000).get();

                if(doc == null || doc.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
                    System.out.println("出现ip被拦截或者其他情况");
                    getBookUrl(tag);
                }


                Elements elements = doc.select("ul").select("h2").select("a");
                int pageSize = elements.size();
                for (Element e : elements) {
                    Book book = getBookData(e.attr("href"));
                    FileUtil.writerFile("/Users/wy/workspace/book-recommendation/common/src/main/resources/data/books.dat",book);
                    count++;
                    if(count == 66){
                        break outer;
                    }
                }
                index += pageSize;
            }
        } catch (Exception e) {
            getBookUrl(tag);
        }
    }

    /**
     * 获取图书基本信息
     * @param url 图书基本信息链接地址
     * @return
     */
    public static synchronized Book getBookData(String url) {
        Book book = null;
        try {
            Document doc = Jsoup.connect(url)
                    .proxy(HttpUtil.setProxyIp())
                    .header("User-Agent", USER_AGENT)
                    .cookies(cookies)
                    .timeout(15000).get();

            if(doc == null || doc.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
                getBookData(url);
            }

            Elements titleElement = doc.getElementsByClass("subject clearfix").select("a");
            Elements authorElement = doc.getElementById("info").select("span").first().select("a");
            Element pressElement = doc.getElementById("info");
            Elements tagsElement = doc.getElementById("db-tags-section").select("a");
            Elements imgElements = doc.getElementById("mainpic").select("img");
            //编号
            String id = url.substring(url.indexOf('e')+4,url.length()-1);
            // 书名
            String title = titleElement.attr("title");
            // 作者
            String author = authorElement.html();
            // 出版社
            String press = pressElement.text();
            if (press.indexOf("出版社:") > -1) {
                press = pressElement.text().split("出版社:")[1].split(" ")[1];
            } else {
                press = "";
            }
            //标签
            String tag = "";
            if(tagsElement.size()>0){
                for (Element element : tagsElement){
                    tag += element.html()+"|";
                }
            }
            tag = tag.substring(0,tag.length()-1);
            //图片地址
            String imgurl = imgElements.attr("src");

            book = new Book(id,title,author,press,tag,imgurl);
            String context = id+"::"+title+"::"+author+"::"+press+"::"+tag+"::"+imgurl;
            System.out.println(context);
        } catch (Exception e) {
            getBookData(url);
        }
        return book;
    }

    /**
     * 得到图书的所有标签
     * @return
     */
    public static BlockingQueue<String> getAllTag(){
        BlockingQueue<String> tags = new LinkedBlockingQueue<>();
        try {
            String data = "";
            Document doc = Jsoup.connect(URL)
                    .proxy(HttpUtil.setProxyIp())
                    .header("User-Agent", USER_AGENT)
                    .cookies(cookies)
                    .timeout(15000).get();

            if(doc == null || doc.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
                System.out.println("出现ip被拦截或者其他情况");
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getAllTag();
            }

            Elements elements = doc.select("td").select("a");
            for (Element element : elements){
                tags.put(element.html());
                data += element.html()+",";
            }
            System.out.println(data);
        } catch (Exception e) {
            getAllTag();
        }
        return tags;
    }

    public static void getProxyIp(){
        int index = 1;
        while (true) {
            try {
                Document doc = Jsoup.connect("http://www.kuaidaili.com/free/inha/"+index+"/")
                        .header("User-Agent", USER_AGENT)
                        .timeout(15000).get();
                Elements td = doc.select("td");
                for (Element e :td) {
                    if (e.attr("data-title").equals("IP")) {
                        System.out.print(e.html()+":");
                        if(e.attr("data-title").equals("PORT")){
                            System.out.println(e.html());
                        }
                    }
                }
                index++;
                if(index == 3){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

