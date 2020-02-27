package tech.feily.asusual.spider.utils;

import java.util.List;

import tech.feily.asusual.spider.model.UserModel;

public class PhoneUtils {

    public static String[] get(List<UserModel> users) {
        String[] phones = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            phones[i] = users.get(i).getPhone();
        }
        return phones;
    }
    
}
