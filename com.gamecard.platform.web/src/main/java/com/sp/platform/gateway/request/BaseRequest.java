package com.sp.platform.gateway.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@XmlRootElement
public class BaseRequest{
    private String clientId;
    private String mallId;

}
