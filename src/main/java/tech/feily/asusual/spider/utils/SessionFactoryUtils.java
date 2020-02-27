package tech.feily.asusual.spider.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * This class obtains an instance of sessionfactory through the factory method.
 * @author Feily Zhang
 * @version v0.1
 * @email fei@feily.tech
 */
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
