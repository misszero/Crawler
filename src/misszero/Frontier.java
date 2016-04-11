package misszero;

import misszero.DB.CrawlerDB;
import misszero.Entities.LinkEntity;

import java.util.*;

public class Frontier {

    private CrawlerDB crawlerDB;
    private LinkDB linkDB;
    private LinkDB downDB;
    private List<Extractor> extractors;
    private List<Fetcher> fetchers;

    public Frontier() {

        this.crawlerDB = new CrawlerDB();
        this.linkDB = new LinkDB();
        this.downDB = new LinkDB();
        this.extractors = new ArrayList<Extractor>();
        this.fetchers = new ArrayList<Fetcher>();

        initWithLinks();
    }

    /* 使用种子 url 初始化 URL 队列*/
    private void initWithLinks()
    {
        Set<LinkEntity> links = this.crawlerDB.getLinks();
        Iterator<LinkEntity> it = links.iterator();
        while(it.hasNext())
        {
            LinkEntity link = it.next();
            this.linkDB.addVisitedUrl(link.getUrl());
            if(link.isDownloaded()) {
                this.downDB.addVisitedUrl(link.getUrl());
            } else {
                this.downDB.addUnvisitedUrl(link.getUrl());
            }
        }
    }


    public void createExtractorAndRun(int count, String[] seeds) throws Exception {

        if(count < 1) {
            throw new Exception("提取任务的数量不能小于1个。");
        }

        for(int i = 0; i < seeds.length; i ++) {
            this.linkDB.addUnvisitedUrl(seeds[i]);
        }

        for(int i = 0; i < count; i ++) {
            Extractor extractor = new Extractor(this.crawlerDB, this.linkDB, this.downDB);
            this.extractors.add(extractor);
            extractor.start();
        }
    }

    public void createFetcherAndRun(int count) throws Exception {

        if(count < 1) {
            throw new Exception("下载任务的数量不能小于1个。");
        }

        for(int i = 0; i < count; i ++) {
            Fetcher fetcher = new Fetcher(this.crawlerDB, this.downDB);
            this.fetchers.add(fetcher);
            fetcher.start();
        }
    }

    public void stopAll() {

        stopExtractors();
        stopFetchers();
    }

    public void stopExtractors() {
        for(Extractor extractor : this.extractors) {
            extractor.stopRun();
        }
    }

    public void stopFetchers() {
        for(Fetcher fetcher : this.fetchers) {
            fetcher.stopRun();
        }
    }
}