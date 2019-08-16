package com.piesoftsol.oneservice.common.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error Bean class
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author Kiran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {

	@JsonProperty(value = "ERROR_CODE", required = true)
	private String errorCode;

	@JsonProperty(value = "ERROR_DESC", required = true)
	private String errorDescription;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}