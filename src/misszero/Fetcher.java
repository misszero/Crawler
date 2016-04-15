package misszero;

import misszero.DB.CrawlerDB;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

import java.util.Set;
import java.util.UUID;

public class Fetcher extends Thread {

    private Task task;
    private CrawlerDB crawlerDB;
    private LinkDB downDB;
    private ContentFilter contentFilter;
    private volatile boolean running;

    public Fetcher(Task task) {

        this.task = task;
        this.crawlerDB = this.task.getCrawlerDB();
        this.downDB = this.task.getDownDB();
        this.contentFilter = this.task.getContentFilter();
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

                synchronized (LinkDB.class) {

                    visitUrl = getUrl();

                    if (visitUrl == null) {
                        continue;
                    }

                }

                if(visitUrl != null) {

                    //String content = HtmlParserTool.extracContent(visitUrl, this.contentFilter);
                    //String content = HtmlParserTool.extracContent1(visitUrl);
                    String content = this.contentFilter.accept(visitUrl);
                    this.crawlerDB.updateLinkToDownloaded(visitUrl, content);

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