package top.jinhaoplus.core;


import junit.framework.TestCase;

public class SpiderTest extends TestCase {

    public void testCrawl() throws Exception {
        new Spider("https://www.baidu.com/").crawl();
//        new Spider(new Request("https://www.baidu.com/")).crawl();
//        new Spider(new Config(), "https://www.baidu.com/").crawl();
//        new Spider(new Config(), new Request("https://www.baidu.com/")).crawl();
    }
}