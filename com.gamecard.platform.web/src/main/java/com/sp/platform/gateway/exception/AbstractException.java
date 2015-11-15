package com.sp.platform.gateway.exception;

import java.util.List;
import java.util.Map;

/**
 * @author yang lei
 */
public class AbstractException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Integer status;
    
    private List<String> messageList;
    
    private Map<String, Object> attributes;
    
    
    public AbstractException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public AbstractException(Integer status,String message,List<String> messageList,Throwable cause) {
        super(message, cause);
        this.messageList = messageList;
        this.status = status;
    }
    
    /**
     * @param message
     * @param cause
     */
    public AbstractException(Integer status,String message,List<String> messageList,Map<String, Object> attributes,Throwable cause) {
        super(message, cause);
        this.messageList = messageList;
        this.status = status;
        this.attributes = attributes;
    }

    /**
     * @param message
     */
    public AbstractException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AbstractException(Throwable cause) {
        super(cause);
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
