package top.jinhaoplus.http;

public class ErrorResponse extends Response {

    private static final int ERROR_STATUS_CODE = 500;

    private String error;

    public ErrorResponse(Request request) {
        super(request);
        super.statusCode(ERROR_STATUS_CODE);
    }

    public static ErrorResponse wrap(Response response) {
        return (ErrorResponse) new ErrorResponse(response.request()).statusCode(ERROR_STATUS_CODE);
    }

    public String error() {
        return error;
    }

    public ErrorResponse error(String error) {
        this.error = error;
        return this;
    }
}
