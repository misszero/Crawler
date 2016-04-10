package misszero;

import java.util.HashSet;
import java.util.Set;

/**
 * 用来保存已经访问过 Url 和待访问的 Url 的类
 */
public class LinkDB {

    //已访问的 url 集合
    private Set<String> visitedUrl = new HashSet<String>();
    //待访问的 url 集合
    private Queue<String> unVisitedUrl = new Queue<String>();


    public Queue<String> getUnVisitedUrl() {
        return unVisitedUrl;
    }

    public void addVisitedUrl(String url) {
        visitedUrl.add(url);
    }

    public void removeVisitedUrl(String url) {
        visitedUrl.remove(url);
    }

    public String unVisitedUrlDeQueue() {
        return unVisitedUrl.deQueue();
    }

    // 保证每个 url 只被访问一次
    public void addUnvisitedUrl(String url) {
        if(!checkUrlIsVisited(url))
            unVisitedUrl.enQueue(url);
    }

    // 检查url是否访问过
    public boolean checkUrlIsVisited(String url) {

        if (url != null && !url.trim().equals("")
                && !visitedUrl.contains(url)
                && !unVisitedUrl.contians(url)) {

            return false;
        } else {
            return true;
        }
    }

    public int getVisitedUrlNum() {
        return visitedUrl.size();
    }

    public boolean unVisitedUrlsEmpty() {
        return unVisitedUrl.empty();
    }
}