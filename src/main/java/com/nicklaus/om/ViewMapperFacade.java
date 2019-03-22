package com.nicklaus.om;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.nicklaus.om.preloader.PreLoader;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * view mapper facade
 *
 * @author nicklaus
 */
public class ViewMapperFacade<S> {

    private MapperFacade internalFacade;

    private MappingContext context;

    private PreLoader preLoader;

    public ViewMapperFacade(MapperFacade internalFacade, PreLoader preLoader, MappingContext context) {
        this.internalFacade = internalFacade;
        this.context = context;
        this.preLoader = preLoader;
    }

    /**
     * context properties is a hashmap
     * not thread safe
     */
    public <E> E getProperty(Object key) {
        synchronized (this) {
            if (null == context || null == key) {
                return null;
            }

            //noinspection unchecked
            return (E) context.getProperty(key);
        }
    }

    /**
     * context properties is a hashmap
     * not thread safe
     */
    public <K, V> void injectProperty(K key, V val) {
        if (null == context) {
            return;
        }

        synchronized (this) {
            context.setProperty(key, val);
        }
    }

    public <D> List<D> multiMap(final Collection<S> sources, final Class<D> destinationClass) {
        return internalFacade.mapAsList(preload(sources), destinationClass, context);
    }

    public <D> D map(final S source, final Class<D> destinationClass) {
        return internalFacade.map(preload(source), destinationClass, context);
    }

    public <D> D directMap(final S source, final Class<D> destinationClass) {
        return internalFacade.map(source, destinationClass, context);
    }

    public <D> List<D> directMultiMap(final Collection<S> sources, final Class<D> destinationClass) {
        return internalFacade.mapAsList(sources, destinationClass, context);
    }

    /**
     * this method is used for internal mapping
     * please make sure u have preload before call this method
     */
    public <K, D> D cascadingMap(final K source, final Class<D> destinationClass) {
        return internalFacade.map(source, destinationClass, context);
    }

    /**
     * this method is used for internal mapping
     * please make sure u have preload before call this method
     */
    public <K, D> List<D> cascadingMultiMap(final Collection<K> sources, final Class<D> destinationClass) {
        return internalFacade.mapAsList(sources, destinationClass, context);
    }

    public Collection<S> preload(Collection<S> sources) {
        if (preLoader != null) {
            //noinspection unchecked
            preLoader.preload(sources, context);
        }
        return sources;
    }

    public S preload(S source) {
        if (preLoader != null) {
            preLoader.preload(Collections.singleton(source), context);
        }
        return source;
    }
}
