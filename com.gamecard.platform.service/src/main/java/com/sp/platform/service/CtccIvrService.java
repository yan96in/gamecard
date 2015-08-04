package com.sp.platform.service;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.IvrBillLog;
import com.sp.platform.entity.IvrCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.XDEncodeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanglei on 15/7/17.
 */
@Service
@Transactional
public class CtccIvrService {
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private CardPasswordService cardPasswordService;
    @Autowired
    private IvrCardLogService ivrCardLogService;
    @Autowired
    private IvrBillLogService ivrBillLogService;

    public String checkUser(String caller, String called, String body) {
        if (BlackCache.isBlack(caller)) {
            return "1";
        }

        return "0";
    }

    public String getCardConfig(String caller, String called, String body) {
        return propertyUtils.getProperty("dx.ivr.card.config");
    }

    /**
     * @param caller
     * @param called
     * @param body
     * @return
     */
    public String getCard(String caller, String called, String body) {
        int cardId = 0;
        int priceId = 0;
        if (called.startsWith("16836580")) {
            cardId = propertyUtils.getInteger("card.convert.cardId." + body);
            priceId = propertyUtils.getInteger("card.convert.priceId." + body);
        } else if (called.startsWith("16836556")) {
            cardId = propertyUtils.getInteger("card.16836556.cardId." + body);
            priceId = propertyUtils.getInteger("card.16836556.priceId." + body);
        } else if (called.startsWith("16836570")) {
            cardId = propertyUtils.getInteger("card.16836570.cardId." + body);
            priceId = propertyUtils.getInteger("card.16836570.priceId." + body);
        } else {
            return "1&123&123";
        }

        if (cardId == 0 || priceId == 0) {
            LogEnum.DEFAULT.warn(caller + "  IVR " + "取卡失败,配置有误 cardId=" + cardId + " priceId=" + priceId + " body=" + body);
            return "0&000&000";
        }
        CardPassword card = cardPasswordService.getUserCard(cardId, priceId);
        if (card == null) {
            LogEnum.DEFAULT.warn(caller + "  IVR " + "取卡失败 cardId=" + cardId + " priceId=" + priceId + " body=" + body);
            return "0&000&000";
        }

        try {
            XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));
            String cardNo = xdEncodeHelper.XDDecode(card.getCardno(), true);
            String password = xdEncodeHelper.XDDecode(card.getPassword(), true);
            IvrCardLog ivrCardLog = new IvrCardLog();
            ivrCardLog.setCaller(caller);
            ivrCardLog.setCalled(called);
            ivrCardLog.setCardid(cardId);
            ivrCardLog.setPriceid(priceId);
            ivrCardLog.setProvince(HaoduanCache.getProvince(caller));
            ivrCardLog.setCity(HaoduanCache.getCity(caller));
            ivrCardLog.setCard(cardNo);
            ivrCardLog.setPassword(password);
            Date now = new Date();
            ivrCardLog.setCtime(now);
            ivrCardLog.setUtime(now);
            ivrCardLogService.save(ivrCardLog);
            return "1&" + cardNo + "&" + password;
        } catch (Exception e) {
            LogEnum.DEFAULT.warn(caller + "  IVR " + "取卡失败异常[" + e.toString() + "] cardId=" + cardId + " priceId=" + priceId + " body=" + body);
            return "0&000&000";
        }
    }

    public String saveKeyLog(String caller, String called, String body) {
        return "0";
    }

    public String saveBillLog(String caller, String called, String body) {
        LogEnum.DEFAULT.info("保存电信话单" + caller + " " + called + " " + body);
        try {
            String[] temp = body.split(",");
            String btime = temp[0];
            String etime = temp[1];
            IvrBillLog ivrBillLog = new IvrBillLog();
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date b = format.parse(btime);
            Date e = format.parse(etime);
            ivrBillLog.setCtime(b);
            ivrBillLog.setUtime(e);
            ivrBillLog.setCaller(caller);
            ivrBillLog.setCalled(called);
            ivrBillLog.setProvince(HaoduanCache.getProvince(caller));
            ivrBillLog.setCity(HaoduanCache.getCity(caller));

            String key = null;
            if (temp.length >= 4) {
                ivrBillLog.setUserKey(temp[3]);
                key = temp[3];
            }

            if (called.equals("16836580")) {
                int fee = (int) Math.ceil(((e.getTime() - b.getTime()) / 1000) * 1.0 / 60) * 2;
                ivrBillLog.setFee(fee);
            } else {
                if (temp.length >= 4) {
                    int price = Integer.parseInt(propertyUtils.getProperty("card." + called + ".price." + key));
                    ivrBillLog.setFee(price);
                }
            }

            ivrBillLogService.save(ivrBillLog);
        } catch (Exception e) {
            LogEnum.DEFAULT.error("保存电信话单" + caller + " " + called + " " + body + " 异常:" + e.toString());
        }
        return "0";
    }
}
