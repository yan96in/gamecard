package com.sp.platform.service.pay;

import com.sp.platform.entity.PcCardLog;

/**
 * Created by yanglei on 15/9/29.
 */
public interface PayService {
    public boolean checkAccount(String account);
    public String pay(PcCardLog pcCardLog);
}
