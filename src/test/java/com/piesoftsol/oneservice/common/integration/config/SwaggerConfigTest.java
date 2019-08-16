package com.piesoftsol.oneservice.common.integration.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.piesoftsol.oneservice.common.integration.config.SwaggerConfig;
import com.piesoftsol.oneservice.common.integration.swagger.ExtendedSwaggerProxyRequestMappingHandlerMapping;

import io.swagger.models.Swagger;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiResourceController;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

/**
 * Test case for SwaggerConfig.java.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SwaggerConfigTest {

	// Success scenarios
	@Test
	public void testSwaggerApi() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();
		Docket docket = swaggerConfig.swaggerApi();
		assertNotNull(docket);
	}

	@Test
	public void testSwaggerProxyRequestMappingHandlerMapping() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);

		HandlerMapping handlerMapping = swaggerConfig.swaggerProxyRequestMappingHandlerMapping(apiResourceController);
		assertNotNull(handlerMapping);
	}

	@Test
	public void testExtendedSwaggerProxyRequestMappingHandlerMapping() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		Environment environment = mock(Environment.class);
		when(environment.getProperty(anyString())).thenReturn("propertyKey");

		DocumentationCache documentationCache = mock(DocumentationCache.class);
		when(documentationCache.documentationByGroup(anyString())).thenReturn(mock(Documentation.class));

		Swagger swagger = mock(Swagger.class);
		when(swagger.getBasePath()).thenReturn("basePath");

		ServiceModelToSwagger2Mapper mapper = mock(ServiceModelToSwagger2Mapper.class);
		when(mapper.mapDocumentation(any(Documentation.class))).thenReturn(swagger);

		JsonSerializer jsonSerializer = mock(JsonSerializer.class);
		when(jsonSerializer.toJson(any(Swagger.class))).thenReturn(mock(Json.class));

		ExtendedSwaggerProxyRequestMappingHandlerMapping handlerMapping = (ExtendedSwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.extendedSwaggerProxyRequestMappingHandlerMapping(environment, documentationCache, mapper,
						jsonSerializer);
		assertNotNull(handlerMapping);
	}

	@Test
	public void testAddResourceHandlers() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ResourceHandlerRegistry resourceHandlerRegistry = mock(ResourceHandlerRegistry.class);
		ResourceHandlerRegistration resourceHandlerRegistration = mock(ResourceHandlerRegistration.class);
		when(resourceHandlerRegistry.addResourceHandler(anyString())).thenReturn(resourceHandlerRegistration);

		swaggerConfig.addResourceHandlers(resourceHandlerRegistry);
	}
}
