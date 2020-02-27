package tech.feily.asusual.spider.action;

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

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.feily.asusual.spider.model.BQModel;
import tech.feily.asusual.spider.model.CampusModel;
import tech.feily.asusual.spider.model.InfoModel;
import tech.feily.asusual.spider.service.InfoService;
import tech.feily.asusual.spider.service.InfoServiceImpl;
import tech.feily.asusual.spider.utils.BlockedQueue;

/*
 * This class initiates a request for the URL, 
 * parses the HTML document, and then performs further operations.
 * @author FeilyZhang
 * @version v0.1
 */
public class HttpRequest {
    
    String keyWord = ".*2020.*调剂.*";
    String contentType = "text/html.*";
    Timestamp timestamp = null;

    Logger log = Logger.getLogger(HttpRequest.class);
    InfoService is = new InfoServiceImpl();
    OkHttpClient client = new OkHttpClient();
    ExecutorService exec = Executors.newFixedThreadPool(3);
    Map<String, InfoModel> cache = new ConcurrentHashMap<String, InfoModel>();
    
    public HttpRequest() {
        List<InfoModel> infos = is.selectAll();
        for (InfoModel info : infos) {
            System.out.println(info.toString());
            cache.put(info.getTitle(), info);
        }
        log.info("缓存初始化完成。");
    }
    
    public void get(final CampusModel campus, final BlockedQueue queue) throws IOException {
        Request request = new Request.Builder()
                .url(campus.getUrl())
                .headers(setHeaders())
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            public void onFailure(Call arg0, final IOException arg1) {
                // Submit a task to thread pool for handling exception.
                log.error("http请求异常，请求url为 " + campus.getUrl() + ", 相关信息为 " + arg1.getMessage());
            }

            public void onResponse(Call arg0, Response arg1) throws IOException {
                if (arg1.code() == 200) {
                    byte[] html = arg1.body().bytes();
                    // First, analyze charset.
                    String charset = parseCharset(html);
                    if (charset != null) {
                        // Then, parsing all eligible content in HTML document.
                        final List<InfoModel> infos = parseLink(html, charset, getHost(campus.getUrl()));
                        if (infos.isEmpty() || infos == null) {
                            log.info(campus.getName() + "已扫描，未发现信息！");
                        } else {
                            for (final InfoModel info : infos) {
                                // If the cache already exists, refresh the cache first and then update the table 'target' .
                                if (info != null && cache.containsKey(info.getTitle())) {
                                    cache.put(info.getTitle(), info);
                                    exec.execute(new Runnable() {
                                        public void run() {
                                            is.update(info);
                                            log.info("已存在于缓存和数据表中，同时已刷新缓存和数据表。" + info.toString());
                                        }
                                    });
                                // Otherwise, refresh the cache first
                                // and submit a task to submit the infoModel to the BlockedQueue.
                                } else if (info != null && !cache.containsKey(info.getTitle())) {
                                    log.info("初次请求，已写入数据表并刷新缓存，" + info.toString());
                                    cache.put(info.getTitle(), info);
                                    final BQModel bq = new BQModel();
                                    bq.setCampusModel(campus);
                                    bq.setInfoModel(info);
                                    exec.execute(new Runnable() {
                                        public void run() {
                                            queue.produce(bq);
                                        }
                                    });
                                }
                            }
                        }
                    }
                } else {
                    log.error(campus.toString() + "扫描失败, 请求状态为" + arg1.isSuccessful() + ", 请求码为" + arg1.code());
                }
                arg1.close();
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
                // If the current cache already contains the resolved hyperlink.
                // Then you only need to modify the necessary fields.
                if (cache.containsKey(link.text())) {
                    InfoModel infoModel = cache.get(link.text());
                    infoModel.setLastVisit(timestamp);
                    infoModel.setVisitCount((long)(infoModel.getVisitCount() + 1));
                    infos.add(infoModel);
                // Otherwise, reassemble an infoModel.
                } else {
                    InfoModel infoModel = new InfoModel();
                    infoModel.setTitle(link.text());
                    infoModel.setFirstVisit(timestamp);
                    infoModel.setLastVisit(timestamp);
                    infoModel.setVisitCount((long)1);
                    // Generate full URL.
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
    
    public static Headers setHeaders() {
        Builder headerBuilder = new Builder();
        headerBuilder.add("Accept", "text/plain, text/html");
        headerBuilder.add("Accept-Language", "en,zh");
        headerBuilder.add("User-Agent", "Mozilla/5.0 (Linux; X11)");
        headerBuilder.add("Cache-Control", "no-cache");
        return headerBuilder.build();
    }
    
}