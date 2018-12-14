package top.jinhaoplus.downloader.helper;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.RequestHeader;

import java.util.List;

public class HttpHeaderHelper {

    public static void convertHeaders(Request request, HttpRequestBase httpRequest) {
        List<RequestHeader> headers = request.headers();
        if (headers != null && headers.size() > 0) {
            for (RequestHeader header : headers) {
                httpRequest.addHeader(new BasicHeader(header.name(), header.value()));
            }
        }
    }
}
