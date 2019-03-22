package com.nicklaus.om.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.nicklaus.om.CustomMapperContainer;
import com.nicklaus.om.ViewMapperFactory;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContextFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.NonCyclicMappingContext;

/**
 * orika spring factory
 *
 * @author nicklaus
 */
@Configuration
@Conditional(ObjectMapperCondition.class)
public class ObjectMapperConfiguration {

    private static final MappingContextFactory CONTEXT_FACTORY = new NonCyclicMappingContext.Factory(Maps.newHashMap());

    @Bean
    public MappingContextFactory contextFactory() {
        return CONTEXT_FACTORY;
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new DefaultMapperFactory.Builder()
                .mappingContextFactory(CONTEXT_FACTORY)
                .build();
    }

    @Bean
    public CustomMapperContainer customMapperContainer() {
        return new CustomMapperContainer();
    }

    @Bean
    public ViewMapperFactory viewMapperFactory() {
        return new ViewMapperFactory();
    }
}
