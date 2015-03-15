package lib.web;

import lib.back.dataobj.mysqldumpparser.JpaParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by alex on 15.03.2015.
 */
@Controller
public class ConfigEp {

    private static Logger log = LoggerFactory.getLogger(ConfigEp.class);

    @Autowired
    @Lazy
    JpaParserImpl _parser;
//    MultiThreadJDBIParserImpl _parser;
//            JDBIParserImpl _parser;


    @RequestMapping(value="/parse")
    public  @ResponseBody
    String  parser(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("Parser thread");
                    _parser.parseAll("D:\\dev\\projects\\ZombieLib\\dump");
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }.start();

        return "started";
    }
}
