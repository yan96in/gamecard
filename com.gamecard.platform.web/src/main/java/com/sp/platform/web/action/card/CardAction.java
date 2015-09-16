package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.BlackCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.*;
import com.sp.platform.service.*;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.JsonVo;
import com.sp.platform.vo.PhoneVo;
import com.sp.platform.web.cache.CheckUserCache;
import com.sp.platform.web.constants.LthjService;
import com.sp.platform.util.PinyinUtil;
import com.yangl.common.IpAddressUtil;
import com.yangl.common.Struts2Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
        @Result(name = "channel", location = "channel.jsp"),
        @Result(name = "pcChannel", location = "pcChannel.jsp"),
        @Result(name = "channel-lthj", location = "channel-lthj.jsp")})
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
    private UserCardLogSerivce userCardLogSerivce;
    @Autowired
    private BillTempService billTempService;

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
    private String msg;
    private int type;

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
        if (StringUtils.indexOf(propertyUtils.getProperty("ivr.paytype"), paytypeId+",") >= 0) {
            paytype = paytypeService.get(paytypeId);
            list = ivrChannelService.find(id, priceId, paytypeId);
            return "ivr";
        }

        list = paytypeService.findPayType(id, priceId, paytypeId);
        if (CollectionUtils.isNotEmpty(list)) {
            paytype = (Paytype) list.get(0);
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

        CheckUserCache.addIp(paychannel.getPaytypeId() + "_" + IpAddressUtil.getRealIp());

        card = cardService.get(paychannel.getCardId());
        price = priceService.getDetail(paychannel.getPriceId(), paychannel.getCardId());
        paytype = paytypeService.get(paychannel.getPaytypeId());
//        if (!StringUtils.isNotBlank(msg)) {
//            paychannel.setMsg(msg);
//            list = new ArrayList(1);
//            list.add(paychannel);
//        } else {
        list = paychannelService.find(paychannel.getCardId(), paychannel.getPriceId(), paychannel.getPaytypeId(), paychannel.getFeetype(), phoneVo.getProvince(), phoneNumber, msg);
//        }

        if (StringUtils.indexOf(propertyUtils.getProperty("lthj.channel.id"), String.valueOf(channelId)) >= 0) {
            commitOrderToLthj();
            return "channel-lthj";
        }
        return "channel";
    }


    @Action("pcChannel")
    public String pcChannel() {
        if (channelId == null || channelId < 1 || StringUtils.isBlank(phoneNumber)) {
            return "403";
        }

        phoneVo = new PhoneVo(phoneNumber,
                HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));
        paychannel = paychannelService.get(channelId);
        if (paychannel == null) {
            return "403";
        }

        CheckUserCache.addIp(paychannel.getPaytypeId() + "_" + IpAddressUtil.getRealIp());

        card = cardService.get(paychannel.getCardId());
        price = priceService.getDetail(paychannel.getPriceId(), paychannel.getCardId());
        paytype = paytypeService.get(paychannel.getPaytypeId());
        return "pcChannel";
    }

    //向联通华建提交订单信息（
    private void commitOrderToLthj() {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://220.181.87.55/TeleNotificationServers/servlet/CancelWebOrder?mobile=" + phoneNumber;
            HttpGet get = new HttpGet(url);
            client.execute(get);
            get.reset();

            LthjService lthjService = com.sp.platform.web.constants.Constants.getLthjService(paychannel.getMsg());
            url = "http://sms.uupay.com.cn/smsservice/order/telecom.jsp?" +
                    "mobile=" + phoneNumber + "&serviceid=" + lthjService.getServiceid();
            LogEnum.DEFAULT.info("联通华建短信提交订单: " + url);
            get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info("联通华建短信提交订单 phone: " + phoneNumber + " 结果: " + String.valueOf(body.indexOf(paychannel.getMsg()) >= 0));
        } catch (Exception e) {
            LogEnum.DEFAULT.error("联通华建短信提交订单出错 phone: " + phoneNumber + " - error: " + e);
        }
    }

    @Action("checkPhone")
    public void checkPhone() {
        String ip = IpAddressUtil.getRealIp();
        LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                append("--checkPhone-- 请求IP：").append(ip)
                .append(" ：").append(" , paytypeId: ").append(paytypeId).toString());

        JsonVo result = null;
        if (BlackCache.isBlack(ip) || BlackCache.isBlack(phoneNumber)) {
            result = new JsonVo(false, "无法使用该业务");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + " or " + ip + " 屏蔽.");
            return;
        }

        int uc = cacheCheckUser.getCallerDayCount(phoneNumber);
        if (uc >= propertyUtils.getInteger("pc.caller.day.count", 20)) {
            result = new JsonVo(false, "号码超过使用限制");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + " 号码超过日使用次数限制 : " + uc);
            return;
        }

        if (paytypeId.equals(22) || paytypeId.equals(23)) {
            // 检查普通短信IP
            int ipCount = CheckUserCache.checkIp(paytypeId + "_" + IpAddressUtil.getRealIp());
            int maxCount = 0;
            if (paytypeId.equals(23)) {
                maxCount = 5;
            } else if (paytypeId.equals(22)) {
                maxCount = 8;
            }
            if (ipCount >= maxCount) {
                result = new JsonVo(false, "使用超过限制");
                Struts2Utils.renderJson(result);
                LogEnum.DEFAULT.info(phoneNumber + " IP 超过限制 : " + IpAddressUtil.getRealIp());
                return;
            }

            // 单用户获卡上限， 日1， 月3
            int dayCardCount = userCardLogSerivce.getTodayCardCount(phoneNumber);
            maxCount = 0;
            if (paytypeId.equals(23)) {
                maxCount = 1;
            } else if (paytypeId.equals(22)) {
                maxCount = 2;
            }
            if (dayCardCount >= maxCount) {
                result = new JsonVo(false, "使用超过限制");
                Struts2Utils.renderJson(result);
                LogEnum.DEFAULT.info(phoneNumber + " 日取卡数超过限制 : " + dayCardCount);
                return;
            }

            int monthCardCount = userCardLogSerivce.getMonthCardCount(phoneNumber);
            maxCount = 0;
            if (paytypeId.equals(23)) {
                maxCount = 3;
            } else if (paytypeId.equals(22)) {
                maxCount = 6;
            }
            if (monthCardCount >= maxCount) {
                result = new JsonVo(false, "使用超过限制");
                Struts2Utils.renderJson(result);
                LogEnum.DEFAULT.info(phoneNumber + " 月取卡数超过限制 : " + monthCardCount);
                return;
            }

            //省份上限
            int provinceFee = billTempService.getProvinceFee(phoneNumber);
            maxCount = 0;
            if (paytypeId.equals(23)) {
                maxCount = 1000;
            } else if (paytypeId.equals(22)) {
                maxCount = 3000;
            }
            if (provinceFee >= maxCount) {
                result = new JsonVo(false, "超过限制");
                Struts2Utils.renderJson(result);
                LogEnum.DEFAULT.info(phoneNumber + " : " + HaoduanCache.getProvince(phoneNumber) + " 省份收超过限制 : " + monthCardCount);
                return;
            }
        }

        if (StringUtils.isBlank(phoneNumber)) {
            result = new JsonVo(false, "请输入正确的手机号码");
        } else {
            if ((paytypeId.equals(16) || paytypeId.equals(23)) && !checkChinamobile()) {
                result = new JsonVo(false, "请输入正确的移动手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(18) && !checkChinaunicom()) {
                result = new JsonVo(false, "请输入正确的联通手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(21) && !checkChintelecom()) {
                result = new JsonVo(false, "请输入正确的手机号码");
                Struts2Utils.renderJson(result);
                return;
            } else if (paytypeId.equals(22) && !checkChintelecom()) {
                result = new JsonVo(false, "请输入正确的手机号码");
                Struts2Utils.renderJson(result);
                return;
            }
            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));

            channelVo = paychannelService.findChannels(id, priceId, paytypeId, phone.getProvince(), phoneNumber);
            channelVo.setPhoneVo(phone);
            result = new JsonVo(true, channelVo, "");
        }
        Struts2Utils.renderJson(result);
    }

    @Action("checkPcPhone")
    public void checkPcPhone() {
        String ip = IpAddressUtil.getRealIp();
        LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                append("--checkPcPhone-- 请求IP：").append(ip).toString());
        JsonVo result = null;

        if (BlackCache.isBlack(ip) || BlackCache.isBlack(phoneNumber)) {
            result = new JsonVo(false, "无法使用该业务");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + " or " + ip + " 屏蔽.");
            return;
        }

        int uc = cacheCheckUser.getCallerDayCount(phoneNumber);
        if (uc >= propertyUtils.getInteger("pc.caller.day.count", 20)) {
            result = new JsonVo(false, "号码超过使用限制");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + " 号码超过日使用次数限制 : " + uc);
            return;
        }

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
            } else if (paytypeId.equals(21) && !checkChintelecom()) {
                result = new JsonVo(false, "请输入正确的手机号码");
                Struts2Utils.renderJson(result);
                return;
            }

            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));

            channelVo = paychannelService.findPcChannels(id, priceId, paytypeId, phone.getProvince(), phoneNumber);
            channelVo.setPhoneVo(phone);
            if (channelVo.isPcflag()) {
                channelVo.setPcflag(checkLimit(phoneNumber, phone.getProvince(), paytypeId));
            }
            result = new JsonVo(true, channelVo, "通道暂停");
        }
        Struts2Utils.renderJson(result);
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
        JsonVo result = null;

        String ip = IpAddressUtil.getRealIp();

        cacheCheckUser.addIpDayCount(ip);

        if (BlackCache.isBlack(ip) || BlackCache.isBlack(phoneNumber)) {
            result = new JsonVo(false, "无法使用该业务");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + " or " + ip + " 屏蔽.");
            return;
        }

