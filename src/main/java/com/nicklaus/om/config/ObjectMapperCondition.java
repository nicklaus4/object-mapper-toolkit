package com.nicklaus.om.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


/**
 *
 * @author nicklaus
 */
public class ObjectMapperCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Boolean.valueOf(context.getEnvironment().getProperty("object.mapper.disabled"));
    }
}
