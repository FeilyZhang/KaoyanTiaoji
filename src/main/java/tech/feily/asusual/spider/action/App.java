package tech.feily.asusual.spider.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import tech.feily.asusual.spider.model.InfoModel;
import tech.feily.asusual.spider.service.BlockedQueue;
import tech.feily.asusual.spider.service.HttpService;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        final BlockedQueue queue = new BlockedQueue();
        final List<String> urls = new ArrayList<String>();
        urls.add("https://yz.scu.edu.cn/");
        urls.add("http://yzb.nwu.edu.cn/");
        urls.add("http://gs.djtu.edu.cn/zs.asp?f_menu_id=4");
        urls.add("https://gs.sit.edu.cn/_t195/5703/list.psp");
        urls.add("http://ge.sues.edu.cn/19709/list.htm");
        urls.add("https://yjsc.shiep.edu.cn/948/list.htm");
        HttpService http = new HttpService();
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    InfoModel infoModel = queue.consume();
                    System.out.println("取出阻塞队列，发邮件并添加至target表。\n " + infoModel.toString());
                }
            }
            
        }).start();
        
        while (true) {
            for (String url : urls) {
                try {
                    http.get(url, queue);
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}

