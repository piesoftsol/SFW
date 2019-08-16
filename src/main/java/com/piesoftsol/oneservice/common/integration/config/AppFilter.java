package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.APPLICATION_JSON;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.CONTENT_TYPE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.ERROR_CODE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_PATTERN;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_INTIME_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_OUTTIME_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_INTIME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_URL;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_IP;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.RESPONSE_CODE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.RESPONSE_MESSAGE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.SERVICE_NAME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.TAG_INSTANCE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.VALIDATION_ERRORS;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PROCESS_TIME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PC_REQ;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.YES;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.replace;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.piesoftsol.oneservice.common.integration.util.AppLogger;
import com.piesoftsol.oneservice.common.integration.util.EnableJsonValidation;
import com.piesoftsol.oneservice.common.integration.util.JSONSchemaValidator;

/**
 * Application Filter class
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
@SuppressWarnings("deprecation")
@Conditional(EnableJsonValidation.class)
@Component
public class AppFilter implements Filter {

	private static final AppLogger LOGGER = new AppLogger(AppFilter.class.getName());

	private static final List<String> JSON_VALIDATION_TAGS_NOT_ALLOWED = new ArrayList<String>();
	private static final List<String> ALLOWED_FILTER_REQUEST_METHODS = new ArrayList<String>();

	static {
		JSON_VALIDATION_TAGS_NOT_ALLOWED.add("schema");

		ALLOWED_FILTER_REQUEST_METHODS.add(RequestMethod.PUT.toString());
		ALLOWED_FILTER_REQUEST_METHODS.add(RequestMethod.PATCH.toString());
		ALLOWED_FILTER_REQUEST_METHODS.add(RequestMethod.POST.toString());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		final String methodName = "doFilter";
		LOGGER.startMethod(methodName);
		// Creating a wrapper class for servletResponse
		final AppHttpServletResponseWrapper responseWrapper = new AppHttpServletResponseWrapper(
				(HttpServletResponse) servletResponse);

		// Creating a wrapper class for servletRequest
		final AppHttpServletRequestWrapper requestWrapper = new AppHttpServletRequestWrapper(
				(HttpServletRequest) servletRequest);
		String requestURI = requestWrapper.getRequestURI();
		final String requestMethod = requestWrapper.getMethod();
		if (isNotBlank(requestURI)) {
			MDC.put(REQUEST_URL, requestURI);
			LOGGER.debug("Request Initiated for :: " + MDC.get(REQUEST_URL));
		}
		
		LOGGER.info("Incoming Request URI :: " + requestURI + ". Request Method :: " + requestMethod);
		
		if (isNotBlank(requestURI) && isNotBlank(requestMethod)
				&& ALLOWED_FILTER_REQUEST_METHODS.contains(requestMethod) && requestURI.contains(SERVICE_NAME)) {
			ProcessingReport report = null;
			InputStream schemaStream = null;

			try {
				final byte[] requestBody = requestWrapper.getBody();
				final String requestBodyStr = new String(requestBody);

				LOGGER.info("Incoming Request Body :: " + requestBodyStr.replaceAll("\n", ""));
				
				requestURI = requestURI.substring(requestURI.lastIndexOf("/") + 1, requestURI.length());
				if (isNotBlank(requestURI)) {
					// Read schema as stream
					schemaStream = JSONSchemaValidator.class.getResourceAsStream("/schema/" + requestURI + ".json");
					// Validating the input request against its JSON schema
					report = JSONSchemaValidator.validate(requestBody, schemaStream);					
				}
			} catch (Exception exception) {
				LOGGER.error(methodName, "Exception: " + exception.getMessage());

				JSONObject resposonseObj = new JSONObject();

				// In case of any exception
				setReponseDetails(responseWrapper, resposonseObj, HttpStatus.INTERNAL_SERVER_ERROR, ERROR_CODE,
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
			} finally {
				try {
					if (null != schemaStream) {
						schemaStream.close();
					}
				} catch (IOException ioException) {
					LOGGER.error(methodName, "Exception while closing schema input stream - " + requestURI + ".json : "
							+ ioException.getMessage());
				}
			}

			if (null != report) {
				if (report.isSuccess()) {
					/*
					 * Forwarding the wrapped request as the original
					 * inputStream is read and is unusable in controller
					 */
					chain.doFilter(requestWrapper, responseWrapper);
				} else {
					JSONObject resposonseObj = new JSONObject();

					// In case if the JSON schema validation fails,
					// populate the report in response
					final List<JSONObject> jsonValidationErrorList = extractJSONValidationError(report);

					// In case of bad request
					setReponseDetails(responseWrapper, resposonseObj, HttpStatus.BAD_REQUEST, ERROR_CODE, null,
							jsonValidationErrorList);
				}
			}
		} else {
			chain.doFilter(requestWrapper, responseWrapper);
		}

		responseWrapper.flushBuffer();
		LOGGER.info("Outgoing Response Body :: " + new String(responseWrapper.getCopy()));

		LOGGER.endMethod(methodName);
	}

	/**
	 * Sets the response details
	 * 
	 * @param httpServletResponse
	 * @param resposonseObj
	 * @param httpStatus
	 * @param responseCode
	 * @param responseMessage
	 * @param jsonValidationErrorList
	 * @throws IOException
	 */
	private static void setReponseDetails(HttpServletResponse httpServletResponse, JSONObject resposonseObj,
			final HttpStatus httpStatus, final Integer responseCode, final String responseMessage,
			final List<JSONObject> jsonValidationErrorList) throws IOException {
		final String methodName = "setReponseDetails";
		LOGGER.startMethod(methodName);

		if (null != httpServletResponse && null != resposonseObj) {
			try {
				if(PC_REQ.equals(YES)) {
					Date date = Calendar.getInstance().getTime();  
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");  
					String outTime = dateFormat.format(date);
					resposonseObj.put(REQUEST_INTIME_ID, MDC.get(REQUEST_INTIME));
					resposonseObj.put(REQUEST_OUTTIME_ID, outTime);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS", Locale.ENGLISH);
				    Date firstDate;
					try {
						firstDate = sdf.parse(MDC.get(REQUEST_INTIME).toString());
						Date secondDate = sdf.parse(outTime);
						long requestTime = Math.abs(secondDate.getTime() - firstDate.getTime());
						resposonseObj.put(PROCESS_TIME, requestTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
					}
					resposonseObj.put(REQUEST_ID, MDC.get(REQUEST_IP));
				}
				resposonseObj.put(REQUEST_ID, MDC.get(REQUEST_PATTERN));
				
				resposonseObj.put(RESPONSE_CODE, responseCode);

				if (null != responseMessage) {
					resposonseObj.put(RESPONSE_MESSAGE, responseMessage);
				}

				if (null != jsonValidationErrorList) {
					resposonseObj.put(VALIDATION_ERRORS, (Object) jsonValidationErrorList);
				}

				LOGGER.info(methodName,
						" REQUEST_ID :: " + resposonseObj.get(REQUEST_ID) + " RESPONSE_CODE :: "
								+ resposonseObj.get(RESPONSE_CODE) + " RESPONSE_MESSAGE :: "
								+ resposonseObj.get(RESPONSE_MESSAGE));
			} catch (JSONException jsonException) {
				LOGGER.error(methodName, "Exception: " + jsonException.getMessage());
			}

			httpServletResponse.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			httpServletResponse.setStatus(httpStatus.value());
			httpServletResponse.getWriter().write(resposonseObj.toString());
			httpServletResponse.getWriter().flush();
			httpServletResponse.getWriter().close();
		}
		LOGGER.endMethod(methodName);
	}

	/**
	 * Extracts JSON validation error messages
	 * 
	 * @param report
	 * @return List<JSONObject> - jsonValidationErrorList
	 */
	private static List<JSONObject> extractJSONValidationError(final ProcessingReport report) {
		final String methodName = "extractJSONValidationError";
		LOGGER.startMethod(methodName);

		List<JSONObject> jsonValidationErrorList = null;

		if (null != report && null != report.iterator()) {
			jsonValidationErrorList = new ArrayList<JSONObject>();
			final Iterator<ProcessingMessage> iterator = report.iterator();

			// Iterate through the report to form error messages array
			while (iterator.hasNext()) {
				final ProcessingMessage message = (ProcessingMessage) iterator.next();

				if (null != message && message.asJson() instanceof ObjectNode) {
					final ObjectNode objectNode = (ObjectNode) message.asJson();
					// Get the keySet of the error messages
					final Iterator<String> keySetIterator = objectNode.fieldNames();

					if (null != objectNode && null != keySetIterator) {
						final JSONObject jsonObject = new JSONObject();

						// Iterate over the error messages keySet
						while (keySetIterator.hasNext()) {
							final String key = keySetIterator.next();

							if (isNotBlank(key) && !JSON_VALIDATION_TAGS_NOT_ALLOWED.contains(key)) {
								try {
									// Get the error value for error message key
									String value = objectNode.get(key).toString();
									// Remove unwanted quote/text from value
									value = removeStart(value, "\"");
									value = removeEnd(value, "\"");
									value = replace(value, "\\\"", "\"");
									if (TAG_INSTANCE.equalsIgnoreCase(key)) {
										value = removeStart(value, "{\"pointer\":\"");
										value = removeEnd(value, "\"}");
									}

									jsonObject.put(key, value);
								} catch (JSONException jsonException) {
									LOGGER.error(methodName, "Exception: " + jsonException.getMessage());
								}
							}
						}
						if (null != jsonObject) {
							LOGGER.debug(methodName, "JSON Schema validation error: " + jsonObject.toString());
							jsonValidationErrorList.add(jsonObject);
						}
					}
				}
			}
		}
		LOGGER.endMethod(methodName);
		return jsonValidationErrorList;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
