package tech.feily.asusual.spider.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import tech.feily.asusual.spider.model.InfoModel;
import tech.feily.asusual.spider.utils.SessionFactoryUtils;

public class InfoDaoImpl implements InfoDao {

    SessionFactory sf = SessionFactoryUtils.getSessionFactory();
    
    public void add(InfoModel info) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.save(info);
        tx.commit();
        session.close();
    }

    public void update(InfoModel info) {
        SessionFactory sf = SessionFactoryUtils.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.update(info);
        tx.commit();
        session.close();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<InfoModel> selectAll() {
        SessionFactory sf = SessionFactoryUtils.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from InfoModel");
        List<InfoModel> infos = query.list();
        tx.commit();
        session.close();
        return infos;
    }

}
