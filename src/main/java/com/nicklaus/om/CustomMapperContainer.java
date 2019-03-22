package com.nicklaus.om;

import static org.apache.commons.collections4.CollectionUtils.size;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;

/**
 * custom mappers management
 *
 * @author nicklaus
 */
public class CustomMapperContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMapperContainer.class);

    @Autowired
    private MapperFactory mapperFactory;

    @Autowired(required = false)
    private List<Mapper> mappers;

    @Autowired(required = false)
    private List<Converter> converters;

    @PostConstruct
    public void init() {
        LOGGER.info("init mappers {} converters {}", size(mappers), size(converters));
        addSpringMappers();
        addSpringConverters();
    }

    private void addSpringMappers() {
        if (!CollectionUtils.isEmpty(mappers)) {
            mappers.forEach(this::addMapper);
        }
    }

    public void addMapper(Mapper<?, ?> mapper) {
        this.mapperFactory.registerMapper(mapper);
    }

    private void addSpringConverters() {
        if (!CollectionUtils.isEmpty(converters)) {
            converters.forEach(this::addConverter);
        }
    }

    public void addConverter(Converter<?, ?> converter) {
        this.mapperFactory.getConverterFactory().registerConverter(converter);
    }
}

