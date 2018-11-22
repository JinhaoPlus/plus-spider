package top.jinhaoplus.http;

public enum ResponseStatus {
    /**
     * 响应成功：200
     */
    SUCCESS("success"),
    /**
     * 响应不正常：非200
     */
    UNUSUAL("unusual"),
    /**
     * 响应异常：跑出异常
     */
    EXCEPTION("exception"),
    /**
     * 超出重试次数
     */
    OUT_OF_RETRY("out_of_retry");
    String status;

    ResponseStatus(String status) {
        this.status = status;
    }
}
