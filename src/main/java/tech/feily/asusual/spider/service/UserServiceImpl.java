package tech.feily.asusual.spider.service;

import java.util.List;

import tech.feily.asusual.spider.dao.UserDao;
import tech.feily.asusual.spider.dao.UserDaoImpl;
import tech.feily.asusual.spider.model.UserModel;

public class UserServiceImpl implements UserService {

    UserDao dao = new UserDaoImpl();
    
    public List<UserModel> selectAll() {
        return dao.selectAll();
    }

    public void add(UserModel userModel) {
        dao.add(userModel);
    }
    
    public void update(UserModel userModel) {
        dao.update(userModel);
    }
    
}
