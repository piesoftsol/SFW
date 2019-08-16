package com.piesoftsol.oneservice.common.integration.model;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_PATTERN;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_INTIME_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_OUTTIME_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PC_REQ;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_INTIME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.SUCCESS_RESPONSE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.YES;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_ID;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PROCESS_TIME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.RESPONSE_CODE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.RESPONSE_MESSAGE;
import static com.piesoftsol.oneservice.common.integration.util.SwaggerConstants.EXP_REQUEST_ID;
import static com.piesoftsol.oneservice.common.integration.util.SwaggerConstants.EXP_REQUEST_IP;
import static com.piesoftsol.oneservice.common.integration.util.SwaggerConstants.EXP_RESPONSE_CODE;
import static com.piesoftsol.oneservice.common.integration.util.SwaggerConstants.EXP_PROCESS_TIME_ID;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.MDC;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;

/**
 * Abstract class to hold service response values.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
@SuppressWarnings("deprecation")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public abstract class ServiceResponse {

	@JsonProperty(value = REQUEST_ID, required = true)
	@ApiModelProperty(position = 1, example = EXP_REQUEST_ID)
	private Object requestId = MDC.get(REQUEST_PATTERN);
	
	@JsonProperty(value = REQUEST_INTIME_ID)
	@ApiModelProperty(position = 2, example = REQUEST_INTIME_ID)
	private Object requestInTime = MDC.get(REQUEST_INTIME);
	
	@JsonProperty(value = REQUEST_OUTTIME_ID)
	@ApiModelProperty(position = 3, example = REQUEST_OUTTIME_ID)
	private Object requestOutTime;

	@JsonProperty(value = RESPONSE_CODE, required = true)
	@ApiModelProperty(position = 4, example = EXP_RESPONSE_CODE)
	private Integer responseCode;

	@JsonProperty(value = RESPONSE_MESSAGE, required = true)
	@ApiModelProperty(position = 5, example = SUCCESS_RESPONSE)
	private String responseMsg;
	
	@JsonProperty(value = PROCESS_TIME)
	@ApiModelProperty(position = 6, example = EXP_PROCESS_TIME_ID)
	private Object requestTime;
	
	@JsonProperty(value = "clientIp")
	@ApiModelProperty(position = 7, example = EXP_REQUEST_IP)
	private Object requestIp;

	@JsonProperty(value = "error")
	@ApiModelProperty(hidden = true)
	private List<ErrorDetails> error;

	public Object getRequestId() {
		return requestId;
	}

	public void setRequestId(Object requestId) {
		this.requestId = requestId;
	}

	public Object getRequestInTime() {
		return requestInTime;
	}

	public void setRequestInTime(Object requestInTime) {
		this.requestInTime = requestInTime;
	}

	public Object getRequestOutTime() {
		if(PC_REQ.equals(YES)) {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");  
			requestOutTime = dateFormat.format(date);
			return requestOutTime;
		}else {
			return null;
		}
	}

	public void setRequestOutTime(Object requestOutTime) {
		this.requestOutTime = requestOutTime;
	}	

	public Object getRequestTime() {
		if(PC_REQ.equals(YES)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS", Locale.ENGLISH);
		    Date firstDate;
			try {
				firstDate = sdf.parse(this.requestOutTime.toString());
				Date secondDate = sdf.parse(this.requestInTime.toString());
				requestTime = Math.abs(secondDate.getTime() - firstDate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			}
		}
		return requestTime;
	}

	public void setRequestTime(Object requestTime) {
		this.requestTime = requestTime;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public List<ErrorDetails> getError() {
		return error;
	}

	public void setError(List<ErrorDetails> error) {
		this.error = error;
	}

	public Object getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(Object requestIp) {
		this.requestIp = requestIp;
	}
	
}
