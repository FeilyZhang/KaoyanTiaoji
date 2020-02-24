package tech.feily.asusual.spider.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtils {

    private static SessionFactory sessionFactory = null;
    
    static {
        Configuration cfg = new Configuration();
        cfg.configure();
        sessionFactory = cfg.buildSessionFactory();
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
}
