package com.sp.platform.service.impl;

import com.sp.platform.cache.CardCache;
import com.sp.platform.common.Constants;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.SmsBillTempDao;
import com.sp.platform.dao.UserCardLogDao;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.CardPasswordService;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.XDEncodeHelper;
import com.sp.platform.vo.CardVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-5
 * Time: 上午7:31
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UserCardLogServiceImpl implements UserCardLogSerivce {
    @Autowired
    private PropertyUtils propertyUtils;

    @Autowired
    private UserCardLogDao userCardLogDao;
    @Autowired
    private PaychannelService paychannelService;
    @Autowired
    private SmsBillTempDao smsBillTempDao;
    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private CardPasswordService cardPasswordService;

    @Override
    public UserCardLog get(int id) {
        return userCardLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        userCardLogDao.delete(id);
    }

    @Override
    public void save(UserCardLog object) {
        userCardLogDao.save(object);
    }

    @Override
    public List<UserCardLog> getAll() {
        return userCardLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("btime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("btime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return userCardLogDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public List<UserCardLog> getCalloutData() {

        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("flag", 4));
        dc.add(Restrictions.lt("sendnum", 3));

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(propertyUtils.getInteger("out.timeout"));

        dc.add(Restrictions.lt("btime", dateTime.toDate()));
        return userCardLogDao.findByCriteria(dc);
    }

    @Override
    public List<UserCardLog> getSendCardData() {
        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("flag", 6));
        dc.add(Restrictions.lt("sendnum", 3));
        return userCardLogDao.findByCriteria(dc);
    }

    @Override
    public List<CardVo> getCardBill(PageView pageView) {
        List<CardVo> list = userCardLogDao.getCardBill(pageView);
        if(list != null && list.size() > 0){
            for(CardVo cardVo : list){
                cardVo.setCardName(CardCache.getCard(cardVo.getCardId()).getName());
                cardVo.setPriceName(CardCache.getPrice(cardVo.getPriceId()).getDescription());
            }
        }
        return list;
    }

    @Override
    public List getCardProvince(PageView pageView) {
        return userCardLogDao.getCardProvince(pageView);
    }

    @Override
    public List getCardCount() {
        List<CardVo> list = userCardLogDao.getCardCount();

        if(list != null && list.size() > 0){
            for(CardVo cardVo : list){
                cardVo.setCardName(CardCache.getCard(cardVo.getCardId()).getName());
                cardVo.setPriceName(CardCache.getPrice(cardVo.getPriceId()).getDescription());
            }
        }
        return list;
    }

    @Override
    public List getListByCaller(PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("mobile", pageView.getCaller()));
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("btime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("btime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return userCardLogDao.findByCriteria(dc);
    }

    @Override
    public int qxtSmsSuccess(String phone, String spNumber) {
        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("mobile", phone));
        dc.add(Restrictions.eq("flag", 5));

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(propertyUtils.getInteger("valid.day"));
        dc.add(Restrictions.gt("btime", dateTime.toDate()));
        List<UserCardLog> list = userCardLogDao.findByCriteria(dc);
        if(CollectionUtils.isNotEmpty(list)){
            UserCardLog userCardLog = list.get(0);
            userCardLog.setEtime(new Date());
            userCardLog.setFlag(6);
            userCardLogDao.save(userCardLog);
            return 1;
        }
        return 0;
    }

    @Override
    public int getTodayCardCount(String phoneNumber) {
        return userCardLogDao.getTodayCardCount(phoneNumber);
    }
    public int getMonthCardCount(String phoneNumber){
        return userCardLogDao.getMonthCardCount(phoneNumber);
    }

    @Override
    public int calloutsucess(int id) {
        String sql = "update tbl_user_card_log set flag = 6, etime=now() where id=" + id;
        return userCardLogDao.executeSQL(sql);
    }



    @Override
    public UserCardLog getLthjCard(SmsBillTemp billTemp, Paychannel paychannel) {
        try {
            cacheCheckUser.addCallerFee(billTemp.getMobile() + Constants.split_str + "lthj", billTemp.getFee());
            cacheCheckUser.addCalledProvinceFee(billTemp.getProvince() + Constants.split_str + "lthj" + paychannel.getPaytypeId(), billTemp.getFee(), false);
            UserCardLog cardLog = null;
            cardLog = userCardLogDao.getCardBySms(billTemp.getMobile(), billTemp.getId());
            if(cardLog != null){
                billTemp.setFlag(billTemp.getFlag() + 1);
                return cardLog;
            }

            CardPassword card = cardPasswordService.getUserCard(paychannel.getCardId(), paychannel.getPriceId());
            if (card == null) {
                LogEnum.DEFAULT.warn(billTemp.getMobile() + "  提交验证码 " + "取卡失败 paymentcode=" + billTemp.getPaymentcode() + " cardId=" + paychannel.getCardId() + " priceId=" + paychannel.getPriceId());
                return null;
            }
            billTemp.setFlag(billTemp.getFlag() + 1);

            XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));
            cardLog = new UserCardLog();
            cardLog.setMobile(billTemp.getMobile());
            cardLog.setProvince(billTemp.getProvince());
            cardLog.setCity(billTemp.getCity());
            cardLog.setChannelid(String.valueOf(paychannel.getId()));
            cardLog.setCardId(paychannel.getCardId());
            cardLog.setPriceId(paychannel.getPriceId());
            cardLog.setFlag(7);
            cardLog.setSmsids(String.valueOf(billTemp.getId()));
            cardLog.setCardno(xdEncodeHelper.XDDecode(card.getCardno(), true));
            cardLog.setCardpwd(xdEncodeHelper.XDDecode(card.getPassword(), true));
            Date now = new Date();
            cardLog.setBtime(now);
            cardLog.setEtime(now);
            userCardLogDao.save(cardLog);
            return cardLog;
        } finally {
            smsBillTempDao.save(billTemp);
        }
    }
}
