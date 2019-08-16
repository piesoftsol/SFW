package com.piesoftsol.oneservice.common.integration.swagger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.lang.reflect.AnnotatedElement;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;

import com.piesoftsol.oneservice.common.integration.config.SwaggerConfig;
import com.piesoftsol.oneservice.common.integration.swagger.SwaggerProxyRequestMappingHandlerMapping;

import springfox.documentation.swagger.web.ApiResourceController;

/**
 * Test case for SwaggerProxyRequestMappingHandlerMapping.java.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("unchecked")
@PrepareForTest({ AnnotationUtils.class, AnnotatedElementUtils.class })
public class SwaggerProxyRequestMappingHandlerMappingTest {

	// Success scenarios
	@Test
	public void testInitHandlerMethods() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);

		SwaggerProxyRequestMappingHandlerMapping handlerMapping = (SwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.swaggerProxyRequestMappingHandlerMapping(apiResourceController);
		ReflectionTestUtils.invokeMethod(handlerMapping, "initHandlerMethods");
	}

	@Test
	public void testLookupHandlerMethod() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		SwaggerProxyRequestMappingHandlerMapping handlerMapping = (SwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.swaggerProxyRequestMappingHandlerMapping(apiResourceController);

		ReflectionTestUtils.invokeMethod(handlerMapping, "initHandlerMethods");
		HandlerMethod handlerMethod = ReflectionTestUtils.invokeMethod(handlerMapping, "lookupHandlerMethod",
				new Object[] { "/service.name/swagger-resources/configuration/ui", httpServletRequest });
		assertNotNull(handlerMethod);
	}

	// Negative scenarios
	@Test
	public void testInitHandlerMethodsMockAnnotationUtil() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);

		SwaggerProxyRequestMappingHandlerMapping handlerMapping = (SwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.swaggerProxyRequestMappingHandlerMapping(apiResourceController);

		mockStatic(AnnotationUtils.class);
		when(AnnotationUtils.findAnnotation(any(Class.class), any(Class.class))).thenReturn(null);

		ReflectionTestUtils.invokeMethod(handlerMapping, "initHandlerMethods");
	}

	@Test
	public void testInitHandlerMethodsMockAnnotationUtil1() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);

		SwaggerProxyRequestMappingHandlerMapping handlerMapping = (SwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.swaggerProxyRequestMappingHandlerMapping(apiResourceController);

		mockStatic(AnnotatedElementUtils.class);
		when(AnnotatedElementUtils.findMergedAnnotation(any(AnnotatedElement.class), any(Class.class)))
				.thenReturn(null);

		ReflectionTestUtils.invokeMethod(handlerMapping, "initHandlerMethods");
	}

	@Test
	public void testLookupHandlerMethodWithURLNotPresent() {
		SwaggerConfig swaggerConfig = new SwaggerConfig();

		ApiResourceController apiResourceController = mock(ApiResourceController.class);
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		SwaggerProxyRequestMappingHandlerMapping handlerMapping = (SwaggerProxyRequestMappingHandlerMapping) swaggerConfig
				.swaggerProxyRequestMappingHandlerMapping(apiResourceController);

		ReflectionTestUtils.invokeMethod(handlerMapping, "initHandlerMethods");
		HandlerMethod handlerMethod = ReflectionTestUtils.invokeMethod(handlerMapping, "lookupHandlerMethod",
				new Object[] { "url", httpServletRequest });
		assertNull(handlerMethod);
	}
}
