package lib;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * Created by apotekhin on 7/2/2014.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("/spring/data-access.properties")
public class SpringConfig {

    @Bean
    public DataSource dataSource(
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.username}") String login,
            @Value("${jdbc.password}") String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(login);
        dataSource.setPassword(password);
        return dataSource;
    }

   @Bean
    public LocalSessionFactoryBean createSessionFactoryBean(DataSource dataSource, @Value("${hiberante.databasePlatform}") String dialect) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan("lib.back.dataobj");
        Properties properties = new Properties();
        properties.setProperty("DIALECT", dialect);
//        properties.setProperty("database", "H2");
        localSessionFactoryBean.setHibernateProperties(properties);
        return localSessionFactoryBean;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringConfig.class, args);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory(EntityManagerFactory emf) {
        HibernateJpaSessionFactoryBean factory = new HibernateJpaSessionFactoryBean();
        factory.setEntityManagerFactory(emf);
        return factory;
    }

}
