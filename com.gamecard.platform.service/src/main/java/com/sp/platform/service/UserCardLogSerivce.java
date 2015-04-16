package com.sp.platform.service;


import com.sp.platform.common.PageView;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.vo.CardVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-5
 * Time: 上午7:30
 * To change this template use File | Settings | File Templates.
 */
public interface UserCardLogSerivce extends AbstractService<UserCardLog> {
    public List<UserCardLog> getCalloutData();

    public int calloutsucess(int id);

    public List<UserCardLog> getSendCardData();

    public List<CardVo> getCardBill(PageView pageView);

    public List getCardProvince(PageView pageView);

    public List getCardCount();

    public List getListByCaller(PageView pageView);

    int qxtSmsSuccess(String phone, String spNumber);

    int getTodayCardCount(String phoneNumber);

    public int getMonthCardCount(String phoneNumber);
}
