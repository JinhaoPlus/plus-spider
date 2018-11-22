package top.jinhaoplus.core;


import junit.framework.TestCase;

public class SpiderTest extends TestCase {

    public void testCrawl() throws Exception {
//        new Spider("https://www.baidu.com/").crawl();
        new Spider("https://weibo.com/JinhaoTek/home?wvr=5").crawl();
    }
}