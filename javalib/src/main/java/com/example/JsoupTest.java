package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Java HTML Parser
 * https://jsoup.org/
 *  
 * @author VanceKing
 * @since 19-4-14.
 */
public class JsoupTest {
    public static void main(String[] args) {
        try {
            parseHtml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        parseHtmlString();
    }

    private static void parseHtmlString() {
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
        log(doc.toString());
    }

    private static void parseHtml() throws Exception {
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Main_Page").get();
        log(doc.title());

        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            log("%s\n\t%s", headline.attr("title"), headline.absUrl("href"));
        }
    }

    private static void log(String msg, Object... vals) {
        System.out.printf((msg) + "%n", vals);
    }

}
