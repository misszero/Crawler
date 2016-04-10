package misszero;

import misszero.DB.CrawlerDB;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Crawler extends Thread {

    private CrawlerDB crawlerDB;
    private LinkDB seedDB;
    private LinkDB linkDB;
    private volatile boolean running;

    public Crawler(CrawlerDB crawlerDB, LinkDB linkDB, String[] seeds) {

        this.crawlerDB = crawlerDB;
        this.seedDB = new LinkDB();
        this.linkDB = linkDB;
        this.running = false;

        for(int i=0;i<seeds.length;i++)
            this.seedDB.addUnvisitedUrl(seeds[i]);
    }

    /* 爬取方法*/
    private void crawling()
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



                while (!seedDB.unVisitedUrlsEmpty()) {

                    if (!running) {
                        break;
                    }

                    String visitUrl = seedDB.unVisitedUrlDeQueue();

                    if (visitUrl == null) {
                        continue;
                    }

                    if (linkDB.checkUrlIsVisited(visitUrl)) {
                        continue;
                    }

                    seedDB.addVisitedUrl(visitUrl);

                    synchronized (linkDB) {
                        linkDB.addVisitedUrl(visitUrl);
                    }

                    if (URLMatcher.matchLinkURL(visitUrl)) {

                        String code = UUID.randomUUID().toString();
                        FileDownLoader downLoader = new FileDownLoader();
                        String filePath = downLoader.downloadFile(code, visitUrl);
                        String url = visitUrl;
                        String path = filePath;
                        crawlerDB.addLink(code, type, url, path);

                    } else {

                        Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
                        for (String link : links) {
                            seedDB.addUnvisitedUrl(link);
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
        crawling();
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