package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.COMMON_PACKAGE_STRUCTURE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.SERVICE_NAME;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.piesoftsol.oneservice.common.integration.swagger.ExtendedSwaggerProxyRequestMappingHandlerMapping;
import com.piesoftsol.oneservice.common.integration.swagger.SwaggerProxyRequestMappingHandlerMapping;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiResourceController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * Swagger configuration
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author ArchanaK
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

	private static final String SWAGGER_BASE_PATH = "/" + SERVICE_NAME;

	/**
	 * To configure Swagger API
	 * 
	 * @param swaggerHostPort
	 * @return
	 */
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage(COMMON_PACKAGE_STRUCTURE)).paths(PathSelectors.any())
				.build().apiInfo(apiEndPointsInfo());
	}
	
	private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(SERVICE_NAME)
            .description(SERVICE_NAME)
            .contact(new Contact("Kiran Kumar B", "http://iampsk.in", "hydsri.kiran@gmail.com"))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build();
    }

	@Bean
	public HandlerMapping extendedSwaggerProxyRequestMappingHandlerMapping(Environment environment,
			DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
		return new ExtendedSwaggerProxyRequestMappingHandlerMapping(environment,
				new Swagger2Controller(environment, documentationCache, mapper, jsonSerializer), SWAGGER_BASE_PATH);
	}

	@Bean
	public HandlerMapping swaggerProxyRequestMappingHandlerMapping(ApiResourceController apiResourceController) {
		return new SwaggerProxyRequestMappingHandlerMapping(apiResourceController, SWAGGER_BASE_PATH);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		resourceHandlerRegistry.addResourceHandler(SWAGGER_BASE_PATH + "/swagger-ui.html**")
				.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
		resourceHandlerRegistry.addResourceHandler(SWAGGER_BASE_PATH + "/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}