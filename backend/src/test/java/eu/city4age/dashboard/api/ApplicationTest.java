package eu.city4age.dashboard.api;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import eu.city4age.dashboard.api.persist.generic.GenericRepositoryFactoryBean;


@Profile("test")
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = { "eu.city4age.dashboard.api.persist", "eu.city4age.dashboard.api.rest" })
@EnableJpaRepositories(basePackages = "eu.city4age.dashboard.api.persist", repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class)
//@ActiveProfiles("test")
@PropertySource("classpath:application-test.yml")

public class ApplicationTest {

	
	static protected Logger logger = LogManager.getLogger(ApplicationTest.class);
	
	public static void main(String[] args) {
		new SpringApplication(ApplicationTest.class).run(args);

	}
	


	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.driver-class-name}")
	private String drivercn;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;  

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPackagesToScan("eu.city4age.dashboard.api.pojo.domain");
		entityManagerFactoryBean.setJpaProperties(buildHibernateProperties());
		entityManagerFactoryBean.setJpaProperties(new Properties() {
			private static final long serialVersionUID = -1589476961998384958L;
			{
				put("hibernate.current_session_context_class", SpringSessionContext.class.getName());
			}
		});
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {
			{
				setDatabase(Database.POSTGRESQL);
			}
		});
		return entityManagerFactoryBean;
	}
	
	protected Properties buildHibernateProperties() {
		Properties hibernateProperties = new Properties();
		
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
		hibernateProperties.setProperty("hibernate.show_sql", "true");
		hibernateProperties.setProperty("hibernate.format_sql", "true");
		hibernateProperties.setProperty("hibernate.use_sql_comments", "true");
		hibernateProperties.setProperty("hibernate.generate_statistics", "false");
		hibernateProperties.setProperty("javax.persistence.validation.mode", "none");

		// Audit History flags
		hibernateProperties.setProperty("org.hibernate.envers.store_data_at_delete", "true");
		hibernateProperties.setProperty("org.hibernate.envers.global_with_modified_flag", "true");

		hibernateProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

		return hibernateProperties;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}

	@Bean
	public TransactionTemplate transactionTemplate() {
		return new TransactionTemplate(transactionManager());
	}

	@Bean
	public DataSource dataSource() throws IOException {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(drivercn);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

}
