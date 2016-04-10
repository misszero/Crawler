package misszero;

public class URLMatcher {

    public static boolean matchURL(String[] urls, String targetURL) {

        boolean matched = false;

        for(int i = 0; i < urls.length; i ++) {
            String url = urls[i];
            if(targetURL.startsWith(url)) {
                matched = true;
                break;
            }
        }

        return matched;
    }

    public static boolean matchFilterURL(String url) {

        return  matchURL(Config.FilterUrlList, url);
    }

    public static boolean matchSeedURL(String url) {

        return  matchURL(Config.SeedUrlList, url);
    }

    public static boolean matchLinkURL(String url) {

        return  matchURL(Config.LinkUrlList, url);
    }

}
