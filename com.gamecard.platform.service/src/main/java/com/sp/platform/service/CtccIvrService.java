package com.sp.platform.service;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.XDEncodeHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (called.startsWith("16836556")) {
            int cardId = propertyUtils.getInteger("card.convert.cardId." + body);
            int priceId = propertyUtils.getInteger("card.convert.priceId." + body);
            if(cardId == 0 || priceId == 0){
                LogEnum.DEFAULT.warn(caller + "  IVR " + "取卡失败 cardId=" + cardId + " priceId=" + priceId);
                return "0&000&000";
            }
            CardPassword card = cardPasswordService.getUserCard(cardId, priceId);
            if (card == null) {
                LogEnum.DEFAULT.warn(caller + "  IVR " + "取卡失败 cardId=" + cardId + " priceId=" + priceId);
                return "0&000&000";
            }
            XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));
            String cardNo = xdEncodeHelper.XDDecode(card.getCardno(), true);
            String password = xdEncodeHelper.XDDecode(card.getPassword(), true);
            return "1&" + cardNo + "&" + password;
        }
        return "1&123&123";
    }

    public String saveKeyLog(String caller, String called, String body) {
        return "0";
    }

    public String saveBillLog(String caller, String called, String body) {
        return "0";
    }
}
