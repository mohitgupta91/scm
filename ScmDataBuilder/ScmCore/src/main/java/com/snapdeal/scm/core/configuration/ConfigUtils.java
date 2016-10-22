package com.snapdeal.scm.core.configuration;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.ScmPropertyCache;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.mongo.doc.ScmProperty;
import com.snapdeal.scm.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
     * 1. Name of the {@link Property} present in {@link ScmProperty} document.
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
     * Get the value of map {@link Property} having String as key and Map value.
     */
    public static String getMapValue(Property p, String key) {
        String value = CacheManager.getInstance().getCache(ScmPropertyCache.class).getMapValue(p.getName(), key);
        if (StringUtils.isEmpty(value)) {
            LOG.warn("Property: {} not found. Returning EMPTY string.", p.getName());
            value = "";
        }
        return value;
    }

    public static boolean isPresentInList(Property p, String key) {
        List<String> values = CacheManager.getInstance().getCache(ScmPropertyCache.class).getList(p.getName());
        if (values == null) {
            LOG.warn("Property: {} not found. Default List: {} will be used.", p.getName(), p.getValue());
            values = Collections.unmodifiableList(StringUtils.split(p.getValue()));
        }
        if (values == null)
            return false;
        return values.contains(key);
    }

    public static Map<String, String> getMapProperty(Property p) {
        Map<String, String> value = CacheManager.getInstance().getCache(ScmPropertyCache.class).getMap(p.getName());
        if (value == null) {
            LOG.warn("Property: {} not found. Default Map: {} will be used.", p.getName(), new HashMap<String, String>());
            value = new HashMap<String, String>();
        }

        return value;
    }

    public static List<String> getListProperty(Property p) {
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

        ScmPropertyCache scmPropertyCache = CacheManager.getInstance().getCache(ScmPropertyCache.class);

        if (scmPropertyCache != null && scmPropertyCache.getLastUpdatedTime().after(date)) {
            date = scmPropertyCache.getLastUpdatedTime();
        }
        //if date is still 0L , return current time
        if (date.getTime() == 0) {
            date = DateUtils.getCurrentTime();
        }
        return date;
    }
}
