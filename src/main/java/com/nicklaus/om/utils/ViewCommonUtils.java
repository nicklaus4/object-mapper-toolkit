package com.nicklaus.om.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.nicklaus.om.ViewMapperFacade;

import ma.glasnost.orika.MappingContext;

/**
 * view common utils
 *
 * @author weishibai
 * @date 2018/10/13 12:05 PM
 */
public class ViewCommonUtils {

    public static final String EXT_PARAM = "extParams";

    /* make sure the val is a map type or there will be a cast exception */
    public static void appendMap(MappingContext context, String key, Map<?, ?> appendMap) {
        if (MapUtils.isEmpty(appendMap) || StringUtils.isBlank(key)) {
            return;
        }

        if (null == context.getProperty(key)) {
            context.setProperty(key, appendMap);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Object, Object> previousMap = (Map<Object, Object>) context.getProperty(key);
        appendMap.forEach(previousMap::putIfAbsent);
    }

    public static <K, V> Map<K, V> nullToEmpty(Map<K, V> originMap) {
        return null == originMap ? Collections.emptyMap() : originMap;
    }

    public static <S> void appendExtParams(ViewMapperFacade<S> facade, String key, Object val) {
        if (null == facade) {
            return;
        }

        if (null == facade.getProperty(EXT_PARAM)) {
            facade.injectProperty(EXT_PARAM, Maps.newConcurrentMap());
        }
        MapUtils.safeAddToMap(facade.getProperty(EXT_PARAM), key, val);
    }

    public static <S> void appendCustomParams(ViewMapperFacade<S> facade, Object key, Object val, boolean override) {
        if (null == facade) {
            return;
        }

        if (null == facade.getProperty(key) || override) {
            facade.injectProperty(key, val);
        }
    }

    public static <S> void overrideCustomParams(ViewMapperFacade<S> facade, Object key, Object val) {
        appendCustomParams(facade, key, val, true);
    }


    public static <S, V> void mergeCustomParams(ViewMapperFacade<S> facade, Object key, V val) {
        mergeCustomParams(facade, key, val, false);
    }

    /**
     * @param override if exists key or same key different val, whether ignore or override
     * attention if type is not match, we ignore it no exception will be threw
     */
    @SuppressWarnings("unchecked")
    public static <S, V> void mergeCustomParams(ViewMapperFacade<S> facade, Object key, V val, boolean override) {
        if (null == val || null == facade) {
            return;
        }

        Object originVal = facade.getProperty(key);
        if (null == originVal) {
            overrideCustomParams(facade, key, val);
            return;
        }

        if (Map.class.isAssignableFrom(originVal.getClass())
                && Map.class.isAssignableFrom(val.getClass())) {
            Map<Object, Object> appendMap = (Map<Object, Object>) val;
            Map<Object, Object> previousMap = (Map<Object, Object>) originVal;

            if (MapUtils.isEmpty(previousMap)) {
                overrideCustomParams(facade, key, val);
                return;
            } else if (MapUtils.isEmpty(appendMap)) {
                return;
            }

            if (!override) {
                appendMap.forEach(previousMap::putIfAbsent);
            } else {
                previousMap.putAll(appendMap);
            }
            return;
        }

        if (Set.class.isAssignableFrom(originVal.getClass())
                && Set.class.isAssignableFrom(val.getClass())) {

            Set<?> originSet = (Set<?>) originVal;
            Set<?> targetSet = (Set<?>) val;

            if (CollectionUtils.isEmpty(originSet)) {
                overrideCustomParams(facade, key, val);
                return;
            }

            if (CollectionUtils.isEmpty(targetSet)) {
                return;
            }
            TypeToken<?> paramType = TypeToken.of(Iterables.getLast(originSet).getClass());
            TypeToken<?> newType = TypeToken.of(Iterables.getLast(targetSet).getClass());

            if (paramType.getRawType() == newType.getRawType()) {
                //noinspection unchecked
                originSet.addAll((Set) targetSet);
            }
            return;
        }

        if (List.class.isAssignableFrom(originVal.getClass())
                && List.class.isAssignableFrom(val.getClass())) {

            List<?> originList = (List<?>) originVal;
            List<?> targetList = (List<?>) val;

            if (CollectionUtils.isEmpty(originList)) {
                overrideCustomParams(facade, key, val);
                return;
            }

            if (CollectionUtils.isEmpty(targetList)) {
                return;
            }
            TypeToken<?> paramType = TypeToken.of(Iterables.getLast(originList).getClass());
            TypeToken<?> newType = TypeToken.of(Iterables.getLast(targetList).getClass());

            if (paramType.getRawType() == newType.getRawType()) {
                facade.injectProperty(key, Stream.concat(Stream.of(originList), Stream.of(targetList)).distinct()
                        .collect(Collectors.toList()));
            }
        }
    }

}
