package com.sp.platform.gateway.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-12-10
 * Time: 上午7:26
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@ToString(callSuper=true)
@XmlRootElement
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class MnSmsResponse extends BaseResponse{
    private String flg;
}
