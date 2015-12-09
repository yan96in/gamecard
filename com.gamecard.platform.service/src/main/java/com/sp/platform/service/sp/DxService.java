package com.sp.platform.service.sp;

import com.sp.platform.entity.PcCardLog;
import com.sp.platform.util.*;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.LtPcResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglei on 15/6/25.
 */
@Service
public class DxService {
    @Autowired
    private PropertyUtils propertyUtils;

    public LtPcResult sendYdCode(String phone, int fee, String province) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            String orderid = IdUtils.idGenerator("te");
            String MERCHANTID = propertyUtils.getProperty("dx.merchant.id");
            formparams.add(new BasicNameValuePair("MERCHANTID", MERCHANTID));
            formparams.add(new BasicNameValuePair("ORDERSEQ", orderid));
            formparams.add(new BasicNameValuePair("ORDERREQTRANSEQ", "seq" + orderid));
            formparams.add(new BasicNameValuePair("TELEPHONE", phone));
            formparams.add(new BasicNameValuePair("FUNCTIONTYPE", "1"));
            formparams.add(new BasicNameValuePair("ORDERAMOUNT", String.valueOf(fee)));
            StringBuilder builder = new StringBuilder();
            builder.append("MERCHANTID=").append(MERCHANTID);
            builder.append("&ORDERSEQ=").append(orderid);
            builder.append("&ORDERREQTRANSEQ=").append("seq" + orderid);
            builder.append("&TELEPHONE=").append(phone);
            builder.append("&KEY=").append(propertyUtils.getProperty("dx.merchant.data.key"));
            System.out.println(builder.toString());
            formparams.add(new BasicNameValuePair("MAC", CryptTool.md5Digest(builder.toString())));
            UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

            System.out.println(IOUtils.toString(entity1.getContent()));
            //新建Http  post请求
            HttpPost httppost = new HttpPost("https://webpaywg.bestpay.com.cn/verifyCode.do");
            httppost.setEntity(entity1);
            HttpClient client = HttpUtils.getSecuredHttpClient();

            //处理请求，得到响应
            HttpResponse httpResponse = client.execute(httppost);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(phone + " 调用电信 " + httpResponse.getStatusLine().getStatusCode() + " : " + body);

