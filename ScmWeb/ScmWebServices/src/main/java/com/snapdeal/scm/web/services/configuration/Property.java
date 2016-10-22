package com.snapdeal.scm.web.services.configuration;

/**
 * @author prateek
 */
public enum Property {

    AUTO_CACHE_RELOAD_CRON("auto.cache.reload.cron", "0 * * * * ?"),
    WEB_SERVER_STATIC_URL("web.server.url", "http://10.41.92.108/SCMTower"),
    VERSION("version", "1000000"),
    MAX_DATE_RANGE_ALLOWED("max.date.range", "30"),
    MAX_PAGE_RANGE_ALLOWED("max.page.range", "10");

    private String name;
    private String value;

    private Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
