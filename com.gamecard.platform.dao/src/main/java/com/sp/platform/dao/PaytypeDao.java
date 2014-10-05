package com.sp.platform.dao;

import com.sp.platform.entity.Paytype;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:44
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class PaytypeDao extends HibernateDaoUtil<Paytype, Integer> {
    public List<Paytype> getPaytypeByPriceId(int cardId, int priceId) {
        return getSessionFactory().getCurrentSession().createSQLQuery
                ("select a.* from gc_paytype a, gc_card_price b, gc_price_paytype c " +
                        "where a.id=c.paytypeid and b.id=c.priceid and " +
                        "b.cardId=" + cardId + " and b.priceId = " +
                        priceId + " order by a.id").addEntity(Paytype.class).list();
    }
}
