package com.piesoftsol.oneservice.common.integration.util;

public class CommonConstants {
	
	public static String SERVICE_NAME = "service.name";
	public static String[] ALLOWED_SERVICE_PATHS = new String[] {};
	public static final String COMMON_PACKAGE_STRUCTURE = "com.piesoftsol.oneservice";

	// SQL Query Constants
	public static final String SERVICE_NAME_REPLACEMENT_STRING = "!SERVICE_NAME!";
	public static String COMMON_USER_TABLE = "TB_ONESERVICE_MS_USER";
	public static String COMMON_USER_ROLE_TABLE = "TB_ONESERVICE_MS_USER_ROLES";
	public static String COMMON_SERVICE_TABLE = "TB_ONESERVICE_MS_SERVICE";
	public static String COMMON_SERVICE_USER = "USERNAME";
	public static String COMMON_SERVICE_PWD = "PASSWORD";
	public static String COMMON_SERVICE_ENABLED = "ENABLES";
	public static String COMMON_SERVICE_ROLE = "ROLE";
	public static String COMMON_SERVICE_SERVICE = "SERVICE";

	// Listener constants
	public static final String REQUEST_ID = "transactionid";
	public static final String REQUEST_PATTERN = "RequestId";
	public static final String REQUEST_INTIME_ID = "requestintime";
	public static final String REQUEST_INTIME = "RequestInTime";
	public static final String REQUEST_OUTTIME_ID = "requestouttime";
	public static final String PROCESS_TIME = "elapsedtime";
	public static final String PROCESS_TIME_VAL = "ProcessTime";
	public static final String REQUEST_OUTTIME = "RequestOutTime";
	public static final String REQUEST_URL = "RequestUri";
	public static final String REQUEST_IP = "RequestIp";

	// Filter constants
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	public static final String RESPONSE_CODE = "responsecode";
	public static final String RESPONSE_MESSAGE = "responsemessage";
	public static final String VALIDATION_ERRORS = "VALIDATION_ERRORS";
	public static final String TAG_INSTANCE = "instance";

	// Bean Qualifier Strings
	public static final String ONESERVICE_JDBC_TEMPLATE = "oneserviceJdbcTemplate";
	public static final String ONESERVICE_DATA_SOURCE = "oneserviceDataSource";

	// Response codes
	public static final int SUCCESS_CODE = 200;
	public static final String SUCCESS_CODE_STRING = "200";
	public static final int EXCEPTION_CODE = 203;
	public static final int ERROR_CODE = 206;
	public static final int BAD_REQUEST_CODE = 400;
	public static final int INTERNAL_EXEPTION_CODE = 500;

	// Response Strings
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String SUCCESS_RESPONSE = "Success";
	public static final String UNKNOWN_ERROR = "Unknown error";
	public static final String EXCEPTION_RESPONSE = "Please contact Admin. - EXE";
	public static final String BAD_REQUEST_RESPONSE = "Error / Validation Error";
	public static final String INTERNAL_EXCEPTION_RESPONSE = "Exception";
	
	// Security Enable Strings
	//public static String SEC_REQ = "Y";
	//public static String JSON_VAL_REQ = "Y";
	public static String PC_REQ = "N";

}
