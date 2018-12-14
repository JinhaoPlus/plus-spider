package top.jinhaoplus.downloader.helper;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import top.jinhaoplus.http.Request;
import top.jinhaoplus.http.RequestCookie;

import java.util.List;

public class HttpCookieHelper {

    public static BasicCookieStore convertCookies(Request request) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        List<RequestCookie> cookies = request.cookies();
        if (cookies != null && cookies.size() > 0) {
            for (RequestCookie cookie : cookies) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.name(), cookie.value());
                basicClientCookie.setDomain(cookie.domain());
                basicClientCookie.setPath(cookie.path());
                cookieStore.addCookie(basicClientCookie);
            }
        }
        return cookieStore;
    }
}
