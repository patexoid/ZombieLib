package lib.back.dataobj.repository;

import lib.back.dataobj.Author;
import lib.back.dataobj.AuthorAnnotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by apotekhin on 7/2/2014.
 */
public interface AuthorAnnotationRepository extends AbstractRepository<AuthorAnnotation,Long> {

}
