package com.sp.platform.web.action.pay;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.service.pay.PayService;
import com.sp.platform.service.pay.PayServiceFactory;
import com.sp.platform.util.LogEnum;
import com.sp.platform.vo.JsonVo;
import com.sp.platform.web.action.ChargeBaseAction;
import com.yangl.common.IpAddressUtil;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created by yanglei on 15/9/26.
 */
@Namespace("/pay")
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
public class PayAction extends ChargeBaseAction {
    private int chargeResult;

    public int getChargeResult() {
        return chargeResult;
    }

    public void setChargeResult(int chargeResult) {
        this.chargeResult = chargeResult;
    }
    @Autowired
    private PayServiceFactory payServiceFactory;

    @Action("index")
    public String index() {
        return super.index();
    }

    @Action("select")
    public String select() {
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

    @Action("checkAccount")
    public void checkAccount() {
        JsonVo result;
        if (StringUtils.isBlank(account)) {
            result = new JsonVo(false, "请确认帐号信息无误！");
            Struts2Utils.renderJson(result);
        }

        PayService payService = payServiceFactory.getPayService(id);
        if (payService != null) {
            Boolean flag = payService.checkAccount(account);
            if(!flag){
                result = new JsonVo(false, "请确认帐号信息无误！");
                Struts2Utils.renderJson(result);
                return;
            }
        }
        result = new JsonVo(true, "");
        Struts2Utils.renderJson(result);
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
                chargeResult = pcCardLogService.charge(id, priceId, phoneNumber, identifyingCode, sid, paytypeId, type);
                if (chargeResult != 2) {
                    message = "充值不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
                }
            } else {
                message = "充值不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
            message = e.getMessage() + " 请联系客服";
        }
        card = cardService.get(id);
        price = priceService.get(priceId);
        paytype = paytypeService.get(paytypeId);
        return "pccard";
    }
}
