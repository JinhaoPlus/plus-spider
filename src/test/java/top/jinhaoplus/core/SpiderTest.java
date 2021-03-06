package top.jinhaoplus.core;


import com.google.common.collect.Lists;
import junit.framework.TestCase;
import top.jinhaoplus.config.Config;
import top.jinhaoplus.downloader.impl.DefaultAsyncDownloaderExtraConfigrator;
import top.jinhaoplus.downloader.impl.DefaultAsyncDownloaderFactory;
import top.jinhaoplus.http.Proxy;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.RequestCookie;
import top.jinhaoplus.http.RequestMethod;

import java.util.List;

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
                        new RequestCookie("SUB", "_2A25xDlz2DeRhGeVM61YS8SrOyzyIHXVSesk-rDV8PUNbmtAKLRDEkW9NTMf7EFNoG4_h3y02eDhBcRnSDSFdmlYe", ".weibo.com", "/")
                )
        ).crawl();
    }

    public void testCrawl7() throws Exception {
        new Spider(
                Config.defaultConfig(),
                Lists.newArrayList(
                        new Request("https://weibo.com/JinhaoTek").addCookie(
                                new RequestCookie("SUB", "_2A25xDlz2DeRhGeVM61YS8SrOyzyIHXVSesk-rDV8PUNbmtAKLRDEkW9NTMf7EFNoG4_h3y02eDhBcRnSDSFdmlYe", ".weibo.com", "/")
                        ),
                        new Request("https://weibo.com/JinhaoTek"))
        ).crawl();
    }

    public void testCrawl8() throws Exception {
        new Spider(
                Config.defaultConfig()
                        .downloaderFactoryClass(DefaultAsyncDownloaderFactory.class.getName())
                , "https://www.baidu.com").crawl();
    }

    public void testCrawl9() throws Exception {
        List<Request> requests = Lists.newArrayList();
        for (int i = 0; i < 16; i++) {
            requests.add(new Request("https://www.baidu.com/s?wd=" + i));
        }
        new Spider(
                Config.defaultConfig()
                        .downloaderFactoryClass(DefaultAsyncDownloaderFactory.class.getName())
                , requests).crawl();
    }

    public void testCrawl10() throws Exception {
        List<Request> requests = Lists.newArrayList();
        for (int i = 0; i < 16; i++) {
            requests.add(new Request("https://www.baidu.com/s?wd=" + i));
        }
        new Spider(
                Config.defaultConfig()
                        .downloaderFactoryClass(DefaultAsyncDownloaderFactory.class.getName())
                        .extraConfig(DefaultAsyncDownloaderExtraConfigrator.IO_THREAD_COUNT, 10)
                , requests).crawl();
    }

    public void testCrawl11() throws Exception {
        new Spider(
                Config.defaultConfig()
                        .proxyConfig(new Proxy("", 8411, "", "")),
                new Request("https://www.baidu.com")
        ).crawl();
    }

    public void testCrawl12() throws Exception {
        new Spider(
                Config.defaultConfig()
                        .downloaderFactoryClass(DefaultAsyncDownloaderFactory.class.getName())
                        .proxyConfig(new Proxy("", 8411, "", "")),
                new Request("https://www.baidu.com")
        ).crawl();
    }

    public void testCrawl13() throws Exception {
        new Spider(
                Config.defaultConfig(),
                new Request("http://localhost:8080/post").method(RequestMethod.POST).entity("ccc")
        ).crawl();
    }
}