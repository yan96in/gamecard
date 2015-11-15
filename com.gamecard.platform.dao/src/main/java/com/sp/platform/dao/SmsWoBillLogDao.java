package com.sp.platform.dao;

import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.SmsWoBillLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.BillVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-3
 * Time: 上午12:17
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SmsWoBillLogDao extends HibernateDaoUtil<SmsWoBillLog, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PropertyUtils propertyUtils;

    public List<BillVo> getBillInfo(PageView pageView) {
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by cpid ";

        String str_a;
        if (pageView.getCpflag() > 0) {
            str_a = "<a href='wobill!cplist.action?pageView.btime=";
        } else {
            str_a = "<a href='wobill!list.action?pageView.btime=";
        }
        str_a += pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts,count(status) sendCounts, " +
                "sum(case status when 4 then 1 else 0 end) users, " +
                "sum(totalFee) salefee," +
                "sum(case status when 4 then totalFee else 0 end) fee," +
                "sum(case type when 1 then totalFee else 0 end) limitfee,";

        //  查询条件 --------------------------
        String where_sql = " from " + tableRoute("sms_wo_bill_log", kssj) + " where ctime>'" + kssj + "' and ctime<'" + jssj + "'";

        if (pageView.getSpid() > 0) {
            where_sql += " and randomid=" + pageView.getSpid();
            parmMap.put("pageView.spid", pageView.getSpid());
        }
        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            try {
                String appName = new String(pageView.getCalled().getBytes("ISO-8859-1"), "UTF-8");
                where_sql += " and appname='" + appName + "'";
                parmMap.put("pageView.called", appName);
            } catch (Exception e) {
                LogEnum.DEFAULT.error(e.toString());
            }
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }
        if (pageView.getCpflag() > 0) {
            where_sql += " and type=0";
        }

        if (pageView.getWoCompanyId() > 0) {
            where_sql += " and woCompanyId=" + pageView.getWoCompanyId();
            parmMap.put("pageView.woCompanyId", pageView.getWoCompanyId());
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_CP_PARENT.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("appName");
                col_sql += " appname groupcol ";
                group_sql = " group by appname ";
                flag = true;
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("省份");
                col_sql += " province groupcol ";
                group_sql = " group by province ";
            } else if (GroupType.GROUP_HOUR.isEquals(groupType)) {
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(ctime) groupcol ";
                    group_sql = " group by hour(ctime) ";
                } else {
                    col_sql += " datepart(hh, ctime) groupcol ";
                    group_sql = " group by datepart(hh, ctime) ";
                }
            } else if (GroupType.GROUP_SP_CHANNEL.isEquals(groupType)) {
                pageView.setMessage("WO+类型");
                col_sql += " randomid groupcol ";
                group_sql = " group by randomid ";
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            if (GroupType.GROUP_TIME.isEquals(groupType)) {
                flag = true;
                pageView.setMessage("日期");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    timeFlag = true;
                    col_sql += " left(ctime, 10) groupcol ";
                    group_sql = " group by left(ctime, 10) ";
                } else {
                    col_sql += " convert(varchar(10),ctime,120) groupcol ";
                    group_sql = " group by convert(varchar(10),ctime,120) ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
                parmMap.put("dynamic", "pageView.date");
                type = 3;
            } else if (GroupType.GROUP_SP.isEquals(groupType)) {
                pageView.setMessage("WO+公司");
                flag = true;
                col_sql += " woCompanyId groupcol ";
                group_sql = " group by woCompanyId ";
                parmMap.put("pageView.group2", GroupType.GROUP_SP_CHANNEL.getType());
                parmMap.put("dynamic", "pageView.woCompanyId");
                type = 5;
            } else {
                pageView.setMessage("主渠道");
                flag = true;
                col_sql += " cpid groupcol ";
                group_sql = " group by cpid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.cpid");
                type = 2;
            }
        }

        String sql = col_sql + where_sql + group_sql + " order by 7";
        logger.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processResult(result2, str_a, flag, type, timeFlag, parmMap);
    }

    private List<BillVo> processResult(Iterator<Map<String, Object>> result2, String str_a, boolean flag, int type, boolean timeFlag, Map<String, Object> parmMap) {
        Map<String, Object> map = null;
        List<BillVo> list = new ArrayList<BillVo>();
        BillVo bean;


        String dynamic = toString(parmMap.get("dynamic"));
        if (StringUtils.isNotBlank(dynamic)) {
            parmMap.remove(dynamic);
            parmMap.remove("dynamic");
        }
        StringBuilder str = new StringBuilder(str_a);
        for (String key : parmMap.keySet()) {
            str.append("&").append(key).append("=").append(parmMap.get(key));
        }
        str_a = str.append("&").append(dynamic).append("=").toString();

        int fusers = 0;
        int fcounts = 0;
        float ffees = 0;
        float fsendCounts = 0;
        float flimitfees = 0;
        float fchannelfees = 0;
        float fsalefees = 0;
        BigDecimal bFees;
        BigDecimal bLimitfees;
        BigDecimal channelfees;
        BigDecimal salefees;


        while (result2.hasNext()) {
            map = result2.next();
            bean = new BillVo();
            salefees = (BigDecimal) map.get("salefee");
            bFees = (BigDecimal) map.get("fee");
            bLimitfees = (BigDecimal) map.get("limitfee");
            if (bLimitfees == null) {
                bLimitfees = new BigDecimal(0);
            }
            if (bFees == null) {
                bFees = new BigDecimal(0);
            }
            if (salefees == null) {
                salefees = new BigDecimal(0);
            }
            channelfees = bFees.subtract(bLimitfees);

            bean.setCounts(toString(map.get("counts")));
            bean.setSendCounts(toString(map.get("sendCounts")));
            bean.setUsers(toString(map.get("users")));
            bean.setSalefee(salefees.toString());
            bean.setFees(bFees.toString());
            bean.setLimitfee(bLimitfees.toString());
            bean.setChannelfee(channelfees.toString());

            bean.setType(type);

            String temp;
            if (timeFlag) {
                temp = toString(map.get("groupcol"));
            } else {
                temp = toString(map.get("groupcol"));
            }

            if (flag) {
                bean.setHref(str_a + temp + "'>");
            }
            bean.setGroup(temp);
            list.add(bean);

            fusers = fusers + Integer.parseInt(bean.getUsers());
            fcounts = fcounts + Integer.parseInt(bean.getCounts());
            fsendCounts = fsendCounts + (StringUtils.isBlank(bean.getSendCounts()) ? 0 : Integer.parseInt(bean.getSendCounts()));
            fsalefees = fsalefees + salefees.floatValue();
            ffees = ffees + bFees.floatValue();
            fchannelfees = fchannelfees + channelfees.floatValue();
            flimitfees = flimitfees + bLimitfees.floatValue();
        }

        bean = new BillVo();
        bean.setGroup("总计");
        bean.setCounts(fcounts + "");
        bean.setUsers(fusers + "");
        bean.setSendCounts(fsendCounts + "");
        BigDecimal bd;

        bean.setSalefee(fsalefees + "");
        bean.setFees(ffees + "");
        bean.setLimitfee(flimitfees + "");
        bean.setChannelfee(fchannelfees + "");
        list.add(bean);
        return list;
    }

    public String[] chuli(String kssj, String jssj) {
        String str[] = new String[2];
        if (StringUtils.isEmpty(kssj) || StringUtils.isEmpty(jssj)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            kssj = shj;
            jssj = shj;
        }
        if (kssj.length() == 10) {
            kssj = kssj + " 00:00:00";
        }
        if (jssj.length() == 10) {
            jssj = jssj + " 23:59:59";
        }

        str[0] = kssj;
        str[1] = jssj;
        return str;
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public List<BillVo> getCpBillInfo(PageView pageView) {
        String[] sj;
        if (StringUtils.isNotBlank(pageView.getDate())) {
            sj = chuli(pageView.getDate(), pageView.getDate());
        } else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by cpid ";

        String str_a = "<a href='cpwobill!list.action?pageView.btime=" + pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts,count(status) sendCounts, " +
                "sum(case when status=4 and type=0 then 1 else 0 end) users," +
                "sum(totalFee) salefee," +
                "sum(case status when 4 then totalFee else 0 end) fee," +
                "sum(case type when 1 then totalFee else 0 end) limitfee,";

        //  查询条件 --------------------------
        String where_sql = " from " + tableRoute("sms_wo_bill_log", kssj) + " where ctime>'" + kssj + "' and ctime<'" + jssj + "'";

        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            where_sql += " and appkey='" + pageView.getCalled() + "'";
            parmMap.put("pageView.called", pageView.getCalled());
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_CP.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("appkey");
                col_sql += " appkey groupcol ";
                group_sql = " group by appkey ";
                flag = true;
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("计费点");
                col_sql += " iapId groupcol ";
                group_sql = " group by iapId ";
            } else if (GroupType.GROUP_HOUR.isEquals(groupType)) {
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(ctime) groupcol ";
                    group_sql = " group by hour(ctime) ";
                } else {
                    col_sql += " datepart(hh, ctime) groupcol ";
                    group_sql = " group by datepart(hh, ctime) ";
                }
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            flag = true;
            pageView.setMessage("日期");
            if (propertyUtils.getProperty("db.type").equals("mysql")) {
                timeFlag = true;
                col_sql += " left(ctime, 10) groupcol ";
                group_sql = " group by left(ctime, 10) ";
            } else {
                col_sql += " convert(varchar(10),ctime,120) groupcol ";
                group_sql = " group by convert(varchar(10),ctime,120) ";
            }
            parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
            parmMap.put("dynamic", "pageView.date");
            type = 3;
        }

        String sql = col_sql + where_sql + group_sql + " order by 7";
        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processResult(result2, str_a, flag, type, timeFlag, parmMap);
    }

    private String tableRoute(String tableName, String time) {
        DateTime dateTime = new DateTime();
        DateTime maxTime = null;
        if (dateTime.dayOfMonth().get() >= 15) {
            maxTime = DateTime.parse(dateTime.dayOfMonth().
                    withMinimumValue().toString("yyyy-MM-dd"));
        } else {
            maxTime = DateTime.parse(dateTime.plusMonths(-1).dayOfMonth().
                    withMinimumValue().toString("yyyy-MM-dd"));
        }

        DateTime date = DateTime.parse(StringUtils.left(time, 10));
        if (date.isBefore(maxTime.toDate().getTime())) {
            return tableName + "_" + StringUtils.right(StringUtils.left(time, 7).replaceAll("-", ""), 4);
        } else {
            return tableName;
        }
    }
}
