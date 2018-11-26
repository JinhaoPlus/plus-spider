package top.jinhaoplus.core;


import com.google.common.collect.Lists;
import junit.framework.TestCase;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.DefaultAsyncDownloaderFactory;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.RequestCookie;

public class SpiderTest extends TestCase {

    public void testCrawl0() throws Exception {
        new Spider().crawl();
    }

    public void testCrawl1() throws Exception {
        new Spider("https://www.baidu.com/").crawl();
    }

    public void testCrawl2() throws Exception {
        new Spider(new Request("https://www.baidu.com/")).crawl();
    }

    public void testCrawl3() throws Exception {
        new Spider(Config.defaultConfig(), "https://www.baidu.com/").crawl();
    }

    public void testCrawl4() throws Exception {
        new Spider(Config.defaultConfig(), new Request("https://www.baidu.com/")).crawl();
    }

    public void testCrawl5() throws Exception {
        new Spider(Config.defaultConfig(), new Request("https://weibo.com/JinhaoTek")).crawl();
    }

    public void testCrawl6() throws Exception {
        new Spider(
                Config.defaultConfig(),
                new Request("https://weibo.com/JinhaoTek").addCookie(
                        new RequestCookie("SUB", "_2A252_86eDeRhGeVM61YS8SrOyzyIHXVVjKdWrDV8PUNbmtAKLUfskW9NTMf7EALNxvvLllufc1iW_cB9PYs0ao1h", ".weibo.com", "/")
                )
        ).crawl();
    }

    public void testCrawl7() throws Exception {
        new Spider(
                Config.defaultConfig(),
                Lists.newArrayList(
                        new Request("https://weibo.com/JinhaoTek").addCookie(
                                new RequestCookie("SUB", "_2A252_86eDeRhGeVM61YS8SrOyzyIHXVVjKdWrDV8PUNbmtAKLUfskW9NTMf7EALNxvvLllufc1iW_cB9PYs0ao1h", ".weibo.com", "/")
                        ),
                        new Request("https://weibo.com/JinhaoTek"))
        ).crawl();
    }

    public void testCrawl8() throws Exception {
        new Spider(Config.defaultConfig().downloaderFactoryClass(DefaultAsyncDownloaderFactory.class.getName()), "https://www.baidu.com/").crawl();
    }


}