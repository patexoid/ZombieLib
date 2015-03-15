package lib.back.dataobj.repository;

import lib.back.dataobj.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apotekhin on 7/3/2014.
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends CrudRepository<T,ID> {

}
