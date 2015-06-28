package com.sp.platform.service.sp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.constants.Constants;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.Encrypt;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.LtPcResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by yanglei on 15/6/22.
 */
@Service
public class SpYdService {
    @Autowired
    private PropertyUtils propertyUtils;

    public String getSessionKey() throws IOException {
        HttpClient client = new DefaultHttpClient();
        long epoch = System.currentTimeMillis() / 1000;
        String url = "http://g.10086.cn/pay/open/index?";
        String body2 = "app=cwbk&method=getsessionkey&time=" + epoch + "&key=c4574c3ffefc72da84901614e345a7dd";
        url = url + "app=cwbk&method=getsessionkey&format=json&time=" + epoch + "&hash=" + Encrypt.md532(body2);
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("accept", "application/json;charset=UTF-8");

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httpGet);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
        JSONObject json = JSON.parseObject(body);
        if (json.get("resultCode") != null) {
            if (StringUtils.equals("2000", json.get("resultCode").toString())) {
                return json.get("sessionkey").toString();
            }
        }
        return "";
    }

    public LtPcResult sendYdCode(String phone, int fee) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        try {
            String feeCode = Constants.feeConfig.get(fee);
            if(StringUtils.isBlank(feeCode)){
                chanels.setPcflag(false);
                pcResult.setChanels(chanels);
                return pcResult;
            }
            String sessionKey = getSessionKey();
            if (StringUtils.isBlank(sessionKey)) {
                chanels.setPcflag(false);
                pcResult.setChanels(chanels);
                return pcResult;
            }

            HttpClient client = new DefaultHttpClient();
            long epoch = System.currentTimeMillis() / 1000;
            String url = "http://g.10086.cn/pay/open/index?";
            String body2 = "app=cwbk&method=applyforpurchase"
                    + "&tel=" + phone
                    + "&consumecode=" + feeCode
                    + "&time=" + epoch
                    + "&sessionkey=" + sessionKey
                    + "&key=c4574c3ffefc72da84901614e345a7dd";

            url = url + "app=cwbk&method=applyforpurchase&format=json&time=" + epoch
                    + "&tel=" + phone
                    + "&consumecode=" + feeCode
                    + "&salechannelid=41937000"
                    + "&sessionkey=" + sessionKey
                    + "&hash=" + Encrypt.md532(body2);
            LogEnum.DEFAULT.info(phone + "  移动基地申请验证码 " + url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpGet.addHeader("accept", "application/json;charset=UTF-8");

            //处理请求，得到响应
            HttpResponse httpResponse = client.execute(httpGet);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(phone + httpResponse.getStatusLine().getStatusCode() + " : " + body);

            JSONObject result = JSON.parseObject(body);
            if (StringUtils.equals("200000", result.getString("resultCode"))) {
                chanels.setPcflag(true);
                chanels.setSid(result.getString("orderid"));
                pcResult.setChanels(chanels);
                return pcResult;
            } else {
                LogEnum.DEFAULT.info(phone + " 调用移动PC接口失败，失败描述：" + Constants.resultCode.get(result.getString("resultCode")));
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用移动PC接口异常：" + e.toString());
        }

        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    public String commitPaymentCode(String phone, String code, String sid, int channeType, PcCardLog pcCardLog) throws IOException {
        String sessionKey = getSessionKey();
        if (StringUtils.isBlank(sessionKey)) {
            return "error";
        }

        HttpClient client = new DefaultHttpClient();
        long epoch = System.currentTimeMillis() / 1000;
        String url = "http://g.10086.cn/pay/open/index?";
        String body2 = "app=cwbk&method=confirmpurchase"
                + "&verifycode=" + code
                + "&orderid=" + sid
                + "&time=" + epoch
                + "&sessionkey=" + sessionKey
                + "&key=c4574c3ffefc72da84901614e345a7dd";

        url = url + "app=cwbk&method=confirmpurchase&format=json&time=" + epoch
                + "&verifycode=" + code
                + "&orderid=" + sid
                + "&time=" + epoch
                + "&sessionkey=" + sessionKey
                + "&hash=" + Encrypt.md532(body2);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("accept", "application/json;charset=UTF-8");
        LogEnum.DEFAULT.info(phone + "  移动基地提交验证码 " + url);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httpGet);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + "  移动基地提交验证码 " + url+ "， 返回" + httpResponse.getStatusLine().getStatusCode() + " : " + body);
        JSONObject object = JSON.parseObject(body);
        return object.getString("resultCode");
    }
}
