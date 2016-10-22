package com.snapdeal.scm.poller;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * DataProcessorCondition : Condition check for Data processing job 
 * 
 * @author pranav
 *
 */
@Deprecated
public class PollerCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return true;
	}
}
