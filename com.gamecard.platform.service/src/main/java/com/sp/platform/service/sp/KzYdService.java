package com.sp.platform.service.sp;

import com.alibaba.fastjson.JSON;
import com.sp.platform.constants.Constants;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.LtPcResult;
import com.sp.platform.vo.PcVo1;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by yanglei on 15/6/23.
 */
@Service
public class KzYdService {
    @Autowired
    private PropertyUtils propertyUtils;


    public LtPcResult sendYdCode(String phone, int fee, String province) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        try {
            HttpClient client = new DefaultHttpClient();
            //空中移动
            String resource = propertyUtils.getProperty("pc.pay.url") +
                    "?uid=" + phone + "&bid=" + fee + "&ext=test";
            if (StringUtils.indexOf(propertyUtils.getProperty("pc.province.qzsj"), province) >= 0) {
                resource = propertyUtils.getProperty("pc.province.qzsj.url") +
                        "?uid=" + phone + "&bid=" + fee + "&ext=test";
            } else if (StringUtils.indexOf(propertyUtils.getProperty("pc.province.gfyx"), province) >= 0) {
                resource = propertyUtils.getProperty("pc.province.gfyx.url") +
                        "?uid=" + phone + "&bid=" + fee + "&ext=test";
            }
            LogEnum.DEFAULT.info(resource);
            HttpGet get = new HttpGet(resource);
            HttpResponse httpResponse = client.execute(get);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                LogEnum.DEFAULT.info(phone + " 移动申请指令返回1:" + body);
                PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);

                if (StringUtils.equals("0", resultVo.getResultCode())) {
                    chanels.setPcflag(true);
                    chanels.setSid(resultVo.getSid());
                    pcResult.setChanels(chanels);
                    return pcResult;
                } else {
                    LogEnum.DEFAULT.info(phone + " 调用空中移动PC接口失败，失败描述：" + Constants.resultCode.get(resultVo.getResultCode()));
                }
            }
        }catch(Exception e){
            LogEnum.DEFAULT.error("调用空中移动PC接口异常：" + e.toString());
        }

        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    public String commitPaymentCode(String phone, String code, String sid, int channeType, PcCardLog pcCardLog) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String url = propertyUtils.getProperty("pc.order.url") +
                "?sid=" + sid + "&vid=" + code + "&payid=test&amount=" + pcCardLog.getFee();
        LogEnum.DEFAULT.info(phone + "  空中提交验证码 " + url);
        HttpGet get = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(get);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + "  空中提交验证码 " + url+ "， 返回" + httpResponse.getStatusLine().getStatusCode() + " : " + body);
        PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
        return resultVo.getResultCode();
    }

}
