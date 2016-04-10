package misszero;

import misszero.DB.CrawlerDB;

import java.util.*;

public class CrawlerController {

    private CrawlerDB crawlerDB;
    private LinkDB linkDB;
    private List<Crawler> crawlers;

    public CrawlerController() {

        this.crawlerDB = new CrawlerDB();
        this.linkDB = new LinkDB();
        this.crawlers = new ArrayList<Crawler>();

        initCrawlerWithLinks();
    }

    /* 使用种子 url 初始化 URL 队列*/
    private void initCrawlerWithLinks()
    {
        Set<String> urls = crawlerDB.getLinkUrls();
        Iterator<String> it = urls.iterator();
        while(it.hasNext())
        {
            String url= it.next();
            linkDB.addVisitedUrl(url);
        }
    }

    public Crawler createCrawlerAndRun(String[] seeds) {
        Crawler crawler = new Crawler(crawlerDB, linkDB, seeds);
        this.crawlers.add(crawler);
        crawler.start();

        return crawler;
    }

    public void stopAll() {
        for(Crawler crawler : this.crawlers) {
            crawler.stopRun();
        }
    }
}