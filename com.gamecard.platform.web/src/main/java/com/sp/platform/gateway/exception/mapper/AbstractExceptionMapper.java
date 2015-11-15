package com.sp.platform.gateway.exception.mapper;

import com.sp.platform.gateway.constant.Status;
import com.sp.platform.gateway.exception.AbstractException;
import com.sp.platform.gateway.response.BaseResponse;
import lombok.Setter;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Arrays;

/**
 * @author yang lei
 */
public abstract class AbstractExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {

    /**
     * logger
     */
    private Logger log = LoggerFactory.getLogger(AbstractExceptionMapper.class);

    /**
     * response content type
     */
    @Setter
    protected String contentType = "application/json;charset=utf-8";

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(E e) {

        log(e);
        // default error code and error message
        BaseResponse error = new BaseResponse();
        error.setStatus(Status.SERVER_ERROR.getHttpStatusCode());
        error.setMessage(Arrays.asList(Status.SERVER_ERROR.getReasonPhrase()));
        if (e instanceof AbstractException) {
            AbstractException ae = (AbstractException) e;
            error.setStatus(ae.getStatus());
            error.setMessage(ae.getMessageList());
            error.setErrorData(ae.getAttributes());

        } else if (e instanceof UnrecognizedPropertyException || e instanceof WebApplicationException) {

            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            error.setMessage(Arrays.asList(Response.Status.BAD_REQUEST.getReasonPhrase()));

        }
        return Response.status(error.getStatus()).entity(error).header("Content-type", contentType).build();

    }

    protected void log(Throwable t) {

        if (t instanceof AbstractException) {
            AbstractException e = (AbstractException) t;
            log.warn(e.getStatus() + ":" + e.getMessage());
        } else if (t instanceof UnrecognizedPropertyException || t instanceof WebApplicationException) {
            log.warn(Status.BAD_REQUEST.getHttpStatusCode() + ":" + Status.BAD_REQUEST.getReasonPhrase()
                    + "[" + t.getMessage() + "0-" + t.toString() + "]");
        } else {
            log.error(t.getMessage(), t.getCause());
        }
    }


}
