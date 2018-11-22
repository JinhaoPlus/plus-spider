package top.jinhaoplus.core;

import com.google.common.collect.Maps;

import java.util.Map;

public class Config {
    private String schedulerFactoryClass;
    private String downloaderFactoryClass;
    private String parserFactoryClass;
    private String pipelineFactoryClass;
    private String interval;
    private String timeUnit;
    private String maxRetryTimes;
    private Map<String, String> extraConfigs = Maps.newHashMapWithExpectedSize(16);

    public Config() {
        defaultConfig();
    }

    private void defaultConfig() {
        schedulerFactoryClass = "top.jinhaoplus.scheduler.DefaultSchedulerFactory";
        downloaderFactoryClass = "top.jinhaoplus.downloader.DefaultDownloaderFactory";
        parserFactoryClass = "top.jinhaoplus.parser.DefaultParserFactory";
        pipelineFactoryClass = "top.jinhaoplus.pipeline.DefaultPipelineFactory";
        interval = "100";
        timeUnit = "MILLISECONDS";
        maxRetryTimes = "1";
    }

    public String schedulerFactoryClass() {
        return schedulerFactoryClass;
    }

    public Config schedulerFactoryClass(String schedulerConfig) {
        this.schedulerFactoryClass = schedulerConfig;
        return this;
    }

    public String downloaderFactoryClass() {
        return downloaderFactoryClass;
    }

    public Config downloaderFactoryClass(String downloaderConfig) {
        this.downloaderFactoryClass = downloaderConfig;
        return this;
    }

    public String parserFactoryClass() {
        return parserFactoryClass;
    }

    public Config parserFactoryClass(String parserConfig) {
        this.parserFactoryClass = parserConfig;
        return this;
    }

    public String pipelineFactoryClass() {
        return pipelineFactoryClass;
    }

    public Config pipelineFactoryClass(String pipelineConfig) {
        this.pipelineFactoryClass = pipelineConfig;
        return this;
    }

    public String interval() {
        return interval;
    }

    public Config interval(String interval) {
        this.interval = interval;
        return this;
    }

    public String timeUnit() {
        return timeUnit;
    }

    public Config timeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public String maxRetryTimes() {
        return maxRetryTimes;
    }

    public Config maxRetryTimes(String maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
        return this;
    }

    public Map<String, String> extraConfigs() {
        return extraConfigs;
    }

    public Config extraConfigs(Map<String, String> extraConfigs) {
        this.extraConfigs = extraConfigs;
        return this;
    }

    public Config addExtraConfigs(String configName, String configValue) {
        this.extraConfigs.put(configName, configValue);
        return this;
    }
}
