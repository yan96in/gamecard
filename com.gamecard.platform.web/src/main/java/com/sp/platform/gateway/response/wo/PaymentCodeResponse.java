package com.sp.platform.gateway.response.wo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yanglei on 15/4/24.
 */
@Getter
@Setter
@ToString(callSuper=false)
@XmlRootElement
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class PaymentCodeResponse {
    private String resultCode;
    private String resultDescription;
    private String transactionId;
    private String outTradeNo;
}
