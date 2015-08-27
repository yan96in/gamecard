package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.CardDao;
import com.sp.platform.dao.PaytypeDao;
import com.sp.platform.dao.PriceDao;
import com.sp.platform.entity.Card;
import com.sp.platform.entity.Paytype;
import com.sp.platform.entity.Price;
import com.sp.platform.service.CardService;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:24
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {
    @Autowired
    private CardDao cardDao;
    @Autowired
    private PriceDao priceDao;
    @Autowired
    private PaytypeDao paytypeDao;

    @Override
    @Transactional(readOnly = true)
    public Card get(int id) {
        return cardDao.get(id);
    }

    @Transactional(readOnly = true)
    public Card getDetail(int id){
        Card card = null;
        try{
            card = cardDao.get(id);
            if(card != null){
                List<Price> prices = priceDao.getPriceByCardId(card.getId());
                for(Price price : prices){
                    List<Paytype> temp = paytypeDao.getPaytypeByPriceId(card.getId(), price.getId());
                    List<Paytype> paytypes = new ArrayList<Paytype>();
                    Map<String, String> map = new HashMap<String, String>();
                    for(Paytype paytype : temp){
                        if(StringUtils.isNotBlank(map.get(paytype.getOp()))){
                            continue;
                        }
                        map.put(paytype.getOp(), paytype.getOp());
                        paytypes.add(paytype);
                    }
                    price.setPaytypes(paytypes);
                }
                card.setPrices(prices);
            }
        }catch (Exception e){
            return null;
        }
        return card;
    }

    @Override
    public void delete(int id) {
        cardDao.delete(id);
    }

    @Override
    public void save(Card object) {
        cardDao.save(object);
    }

    @Override
    public List<Card> getAll() {
        return cardDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
