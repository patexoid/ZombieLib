package lib.back.dataobj.repository;

import lib.back.dataobj.Author;
import lib.back.dataobj.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by apotekhin on 7/2/2014.
 */
public interface BookRepository extends CrudRepository<Book,Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Book b where b.authors IS EMPTY and b.series IS EMPTY")
    int deleteOrphans();
}
