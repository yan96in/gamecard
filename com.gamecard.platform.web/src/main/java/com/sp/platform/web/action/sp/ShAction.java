package com.sp.platform.web.action.sp;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.action.ChargeBaseAction;
import com.yangl.common.IpAddressUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * 搜狐
 * Created by yanglei on 15/11/23.
 */
@Namespace("/sp/sh")
@Scope("prototype")
@Results({@Result(name = "main", location = "main.jsp"),
        @Result(name = "index", location = "index.jsp"),
        @Result(name = "select", location = "select.jsp"),
        @Result(name = "ivr", location = "ivr.jsp"),
        @Result(name = "pc", location = "pc.jsp"),
        @Result(name = "pccard", location = "pccard.jsp"),
        @Result(name = "channel", location = "channel.jsp"),
        @Result(name = "pcChannel", location = "pcChannel.jsp"),
        @Result(name = "channel-lthj", location = "channel-lthj.jsp")})
public class ShAction extends ChargeBaseAction {
    private String rmb;
    private String pro_no;
    private String user;
    private String area;
    private String tel;
    private String merchant_no;
    private String orderid;
    private String productid;
    private String uip;
    private static Map<String, String> idMap = new HashMap<String, String>();
    private static Map<String, String> priceMap = new HashMap<String, String>();
    private static Map<String, String> feeMap = new HashMap<String, String>();

    static {
        idMap.put("5222", "52");
        priceMap.put("5222", "22");
        idMap.put("5224", "52");
        priceMap.put("5224", "24");
        idMap.put("5225", "52");
        priceMap.put("5225", "25");
        feeMap.put("10", "5222");
        feeMap.put("20", "5224");
        feeMap.put("30", "5225");
    }

    @Action("order")
    public String order() {
        LogEnum.DEFAULT.info(this.toString());
        if (StringUtils.isBlank(orderid) || StringUtils.isBlank(rmb)) {
            return "403";
        }
        productid = feeMap.get(rmb);
        if(StringUtils.isBlank(productid)){
            return "403";
        }

        String strId = idMap.get(productid);
        String strPriceId = priceMap.get(productid);
        if (StringUtils.isBlank(strId) || StringUtils.isBlank(strPriceId)) {
            return "403";
        }

        setAccount(orderid);
        setUserAccount(user);

        id = Integer.parseInt(strId);
        priceId = Integer.parseInt(strPriceId);
        String haoduan = StringUtils.left(tel, 3);
        if ("130,131,132,145,155,156,185,186,170,".indexOf(haoduan) >= 0) {
            paytypeId = 2;
        } else if ("133,153,180,181,189,173,177,170,".indexOf(haoduan) >= 0) {
            paytypeId = 3;
        } else {
            paytypeId = 1;
        }

        return super.select();
    }

    @Action("select")
    public String select(){
        return super.select();
    }

    @Action("channel")
    public String channel() {
        return super.channel();
    }

    @Action("pcChannel")
    public String pcChannel() {
        return super.pcChannel();
    }

    @Action("checkPhone")
    public void checkPhone() {
        super.checkPhone();
    }

    @Action("checkPcPhone")
    public void checkPcPhone() {
        super.checkPcPhone();
    }

    @Action("sendPcCode2")
    public void sendPcCode2() {
        super.sendPcCode2();
    }


    @Action("charge")
    public String charge() {
        String ip = IpAddressUtil.getRealIp();
        if (BlackCache.isBlack(ip) || BlackCache.isBlack(phoneNumber)) {
            message = "无法使用该业务";
            card = cardService.get(id);
            price = priceService.get(priceId);
            paytype = paytypeService.get(paytypeId);
            return "pccard";
        }

        try {
            boolean limitflg = checkLimit(phoneNumber, HaoduanCache.getProvince(phoneNumber), paytypeId);

            LogEnum.DEFAULT.info("charge, phoneNumber:" + phoneNumber + " , paytypeId:" + paytypeId + " , limitflg:" + limitflg);

            if (limitflg) {
                pcCardLog = pcCardLogService.charge(id, priceId, phoneNumber, identifyingCode, sid, paytypeId, type);
                if (pcCardLog.getStatus() == 0) {
                    message = "充值不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
                } else if (pcCardLog.getStatus() == 1) {
                    message = "充值不成功，请确认填写信息是否正确，如有疑问，请联系客服";
                } else if (pcCardLog.getStatus() == 3) {
                    message = "充值不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
                }
            } else {
                message = "充值不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
            message = "充值出现异常, 请联系客服";
        }
        card = cardService.get(id);
        price = priceService.get(priceId);
        paytype = paytypeService.get(paytypeId);
        return "pccard";
    }

    public String getRmb() {
        return rmb;
    }

    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    public String getPro_no() {
        return pro_no;
    }

    public void setPro_no(String pro_no) {
        this.pro_no = pro_no;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getUip() {
        return uip;
    }

    public void setUip(String uip) {
        this.uip = uip;
    }

    @Override
    public String toString() {
        return "ShAction{" +
                "rmb='" + rmb + '\'' +
                ", pro_no='" + pro_no + '\'' +
                ", user='" + user + '\'' +
                ", area='" + area + '\'' +
                ", tel='" + tel + '\'' +
                ", merchant_no='" + merchant_no + '\'' +
                ", orderid='" + orderid + '\'' +
                ", productid='" + productid + '\'' +
                ", uip='" + uip + '\'' +
                '}';
    }

    public static void main(String[] args) {
        String mobile = "135,";
        String str = "132,133,135,";
        System.out.println(str.indexOf(mobile));
    }
}
