package misszero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Text;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParserTool {

    // 获取一个网站上的链接,filter 用来过滤链接
    public static Set<String> extracLinks(String url, LinkFilter filter) {

        Set<String> links = new HashSet<String>();
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");
            // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
            NodeFilter frameFilter = new NodeFilter() {
                public boolean accept(Node node) {
                    if (node.getText().startsWith("frame src=")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            // OrFilter 来设置过滤 <a> 标签，和 <frame> 标签
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(
                    LinkTag.class), frameFilter);
            // 得到所有经过过滤的标签
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if (tag instanceof LinkTag)// <a> 标签
                {
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();// url
                    if(filter.accept(linkUrl))
                        links.add(linkUrl);
                } else// <frame> 标签
                {
                    // 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1)
                        end = frame.indexOf(">");
                    String frameUrl = frame.substring(5, end - 1);
                    if(filter.accept(frameUrl))
                        links.add(frameUrl);
                }
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }
        return links;
    }

    public static String extracContent(String url, NodeFilter filter) {

        String content = "";

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if(tag.getText() != null) {
                    content += tag.getText();
                }
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return content;

    }

    private static class ParentPosition {
        public int start;
        public int end;
    }

    private static boolean isError(String url) {

        boolean isError = false;

        NodeFilter filter = new NodeFilter() {
            @Override
            public boolean accept(Node node) {
                if(node.getText().startsWith("div class=\"error\"")) {
                    return true;
                }
                return false;
            }
        };

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if(list != null && list.size() > 0) {
                isError = true;
            }

        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return isError;
    }

    private static ParentPosition getParentPosition(String url) {

        ParentPosition position = null;

        NodeFilter filter = new NodeFilter() {
            @Override
            public boolean accept(Node node) {
                if(node.getText().startsWith("div id=\"list\"")) {
                    return true;
                }
                return false;
            }
        };

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if(list != null && list.size() > 0) {

                Node node = list.elementAt(0);
                position = new ParentPosition();
                position.start = node.getStartPosition();
                NodeList ll = node.getChildren().extractAllNodesThatMatch(new NodeFilter() {
                    @Override
                    public boolean accept(Node node) {
                        if(node.getText().contains("联系方式")) {
                            return true;
                        }
                        return false;
                    }
                }, true);
                position.end = ll.elementAt(0).getEndPosition();
            }

        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return position;
    }

    private static ParentPosition getParentPosition1(String url) {

        ParentPosition position = null;

        NodeFilter filter = new NodeFilter() {
            @Override
            public boolean accept(Node node) {
                if(node.getText().startsWith("div class=\"VipBody\"")) {
                    return true;
                }
                return false;
            }
        };

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if(list != null && list.size() > 0) {

                Node node = list.elementAt(0);
                position = new ParentPosition();
                position.start = node.getStartPosition();
                NodeList ll = node.getChildren().extractAllNodesThatMatch(new NodeFilter() {
                    @Override
                    public boolean accept(Node node) {
                        if(node.getText().contains("联系方式")) {
                            return true;
                        }
                        return false;
                    }
                }, true);
                position.end = ll.elementAt(0).getEndPosition();
            }

        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return position;
    }

    public static String extracContent1(String url) {

        String content = "";
        ParentPosition position = null;

        if(isError(url)) {

            return "";

        } else {

            position = getParentPosition(url);
            if(position == null) {
                position = getParentPosition1(url);
            }
            if(position == null) {
                return "";
            }
        }

        final int start = position.start;
        final int end = position.end;

        NodeFilter filter = new NodeFilter() {
            public boolean accept(Node node) {

                if (node.getStartPosition() > start && node.getEndPosition() < end && node instanceof TextNode) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if(tag.getText() != null) {
                    if(!tag.getText().trim().equals("")) {
                        content += tag.getText().trim() + "\n";
                    }
                }
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return content;

    }
}