package top.jinhaoplus.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import top.jinhaoplus.downloader.DefaultDownloaderFactory;
import top.jinhaoplus.downloader.capacity.DefaultDownloadingCapacity;
import top.jinhaoplus.downloader.filter.RetryDownloadFilter;
import top.jinhaoplus.http.Proxy;
import top.jinhaoplus.parser.DefaultParserFactory;
import top.jinhaoplus.pipeline.DefaultPipelineFactory;
import top.jinhaoplus.scheduler.DefaultSchedulerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Config {
    private String name;
    private String schedulerFactoryClass;
    private String downloaderFactoryClass;
    private List<String> downloaderFilterClasses = Lists.newArrayList();
    private String downloadingCapacityClass;
    private String parserFactoryClass;
    private String pipelineFactoryClass;
    private int maxDownloadingCapacity;
    private int interval;
    private String timeUnit;
    private int maxRetryTimes;
    private Proxy proxyConfig;
    private Map<String, Object> extraConfigs = Maps.newHashMapWithExpectedSize(16);

    private Config() {
    }

    public static Config defaultConfig() {
        Config config = new Config();
        config.name = RandomStringUtils.randomAlphabetic(10);
        config.schedulerFactoryClass = DefaultSchedulerFactory.class.getName();
        config.downloaderFactoryClass = DefaultDownloaderFactory.class.getName();
        config.downloadingCapacityClass = DefaultDownloadingCapacity.class.getName();
        config.downloaderFilterClasses = Lists.newArrayList(RetryDownloadFilter.class.getName());
        config.parserFactoryClass = DefaultParserFactory.class.getName();
        config.pipelineFactoryClass = DefaultPipelineFactory.class.getName();
        config.maxDownloadingCapacity = 10;
        config.interval = 1000;
        config.timeUnit = TimeUnit.MILLISECONDS.name();
        config.maxRetryTimes = 3;
        return config;
    }

    public String name() {
        return name;
    }

    public Config name(String name) {
        this.name = name;
        return this;
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

    public String downloadingCapacityClass() {
        return downloadingCapacityClass;
    }

    public Config downloadingCapacityClass(String downloadingCapacityClass) {
        this.downloadingCapacityClass = downloadingCapacityClass;
        return this;
    }

    public List<String> downloaderFilterClasses() {
        return downloaderFilterClasses;
    }

    public Config downloaderFilterClasses(List<String> downloaderFilterClasses) {
        this.downloaderFilterClasses = downloaderFilterClasses;
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

    public int maxDownloadingCapacity() {
        return maxDownloadingCapacity;
    }

    public Config maxDownloadingCapacity(int maxDownloadingCapacity) {
        this.maxDownloadingCapacity = maxDownloadingCapacity;
        return this;
    }

    public int interval() {
        return interval;
    }

    public Config interval(int interval) {
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

    public int maxRetryTimes() {
        return maxRetryTimes;
    }

    public Config maxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
        return this;
    }

    public Proxy proxyConfig() {
        return proxyConfig;
    }

    public Config proxyConfig(Proxy proxyConfig) {
        this.proxyConfig = proxyConfig;
        return this;
    }

    public Map<String, Object> extraConfigs() {
        return extraConfigs;
    }

    public Config extraConfigs(Map<String, Object> extraConfigs) {
        this.extraConfigs = extraConfigs;
        return this;
    }

    public Config addExtraConfigs(String configName, Object configValue) {
        this.extraConfigs.put(configName, configValue);
        return this;
    }
}
