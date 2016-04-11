package misszero;

import misszero.DB.CrawlerDB;
import java.util.Set;
import java.util.UUID;

public class Fetcher extends Thread {

    private CrawlerDB crawlerDB;
    private LinkDB downDB;
    private volatile boolean running;

    public Fetcher(CrawlerDB crawlerDB, LinkDB downDB) {

        this.crawlerDB = crawlerDB;
        this.downDB = downDB;
        this.running = false;
    }

    private String getUrl() {

        if(this.downDB.unVisitedUrlsEmpty()) {
            return null;
        }

        String visitUrl = this.downDB.unVisitedUrlDeQueue();

        if(visitUrl == null) {
            return null;
        }

        if (this.downDB.checkUrlIsVisited(visitUrl)) {
            return null;
        }

        this.downDB.addVisitedUrl(visitUrl);

        return visitUrl;
    }

    private void fetching()
    {
        try {

            while (this.running) {

                String visitUrl = null;
                synchronized (this.downDB) {
                    visitUrl = getUrl();
                }

                if(visitUrl == null) {
                    continue;
                }

                synchronized (this.downDB) {
                    this.downDB.addUnvisitedUrl(visitUrl);
                }

                String code = UUID.randomUUID().toString();
                FileDownLoader downLoader = new FileDownLoader();
                String path = downLoader.downloadFile(code, visitUrl);
                this.crawlerDB.updateLinkToDownloaded(visitUrl, path);
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.running = true;
        fetching();
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