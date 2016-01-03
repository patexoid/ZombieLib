package lib.web;

import lib.back.dataobj.Author;

import lib.back.mysqldumpparser.JpaParserImpl;
import lib.back.dataobj.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by apotekhin on 7/4/2014.
 */
@Controller
@RequestMapping(value="/authors")
public class AuthorWebPoint {
    private static Logger log = LoggerFactory.getLogger(JpaParserImpl.class);

    @Autowired
    private AuthorRepository _authorRepository;




    @RequestMapping(value="/main")
    @Transactional(readOnly = true)
    public String mainPage( Map<String,Object> model){

        Page<Author> page = _authorRepository.findAll(new PageRequest(0, 10));
        model.put("authors", page.getContent());
        model.put("pageSize", page.getTotalPages());
        return "mainPage";
    }



    @Transactional(readOnly = true)
    @RequestMapping
    public @ResponseBody Page<Author> authorList(){
        Page<Author> authorsPage = _authorRepository.findAll(new PageRequest(0, 100));
        return authorsPage;
    }

    @RequestMapping(value="/{filter}/{page}")
    @Transactional(readOnly = true)
    public @ResponseBody Page<Author> authorsByFilter(@PathVariable(value = "filter") String filter,@PathVariable(value = "page") Integer page){
        Page<Author> authorsPage = _authorRepository.findWithFilter(filter.toUpperCase(),new PageRequest(page-1,100));
        return authorsPage;
    }


 /*   @RequestMapping(value="/{id}")
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
    }*/


}
