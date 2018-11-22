package top.jinhaoplus.downloader;

public class DownloaderException extends Exception {

    public DownloaderException(Exception e) {
        super(e);
    }

    public DownloaderException(String message) {
        super(message);
    }
}
