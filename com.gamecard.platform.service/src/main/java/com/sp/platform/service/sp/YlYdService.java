package com.sp.platform.service.sp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
public class YlYdService {
    @Autowired
    private PropertyUtils propertyUtils;


    public LtPcResult sendYdCode(String phone, int fee, String province) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://182.92.166.247:8013/pc_skhx/pc.php?";
            String price = StringUtils.leftPad(fee/100 + "", 2, "0");
            url = url + "method=request&tel=" + phone + "&cid=1085&code=" + price;
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpGet.addHeader("accept", "application/json;charset=UTF-8");
            LogEnum.DEFAULT.info(url);

            //处理请求，得到响应
            HttpResponse httpResponse = client.execute(httpGet);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(phone + " 调用翼龙 " + httpResponse.getStatusLine().getStatusCode() + " : " + body);

            JSONObject result = JSON.parseObject(body);
            if (StringUtils.equals("200000", result.getString("resultCode"))) {
                chanels.setPcflag(true);
                chanels.setSid(result.getString("orderid"));
                pcResult.setChanels(chanels);
                return pcResult;
            } else {
                LogEnum.DEFAULT.info(phone + " 调用翼龙移动PC接口失败，失败描述：" + Constants.resultCode.get(result.getString("resultCode")));
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用翼龙移动PC接口异常：" + e.toString());
        }

        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    public String commitPaymentCode(String phone, String code, String sid, int channeType, PcCardLog pcCardLog) throws IOException {

        HttpClient client = new DefaultHttpClient();
        String url = "http://182.92.166.247:8013/pc_skhx/pc.php?";
        url = url + "method=confirm&orderid="+sid+"&verifycode="+code;
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("accept", "application/json;charset=UTF-8");
        LogEnum.DEFAULT.info(phone + "  翼龙提交验证码 " + url);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httpGet);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + "  翼龙提交验证码 " + url + "， 返回" + httpResponse.getStatusLine().getStatusCode() + " : " + body);
        JSONObject object = JSON.parseObject(body);
        return object.getString("resultCode");
    }

}
