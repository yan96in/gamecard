package com.sp.platform.dao;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.CardVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
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
public class PcCardLogDao extends HibernateDaoUtil<PcCardLog, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PropertyUtils propertyUtils;

    public List<CardVo> getCardBill(PageView pageView) {
        String sql = "select cardid,priceid,count(*) as counts," +
                "sum(case resultcode when 4 then 1 else 0 end) as count4," +
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

}
