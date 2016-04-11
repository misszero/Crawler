package misszero;

import misszero.DB.CrawlerDB;
import java.util.Set;
import java.util.UUID;

public class Extractor extends Thread {

    private CrawlerDB crawlerDB;
    private LinkDB linkDB;
    private LinkDB downDB;
    private volatile boolean running;

    public Extractor(CrawlerDB crawlerDB, LinkDB linkDB, LinkDB downDB) {

        this.crawlerDB = crawlerDB;
        this.linkDB = linkDB;
        this.downDB = downDB;
        this.running = false;
    }

    private String getUrl() {

        if(this.linkDB.unVisitedUrlsEmpty()) {
            return null;
        }

        String visitUrl = this.linkDB.unVisitedUrlDeQueue();

        if(visitUrl == null) {
            return null;
        }

        if (this.linkDB.checkUrlIsVisited(visitUrl)) {
            return null;
        }

        this.linkDB.addVisitedUrl(visitUrl);

        return visitUrl;
    }

    /* 爬取方法*/
    private void extracting()
    {
        int type = Config.Type;

        try {

            LinkFilter filter = new LinkFilter() {
                //提取以 http://www.twt.edu.cn 开头的链接
                public boolean accept(String url) {
                    if (URLMatcher.matchFilterURL(url))
                        return true;
                    else
                        return false;
                }
            };


            while (this.running) {

                String visitUrl = null;
                synchronized (this.linkDB) {
                    visitUrl = getUrl();
                }

                if(visitUrl == null) {
                    continue;
                }

                synchronized (this.linkDB) {
                    this.downDB.addUnvisitedUrl(visitUrl);
                }

                if (URLMatcher.matchLinkURL(visitUrl)) {

                    String code = UUID.randomUUID().toString();
                    /*FileDownLoader downLoader = new FileDownLoader();
                    String filePath = downLoader.downloadFile(code, visitUrl);*/
                    String url = visitUrl;
                    this.crawlerDB.addLink(code, type, url);

                } else {

                    Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
                    synchronized (this.linkDB) {
                        for (String link : links) {
                            this.linkDB.addUnvisitedUrl(link);
                        }
                    }
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.running = true;
        extracting();
    }

    public void stopRun() {
        this.running = false;
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}