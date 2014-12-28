package com.sp.platform.dao;

import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.entity.UserCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.TimeUtils;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.CardVo;
import com.sp.platform.vo.PcBillVo;
import com.sp.platform.vo.SmsBillVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

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

    public List<PcBillVo> getBillInfo(PageView pageView) {
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = TimeUtils.chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];

        //  查询字段 --------------------------
        String sql = "select cardid, priceid, count(*) num,sum(fee)/100 fee from tbl_user_pc_card_log where btime>'" + kssj + "' and btime<'" + jssj + "' ";
        if(pageView.getSpid() > 0){
            sql = sql + "and channelid=" + pageView.getSpid() + " ";
        }
        sql = sql + "and status in (2,3) group by cardid, priceid";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<PcBillVo> list = new ArrayList<PcBillVo>();

        int fcounts = 0;
        float ffees = 0;
        BigDecimal bFees;
        while (result2.hasNext()) {
            map = result2.next();
            PcBillVo vo = new PcBillVo();
            vo.setCardid(map.get("cardid").toString());
            vo.setPriceid(map.get("priceid").toString());
            vo.setNum(map.get("num").toString());
            vo.setFee(map.get("fee").toString());
            list.add(vo);

            bFees = new BigDecimal(vo.getFee());
            ffees = ffees + bFees.floatValue();
            fcounts = fcounts + Integer.parseInt(vo.getNum());
        }
        PcBillVo vo = new PcBillVo();
        vo.setCardid("总计");
        vo.setPriceid("");
        vo.setNum(fcounts + "");
        vo.setFee(ffees + "");
        list.add(vo);
        return list;
    }

    public List<PcBillVo> getProvinceBillInfo(PageView pageView) {
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = TimeUtils.chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];

        //  查询字段 --------------------------
        String sql = "select province, count(*) num,sum(fee)/100 fee from tbl_user_pc_card_log where btime>'" + kssj + "' and btime<'" + jssj + "' ";
        if(pageView.getSpid() > 0){
            sql = sql + "and channelid=" + pageView.getSpid() + " ";
        }
        sql = sql + "and status in (2,3) group by province order by 3 desc";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<PcBillVo> list = new ArrayList<PcBillVo>();

        int fcounts = 0;
        float ffees = 0;
        BigDecimal bFees;
        while (result2.hasNext()) {
            map = result2.next();
            PcBillVo vo = new PcBillVo();
            vo.setProvince(map.get("province").toString());
            vo.setNum(map.get("num").toString());
            vo.setFee(map.get("fee").toString());
            list.add(vo);

            bFees = new BigDecimal(vo.getFee());
            ffees = ffees + bFees.floatValue();
            fcounts = fcounts + Integer.parseInt(vo.getNum());
        }
        PcBillVo vo = new PcBillVo();
        vo.setProvince("总计");
        vo.setNum(fcounts + "");
        vo.setFee(ffees + "");
        list.add(vo);
        return list;
    }
}
