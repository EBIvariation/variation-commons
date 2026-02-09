package uk.ac.ebi.eva.commons.batch.configuration;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Spring Batch configuration for JPA/Hibernate compatibility.
 *
 * Updated for Spring Batch 5.x which removes BatchConfigurer and DefaultBatchConfigurer.
 * Extend this class and override getDataSource() to provide your DataSource.
 */
public abstract class SpringBoot1CompatibilityConfiguration extends DefaultBatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;

    protected SpringBoot1CompatibilityConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(getDataSource());
        jpaTransactionManager.setJpaDialect(new HibernateJpaDialect());
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Override
    protected ExecutionContextSerializer getExecutionContextSerializer() {
        return new Jackson2ExecutionContextStringSerializer();
    }
}
