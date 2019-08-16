package com.piesoftsol.oneservice.common.integration.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

public class JSONSchemaValidator {
	
	private static final AppLogger LOGGER = new AppLogger(JSONSchemaValidator.class.getName());
	
	/**
	 * Validates the JSON input against the JSON schema
	 * 
	 * @param inputBytes
	 * @param schemaStream
	 * @return ProcessingReport
	 */
	
	@SuppressWarnings("deprecation")
	public static ProcessingReport validate(byte[] inputBytes, InputStream schemaStream) {
		final String methodName = "validate";
		LOGGER.startMethod(methodName);

		ProcessingReport report = null;

		try {
			// Loads the json schema node
			JsonNode schemaNode = JsonLoader.fromString(IOUtils.toString(schemaStream));
			// Loads the input json node
			JsonNode jsonNode = JsonLoader.fromString(IOUtils.toString(inputBytes));
			JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();
			report = validator.validate(schemaNode, jsonNode);
			LOGGER.debug(methodName + report.isSuccess());
		} catch (IOException ioException) {
			LOGGER.error(methodName, "Exception: " + ioException.getMessage());
		} catch (ProcessingException processingException) {
			LOGGER.error(methodName, "Exception: " + processingException.getMessage());
		}

		LOGGER.endMethod(methodName);
		return report;
	}

}
