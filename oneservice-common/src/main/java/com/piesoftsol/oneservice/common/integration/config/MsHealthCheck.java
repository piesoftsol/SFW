package com.piesoftsol.oneservice.common.integration.config;

import javax.servlet.annotation.WebListener;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Configuration;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;

@Configuration
@WebListener
public class MsHealthCheck implements HealthIndicator {
	
	private static final AppLogger LOGGER = new AppLogger(MsHealthCheck.class.getName());
	
	@Override
	public Health health() {
		int errorCode = check(); // perform some specific health check
		if (errorCode != 0) {
			return Health.down().withDetail("Heap Memory Full", errorCode).build();
		}

		return Health.up().build();
	}

	private int check() {
		// Your logic to check health
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.
		// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		// Get amount of free memory within the heap in bytes. This size will increase
		// after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		
		LOGGER.debug("Heap Memory For  TotalheapSize :: " + heapSize + " MaxheapMaxSize :: " + heapMaxSize + " FreeheapFreeSize :: " + heapFreeSize);
		
		
		if( heapFreeSize <= 0 || ( ( heapSize-heapFreeSize ) > heapMaxSize ))
		{
			return -600;
		}
		return 0;
	}

}
