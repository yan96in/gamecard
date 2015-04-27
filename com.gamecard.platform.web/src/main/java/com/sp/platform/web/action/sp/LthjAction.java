package com.sp.platform.web.action.sp;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.*;
import com.sp.platform.service.*;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.constants.LthjService;
import com.yangl.common.Struts2Utils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by yanglei on 15/3/31.
 */
@Namespace("/sp/lthj")
@Scope("prototype")
@Results({@Result(name = "main", location = "main.jsp"),
        @Result(name = "lthj-card", location = "card.jsp"),
        @Result(name = "lthj-card-error", location = "error.jsp")})
public class LthjAction extends ActionSupport {
    @Autowired
    private BillTempService billTempService;
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private PaychannelService paychannelService;
    @Autowired
    private UserCardLogSerivce cardLogService;
    @Autowired
    private CardService cardService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private PaytypeService paytypeService;

    private String mobile;
    private String serviceid;
    private String msgcontent;
    private String linkid;

    private String status;
    private String errorMsg;
    private UserCardLog userCardLog;
    private Card card;
    private Price price;
    private Paytype paytype;
    private Paychannel paychannel;

    @Action("mo")
    public void mo() {
        try {
            LogEnum.DEFAULT.info("联通华建短信上行: " + toString());
            LthjService lthjService = com.sp.platform.web.constants.Constants.getLthjService(msgcontent);
            if (lthjService == null) {
                throw new Exception("联通华建指令未配置: " + msgcontent);
            }
            Paychannel paychannel = paychannelService.get(lthjService.getChannelid());

            SmsBillLog billLog = new SmsBillLog(mobile, paychannel.getSpnum(), msgcontent, linkid, null);

            billLog.setProvince(HaoduanCache.getProvince(mobile));
            billLog.setCity(HaoduanCache.getCity(mobile));
            billLog.setSfid(4);
            billLog.setCpid(2);

            billLog.setFee(paychannel.getFee());
            billLog.setChannelid(lthjService.getChannelid());
            billLog.setParentid(billLog.getCpid());

            String code = randString();

            billLog.setPaymentcode(code);
            saveBill(billLog, billLog.getFee(), false);

            //下发下行短信（包含取卡验证码）
            sendMt(lthjService, code);
        } catch (Exception e) {
            LogEnum.DEFAULT.error("联通华建短信下行 error: " + e);
        }
        Struts2Utils.renderText("1");
    }

