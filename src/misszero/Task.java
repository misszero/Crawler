package misszero;

import misszero.DB.CrawlerDB;
import misszero.Entities.LinkEntity;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.OrFilter;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Task {

    private CrawlerDB crawlerDB;
    private LinkDB linkDB;
    private LinkDB downDB;
    private List<Extractor> extractors;
    private List<Fetcher> fetchers;
    private LinkFilter linkFilter;
    private ContentFilter contentFilter;

    public CrawlerDB getCrawlerDB() {
        return this.crawlerDB;
    }

    public LinkDB getLinkDB() {
        return this.linkDB;
    }

    public LinkDB getDownDB() {
        return this.downDB;
    }

    public LinkFilter getLinkFilter() {
        return this.linkFilter;
    }

    public ContentFilter getContentFilter() {
        return this.contentFilter;
    }

    public Task(LinkFilter linkFilter, ContentFilter contentFilter) {

        this.crawlerDB = new CrawlerDB();
        this.linkDB = new LinkDB();
        this.downDB = new LinkDB();
        this.extractors = new ArrayList<Extractor>();
        this.fetchers = new ArrayList<Fetcher>();
        this.linkFilter = linkFilter;
        this.contentFilter = contentFilter;

        this.crawlerDB.connect();

        initWithLinks();
    }

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
            Extractor extractor = new Extractor(this);
            this.extractors.add(extractor);
            extractor.start();
        }
    }

    public void createFetcherAndRun(int count) throws Exception {

        if(count < 1) {
            throw new Exception("下载任务的数量不能小于1个。");
        }

        for(int i = 0; i < count; i ++) {
            Fetcher fetcher = new Fetcher(this);
            this.fetchers.add(fetcher);
            fetcher.start();
        }
    }

    public void stopAll() {

        stopExtractors();
        stopFetchers();
        this.crawlerDB.disconnect();
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
