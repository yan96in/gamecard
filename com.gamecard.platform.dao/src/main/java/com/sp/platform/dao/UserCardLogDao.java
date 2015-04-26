package com.sp.platform.dao;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.CardVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-5
 * Time: 上午7:29
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserCardLogDao extends HibernateDaoUtil<UserCardLog, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PropertyUtils propertyUtils;

    public List<CardVo> getCardBill(PageView pageView) {
        String sql = "select cardid,priceid,count(*) as counts," +
                "sum(case flag when 4 then 1 else 0 end) as count4," +
                "sum(case flag when 5 then 1 else 0 end) as count5," +
                "sum(case flag when 6 then 1 else 0 end) as count6," +
                "sum(case flag when 7 then 1 else 0 end) as count7 " +
                "from tbl_user_card_log " +
                "where btime>'" + pageView.getBtime() + "' and btime<'" + pageView.getEtime() + "' " +
                "group by cardid,priceid order by 1,2";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<CardVo> list = new ArrayList<CardVo>();
        CardVo bean = null;
        while (result2.hasNext()) {
            map = result2.next();
            bean = new CardVo();

            bean.setCardId(toInt(map.get("cardid")));
            bean.setPriceId(toInt(map.get("priceid")));
            bean.setCount(toInt(map.get("counts")));
            bean.setCount4(toInt(map.get("count4")));
            bean.setCount5(toInt(map.get("count5")));
            bean.setCount6(toInt(map.get("count6")));
            bean.setCount7(toInt(map.get("count7")));
            list.add(bean);
        }
        return list;
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    private int toInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Integer.parseInt(obj.toString());
    }

    public List getCardProvince(PageView pageView) {
        String sql = "select province,count(*) as counts," +
                "sum(case flag when 4 then 1 else 0 end) as count4," +
                "sum(case flag when 5 then 1 else 0 end) as count5," +
                "sum(case flag when 6 then 1 else 0 end) as count6," +
                "sum(case flag when 7 then 1 else 0 end) as count7 " +
                "from tbl_user_card_log " +
                "where btime>'" + pageView.getBtime() + "' and btime<'" + pageView.getEtime() + "' " +
                "and cardid=" + pageView.getCardId() + " and priceid=" + pageView.getPriceId() +
                " group by province order by 1,2";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<CardVo> list = new ArrayList<CardVo>();
        CardVo bean = null;
        while (result2.hasNext()) {
            map = result2.next();
            bean = new CardVo();

            bean.setProvince(toString(map.get("province")));
            bean.setCount(toInt(map.get("counts")));
            bean.setCount4(toInt(map.get("count4")));
            bean.setCount5(toInt(map.get("count5")));
            bean.setCount6(toInt(map.get("count6")));
            bean.setCount7(toInt(map.get("count7")));
            list.add(bean);
        }
        return list;
    }

    public List<CardVo> getCardCount() {
        String sql = "select a.cardid,a.priceid,coalesce(b.counts,0) as counts from gc_card_price a left join (" +
                "select cardid,priceid,count(*) as counts from gw_card_password where state=0 group by 1,2" +
                ") b on a.cardid=b.cardid and a.priceid=b.priceid order by 1,2";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<CardVo> list = new ArrayList<CardVo>();
        CardVo bean = null;
        while (result2.hasNext()) {
            map = result2.next();
            bean = new CardVo();

            bean.setCardId(toInt(map.get("cardid")));
            bean.setPriceId(toInt(map.get("priceid")));
            bean.setCount(toInt(map.get("counts")));
            list.add(bean);
        }
        return list;
    }

    public int getTodayCardCount(String phoneNumber) {
        DateTime dateTime = new DateTime();
        String today = dateTime.toString("yyyy-MM-dd");
        String sql = "select count(*) from tbl_user_card_log where mobile = '" + phoneNumber + "' and btime>= '" + today + "'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public int getMonthCardCount(String phoneNumber) {
        DateTime dateTime = new DateTime();
        String currentMonth = dateTime.toString("yyyy-MM");
        String sql = "select count(*) from tbl_user_card_log where mobile = '" + phoneNumber + "' and btime>= '" + currentMonth + "-01'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public UserCardLog getCardBySms(String mobile, Integer id) {
        DetachedCriteria dc = DetachedCriteria.forClass(UserCardLog.class);
        dc.add(Restrictions.eq("mobile", mobile));
        dc.add(Restrictions.eq("smsids", String.valueOf(id)));
        List<UserCardLog> list = findByCriteria(dc);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
