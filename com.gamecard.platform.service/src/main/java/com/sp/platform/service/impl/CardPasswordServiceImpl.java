package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.CardPasswordDao;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.service.CardPasswordService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-7
 * Time: 下午10:31
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class CardPasswordServiceImpl implements CardPasswordService {
    @Autowired
    private CardPasswordDao cardPasswordDao;

    public CardPassword getUserCard(int cardid, int priceid){
        String sql = "select a.* from gw_card_password a where a.cardid=" + cardid + " and priceid=" + priceid +
                " and state=0 ORDER BY rand() LIMIT 1";
        List<CardPassword> list = cardPasswordDao.getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(CardPassword.class).list();
        if(list != null && list.size() > 0){
            CardPassword card = list.get(0);
            card.setState(1);
            card.setUtime(new Date());
            cardPasswordDao.save(card);
            return card;
        }
        return null;
    }

    @Override
    public void save(List<CardPassword> cards) {
        for(CardPassword card : cards){
            cardPasswordDao.save(card);
        }
    }

    @Override
    public CardPassword get(int id) {
        return cardPasswordDao.get(id);
    }

    @Override
    public void delete(int id) {
        cardPasswordDao.delete(id);
    }

    @Override
    public void save(CardPassword object) {
        cardPasswordDao.save(object);
    }

    @Override
    public List<CardPassword> getAll() {
        return cardPasswordDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
