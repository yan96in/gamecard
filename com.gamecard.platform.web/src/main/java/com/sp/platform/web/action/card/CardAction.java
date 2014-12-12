package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.cache.SnumCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.*;
import com.sp.platform.service.*;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.JsonVo;
import com.sp.platform.vo.PhoneVo;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

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
        @Result(name = "channel", location = "channel.jsp")})
public class CardAction extends ActionSupport {
    @Autowired
    private CardService cardService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private PaytypeService paytypeService;
    @Autowired
    private PaychannelService paychannelService;
    @Autowired
    private PcCardLogService pcCardLogService;
    @Autowired
    private IvrChannelService ivrChannelService;
    @Autowired
    private PropertyUtils propertyUtils;

    @Autowired
    private CacheCheckUser cacheCheckUser;

    private Integer id;
    private Card card;
    private Price price;
    private Paytype paytype;
    private Paychannel paychannel;

    private ChannelVo channelVo;
    private PhoneVo phoneVo;
    List list;

    private Integer priceId;
    private Integer paytypeId;
    private Integer channelId;
    private String phoneNumber;
    private String identifyingCode;
    private String sid;
    private PcCardLog pcCardLog;
    private String message;

    @Action("main")
    public String main() {
        list = cardService.getAll();
        return "main";
    }

    @Action("index")
    public String index() {
        if (id == null) {
            id = 1;
        }
        card = cardService.getDetail(id);
        if (card == null) {
            return "404";
        }
        list = paytypeService.getAll();
        return "index";
    }

    @Action("select")
    public String select() {
        if (id == null || priceId == null || paytypeId == null || paytypeId < 1 || priceId < 1) {
            return "403";
        }

        card = cardService.get(id);
        if (card == null) {
            return "403";
        }
        price = priceService.getDetail(priceId, id);
        if (price == null) {
            return "403";
        }
        paytype = paytypeService.get(paytypeId);

        if (StringUtils.indexOf(propertyUtils.getProperty("ivr.paytype"), paytypeId.toString()) >= 0) {
            list = ivrChannelService.find(id, priceId, paytypeId);
            return "ivr";
        } else if (StringUtils.indexOf(propertyUtils.getProperty("pc.paytype"), paytypeId.toString()) >= 0) {
            return "pc";
        }

        return "select";
    }

    @Action("channel")
    public String channel() {
        if (channelId == null || channelId < 1 || StringUtils.isBlank(phoneNumber)) {
            return "403";
        }

        phoneVo = new PhoneVo(phoneNumber,
                HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));
        paychannel = paychannelService.get(channelId);
        if (paychannel == null) {
            return "403";
        }

        card = cardService.get(paychannel.getCardId());
        price = priceService.getDetail(paychannel.getPriceId(), paychannel.getCardId());
        paytype = paytypeService.get(paychannel.getPaytypeId());
        list = paychannelService.find(paychannel.getCardId(), paychannel.getPriceId(), paychannel.getPaytypeId(), paychannel.getFeetype(), phoneVo.getProvince());

