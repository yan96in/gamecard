package com.sp.platform.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yanglei on 15/9/29.
 */
@Service
public class PayServiceFactory {
    @Autowired
    private JcardPaySerivce jcardPaySerivce;
    @Autowired
    private ShPaySerivce shPaySerivce;
    @Autowired
    private Wy163PaySerivce wy163PaySerivce;

    public PayService getPayService(int cardId) {
        if (cardId == 51) {
            return jcardPaySerivce;
        } else if (cardId == 52) {
            return shPaySerivce;
        } else if (cardId == 53) {
            return wy163PaySerivce;
        }
        return null;
    }
}
