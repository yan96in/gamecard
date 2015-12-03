package com.sp.platform.dao;

import com.sp.platform.entity.Price;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:42
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class PriceDao extends HibernateDaoUtil<Price, Integer> {
    public List<Price> getPriceByCardId(int cardId) {
        List<Price> prices = getSessionFactory().getCurrentSession().createSQLQuery("select a.* from gc_price a, gc_card_price b " +
                "where a.id = b.priceId and b.cardId = " + cardId + " order by a.price").addEntity(Price.class).list();
        if (cardId == 1 && CollectionUtils.isNotEmpty(prices)) {
            for (Price price : prices) {
                if (16 == price.getId()) {
                    price.setDescription("10元卡");
                }
            }
        }
        return prices;
    }
}
