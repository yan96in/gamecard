package com.sp.platform.gateway.constant;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * @author yang lei
 */
public enum Status {

    /** 200 */
    SUCCESS(Response.Status.OK.getStatusCode(),"Success"),
    /** 400 */
    BAD_REQUEST(Response.Status.BAD_REQUEST.getStatusCode(),Response.Status.BAD_REQUEST.getReasonPhrase()),
    /** 401 */
    UNAUTHORIZED(Response.Status.UNAUTHORIZED.getStatusCode(),Response.Status.UNAUTHORIZED.getReasonPhrase()),
    /** 403 */
    FORBIDDEN(Response.Status.FORBIDDEN.getStatusCode(),Response.Status.FORBIDDEN.getReasonPhrase()),
    /** 404 */
    DATA_NOT_FOUND(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.getReasonPhrase()),
    /** 409 */
    CONFLICT(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.getReasonPhrase()),
    /** 500 */
    SERVER_ERROR(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()),
    /** 503 */
    SERVICE_UNAVAILABLE(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(),Response.Status.SERVICE_UNAVAILABLE.getReasonPhrase()),
    /** 400 */
    BLACK_USER(Response.Status.BAD_REQUEST.getStatusCode(),"黑名单用户:{0}，拒绝计费"),
    /** 400 */
    BLACK_APP(Response.Status.BAD_REQUEST.getStatusCode(),"appName:{0} 黑名单，拒绝计费"),
    /** 400 */
    FEE_UP_LIMIT(Response.Status.BAD_REQUEST.getStatusCode(),"日通道量已满，全部暂停"),

    WO_ERROR_1010(400, "1010"),
    WO_ERROR_621(400, "621"),
    WO_ERROR_604(400, "604"),
    ;

    /**
     * HTTP status code
     */
    @Getter
    private int httpStatusCode;
    
    @Getter
    private String reasonPhrase;

    /**
     * @param httpStatusCode
     * @param reasonPhrase
     */
    Status(int httpStatusCode,String reasonPhrase) {
        this.httpStatusCode = httpStatusCode;
        this.reasonPhrase = reasonPhrase;
    }


    public static Status fromHttpStatusCode(final int statusCode) {
        for (Status s : Status.values()) {
            if (s.httpStatusCode == statusCode) {
                return s;
            }
        }
        return null;
    }
}
