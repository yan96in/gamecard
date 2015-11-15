package com.sp.platform.gateway.exception;

import com.sp.platform.gateway.constant.Status;

import java.util.Arrays;
import java.util.List;


/**
 * @author yang lei
 */
public class ValidationException extends AbstractException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1500600817575354689L;

	public ValidationException(){
		super();
	}

    public ValidationException(Status status){
        super(status.getHttpStatusCode(),status.getReasonPhrase(), Arrays.asList(status.getReasonPhrase()),new ValidationException());
    }

    public ValidationException(Status status, List<String> messageList, Throwable throwable) {
        super(status.getHttpStatusCode(),status.getReasonPhrase(), messageList, throwable);
    }
}
