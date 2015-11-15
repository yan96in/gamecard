/**
 * 
 */
package com.sp.platform.gateway.exception.mapper;

import com.sp.platform.gateway.exception.HTTPException;
import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author lei.a.yang
 */
@Singleton
@Provider
public class APIExceptionMapper extends AbstractExceptionMapper<HTTPException> {

    @Override
    public Response toResponse(HTTPException e) {
        return super.toResponse(e);
    }
}
