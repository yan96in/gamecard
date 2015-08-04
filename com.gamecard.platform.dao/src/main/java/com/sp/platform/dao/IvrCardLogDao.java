package com.sp.platform.dao;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.IvrCardLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.TimeUtils;
import com.sp.platform.vo.PcBillVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglei on 15/7/26.
 */
@Repository
public class IvrCardLogDao extends HibernateDaoUtil<IvrCardLog, Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PcBillVo> getBillInfo(PageView pageView) {
        String flag = null;
        if (pageView.getCpid() == 1) {
            flag = "province";
        } else {
            flag = "called";
        }
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = TimeUtils.chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        //  查询字段 --------------------------
        String sql = "select " + flag + " province, count(*) num,sum(fee) fee from ivr_bill_log where ctime>'" + kssj + "' and ctime<'" + jssj + "' ";
        if (StringUtils.isNotBlank(pageView.getCalled()) && !StringUtils.equals("ALL", pageView.getCalled())) {
            sql = sql + " and called = '" + pageView.getCalled() + "' ";
        }
        sql = sql + "group by " + flag + " order by 3 desc";

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

    public List<PcBillVo> getCardBillInfo(PageView pageView) {
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = TimeUtils.chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        //  查询字段 --------------------------
        String sql = "select cardid,priceid,count(*) num from ivr_card_log where ctime>'" + kssj + "' and ctime<'" + jssj + "' ";
        if (StringUtils.isNotBlank(pageView.getCalled()) && !StringUtils.equals("ALL", pageView.getCalled())) {
            sql = sql + " and called = '" + pageView.getCalled() + "' ";
        }
        sql = sql + "group by cardid,priceid order by 1,2 desc";

        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        Map<String, Object> map = null;
        List<PcBillVo> list = new ArrayList<PcBillVo>();

        int fcounts = 0;
        while (result2.hasNext()) {
            map = result2.next();
            PcBillVo vo = new PcBillVo();
            vo.setCardid(map.get("cardid").toString());
            vo.setPriceid(map.get("priceid").toString());
            vo.setNum(map.get("num").toString());
            list.add(vo);

            fcounts = fcounts + Integer.parseInt(vo.getNum());
        }
        PcBillVo vo = new PcBillVo();
        vo.setCardid("总计");
        vo.setPriceid("");
        vo.setNum(fcounts + "");
        list.add(vo);
        return list;
    }
}
