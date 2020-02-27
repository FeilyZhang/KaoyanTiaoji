package tech.feily.asusual.spider.action;

import java.sql.Timestamp;

import tech.feily.asusual.spider.model.UserModel;
import tech.feily.asusual.spider.service.UserService;
import tech.feily.asusual.spider.service.UserServiceImpl;
import tech.feily.asusual.spider.utils.SmsUtils;

public class UserAdd {

    public static void main(String[] args) {
        UserService us = new UserServiceImpl();
        UserModel um = new UserModel();
        um.setCreateTime(new Timestamp(System.currentTimeMillis()));
        um.setLastSendTime(new Timestamp(System.currentTimeMillis()));
        um.setPhone("+8615513860576");
        um.setSendCount(1);
        us.add(um);
        int[] lenSuc = SmsUtils.send(new String[] {um.getPhone()}, new String[] {"用户"}, "541912");
        System.out.println(lenSuc[0] + " message(s) sent, " + lenSuc[1] + " successful!");
    }

}
