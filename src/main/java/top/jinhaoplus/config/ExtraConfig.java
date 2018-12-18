package top.jinhaoplus.config;

import com.google.common.collect.Maps;

import java.util.Map;

public class ExtraConfig {

    protected Map<String, Object> extraConfigs;

    public ExtraConfig() {
        this.extraConfigs = Maps.newHashMapWithExpectedSize(16);
    }

    public Object get(String extraConfigName) {
        return extraConfigs.get(extraConfigName);
    }

    public void set(String extraConfigName, Object extraConfigValue) {
        extraConfigs.putIfAbsent(extraConfigName, extraConfigValue);
    }

}
