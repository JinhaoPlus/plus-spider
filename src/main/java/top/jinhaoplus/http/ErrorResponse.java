package top.jinhaoplus.http;

public class ErrorResponse extends Response {

    public ErrorResponse(Request request) {
        super(request);
    }

    private String error;

    public String error() {
        return error;
    }

    public ErrorResponse error(String error) {
        this.error = error;
        return this;
    }
}
