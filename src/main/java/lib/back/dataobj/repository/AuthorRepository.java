package lib.back.dataobj.repository;

import lib.back.dataobj.Author;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by apotekhin on 7/2/2014.
 */
public interface AuthorRepository extends AbstractRepository<Author,Long> {

    @Query("SELECT a FROM Author a order by a.lastName, a.firstName asc")
    Page<Author> findAll(Pageable pageable);


    @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = (:id)")
    public Author findByIdAndFetchBooksEagerly(@Param("id") Long id);

    @Query("SELECT a FROM Author a WHERE UPPER(a.firstName) like :filter% or UPPER(a.middleName) like :filter% or UPPER(a.lastName) like :filter%")
    Page<Author> findWithFilter(@Param("filter") String filter, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("delete from Author a where a.books IS EMPTY")
    int deleteOrphans();

}
