package top.jinhaoplus.http;

public enum RequestMethod {
    /**
     * GET
     */
    GET("get", false),
    /**
     * HEAD
     */
    HEAD("head", false),
    /**
     * TRACE
     */
    TRACE("trace", false),
    /**
     * POST
     */
    POST("post", true),
    /**
     * PUT
     */
    PUT("put", true),
    /**
     * PATCH
     */
    PATCH("patch", true),
    /**
     * DELETE
     */
    DELETE("delete", false);

    public String method;
    public boolean entityEnclosing;

    RequestMethod(String method, boolean entityEnclosing) {
        this.method = method;
        this.entityEnclosing = entityEnclosing;
    }
}
