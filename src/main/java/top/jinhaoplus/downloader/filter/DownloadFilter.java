package top.jinhaoplus.downloader.filter;

import top.jinhaoplus.config.Config;
import top.jinhaoplus.http.EndPoint;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.Response;

public interface DownloadFilter {

    EndPoint processRequest(Request request);

    EndPoint processResponse(Response response);

    void config(Config config);
}
