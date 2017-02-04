package eu.city4age.dashboard.api.persist.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FilterQuery {
	String name() default "";

	String jpql() default "";
}