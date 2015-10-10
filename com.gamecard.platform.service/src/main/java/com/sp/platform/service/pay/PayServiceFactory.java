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

    public PayService getPayService(int cardId){
        if(cardId == 51){
            return jcardPaySerivce;
        }
        return null;
    }
}
