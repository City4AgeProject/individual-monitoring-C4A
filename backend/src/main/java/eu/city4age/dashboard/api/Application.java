package eu.city4age.dashboard.api;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import eu.city4age.dashboard.api.config.JerseyInitialization;
import eu.city4age.dashboard.api.persist.generic.GenericRepositoryFactoryBean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableSwagger2
@ComponentScan(basePackages = { "eu.city4age.dashboard.api.persist", "eu.city4age.dashboard.api.rest",
		"eu.city4age.dashboard.api.config" })
@EnableJpaRepositories(basePackages = "eu.city4age.dashboard.api.persist", repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class)
/**
 * Main configuration of spring-boot.
 * http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/htmlsingle/#using-boot-configuration-classes
 * 
 * @author milos.holclajtner
 */
public class Application extends SpringBootServletInitializer {

	static protected Logger logger = LogManager.getLogger(Application.class);

	@Value("${springfox.documentation.swagger.v2.path}")
	private String swagger2Endpoint;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		new SpringApplication(Application.class).run(args);
	}

	/**
	 * Jersey rest services spring boot integration (registering jersey
	 * servlet).
	 * http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/htmlsingle/#boot-features-jersey
	 */
	/**
	 * @return ServletRegistrationBean
	 */
	@Bean
	public ServletRegistrationBean jerseyServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rest/*");
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyInitialization.class.getName());
		return registration;
	}

	/**
	 * Entity manager factory.
	 * https://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/html/howto-data-access.html#howto-use-custom-entity-manager
	 */
	/**
	 * @return EntityManagerFactory
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setPersistenceUnitName("persistenceUnit");
		entityManagerFactoryBean.setPackagesToScan("eu.city4age.dashboard.api.pojo.domain");
		entityManagerFactoryBean.setJpaProperties(new Properties() {
			private static final long serialVersionUID = -6276565382141267487L;
			{
				put("hibernate.current_session_context_class", SpringSessionContext.class.getName());
			}
		});
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean.getObject();
	}

	/**
	 * Hibernate session factory (using JPA session factory).
	 * http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/html/boot-features-sql.html#boot-features-jpa-and-spring-data
	 */
	/**
	 * @return HibernateJpaSessionFactoryBean
	 */
	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}

	/**
	 * Spring transation manager (using JTA transaction manager for JNDI
	 * datasource).
	 * https://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/html/boot-features-jta.html#boot-features-jta-javaee
	 */
	/**
	 * @return PlatformTransactionManager
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JtaTransactionManager();
	}

	/**
	 * Spring transation template.
	 */
	/**
	 * @return TransactionTemplate
	 */
	@Bean
	public TransactionTemplate transactionTemplate() {
		return new TransactionTemplate(transactionManager());
	}

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

}
