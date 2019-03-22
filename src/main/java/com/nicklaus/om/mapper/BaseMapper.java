package com.nicklaus.om.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * common mapper
 *
 * @author nicklaus
 */
public abstract class BaseMapper<A, B> extends CustomMapper<A, B> {

    protected MapperFacade mapperFacade() {
        return mapperFacade;
    }

    @SuppressWarnings("unchecked")
    protected <K, V> Map<K, V> getProperty(Object key, MappingContext context) {
        Map<K, V> map = (Map<K, V>) context.getProperty(key);

        return null == map ? Collections.emptyMap() : map;
    }

    @SuppressWarnings("unchecked")
    protected <E> Set<E> getSetProp(Object key, MappingContext context) {
        Set<E> set = (Set<E>) context.getProperty(key);
        return null == set ? Collections.emptySet() : set;
    }

    @SuppressWarnings("unchecked")
    protected <E> List<E> getListProp(Object key, MappingContext context) {
        List<E> list = (List<E>) context.getProperty(key);
        return null == list ? Collections.emptyList() : list;
    }

    protected <K, V> V getTarget(K id, Object key, MappingContext context) {
        Map<K, V> property = getProperty(key, context);
        if (MapUtils.isEmpty(property) || !property.containsKey(id)) {
            return null;
        }
        return property.get(id);
    }

    @Override
    public void mapBtoA(B b, A a, MappingContext context) {
        throw new UnsupportedOperationException("not allowed reverse mapping");
    }
}
