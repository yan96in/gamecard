package com.sp.platform.service;

import com.sp.platform.entity.Card;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:23
 * To change this template use File | Settings | File Templates.
 */
public interface CardService extends AbstractService<Card>  {
    public Card getDetail(int id);
}
