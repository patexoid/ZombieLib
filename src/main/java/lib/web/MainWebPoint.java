package lib.web;

import lib.back.dataobj.Author;
import lib.back.dataobj.AuthorAnnotation;
import lib.back.dataobj.AuthorWrapper;
import lib.back.dataobj.mysqldumpparser.JDBIParserImpl;
import lib.back.dataobj.mysqldumpparser.JpaParserImpl;
import lib.back.dataobj.mysqldumpparser.MultiThreadJDBIParserImpl;
import lib.back.dataobj.repository.AuthorAnnotationRepository;
import lib.back.dataobj.repository.AuthorRepository;
import lib.back.dataobj.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by apotekhin on 7/4/2014.
 */
@Controller
public class MainWebPoint {
    private static Logger log = LoggerFactory.getLogger(JpaParserImpl.class);

    @Autowired
    private AuthorRepository _authorRepository;

    @Autowired
    private AuthorAnnotationRepository _authorAnnotationRepository;

    @Autowired
    private BookRepository _bookRepository;


    @RequestMapping(value="/main")
    @Transactional(readOnly = true)
    public String mainPage( Map<String,Object> model){

        Page<Author> page = _authorRepository.findAll(new PageRequest(0, 10));
        model.put("authors", page.getContent());
        model.put("pageSize", page.getTotalPages());
        return "mainPage";
    }



    @RequestMapping(value="/authors")
    @Transactional(readOnly = true)
    public @ResponseBody Page<Author> authorList(){
        Page<Author> authorsPage = _authorRepository.findAll(new PageRequest(0, 100));
        return authorsPage;
    }

    @RequestMapping(value="/authors/{filter}/{page}")
    @Transactional(readOnly = true)
    public @ResponseBody Page<Author> authorsByFilter(@PathVariable(value = "filter") String filter,@PathVariable(value = "page") Integer page){
        Page<Author> authorsPage = _authorRepository.findWithFilter(filter.toUpperCase(),new PageRequest(page-1,100));
        return authorsPage;
    }


    @RequestMapping(value="/author/{id}")
    @Transactional(readOnly = true)
    public @ResponseBody AuthorWrapper author(@PathVariable(value = "id") Long id){
        Author author = _authorRepository.findOne(id);
        AuthorAnnotation authorAnnotation = _authorAnnotationRepository.findOne(id);
        if(authorAnnotation!=null&&authorAnnotation.getBody()!=null){
            String body = authorAnnotation.getBody();
            body=body.replaceAll("\\\\r\\\\n","<br/>").replaceAll("\\\\n\\\\r","<br/>").replaceAll("\\\\n","<br/>").replaceAll("\\\\r","<br/>");
            authorAnnotation.setBody(body);
        }
        return new AuthorWrapper(author, authorAnnotation);
    }

    @Autowired
    @Lazy
    JpaParserImpl _parser;
//    MultiThreadJDBIParserImpl _parser;
//            JDBIParserImpl _parser;


    @RequestMapping(value="/parse")
    public  @ResponseBody String  parser(){
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
