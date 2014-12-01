package lib.back.dataobj.mysqldumpparser;

import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * Created by apotekhin on 9/25/2014.
 */
@Configuration
public class JDBIWrapper {

    @Autowired
    DataSource _dataSource;

    @PersistenceContext
    private EntityManager _entityManager;

    @Bean
    @Scope("prototype")
    @Lazy
    DBI getDBI() {
        return new DBI(_dataSource);
    }

}
