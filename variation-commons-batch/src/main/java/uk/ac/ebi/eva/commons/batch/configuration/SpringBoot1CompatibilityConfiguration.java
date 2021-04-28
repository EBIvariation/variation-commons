package uk.ac.ebi.eva.commons.batch.configuration;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public class SpringBoot1CompatibilityConfiguration {

    private static JpaTransactionManager getTransactionManager(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(dataSource);
        jpaTransactionManager.setJpaDialect(new HibernateJpaDialect());
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    public static BatchConfigurer getSpringBoot1CompatibleBatchConfigurer(DataSource dataSource,
                                                                          EntityManagerFactory entityManagerFactory)
            throws Exception {
        final JpaTransactionManager transactionManager = getTransactionManager(dataSource, entityManagerFactory);
        final SpringBoot1CompatibleSerializer serializer = new SpringBoot1CompatibleSerializer();
        return new DefaultBatchConfigurer(dataSource) {

            @Override
            protected JobRepository createJobRepository() throws Exception {

                JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
                factory.setDataSource(dataSource);
                factory.setTransactionManager(transactionManager);
                factory.setSerializer(serializer);
                factory.afterPropertiesSet();
                return factory.getObject();
            }

            @Override
            protected JobExplorer createJobExplorer() throws Exception {
                JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
                jobExplorerFactoryBean.setSerializer(serializer);
                jobExplorerFactoryBean.setDataSource(dataSource);
                jobExplorerFactoryBean.afterPropertiesSet();
                return jobExplorerFactoryBean.getObject();
            }

            @Override
            public PlatformTransactionManager getTransactionManager() {
                return transactionManager;
            }
        };
    }
}
