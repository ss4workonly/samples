package samples.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

final class Downloader {
    private static final String USER_AGENT = "Mozilla";

    private static final String BASE_URL = "";

    private static final String BASE_SITEMAP_URL = BASE_URL + "/sitemap/main.xml";
    private static final String BASE_EMPLOYER_URL = BASE_URL + "/employer/";

    private static final String LOCATION_SELECTOR = "loc";
    private static final String BODY_SELECTOR = "div.HH-Sticky-AreaOnContent-Wrapper";

    private static final Pattern SITEMAP = Pattern.compile("^" + BASE_URL + "/sitemap/employer\\d+.xml$");
    private static final Pattern COMPANY = Pattern.compile("^" + BASE_URL + "/employer/\\d+$");
    private static final Pattern KEYWORDS = Pattern.compile("[\\W]+", Pattern.UNICODE_CHARACTER_CLASS);

    private static final int BASE_EMPLOYER_URL_LENGTH = BASE_EMPLOYER_URL.length();

    private Downloader() {
    }

    private static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

    private static Stream<String> getElements(Document document, String selector) throws IOException {
        return document.select(selector).stream().map(Element::text);
    }

    static Stream<String> getSitemaps() {
        try {
            return getElements(getDocument(BASE_SITEMAP_URL), LOCATION_SELECTOR)
                    .filter(SITEMAP.asPredicate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    static Stream<Integer> getCompanies(String sitemap) {
        try {
            return getElements(getDocument(sitemap), LOCATION_SELECTOR)
                    .filter(COMPANY.asPredicate())
                    .map(e -> e.substring(BASE_EMPLOYER_URL_LENGTH))
                    .map(Integer::valueOf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    static Stream<String> getKeywords(Integer id) {
        try {
            Document document = getDocument(BASE_EMPLOYER_URL + id.toString());
            return KEYWORDS.splitAsStream(document.select(BODY_SELECTOR).text().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
