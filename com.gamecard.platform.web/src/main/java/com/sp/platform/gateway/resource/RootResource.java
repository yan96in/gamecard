package com.sp.platform.gateway.resource;

import com.sp.platform.gateway.constant.Constants;
import com.sun.jersey.spi.resource.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Path;

/**
 * @author yang lei
 */
@Singleton
@Controller
public class RootResource{
	
	@Autowired
	private GatewayResource gatewayResource;
	
	/**
	 * @return deliveryMethodResource
	 */
	@Path(Constants.SMS)
	public GatewayResource getGatewayResource() {
		return gatewayResource;
	}

}
