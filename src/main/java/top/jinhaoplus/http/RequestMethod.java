package top.jinhaoplus.http;

public enum RequestMethod {
    /**
     * GET
     */
    GET("get"),
    /**
     * POST
     */
    POST("post"),
    /**
     * PUT
     */
    PUT("put"),
    /**
     * DELETE
     */
    DELETE("delete");

    String method;

    RequestMethod(String method) {
        this.method = method;
    }
}
