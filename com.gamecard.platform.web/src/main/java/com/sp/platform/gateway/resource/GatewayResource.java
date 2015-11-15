/**
 *
 */
package com.sp.platform.gateway.resource;


import com.sp.platform.gateway.constant.Constants;
import com.sp.platform.gateway.constant.Status;
import com.sp.platform.gateway.request.MnSmsRequest;
import com.sp.platform.gateway.response.MnSmsResponse;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * @author yang lei
 */
@Controller
@Path("/")
public class GatewayResource extends BaseResource {

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path(Constants.MN_SMS)
    public Response receiveSms(MnSmsRequest request) {
        log.info("POST Method gateway receive an sms from mn" + request);
        return processMnRequest(request);
    }

    private Response processMnRequest(MnSmsRequest request) {
        /** set response and return */
        MnSmsResponse response = new MnSmsResponse();
        response.setStatus(Status.SUCCESS.getHttpStatusCode());
        response.setMessage(Arrays.asList(Status.SUCCESS.getReasonPhrase()));
        response.setFlg("true");

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(response).build();
    }
}
