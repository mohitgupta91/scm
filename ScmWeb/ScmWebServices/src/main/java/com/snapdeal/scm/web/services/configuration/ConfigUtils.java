package com.snapdeal.scm.web.services.configuration;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.ScmPropertyCache;
import com.snapdeal.scm.utils.DateUtils;
import com.snapdeal.scm.web.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author prateek
 */
public class ConfigUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * Get the value of scalar {@link Property} having a String value.
     * <p>
     * Order of precedence:
     * <p>
     * 1. Name of the {@link Property} present in {@link com.snapdeal.scm.mongo.doc.ScmProperty} document.
     * <p>
     * 2. Default value of {@link Property}
     * <p>
     * 3. Returns null if default value is not found. Should never happen.
     * <p>
     */
    public static String getStringScalar(Property p) {
        String value = CacheManager.getInstance().getCache(ScmPropertyCache.class).getStringValue(p.getName());

        // then use default
        if (StringUtils.isEmpty(value)) {
            LOG.debug("Property: {} not found. Default value: {} will be used.", p.getName(), p.getValue());
            value = p.getValue();
        }
        return value;
    }

    /**
     * Get the value of scalar {@link Property} having a Boolean value.
     */
    public static boolean getBooleanScalar(Property p) {
        if ("1".equals(getStringScalar(p)) || "true".equalsIgnoreCase(getStringScalar(p))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the value of scalar {@link Property} having a Integer value.
     */
    public static int getIntegerScalar(Property p) {
        String s = getStringScalar(p);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            LOG.warn("Incorrect property: {} found. Default value: {} will be used.", p.getName(), p.getValue());
            return Integer.parseInt(p.getValue());
        }
    }

    /**
     * Get the value of scalar {@link Property} having a Double value.
     */
    public static double getDoubleScalar(Property p) {
        String s = getStringScalar(p);
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            LOG.warn("Incorrect property: {} found. Default value: {} will be used.", p.getName(), p.getValue());
            return Double.parseDouble(p.getValue());
        }
    }

    /**
     * Get the value of scalar {@link Property} having a Double value.
     */
    public static Long getLongScalar(Property p) {
        String s = CacheManager.getInstance().getCache(ScmPropertyCache.class).getStringValue(p.getName());

        // then use default
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            LOG.warn("Incorrect property: {} found. Default value: {} will be used.", p.getName(), p.getValue());
            return Long.parseLong(p.getValue());
        }
    }

    /**
     * Get the value of map {@link Property} having String as key and Map value.
     */
    public static String getMapValue(Property p, String key) {
        String value = CacheManager.getInstance().getCache(ScmPropertyCache.class).getMapValue(p.getName(), key);
        if (value == null) {
            LOG.warn("Property: {} not found. Returning EMPTY string.", p.getName());
            value = "";
        }

        return value;
    }

    public static List<String> getMapProperty(Property p) {
        List<String> value = CacheManager.getInstance().getCache(ScmPropertyCache.class).getList(p.getName());
        if (value == null) {
            LOG.warn("Property: {} not found. Default List: {} will be used.", p.getName(), p.getValue());
            value = Collections.unmodifiableList(StringUtils.split(p.getValue()));
        }

        return value;
    }

    /**
     * Returns when any configuration source was last updated.
     * In case non of the configuration sources have been initialized yet, it returns current time.
     */
    public static Date getLastUpdatedTime() {

        Date date = new Date(0L);

        ScmPropertyCache scmWebPropertiesCache = CacheManager.getInstance().getCache(ScmPropertyCache.class);

        if (scmWebPropertiesCache != null && scmWebPropertiesCache.getLastUpdatedTime().after(date)) {
            date = scmWebPropertiesCache.getLastUpdatedTime();
        }
        //if date is still 0L , return current time
        if (date.getTime() == 0) {
            date = DateUtils.getCurrentTime();
        }
        return date;
    }

    /**
     * Returns a list of String for a property name. Same order of precedence applies as for getStringScalar.
     */
    public static List<String> getStringList(Property p, String delimitor) {
        List<String> list = null;

        // look for p.name in shipping_property
        if (list == null) {
            list = CacheManager.getInstance().getCache(ScmPropertyCache.class).getList(p.getName());
        }

        // use default value
        if (list == null) {
            list = Collections.unmodifiableList(StringUtils.split(p.getValue(), delimitor));
        }

        if (list != null) {
            return list;
        }
        // return empty list. should *never* happen.
        list = new ArrayList<String>(0);
        return list;
    }

    /**
     * Returns a list of String for a property name. Same order of precedence applies as for getStringScalar. Uses ","
     * as default delimiter.
     */
    public static List<String> getStringList(Property p) {
        return getStringList(p, ",");
    }
}