            if (StringUtils.equals("00", body)) {
                chanels.setPcflag(true);
                chanels.setSid(orderid);
                pcResult.setChanels(chanels);
                pcResult.setFlag(true);
                pcResult.setSid(orderid);
                return pcResult;
            } else {
                LogEnum.DEFAULT.info(phone + " 调用电信PC接口失败，失败描述：" + body);
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用电信PC接口异常：" + e.toString());
        }

        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        pcResult.setFlag(false);
        return pcResult;
    }

    public String commitPaymentCode(String phone, String code, String sid, int channeType, PcCardLog pcCardLog) throws Exception {
        HttpClient client = HttpUtils.getSecuredHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String MERCHANTID = propertyUtils.getProperty("dx.merchant.id");
        String MERCHANTPWD = propertyUtils.getProperty("dx.merchant.key");
        String MERCHANTPHONE = "4000974884";
        String ORDERAMOUNT = String.valueOf(pcCardLog.getFee());
        String USERIP = "127.0.0.1";
        DateTime dateTime = new DateTime();
        String ORDERREQTIME = dateTime.toString("yyyyMMddHHmmss");
        String config = feeConfig.get(pcCardLog.getCardId() + "" + pcCardLog.getPriceId());
        if(StringUtils.isBlank(config)){
            LogEnum.DEFAULT.info(phone + " 资费有误， id: " + pcCardLog.getId());
            return "error";
        }
        String[] temp = config.split(",");

        formparams.add(new BasicNameValuePair("MERCHANTID", MERCHANTID));
        formparams.add(new BasicNameValuePair("MERCHANTPWD", MERCHANTPWD));
        formparams.add(new BasicNameValuePair("MERCHANTPHONE", MERCHANTPHONE));
        formparams.add(new BasicNameValuePair("ORDERSEQ", sid));
        formparams.add(new BasicNameValuePair("ORDERREQTRANSEQ", "seq" + sid));
        formparams.add(new BasicNameValuePair("ORDERAMOUNT", ORDERAMOUNT));
        formparams.add(new BasicNameValuePair("ORDERREQTIME", ORDERREQTIME));
        formparams.add(new BasicNameValuePair("USERACCOUNT", phone));
        formparams.add(new BasicNameValuePair("PHONENUM", phone));
        formparams.add(new BasicNameValuePair("VERIFYCODE", code));
        formparams.add(new BasicNameValuePair("GOODPAYTYPE", "0"));
        formparams.add(new BasicNameValuePair("GOODSCODE", temp[0]));
        formparams.add(new BasicNameValuePair("GOODSNAME", temp[1]));
        formparams.add(new BasicNameValuePair("USERIP", USERIP));
        formparams.add(new BasicNameValuePair("GOODSNUM", "1"));
        formparams.add(new BasicNameValuePair("BACKMERCHANTURL", propertyUtils.getProperty("dx.merchant.data.url")));

        StringBuilder builder = new StringBuilder();
        builder.append("MERCHANTID=").append(MERCHANTID);
        builder.append("&MERCHANTPWD=").append(MERCHANTPWD);
        builder.append("&ORDERSEQ=").append(sid);
        builder.append("&ORDERREQTRANSEQ=seq").append(sid);
        builder.append("&ORDERREQTIME=").append(ORDERREQTIME);
        builder.append("&ORDERAMOUNT=").append(ORDERAMOUNT);
        builder.append("&USERACCOUNT=").append(phone);
        builder.append("&USERIP=").append(USERIP);
        builder.append("&PHONENUM=").append(phone);
        builder.append("&GOODPAYTYPE=").append("0");
        builder.append("&GOODSCODE=").append(temp[0]);
        builder.append("&GOODSNUM=").append("1");
        builder.append("&KEY=").append(propertyUtils.getProperty("dx.merchant.data.key"));

        formparams.add(new BasicNameValuePair("MAC", CryptTool.md5Digest(builder.toString())));

        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams);

        //新建Http  post请求
        HttpPost httppost = new HttpPost("https://webpaywg.bestpay.com.cn/backBillPay.do");
        httppost.setEntity(entity1);
        LogEnum.DEFAULT.info(phone + "  电信提交验证码 " + IOUtils.toString(entity1.getContent()));

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + " 电信提交验证码， 返回" + httpResponse.getStatusLine().getStatusCode() + " : " + body);
        return body;
    }

    private static Map<String, String> feeConfig = new HashMap<String, String>();

    static {
        feeConfig.put("119", "100019,骏网3元卡");
        feeConfig.put("121", "100018,骏网5元卡");
        feeConfig.put("116", "100017,骏网9元卡");
        feeConfig.put("219", "100016,盛大3元卡");
        feeConfig.put("221", "100015,盛大5元卡");
        feeConfig.put("216", "100014,盛大9元卡");
        feeConfig.put("320", "100013,空中5元卡");
        feeConfig.put("721", "100010,天下通5元卡");
        feeConfig.put("722", "100009,天下通10元卡");
        feeConfig.put("821", "100012,天宏5元卡");
        feeConfig.put("822", "100011,天宏10元卡");
        feeConfig.put("1100", "100020,成王败寇1元点卡");
        feeConfig.put("1200", "100021,成王败寇2元点卡");
        feeConfig.put("1300", "100022,成王败寇3元点卡");
        feeConfig.put("1400", "100023,成王败寇4元点卡");
        feeConfig.put("1500", "100024,成王败寇5元点卡");
        feeConfig.put("1700", "100025,成王败寇7元点卡");
        feeConfig.put("1800", "100026,成王败寇8元点卡");
        feeConfig.put("1900", "100027,成王败寇9元点卡");
    }
}
