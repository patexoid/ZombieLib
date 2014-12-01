package lib;

import lib.back.dataobj.mysqldumpparser.Parser;
import lib.front.MainJFrame;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
//        HibernateUtils.getSessionFactory();
//        new Parser().parseAll();
        ClassPathXmlApplicationContext fsapc=new ClassPathXmlApplicationContext("spring\\lib-base-config.xml");
        MainJFrame mainJFrame = fsapc.getBean(MainJFrame.class);
        mainJFrame.start();
    }
}
