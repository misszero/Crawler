package misszero;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Html;


public class Frontier {

    private Task task;

    public Frontier() {

    }

    public void runTask() {

        LinkFilter linkFilter = new LinkFilter() {
            public boolean accept(String url) {
                if (URLMatcher.matchFilterURL(url))
                    return true;
                else
                    return false;
            }
        };

        ContentFilter contentFilter = new ContentFilter() {
            @Override
            public String accept(String url) {
                return HtmlParserTool.extracContent1(url);
            }
        };

        this.task = new Task(linkFilter, contentFilter);

        try {

            this.task.createExtractorAndRun(10, new String[]{"http://m.stzp.cn/search/offer_search_result.aspx?page=1"});
            this.task.createFetcherAndRun(10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTask() {

        if(this.task != null) {

            this.task.stopAll();

        }
    }

    public Task getTask() {

        return this.task;

    }

}