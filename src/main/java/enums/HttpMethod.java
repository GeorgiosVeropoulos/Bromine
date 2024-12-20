package enums;

public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
