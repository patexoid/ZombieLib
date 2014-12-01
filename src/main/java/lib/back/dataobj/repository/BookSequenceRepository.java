package lib.back.dataobj.repository;

import lib.back.dataobj.BookSequence;
import lib.back.dataobj.Sequence;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by apotekhin on 7/2/2014.
 */
public interface BookSequenceRepository extends CrudRepository<BookSequence,Long> {


}
