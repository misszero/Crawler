package misszero;

import java.util.Scanner;

public class Main {

    private static Frontier frontier;

    public static void main(String[] args) {

        frontier = new Frontier();

        try {

            frontier.createExtractorAndRun(5, new String[]{"http://m.stzp.cn/search/offer_search_result.aspx?page=1"});
            frontier.createFetcherAndRun(5);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        waitUserInput();

    }

    private static void waitUserInput() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("正在采集网页数据……。");
        System.out.println("q q - 停止所有任务并退出程序");
        System.out.println("se SE - 停止提取任务");

        String inputText = scanner.next();

        if(inputText.equalsIgnoreCase("q")) {

        } else if(inputText.equalsIgnoreCase("se")) {

            frontier.stopExtractors();
            System.out.println("提取任务已停止。");
            waitUserInput();

        } else {

            waitUserInput();

        }

        frontier.stopAll();
    }
}
