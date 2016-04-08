package misszero;

public class Main {

    public static void main(String[] args) {

        Crawler crawler = new Crawler();
        crawler.crawling(new String[]{"http://m.stzp.cn/search/offer_search_result.aspx?page=1"});

    }
}
