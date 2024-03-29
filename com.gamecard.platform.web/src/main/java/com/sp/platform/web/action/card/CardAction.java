package com.sp.platform.web.action.card;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.util.LogEnum;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.JsonVo;
import com.sp.platform.vo.PhoneVo;
import com.sp.platform.web.action.ChargeBaseAction;
import com.sp.platform.web.cache.CheckUserCache;
import com.yangl.common.IpAddressUtil;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午9:20
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/card")
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
public class CardAction extends ChargeBaseAction {

    @Action("main")
    public String main() {
        list = cardService.getAll();
        return "main";
    }

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

    @Action("sendPcCode")
    public void sendPcCode() {
        JsonVo result = null;
        result = new JsonVo(false, "无法使用该业务");
        Struts2Utils.renderJson(result);
        LogEnum.DEFAULT.info(phoneNumber + " or " + IpAddressUtil.getRealIp() + " 屏蔽------");
        return;
    }

    @Action("sendPcCode2")
    public void sendPcCode2() {
        super.sendPcCode2();
    }

    @Action("getPcCard")
    public String getPcCard() {
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

            LogEnum.DEFAULT.info("getPcCard, phoneNumber:" + phoneNumber + " , paytypeId:" + paytypeId + " , limitflg:" + limitflg);

            if (limitflg) {
                pcCardLog = pcCardLogService.getPcCard(id, priceId, phoneNumber, identifyingCode, sid, paytypeId, type);
                if (pcCardLog == null) {
                    message = "购买不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
                }
            } else {
                message = "购买不成功，请确认您的手机是否有足额话费，并认真填写验证码，如有疑问，请联系客服";
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
