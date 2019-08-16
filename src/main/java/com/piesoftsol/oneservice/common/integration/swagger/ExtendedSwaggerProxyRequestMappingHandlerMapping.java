package com.piesoftsol.oneservice.common.integration.swagger;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;

import springfox.documentation.spring.web.PropertySourcedMapping;

/**
 * Extended Proxy class for Swagger Request mapping handler
 * 
 * @author KARUNAR
 */
public class ExtendedSwaggerProxyRequestMappingHandlerMapping extends SwaggerProxyRequestMappingHandlerMapping {

	private static final AppLogger LOGGER = new AppLogger(
			ExtendedSwaggerProxyRequestMappingHandlerMapping.class.getName());

	private final Environment environment;

	/**
	 * Constructor
	 * 
	 * @param environment
	 * @param handlerObject
	 * @param servicePath
	 */
	public ExtendedSwaggerProxyRequestMappingHandlerMapping(Environment environment, Object handlerObject,
			String servicePath) {
		super(handlerObject, servicePath);
		this.environment = environment;
	}

	@Override
	protected void doMapping(Class<?> clazz, Method method, RequestMappingInfo requestMappingInfo,
			HandlerMethod handlerMethod) {
		PropertySourcedMapping propertySourcedMapping = AnnotationUtils.getAnnotation(method,
				PropertySourcedMapping.class);
		String mappingPath = getMappingPath(propertySourcedMapping);

		if (mappingPath != null) {
			mappingPath = servicePath + mappingPath;

			LOGGER.info("URL path " + mappingPath + " with method " + handlerMethod.toString());
			handlerMethodMap.put(mappingPath, handlerMethod);
		} else {
			super.doMapping(clazz, method, requestMappingInfo, handlerMethod);
		}
	}

	/**
	 * Gets the mapping path with base path using closure lambda expression
	 * 
	 * @param propertySourcedMapping
	 * @return mappingPath - String
	 */
	private String getMappingPath(final PropertySourcedMapping propertySourcedMapping) {
		String mappingPath = null;

		if (null != propertySourcedMapping) {
			final String propertyKey = propertySourcedMapping.propertyKey();
			mappingPath = (String) Optional.ofNullable(environment.getProperty(propertyKey))
					.map(param -> param.replace("${" + propertyKey + "}", param)).orElse(null);
		}
		return mappingPath;
	}
}