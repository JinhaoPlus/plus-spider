package top.jinhaoplus.downloader.capacity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultDownloadingCapacity implements DownloadingCapacity {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDownloadingCapacity.class);

    private AtomicInteger downloadingCount = new AtomicInteger(0);

    private int maxDownloadingCount;

    @Override
    public void init(int maxDownloadingCapacity) {
        this.maxDownloadingCount = maxDownloadingCapacity;
    }

    @Override
    public void consume() {
        downloadingCount.incrementAndGet();
        LOGGER.debug("downloadingCount[+]=" + downloadingCount);
    }

    @Override
    public void free() {
        downloadingCount.decrementAndGet();
        LOGGER.debug("downloadingCount[-]=" + downloadingCount);
    }

    @Override
    public boolean hasFreeCapacity() {
        return downloadingCount.get() < maxDownloadingCount;
    }

    @Override
    public boolean allCapacityFree() {
        return downloadingCount.get() == 0;
    }
}
