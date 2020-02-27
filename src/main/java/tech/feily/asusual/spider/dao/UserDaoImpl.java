package tech.feily.asusual.spider.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import tech.feily.asusual.spider.model.UserModel;
import tech.feily.asusual.spider.utils.SessionFactoryUtils;

public class UserDaoImpl implements UserDao {

    SessionFactory sf = SessionFactoryUtils.getSessionFactory();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<UserModel> selectAll() {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from UserModel");
        List<UserModel> users = query.list();
        tx.commit();
        session.close();
        return users;
    }

    public void add(UserModel userModel) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.save(userModel);
        tx.commit();
        session.close();
    }

    public void update(UserModel userModel) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.update(userModel);
        tx.commit();
        session.close();
    }

}
