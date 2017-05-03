import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
    private List<LinkInfo> allLinks;
    private int levelLimit;
    private String keyword;
    private int keywordCount;
    private int visitedCount;

    public Crawler() {
        this.allLinks = new ArrayList<LinkInfo>();
    }

    public void processLinks(final String url, int lvl, String keyword) {
        final String host;
        this.keyword = keyword;
        this.levelLimit = lvl;
        this.keywordCount = 0;
        this.visitedCount = 0;
        final int level = lvl - 1;
        if (lvl < 0)
            return;
        try {
            final URI baseUrl = new URI(url);
            host = baseUrl.getHost();
        } catch (Exception e) {
            return;
        }
        getLinksWithCount(host, url, level);
    }

    private void getLinksWithCount(String host, String adr, int level) {
        try {
            Document doc = Jsoup.parse(getPageByURL(adr));
            Elements links = doc.select("a[href]");
            int count = countKeywordEntries(doc.body().text());
            keywordCount += count;
            allLinks.add(new LinkInfo(adr, (levelLimit - level), count));
            for (Element link : links) {
                String url = link.attr("href");
                try {
                    URI uri = new URI(url);
                    if (uri.getHost() == null && uri.getPath() == null)
                        continue;
                    if (uri.getHost() == null) {
                        String path = uri.getPath();
                        if (path.length() > 0)
                            if (path.charAt(0) != '/')
                                path = "/" + path;
                        url = "http://" + host + path;
                        //System.out.println("Path " + uri.getPath());
                    } else if (url.charAt(0) == '/')
                        url = "http:" + url;
                    LinkInfo foundLink = new LinkInfo(url, levelLimit - level);
                    int index = allLinks.indexOf(foundLink);
                    if (index < 0) {
                        allLinks.add(foundLink);
                        //System.out.println("Host: " + host + " Level: " + level + " Limit = " + levelLimit + " LEVEL: " + (levelLimit - level) + " Link: " + url);
                        if (level > 0)
                            getLinksWithCount(host, url, level - 1);
                    } else if (level > 0 && allLinks.get(index).getLevel() > foundLink.getLevel() && !allLinks.get(index).isVisited()) {
                        foundLink.setVisited(true);
                        allLinks.remove(index);
                        getLinksWithCount(host, url, level - 1);
                    }
                } catch (URISyntaxException u) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int countKeywordEntries(String text) {
        visitedCount++;
        text = text.toLowerCase();
        String keywordLower = keyword.toLowerCase();
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = text.indexOf(keywordLower, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += keywordLower.length();
            }
        }
        return count;
    }

    private static String getPageByURL(String requestURL) {
        URL url;
        String response = "";
        System.out.println("Get: " + requestURL);
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-agent", System.getProperty("http.agent"));
            conn.setDoInput(true);
            int responseCode = conn.getResponseCode();
            if (responseCode < 400) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.toString();
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void printLink(){
        for (LinkInfo link : allLinks) {
            System.out.println("Link adress: " + link.getLink() + " Count: " + link.getCount());
        }
        System.out.println("Total links found = " + allLinks.size());
        System.out.println("Total keyword usage = " + keywordCount);
    }
}
