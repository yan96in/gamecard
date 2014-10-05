package com.sp.platform.service;

import com.sp.platform.entity.Price;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:45
 * To change this template use File | Settings | File Templates.
 */
public interface PriceService extends AbstractService<Price>{
    public Price getDetail(int id, int cardid);
}
