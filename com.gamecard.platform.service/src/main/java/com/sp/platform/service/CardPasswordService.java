package com.sp.platform.service;

import com.sp.platform.entity.CardPassword;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-7
 * Time: 下午10:15
 * To change this template use File | Settings | File Templates.
 */
public interface CardPasswordService extends AbstractService<CardPassword> {
    public CardPassword getUserCard(int cardid, int priceid);
}