    private void sendMt(LthjService lthjService, String code) throws IOException {
        String msg = "您的验证码为:" + code + "," + lthjService.getMsg();
        String url = "http://sms.uupay.com.cn/smsservice/order/cpsendsmstelecom.jsp?" +
                "mobile=" + mobile + "&serviceid=" + lthjService.getServiceid() +
                "&msgcontent=" + msg + "&linkid=" + linkid + "&feetype=1";
        LogEnum.DEFAULT.info("联通华建短信下行: " + url);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        LogEnum.DEFAULT.info(String.valueOf(response.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(body);
    }

    @Action("mr")
    public void mr() {
        LogEnum.DEFAULT.info("联通华建短信状态报告：" + toString());
        if (StringUtils.equals("0:0", msgcontent)) {
            status = "DELIVRD";
        } else {
            status = "0";
        }

        SmsBillLog billLog = new SmsBillLog(mobile, serviceid, null, linkid, status);
        saveMr(billLog);

        Struts2Utils.renderText("0");
    }

    @Action("getCard")
    public String getUserCard() {
        LogEnum.DEFAULT.info("联通华建取卡：" + toString());
        paytype = paytypeService.get(22);
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(msgcontent)){
            errorMsg = "参数有误";
        }
        SmsBillTemp billTemp = billTempService.getByCode(mobile, msgcontent);
        if(billTemp == null){
            errorMsg = "手机号或验证码有误";
            return "lthj-card-error";
        }
        if(billTemp.getFlag() < 3){
            errorMsg = "订单状态有误，请等1分钟后重试";
            return "lthj-card-error";
        }
        if(billTemp.getFlag() > 6){
            errorMsg = "请勿重复刷新";
            return "lthj-card-error";
        }
        billTemp.setPaymentcode(msgcontent);
        paychannel = paychannelService.get(billTemp.getChannelid());
        userCardLog = cardLogService.getLthjCard(billTemp, paychannel);
        if(userCardLog == null){
            errorMsg = "取卡失败";
            return "lthj-card-error";
        }

        card = cardService.get(paychannel.getCardId());
        price = priceService.get(paychannel.getPriceId());
        paytype = paytypeService.get(paychannel.getPaytypeId());

        return "lthj-card";
    }


    private void saveMr(SmsBillLog billLog) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp == null) {
            billTempService.saveMr(billLog);
        } else {
            if (smsBillTemp.getFlag() >= 3 || smsBillTemp.getFlag() == 2) {
                return;
            } else {
                smsBillTemp.setStatus(billLog.getStatus());
                smsBillTemp.setEtime(new Date());
                smsBillTemp.setFlag(3);
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveMo(SmsBillLog billLog, SmsBillTemp smsBillTemp, boolean flag, int fee) {
        if (smsBillTemp == null) {
            // 长号码_指令
            String tempKey = serviceid + Constants.split_str + msgcontent;

            //缓存记录用户费用、长号费用
            cacheCheckUser.addCalledFee(tempKey, fee, flag);
            cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
            cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

            billTempService.saveMo(billLog);
        } else {
            if (smsBillTemp.getFlag() >= 3 || smsBillTemp.getFlag() == 1) {
                return;
            } else {
                // 长号码_指令
                String tempKey = serviceid + Constants.split_str + msgcontent;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                smsBillTemp.setMobile(billLog.getMobile());
                smsBillTemp.setType(billLog.getType());
                smsBillTemp.setSpnum(billLog.getSpnum());
                smsBillTemp.setMsg(billLog.getMsg());
                smsBillTemp.setBtime(new Date());
                smsBillTemp.setProvince(billLog.getProvince());
                smsBillTemp.setCity(billLog.getCity());
                smsBillTemp.setFee(billLog.getFee());
                smsBillTemp.setSfid(billLog.getSfid());
                smsBillTemp.setCpid(billLog.getCpid());
                smsBillTemp.setChannelid(billLog.getChannelid());
                smsBillTemp.setFlag(3);
                smsBillTemp.setSyncurl(billLog.getSyncurl());
                smsBillTemp.setParentid(billLog.getParentid());
                smsBillTemp.setPaymentcode(billLog.getPaymentcode());
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveBill(SmsBillLog billLog, int fee, boolean flag) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp != null && smsBillTemp.getFlag() >= 3) {
            return;
        }

        String temp = flag == true ? "扣量" : "普通";

        if (flag) { // 扣量
            billLog.setType(1);
        } else {
            String syncUrl = CpSyncCache.getSyncUrl(serviceid + Constants.split_str + msgcontent);
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                temp += "同步";
            }
        }
        if (StringUtils.isBlank(billLog.getStatus())) {
            saveMo(billLog, smsBillTemp, flag, fee);
        } else {
            if (smsBillTemp != null) {
                saveMr(billLog);
            } else {
                // 长号码_指令
                String tempKey = serviceid + Constants.split_str + msgcontent;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                billTempService.saveBill(billLog);
            }
        }

        if (HaoduanCache.NA.equals(billLog.getProvince())) {
            billLogService.saveNaHaoduan(mobile);
            LogEnum.DEFAULT.info("保存未知号码[{}]", mobile);
        }
        LogEnum.DEFAULT.info("保存{}话单 mobile:{}, spnum:{}, msg:{}, linkid:{}, status:{}", temp, mobile, serviceid, msgcontent, linkid, status);
    }

    /**
     * 生成3位随即字符串
     *
     * @return
     */
    private String randString() {
        StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz1234567890");
        StringBuffer sb = new StringBuffer("");
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < 6; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public UserCardLog getUserCardLog() {
        return userCardLog;
    }

    public void setUserCardLog(UserCardLog userCardLog) {
        this.userCardLog = userCardLog;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Paytype getPaytype() {
        return paytype;
    }

    public void setPaytype(Paytype paytype) {
        this.paytype = paytype;
    }

    public Paychannel getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(Paychannel paychannel) {
        this.paychannel = paychannel;
    }

    @Override
    public String toString() {
        return "LthjAction{" +
                "status='" + status + '\'' +
                ", linkid='" + linkid + '\'' +
                ", msgcontent='" + msgcontent + '\'' +
                ", serviceid='" + serviceid + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
