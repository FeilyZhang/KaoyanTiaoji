package tech.feily.asusual.spider.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import tech.feily.asusual.spider.model.BQModel;
import tech.feily.asusual.spider.model.CampusModel;
import tech.feily.asusual.spider.model.UserModel;
import tech.feily.asusual.spider.service.InfoService;
import tech.feily.asusual.spider.service.InfoServiceImpl;
import tech.feily.asusual.spider.service.UserService;
import tech.feily.asusual.spider.service.UserServiceImpl;
import tech.feily.asusual.spider.utils.BlockedQueue;
import tech.feily.asusual.spider.utils.CampusUtils;
import tech.feily.asusual.spider.utils.PhoneUtils;
import tech.feily.asusual.spider.utils.SmsUtils;

/**
 * Main program.
 * @author FeilyZhang
 * @version v0.1
 */
public class App {

    public static void main(String[] args) {
        final Logger log = Logger.getLogger(App.class);
        final BlockedQueue queue = new BlockedQueue();
        List<CampusModel> campuses = new ArrayList<CampusModel>();
        
        // Read file to initializes the campus list.
        String file = "//home//kaoyantiaoji//campus.txt";
        //String file = System.getProperty("user.dir") + "\\src\\main\\java\\campus.txt";
        campuses = CampusUtils.initCampuses(file, campuses);
        
        HttpRequest http = new HttpRequest();
        
        // If the required data is scanned, the thread will be woken up for processing.
        // Take out the data in the blocked queue, add it to the data table, and then send a message to inform the user.
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    InfoService is = new InfoServiceImpl();
                    BQModel bq = queue.consume();
                    // Add InfoModel to the data table.
                    is.add(bq.getInfoModel());;
                    // After getting the user phone number of the user list, send a message to notify the user.
                    UserService us = new UserServiceImpl();
                    List<UserModel> users = us.selectAll();
                    String[] phones = PhoneUtils.get(users);
                    SmsUtils.send(phones, new String[] {bq.getCampusModel().getName(), "其研招网"}, "541589");
                    // Then update related fields of the UserModel.
                    for (UserModel user : users) {
                        user.setLastSendTime(new Timestamp(System.currentTimeMillis()));
                        user.setSendCount(user.getSendCount() + 1);
                        us.update(user);
                    }
                    log.info("取出阻塞队列，发邮件并添加至target表。\n ");
                }
            }
        }).start();
        
        // Continuously scan the target URL through HTTP request.
        while (true) {
            for (CampusModel campus : campuses) {
                try {
                    http.get(campus, queue);
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (IOException e) {
                    log.warn(System.currentTimeMillis() + ", HTTP request IOException.\n\t" + e.getMessage());
                } catch (InterruptedException e) {
                    log.warn(System.currentTimeMillis() + ", HTTP request InterruptedException.\n\t" + e.getMessage());
                }
            }
        }
    }

}

