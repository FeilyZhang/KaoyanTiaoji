package tech.feily.asusual.spider.dao;

import java.util.List;

import tech.feily.asusual.spider.model.UserModel;

public interface UserDao {

    public abstract List<UserModel> selectAll();
    public abstract void add(UserModel userModel);
    public abstract void update(UserModel userModel);
    
}
