package com.piesoftsol.oneservice.common.integration.swagger;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriTemplate;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;

/**
 * Proxy class for Swagger Request mapping handler
 * 
 * @author KARUNAR
 */
public class SwaggerProxyRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private static final AppLogger LOGGER = new AppLogger(SwaggerProxyRequestMappingHandlerMapping.class.getName());

	protected final Map<String, HandlerMethod> handlerMethodMap = new LinkedHashMap<String, HandlerMethod>();
	protected final Object handlerObject;
	protected final String servicePath;

	/**
	 * Constructor
	 * 
	 * @param handler
	 * @param basePath
	 */
	public SwaggerProxyRequestMappingHandlerMapping(Object handler, String basePath) {
		this.handlerObject = handler;
		this.servicePath = basePath;
	}

	@Override
	protected void initHandlerMethods() {
		LOGGER.debug("Handler methods initialization initiated");

		setOrder(Ordered.HIGHEST_PRECEDENCE + 1000);
		Class<?> clazz = handlerObject.getClass();

		if (isHandler(clazz)) {
			RequestMappingInfo classRequestMappingInfo = createRequestMappingInfo(clazz);

			for (Method method : clazz.getMethods()) {
				if (isValidMethod(method)) {
					RequestMappingInfo methodRequestMappingInfo = getMappingForMethod(method, clazz);

					if (methodRequestMappingInfo == null && classRequestMappingInfo == null) {
						LOGGER.info("URL path cannot be mapped onto method " + method.getName() + " for class "
								+ clazz.getName() + " as no [@RequestMapping] was found");
						continue;
					}

					RequestMappingInfo requestMappingInfo = (RequestMappingInfo) ObjectUtils
							.defaultIfNull(methodRequestMappingInfo, classRequestMappingInfo);
					HandlerMethod handlerMethod = createHandlerMethod(handlerObject, method);
					doMapping(clazz, method, requestMappingInfo, handlerMethod);
				}
			}
		}
	}

	/**
	 * Does path mapping with base path
	 * 
	 * @param clazz
	 * @param method
	 * @param requestMappingInfo
	 * @param handlerMethod
	 */
	protected void doMapping(Class<?> clazz, Method method, RequestMappingInfo requestMappingInfo,
			HandlerMethod handlerMethod) {
		if (null != handlerMethodMap && null != clazz && null != method && null != handlerMethod
				&& null != requestMappingInfo && null != requestMappingInfo.getPatternsCondition()) {
			for (String swaggerPath : requestMappingInfo.getPatternsCondition().getPatterns()) {
				swaggerPath = servicePath + swaggerPath;

				LOGGER.info("Mapped URL path " + swaggerPath + " onto method " + handlerMethod.toString());
				handlerMethodMap.put(swaggerPath, handlerMethod);
			}
		}
	}

	@Override
	protected boolean isHandler(Class<?> beanClass) {
		return ((AnnotationUtils.findAnnotation(beanClass, Controller.class) != null)
				|| (AnnotationUtils.findAnnotation(beanClass, RequestMapping.class) != null));
	}

	@Override
	protected HandlerMethod lookupHandlerMethod(String url, HttpServletRequest httpServletRequest) throws Exception {
		LOGGER.debug("Searching handler for path: " + url);
		HandlerMethod handlerMethod = null;

		if (null != handlerMethodMap && isNotBlank(url) && null != httpServletRequest) {
			handlerMethod = handlerMethodMap.get(url);

			if (handlerMethod != null) {
				return handlerMethod;
			}

			for (String path : handlerMethodMap.keySet()) {
				if (isNotBlank(path)) {
					UriTemplate uriTemplate = new UriTemplate(path);
					if (uriTemplate.matches(url)) {
						httpServletRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
								uriTemplate.match(url));
						return handlerMethodMap.get(path);
					}
				}
			}
		}
		return handlerMethod;
	}

	/**
	 * Creates request mapping info
	 * 
	 * @param annotatedElement
	 * @return RequestMappingInfo
	 */
	private RequestMappingInfo createRequestMappingInfo(AnnotatedElement annotatedElement) {
		RequestMappingInfo requestMappingInfo = null;

		if (null != annotatedElement) {
			RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(annotatedElement,
					RequestMapping.class);
			RequestCondition<?> requestCondition = (annotatedElement instanceof Class
					? getCustomTypeCondition((Class<?>) annotatedElement)
					: getCustomMethodCondition((Method) annotatedElement));
			requestMappingInfo = (requestMapping != null ? createRequestMappingInfo(requestMapping, requestCondition)
					: null);
		}
		return requestMappingInfo;
	}

	/**
	 * Verifies if the method is valid
	 * 
	 * @param method
	 * @return isValidMethod
	 */
	private boolean isValidMethod(Method method) {
		return (AnnotationUtils.findAnnotation(method, RequestMapping.class) != null)
				&& Modifier.isPublic(method.getModifiers());
	}
}