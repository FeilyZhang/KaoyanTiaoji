package tech.feily.asusual.spider.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import tech.feily.asusual.spider.service.InfoService;
import tech.feily.asusual.spider.service.InfoServiceImpl;
import tech.feily.asusual.spider.utils.BlockedQueue;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Logger log = Logger.getLogger(App.class);
        final BlockedQueue queue = new BlockedQueue();
        final List<String> urls = new ArrayList<String>();
        urls.add("https://yz.scu.edu.cn/");
        urls.add("http://yzb.nwu.edu.cn/");
        urls.add("http://gs.djtu.edu.cn/zs.asp?f_menu_id=4");
        urls.add("https://gs.sit.edu.cn/_t195/5703/list.psp");
        urls.add("http://ge.sues.edu.cn/19709/list.htm");
        urls.add("https://yjsc.shiep.edu.cn/948/list.htm");
        HttpRequest http = new HttpRequest();
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    InfoService is = new InfoServiceImpl();
                    is.add(queue.consume());;
                    System.out.println("取出阻塞队列，发邮件并添加至target表。\n ");
                }
            }
        }).start();
        
        while (true) {
            for (String url : urls) {
                try {
                    http.get(url, queue);
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (IOException e) {
                    log.warn(System.currentTimeMillis() + ", HTTP request IOException.\n\t" + e.getMessage());
                    //e.printStackTrace();
                } catch (InterruptedException e) {
                    log.warn(System.currentTimeMillis() + ", HTTP request InterruptedException.\n\t" + e.getMessage());
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
            }
        }
    }

}

