package tech.feily.asusual.spider.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.feily.asusual.spider.model.InfoModel;

public class HttpService {
    
    String keyWord = ".*2020.*调剂.*";
    String contentType = "text/html.*";
    Timestamp timestamp = null;
    
    OkHttpClient client = new OkHttpClient();
    Map<String, InfoModel> cache = new ConcurrentHashMap<String, InfoModel>();
    
    ExecutorService exec = Executors.newFixedThreadPool(3);
    
    public void get(final String url, final BlockedQueue queue) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            public void onFailure(Call arg0, final IOException arg1) {
                // Submit a task to thread pool for handling exception.
                exec.execute(new Runnable() {
                    public void run() {
                        System.out.println("Exception occurred, exception stack are as follows.");
                        arg1.printStackTrace();
                    }
                });
            }

            public void onResponse(Call arg0, Response arg1) throws IOException {
                byte[] html = arg1.body().bytes();
                // First, analyze charset.
                String charset = parseCharset(html);
                if (charset != null) {
                    // Then, parsing all eligible content in HTML document.
                    final List<InfoModel> infos = parseLink(html, charset, getHost(url));
                    for (final InfoModel info : infos) {
                        // If the cache already exists, refresh the cache first and then update the table 'target' .
                        if (info != null && cache.containsKey(info.getTitle())) {
                            cache.put(info.getTitle(), info);
                            exec.execute(new Runnable() {
                                public void run() {
                                    // update the table 'target' here.
                                    System.out.println("已添加至target，并已刷新缓存");
                                }
                            });
                        // Otherwise, refresh the cache first
                        // and submit a task to submit the infoModel to the BlockedQueue.
                        } else if (info != null && !cache.containsKey(info.getTitle())) {
                            cache.put(info.getTitle(), info);
                            exec.execute(new Runnable() {
                                public void run() {
                                    queue.produce(info);
                                }
                            });
                        }
                    }
                }
            }
            
        });
    } 
    
    /*
     * Extract its encoding from HTML document.
     * @param html Byte array of HTML document.
     * @return the charset of HTML document.
     */
    public String parseCharset(byte[] html) {
        Document doc = Jsoup.parse(new String(html));
        Elements metas = doc.getElementsByTag("meta");
        for (Element meta : metas) {
            // If the charset is set in the attribute 'content' of the tag 'meta'.
            if (Pattern.matches(contentType, meta.attr("content"))) {
                return meta.attr("content").split("=")[1];
            // Then the charset must be in the attribute 'charset' of the tag 'meta'.
            } else if (meta.hasAttr("charset")) {
                return meta.attr("charset");
            }
        }
        return null;
    }
    
    /*
     * Parsing all hyperlinks in HTML document that conform to regular expressions.
     * @param html Byte array of HTML document.
     * @param charset the charset of HTML document.
     * @param host Host name extracted from URL.
     * @return All infoModels included in this document that conform to regular expressions.
     */
    public List<InfoModel> parseLink(byte[] html, String charset, String host) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(new String(html, charset));
        Elements links = doc.getElementsByTag("a");
        List<InfoModel> infos = new ArrayList<InfoModel>();
        for (Element link : links) {
            if (Pattern.matches(keyWord, link.text())) {
                timestamp = new Timestamp(System.currentTimeMillis());
                // If the current cache already contains the resolved hyperlink。
                // Then you only need to modify the necessary fields。
                if (cache.containsKey(link.text())) {
                    InfoModel infoModel = cache.get(link.text());
                    infoModel.setLastVisit(timestamp);
                    infoModel.setVisitCount(infoModel.getVisitCount() + 1);
                    infos.add(infoModel);
                // Otherwise, reassemble an infoModel。
                } else {
                    InfoModel infoModel = new InfoModel();
                    infoModel.setTitle(link.text());
                    infoModel.setFirstVisit(timestamp);
                    infoModel.setLastVisit(timestamp);
                    infoModel.setVisitCount(1);
                    // Generate full URL。
                    if (link.attr("href").startsWith("http")) {
                        infoModel.setUrl(link.attr("href"));
                    } else {
                        infoModel.setUrl(host + link.attr("href"));
                    }
                    infos.add(infoModel);
                }
            }
        }
        return infos;
    }
    
    /*
     * Resolve host name from URL.
     * @param url
     * @return the host.
     */
    public String getHost(String url) {
        Pattern p = Pattern.compile("(http://|https://)?([^/]*)",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(url);
        return m.find()?m.group(2):url;
    }
    
}