package misszero;

import misszero.DB.CrawlerDB;
import misszero.DB.DBBase;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static CrawlerController crawlerController;

    public static void main(String[] args) {

        crawlerController = new CrawlerController();

        for(int i = 0; i < 10; i ++) {
            int pageNum = 1 + i * 2000;
            crawlerController.createCrawlerAndRun(new String[]{"http://m.stzp.cn/search/offer_search_result.aspx?page=" + String.valueOf(pageNum)});
        }

        waitUserInput();

    }

    private static void waitUserInput() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("正在采集网页数据……停止任务请按下q并回车。");
        String inputText = scanner.next();

        if(inputText.equalsIgnoreCase("q")) {

        } else {

            waitUserInput();

        }

        crawlerController.stopAll();
    }
}
