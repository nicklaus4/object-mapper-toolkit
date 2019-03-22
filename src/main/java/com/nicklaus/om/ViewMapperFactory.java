package com.nicklaus.om;


import static com.nicklaus.om.utils.ViewCommonUtils.EXT_PARAM;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.nicklaus.om.preloader.PreLoader;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingContextFactory;

/**
 * view mapper factory
 *
 * @author nicklaus
 */
public class ViewMapperFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewMapperFactory.class);

    @Autowired
    private MapperFactory mapperFactory;

    @Autowired
    private MappingContextFactory contextFactory;

    @Autowired(required = false)
    private List<PreLoader> preLoaders;

    private Map<String, PreLoader> loaderMap;

    @PostConstruct
    private void init() {
        if (CollectionUtils.isEmpty(preLoaders)) {
            return;
        }

        loaderMap = Maps.newHashMap();
        for (PreLoader preLoader : preLoaders) {
            loaderMap.put(preLoader.name(), preLoader);
        }
    }

    public <S> ViewMapperFacade<S> getDirectMapperFacade() {
        return new ViewMapperFacade<>(mapperFactory.getMapperFacade(), null, contextFactory.getContext());
    }

    public <S> ViewMapperFacade<S> getDirectMapperFacade(Map<String, Object> extParams) {
        MappingContext context = contextFactory.getContext();
        context.setProperty(EXT_PARAM, null != extParams ? extParams : Maps.newConcurrentMap());
        return new ViewMapperFacade<>(mapperFactory.getMapperFacade(), null, context);
    }

    public <S> ViewMapperFacade<S> getMapperFacade(String viewBizType) {
        return getMapperFacade(viewBizType, null);
    }

    public <S> ViewMapperFacade<S> getMapperFacade(String viewBizType, Map<String, Object> extParams) {
        if (!loaderMap.containsKey(viewBizType)) {
            throw new IllegalStateException("pre loader of view type " + viewBizType
                    + " is not set please use direct mapper instead");
        }
        MappingContext context = contextFactory.getContext();
        context.setProperty(EXT_PARAM, null != extParams ? extParams : Maps.newConcurrentMap());
        return new ViewMapperFacade<>(mapperFactory.getMapperFacade(), loaderMap.get(viewBizType), context);
    }

}
