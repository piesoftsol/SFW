package com.piesoftsol.oneservice.common.integration.swagger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.lang.reflect.Method;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;

import com.piesoftsol.oneservice.common.integration.config.SwaggerConfig;
import com.piesoftsol.oneservice.common.integration.swagger.ExtendedSwaggerProxyRequestMappingHandlerMapping;

import io.swagger.models.Swagger;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

/**
 * Test case for ExtendedSwaggerProxyRequestMappingHandlerMapping.java.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("unchecked")
@PrepareForTest(AnnotationUtils.class)
public class ExtendedSwaggerProxyRequestMappingHandlerMappingTest {

	/**
	 * Sets up for test
	 * 
	 * @param propertyKey
	 * @return ExtendedSwaggerProxyRequestMappingHandlerMapping
	 */
	private ExtendedSwaggerProxyRequestMappingHandlerMapping setup(String propertyKey) {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		Environment environment = mock(Environment.class);
		when(environment.getProperty(anyString())).thenReturn(propertyKey);

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

		return handlerMapping;
	}

	// Success scenarios
	@Test
	public void testDoMappingWithMappingPath() {
		ExtendedSwaggerProxyRequestMappingHandlerMapping handlerMapping = setup("propertyKey");

		PropertySourcedMapping propertySourcedMapping = mock(PropertySourcedMapping.class);

		mockStatic(AnnotationUtils.class);
		when(AnnotationUtils.getAnnotation(any(Method.class), any(Class.class))).thenReturn(propertySourcedMapping);

		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.toString()).thenReturn("handlerMethod");

		ReflectionTestUtils.invokeMethod(handlerMapping, "doMapping",
				new Object[] { this.getClass(), PropertySourcedMapping.class.getMethods()[0], null, handlerMethod });
		assertNotNull(handlerMapping);
	}

	// Negative scenarios
	@Test
	public void testDoMappingWithoutMappingPath() {
		ExtendedSwaggerProxyRequestMappingHandlerMapping handlerMapping = setup(null);

		PropertySourcedMapping propertySourcedMapping = mock(PropertySourcedMapping.class);
		when(propertySourcedMapping.propertyKey()).thenReturn(null);

		mockStatic(AnnotationUtils.class);
		when(AnnotationUtils.getAnnotation(any(Method.class), any(Class.class))).thenReturn(propertySourcedMapping);

		HandlerMethod handlerMethod = mock(HandlerMethod.class);
		when(handlerMethod.toString()).thenReturn("handlerMethod");

		ReflectionTestUtils.invokeMethod(handlerMapping, "doMapping",
				new Object[] { this.getClass(), PropertySourcedMapping.class.getMethods()[0], null, handlerMethod });
		assertNotNull(handlerMapping);
	}
}
