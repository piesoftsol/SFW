package com.piesoftsol.oneservice.common.integration.annotations;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.COMMON_PACKAGE_STRUCTURE;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Retention(RUNTIME)
@Target(TYPE)
@EnableEurekaClient
@Configuration
@ComponentScan(basePackages = { COMMON_PACKAGE_STRUCTURE })
@SpringBootApplication
@EnableAspectJAutoProxy
public @interface OneServiceInjector {

}
