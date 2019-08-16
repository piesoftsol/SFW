package com.piesoftsol.oneservice.common.integration.config;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PC_REQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import com.piesoftsol.oneservice.common.integration.annotations.EnablePerformanceCheck;

public class OneServiceInit{
	
	public static Class<?> oneServiceBootClass;
	
	public static void initializeObject(Class<?> clazz, String oneServicePid, String[] args) throws Exception {
	    
	    if (clazz.isAnnotationPresent(EnablePerformanceCheck.class)) {
	    	injectPerformance();
	    }else {
	    	ignorePerformance();
	    }    
	    oneServiceBootClass = clazz;
		SpringApplication springApplication = new SpringApplication(clazz);
		if( null != oneServicePid && !oneServicePid.isEmpty() )
			springApplication.addListeners(new ApplicationPidFileWriter(oneServicePid));
		springApplication.run(args);

	 }
	
	private static void injectPerformance() {
		
		PC_REQ = "Y";
	
	}
	
	private static void ignorePerformance() {
		
		PC_REQ = "N";
	
	}

}
