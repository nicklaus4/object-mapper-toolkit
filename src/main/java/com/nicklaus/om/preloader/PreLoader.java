package com.nicklaus.om.preloader;

import java.util.Collection;

import ma.glasnost.orika.MappingContext;

/**
 * data pre loader
 * such as user or photo data
 * which current request needs
 *
 * @author nicklaus
 */
public interface PreLoader<S, ID> {

    String name();

    /**
     * this method is used for who dependence on other views
     * such as music dependence on user view then music preloader
     * call user preloader by user ids
     * @param ids dependence ids
     */
    void preloadByIds(Collection<ID> ids, MappingContext context);

    /**
     * this method is used for own mapping to load what ever u need
     */
    void preload(Collection<S> sources, MappingContext context);

}
