package org.bromine.utils.net;

import lombok.Getter;

@Getter
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

}