        return "channel";
    }

    @Action("checkPhone")
    public void checkPhone() {
        JsonVo result = null;
        if (StringUtils.isBlank(phoneNumber)) {
            result = new JsonVo(false, "请输入正确的手机号码");
        } else {
            if (paytypeId.equals(16) && !checkChinamobile()) {
                result = new JsonVo(false, "请输入正确的移动手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(18) && !checkChinaunicom()) {
                result = new JsonVo(false, "请输入正确的联通手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(22) && !checkChintelecom()) {
                result = new JsonVo(false, "请输入正确的手机号码");
                Struts2Utils.renderJson(result);
                return;
            }
            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));

            channelVo = paychannelService.findChannels(id, priceId, paytypeId, phone.getProvince());
            channelVo.setPhoneVo(phone);
            result = new JsonVo(true, channelVo, "");
        }
        Struts2Utils.renderJson(result);
    }

    @Action("checkPcPhone")
    public void checkPcPhone() {
        JsonVo result = null;
        if (StringUtils.isBlank(phoneNumber)) {
            result = new JsonVo(false, "请输入正确的手机号码");
        } else {
            if (paytypeId.equals(19) && !checkChinamobile()) {
                result = new JsonVo(false, "请输入正确的移动手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(20) && !checkChinaunicom()) {
                result = new JsonVo(false, "请输入正确的联通手机号码");
                Struts2Utils.renderJson(result);
                return;
            }
            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));

            channelVo = paychannelService.findPcChannels(id, priceId, paytypeId, phone.getProvince(), phoneNumber);
            channelVo.setPhoneVo(phone);
            channelVo.setPcflag(checkLimit(phoneNumber, phone.getProvince(), paytypeId));
            result = new JsonVo(true, channelVo, "");
        }
        Struts2Utils.renderJson(result);
    }

    @Action("sendPcCode")
    public void sendPcCode() {
        JsonVo result = null;
        if (StringUtils.isBlank(phoneNumber)) {
            result = new JsonVo(false, "请输入正确的手机号码");
        } else {
            if (paytypeId.equals(19) && !checkChinamobile()) {
                result = new JsonVo(false, "请输入正确的移动手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(20) && !checkChinaunicom()) {
                result = new JsonVo(false, "请输入正确的联通手机号码");
                Struts2Utils.renderJson(result);
                return;
            }
            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));

            boolean limitflg = checkLimit(phoneNumber, HaoduanCache.getProvince(phoneNumber), paytypeId);
            if (!limitflg) {
                channelVo = new ChannelVo();
                channelVo.setPcflag(false);
                result = new JsonVo(true, channelVo, "");
                Struts2Utils.renderJson(result);
                return;
            }

            channelVo = paychannelService.sendPcCode(id, priceId, paytypeId, phone.getProvince(), phoneNumber);
            channelVo.setPhoneVo(phone);
            result = new JsonVo(true, channelVo, "");
        }
        Struts2Utils.renderJson(result);
    }

    private boolean checkLimit(String phoneNumber, String province, int paytypeId) {
        boolean flag = callerLimit(phoneNumber, paytypeId);
        if (!flag) {
            return false;
        }
        return provinceLimit(province, paytypeId);
    }

    private boolean callerLimit(String phoneNumber, int paytypeId) {
        //----------------------------- 用户日上限 -----------------------
        //取日上限
        int limitFee = propertyUtils.getInteger("pc.caller.day.limit" + paytypeId, 30);

        int tempFee = cacheCheckUser.getCallerDayFee(phoneNumber + Constants.split_str + "pc");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超用户日上限 ").append(limitFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        //----------------------------- 用户月上限 -----------------------
        //取月上限
        limitFee = propertyUtils.getInteger("pc.caller.month.limit" + paytypeId, 104);

        tempFee = cacheCheckUser.getCallerMonthFee(phoneNumber + Constants.split_str + "pc");
        //如果没有设置上限， 或者费用未达到上限，继续
        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超用户月上限 ").append(limitFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            return false;
        }
        //----------------------------- 用户上限 -----------------------
        return true;
    }

    private boolean provinceLimit(String province, int paytypeId) {
        //----------------------------- 省份日上限 -----------------------
        //取省份日上限
        int limitFee = propertyUtils.getInteger("pc.province.day.limit" + paytypeId, 2000);

        int tempFee = cacheCheckUser.getCalledProvinceDayFee(province + Constants.split_str + "pc");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超省份日上限 ").append(limitFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        //----------------------------- 省份月上限 -----------------------
        limitFee = propertyUtils.getInteger("pc.province.month.limit" + paytypeId, 50000);

        tempFee = cacheCheckUser.getCalledProvinceMonthFee(province + Constants.split_str + "pc");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超省份月上限 ").append(limitFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            return false;
        }
        //----------------------------- 号码上限 -----------------------
        return true;
    }

    @Action("getPcCard")
    public String getPcCard() {
        try {
            boolean limitflg = checkLimit(phoneNumber, HaoduanCache.getProvince(phoneNumber), paytypeId);
            if (limitflg) {
                pcCardLog = pcCardLogService.getPcCard(id, priceId, phoneNumber, identifyingCode, sid, paytypeId);
                if (pcCardLog == null) {
                    message = "取卡失败， 请联系客服";
                }
            } else {
                message = "取卡失败， 请联系客服";
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

    private boolean checkChinaunicom() {
        String haoduan = StringUtils.left(phoneNumber, 3);
        List<String> chs = new ArrayList<String>();
        chs.add("130");
        chs.add("131");
        chs.add("132");
        chs.add("155");
        chs.add("156");
        chs.add("185");
        chs.add("186");
        if (chs.contains(haoduan)) {
            return true;
        }
        return false;
    }

    private boolean checkChintelecom() {
        String haoduan = StringUtils.left(phoneNumber, 3);
        List<String> chs = new ArrayList<String>();
        chs.add("133");
        chs.add("153");
        chs.add("180");
        chs.add("181");
        chs.add("189");
        if (chs.contains(haoduan)) {
            return true;
        }
        return false;
    }

    private boolean checkChinamobile() {
        String haoduan = StringUtils.left(phoneNumber, 3);
        List<String> chs = new ArrayList<String>();
        chs.add("134");
        chs.add("135");
        chs.add("136");
        chs.add("137");
        chs.add("138");
        chs.add("139");
        chs.add("147");
        chs.add("150");
        chs.add("151");
        chs.add("152");
        chs.add("157");
        chs.add("158");
        chs.add("159");
        chs.add("182");
        chs.add("183");
        chs.add("187");
        chs.add("188");
        if (chs.contains(haoduan)) {
            return true;
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public Integer getPaytypeId() {
        return paytypeId;
    }

    public void setPaytypeId(Integer paytypeId) {
        this.paytypeId = paytypeId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Paychannel getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(Paychannel paychannel) {
        this.paychannel = paychannel;
    }

    public ChannelVo getChannelVo() {
        return channelVo;
    }

    public void setChannelVo(ChannelVo channelVo) {
        this.channelVo = channelVo;
    }

    public PhoneVo getPhoneVo() {
        return phoneVo;
    }

    public void setPhoneVo(PhoneVo phoneVo) {
        this.phoneVo = phoneVo;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public PcCardLog getPcCardLog() {
        return pcCardLog;
    }

    public void setPcCardLog(PcCardLog pcCardLog) {
        this.pcCardLog = pcCardLog;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
