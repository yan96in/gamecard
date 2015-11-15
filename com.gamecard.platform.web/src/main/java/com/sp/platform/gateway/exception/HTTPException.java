package com.sp.platform.gateway.exception;

import com.sp.platform.gateway.constant.Status;

import java.util.Arrays;

/**
 * @author yang lei
 */
public class HTTPException extends AbstractException{

    private static final long serialVersionUID = 1L;

    public HTTPException() {
        
    }

    public HTTPException(Integer status,String message,Throwable throwable){
        super(status,message, Arrays.asList(message), throwable);
    }

    public HTTPException(Status status) {
        super(status.getHttpStatusCode(),status.getReasonPhrase(),Arrays.asList(status.getReasonPhrase()),new HTTPException());
    }
    
    
}
