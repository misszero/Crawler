package misszero;

import misszero.DB.CrawlerDB;
import java.util.Set;
import java.util.UUID;

public class Extractor extends Thread {

    private Task task;
    private CrawlerDB crawlerDB;
    private LinkDB linkDB;
    private LinkDB downDB;
    private LinkFilter linkFilter;
    private volatile boolean running;

    public Extractor(Task task) {

        this.task = task;
        this.crawlerDB = this.task.getCrawlerDB();
        this.linkDB = this.task.getLinkDB();
        this.downDB = this.task.getDownDB();
        this.linkFilter = this.task.getLinkFilter();
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

            while (this.running) {

                String visitUrl = null;
                boolean matchLinkURL = true;

                synchronized (LinkDB.class) {

                    visitUrl = getUrl();

                    if (visitUrl == null) {
                        continue;
                    }

                    matchLinkURL = URLMatcher.matchLinkURL(visitUrl);
                    if (matchLinkURL) {

                        this.downDB.addUnvisitedUrl(visitUrl);
                        String code = UUID.randomUUID().toString();
                        String url = visitUrl;
                        this.crawlerDB.addLink(code, type, url);

                    }
                }

                if (!matchLinkURL)  {

                    Set<String> links = HtmlParserTool.extracLinks(visitUrl, this.linkFilter);

                    synchronized (LinkDB.class) {

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