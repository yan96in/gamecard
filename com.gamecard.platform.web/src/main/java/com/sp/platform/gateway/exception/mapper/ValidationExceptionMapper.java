package com.sp.platform.gateway.exception.mapper;

import com.sp.platform.gateway.exception.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author lei.a.yang
 */
@Provider
public class ValidationExceptionMapper extends AbstractExceptionMapper<ValidationException>{

    @Override
    public Response toResponse(ValidationException e) {
        return super.toResponse(e);
    }

}
