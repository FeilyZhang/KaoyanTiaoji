package tech.feily.asusual.spider.service;

import java.util.List;

import tech.feily.asusual.spider.model.UserModel;

public interface UserService {

    public abstract List<UserModel> selectAll();
    public abstract void add(UserModel userModel);
    public abstract void update(UserModel userModel);
    
}
