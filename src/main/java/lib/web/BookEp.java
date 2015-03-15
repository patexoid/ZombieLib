package lib.web;

import lib.back.dataobj.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by alex on 15.03.2015.
 */
@Controller
public class BookEp {

    private static Logger log = LoggerFactory.getLogger(BookEp.class);
    @Autowired
    private BookRepository _bookRepository;

}
