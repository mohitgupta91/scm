package com.snapdeal.scm.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation should be present in all Cache classes.
 * <p>
 * {@link Cache#cacheKey()} should be defined in {@link CacheKey} corresponding to all cache classes.
 * <p>
 * {@link Cache#cacheReload()} false means, cache will get load only once (i.e. at the time of startup)
 * It will not get reLoaded by {@link IAutoCacheReloadService implementation class}
 *
 * @author prateek
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    CacheKey cacheKey();

    boolean cacheReload() default true;

    int MIN_REPEAT_TIME_IN_MINUTE() default 0;

    int MIN_REPEAT_TIME_IN_HOUR() default 0;
}

