/**
 * 
 */
package com.sp.platform.gateway.exception.mapper;

import com.sp.platform.gateway.exception.BusinessException;
import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author lei.a.yang
 */
@Singleton
@Provider
public class BusinessExceptionMapper extends AbstractExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        return super.toResponse(e);
    }
}