//        int uc = CheckUserCache.getUserCount(phoneNumber);
        int uc = cacheCheckUser.getCallerDayCount(phoneNumber);
        if (uc >= propertyUtils.getInteger("pc.caller.day.count", 20)) {
            result = new JsonVo(false, "无法使用该业务");
            Struts2Utils.renderJson(result);
            LogEnum.DEFAULT.info(phoneNumber + "  " + ip + " 超过日使用次数限制 : " + uc);
            return;
        }
        if (!pcCardLogService.isValidUser(phoneNumber)) {
            result = new JsonVo(false, "请您过60分钟以后再尝试购买");
            Struts2Utils.renderJson(result);
            return;
        }

//        CheckUserCache.addUserCount(phoneNumber);
        cacheCheckUser.addCallerDayCount(phoneNumber);
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
            } else if (paytypeId.equals(21) && !checkChintelecom()) {
                result = new JsonVo(false, "请输入正确的手机号码");
                Struts2Utils.renderJson(result);
                return;
            }
            PhoneVo phone = new PhoneVo(phoneNumber,
                    HaoduanCache.getProvince(phoneNumber), HaoduanCache.getCity(phoneNumber));
            boolean limitflg = checkLimit(phoneNumber, HaoduanCache.getProvince(phoneNumber), paytypeId);

            LogEnum.DEFAULT.info("sendPcCode, phoneNumber:" + phoneNumber + " , paytypeId:" + paytypeId + " , limitflg:" + limitflg);

            if (!limitflg) {
                channelVo = new ChannelVo();
                channelVo.setPcflag(false);
                result = new JsonVo(true, channelVo, "通道关闭");
                Struts2Utils.renderJson(result);
                return;
            }

            CheckUserCache.addIp(paytypeId + "_" + IpAddressUtil.getRealIp());

            channelVo = paychannelService.sendPcCode(id, priceId, paytypeId, phone.getProvince(), phoneNumber);
            channelVo.setPhoneVo(phone);
            result = new JsonVo(true, channelVo, "");
        }
        Struts2Utils.renderJson(result);
    }

    private boolean checkLimit(String phoneNumber, String province, int paytypeId) {
        LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                append("--checkLimit-- 请求IP：").append(IpAddressUtil.getRealIp())
                .append(", 省份：")
                .append(province).toString());
        boolean flag = callerLimit(phoneNumber, paytypeId);
        if (!flag) {
            return false;
        }
        return provinceLimit(province, paytypeId);
    }

    private boolean callerLimit(String phoneNumber, int paytypeId) {
        //----------------------------- 用户日上限 -----------------------
        //取日上限
        int limitFee = propertyUtils.getInteger("pc.caller.day.limit." + paytypeId, 30);

        int tempFee = cacheCheckUser.getCallerDayFee(phoneNumber + Constants.split_str + "pc0");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超用户日上限 ").append(limitFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        //----------------------------- 用户月上限 -----------------------
        //取月上限
        limitFee = propertyUtils.getInteger("pc.caller.month.limit." + paytypeId, 104);

        tempFee = cacheCheckUser.getCallerMonthFee(phoneNumber + Constants.split_str + "pc0");
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

        int calledMaxFee = propertyUtils.getInteger("pc" + paytypeId);
        if (calledMaxFee > 0) {
            int t = cacheCheckUser.getCalledDayFee("pc" + paytypeId + "0");
            if (t >= calledMaxFee) {
                LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                        append("---- 超号码日上限 ").append(calledMaxFee)
                        .append(",日费用：")
                        .append(t).toString());
                return false;
            }
        }

        //----------------------------- 省份日上限 -----------------------
        int provinceMacFee = propertyUtils.getInteger("pc.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
        int limitFee;
        if (provinceMacFee == 0) {
            limitFee = propertyUtils.getInteger("pc.province.day.limit." + paytypeId);
        } else {
            limitFee = provinceMacFee;
        }

        //取省份日上限

        int tempFee = cacheCheckUser.getCalledProvinceDayFee(province + Constants.split_str + "pc" + paytypeId + "0");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超省份日上限 ").append(limitFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        StringBuilder message = new StringBuilder(phoneNumber).
                append("---- 省份").append(province).append("收入正常，")
                .append("日费用：")
                .append(tempFee);

        //----------------------------- 省份月上限 -----------------------
        limitFee = propertyUtils.getInteger("pc.province.month.limit." + paytypeId, 50000);

        tempFee = cacheCheckUser.getCalledProvinceMonthFee(province + Constants.split_str + "pc0");

        if (limitFee > 0 && tempFee >= limitFee) {
            LogEnum.DEFAULT.info(new StringBuilder(phoneNumber).
                    append("---- 超省份月上限 ").append(limitFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            return false;
        }

        LogEnum.DEFAULT.info(message.append(", 月费用：").append(tempFee).toString());

        //----------------------------- 号码上限 -----------------------
        return true;
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

    private boolean checkChinaunicom() {
        String haoduan = StringUtils.left(phoneNumber, 3);
        List<String> chs = new ArrayList<String>();
        chs.add("130");
        chs.add("131");
        chs.add("132");
        chs.add("145");
        chs.add("155");
        chs.add("156");
        chs.add("185");
        chs.add("186");
        chs.add("170");
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
        chs.add("173");
        chs.add("177");
        chs.add("170");
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
        chs.add("184");
        chs.add("187");
        chs.add("188");
        chs.add("170");
        chs.add("178");
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
