package com.piesoftsol.oneservice.common.integration.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.piesoftsol.oneservice.common.integration.annotations.IgnoreSecurity;

import static com.piesoftsol.oneservice.common.integration.config.OneServiceInit.oneServiceBootClass;

public class EnableSecurityCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		
		if(oneServiceBootClass == null) {
			return false;
		}

		if (!oneServiceBootClass.isAnnotationPresent(IgnoreSecurity.class)) {
			return true;
		}
		
		return false;
	}

}
