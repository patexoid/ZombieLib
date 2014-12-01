package lib.back.dataobj;

import lib.back.ObjectList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by alex on 30.01.14.
 */
public class HibernateUtils {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HibernateUtils.class);

    private static SessionFactory sessionFactory;
    private static Session session;
    static {
        sessionFactory = new Configuration().configure(new File("hibernate.cfg.xml")).buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void save(Object obj) {
        if(session.get(obj.getClass(),getId(obj))!=null){
            session.merge(obj);
        } else{
            session.saveOrUpdate(obj);
        }
    }

    public static Object get(Class clazz, Long id) {
        return session.get(clazz, id);
    }

    public static void flush() {
        session.flush();
    }

    public  static Session getSession(){
        return session;
    }


    public static ObjectList loadObjects(Class clazz){
        return new ObjectList(clazz);
    } 
    public static void close(){
        session.close();
        sessionFactory.close();
    }

    public static String getId(Class clazz){
        return sessionFactory.getClassMetadata(clazz).getIdentifierPropertyName();
    }

    public static Serializable getId(Object o){
        try {
            String idField = getId(o.getClass());
            Method method = o.getClass().getMethod("get" + idField.substring(1, 2).toUpperCase() + idField.substring(2));

            return (Serializable) method.invoke(o);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

}
