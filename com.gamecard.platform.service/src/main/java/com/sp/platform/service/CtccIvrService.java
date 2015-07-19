package com.sp.platform.service;

import com.sp.platform.cache.BlackCache;
import com.sp.platform.util.PropertyUtils;
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
            String card = StringUtils.left(body, 1);
            String password = StringUtils.right(body, 1);
            return "1&888" + card + "&999" + password;
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
