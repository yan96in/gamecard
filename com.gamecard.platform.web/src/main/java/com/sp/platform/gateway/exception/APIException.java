package com.sp.platform.gateway.exception;

import com.sp.platform.gateway.constant.Status;

import java.util.Arrays;
import java.util.List;

/**
 * @author lei.a.yang
 */
public class APIException extends AbstractException{

    private static final long serialVersionUID = 1L;

    public APIException() {
    }

    public APIException(Status status) {
        super(status.getHttpStatusCode(),status.getReasonPhrase(),Arrays.asList(status.getReasonPhrase()),new APIException());
    }

    public APIException(Status status, List<String> messageList, Throwable throwable) {
        super(status.getHttpStatusCode(),status.getReasonPhrase(), messageList, throwable);
    }
    
}
