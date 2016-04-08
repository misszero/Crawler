package misszero;

public class Main {

    public static void main(String[] args) {

        Crawler crawler = new Crawler();
        crawler.crawling(new String[]{"https://github.com"});

    }
}
