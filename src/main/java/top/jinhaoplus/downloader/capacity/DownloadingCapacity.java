package top.jinhaoplus.downloader.capacity;

public interface DownloadingCapacity {

    void init(int maxDownloadingCapacity);

    void consume();

    void free();

    boolean hasFreeCapacity();

    boolean allCapacityFree();
}
