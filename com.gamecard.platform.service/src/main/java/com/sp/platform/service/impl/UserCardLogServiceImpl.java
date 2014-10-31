package com.sp.platform.service.impl;

import com.sp.platform.cache.CardCache;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.UserCardLogDao;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.CardVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
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
        dc.add(Restrictions.le("etime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return userCardLogDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public List<UserCardLog> getCalloutData() {

        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("flag", 4));
        dc.add(Restrictions.lt("sendnum", 3));

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusHours(propertyUtils.getInteger("out.timeout"));

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
        dc.add(Restrictions.le("etime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return userCardLogDao.findByCriteria(dc);
    }

    @Override
    public int calloutsucess(int id) {
        String sql = "update tbl_user_card_log set flag = 6 where id=" + id;
        return userCardLogDao.executeSQL(sql);
    }
}
