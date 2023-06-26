package searchengine.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HTMLParser {

    public static String parseHtml(String content, String selector) {
        StringBuilder html = new StringBuilder();
        var doc = Jsoup.parse(content);
        var elements = doc.select(selector);
        for (Element element : elements) {
            html.append(element.html());
        }
        return Jsoup.parse(html.toString()).text();
    }
}