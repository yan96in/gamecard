package com.sp.platform.service.impl;

import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.BillTempDao;
import com.sp.platform.dao.SmsBillTempDao;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.BillTemp;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午8:34
 */
@Service
@Transactional
public class BillTempServiceImpl implements BillTempService {

    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private BillTempDao billTempDao;
    @Autowired
    private SmsBillTempDao smsBillTempDao;

    @Override
    public BillTemp get(int id) {
        return billTempDao.get(id);
    }

    @Override
    public void delete(int id) {
        billTempDao.delete(id);
    }

    @Override
    public void save(BillTemp object) {
        billTempDao.save(object);
    }

    @Override
    public List<BillTemp> getAll() {
        return billTempDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("btime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("etime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public void save(BillLog billLog) {
        billTempDao.save(billLog);
    }

    /**
     * 查询大于一天时间的数据
     */
    @Override
    public List<SmsBillTemp> getNeedProcessData() {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.lt("sendnum", 3));
        dc.add(Restrictions.eq("flag", 3));
        dc.add(Restrictions.eq("type", 0));

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(propertyUtils.getInteger("valid.day"));
        dc.add(Restrictions.gt("btime", dateTime.toDate()));
        return billTempDao.findByCriteria(dc, new Order[]{Order.asc("mobile"), Order.asc("channelid"), Order.asc("btime")});
    }

    @Override
    public void addSendNum(int id) {
        String sql = "update tbl_bill_temp set sendnum=sendnum+1 where id=" + id;
        billTempDao.executeSQL(sql);
    }

    @Override
    public List<BillTemp> getByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(BillTemp.class);
        dc.add(Restrictions.eq("caller", caller));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findByCriteria(dc);
    }

    @Override
    public List<SmsBillTemp> getSmsByCaller(String caller) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.eq("mobile", caller));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findByCriteria(dc);
    }

    @Override
    public void sync(int id) {
        BillTemp billTemp = billTempDao.get(id);
        if (billTemp != null) {
            LogEnum.DEFAULT.info("重新同步{}", billTemp);
            billTemp.setSendnum(0);
            billTempDao.save(billTemp);
        }
    }

    @Override
    public void syncsms(int id) {
        SmsBillTemp billTemp = smsBillTempDao.get(id);
        if (billTemp != null) {
            LogEnum.DEFAULT.info("重新同步{}", billTemp);
            billTemp.setSendnum(0);
            smsBillTempDao.save(billTemp);
        }
    }

    @Override
    public SmsBillTemp getByLinkid(String linkid) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.eq("linkid", linkid));
        List<SmsBillTemp> list = smsBillTempDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void save(SmsBillTemp smsBillTemp) {
        smsBillTempDao.save(smsBillTemp);
    }

    @Override
    public void saveMr(SmsBillLog billLog) {
        smsBillTempDao.saveMr(billLog);
    }

    @Override
    public void saveMo(SmsBillLog billLog) {
        smsBillTempDao.saveMo(billLog);
    }

    public void saveBill(SmsBillLog billLog) {
        smsBillTempDao.saveBill(billLog);
    }

    @Override
    public List<SmsBillTemp> getCalloutData() {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.lt("sendnum", 3));
        dc.add(Restrictions.eq("flag", 3));
        dc.add(Restrictions.eq("type", 0));

        DateTime dateTime = new DateTime();
        dateTime.plusHours(-3);

        dc.add(Restrictions.lt("etime", dateTime.toString("yyyy-MM-dd HH:mm:ss")));
        return smsBillTempDao.findByCriteria(dc);
    }

    @Override
    public List<SmsBillTemp> getSyncSmsBill2() {
        Format format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(new Date());

        String sql = "select max(id) id, max(mobile) mobile, max(spnum) spnum, max(msg) msg, linkid,max(status) status," +
                "max(btime) btime,max(etime) etime,max(province) province,max(city) city,max(fee) fee,max(sfid) sfid," +
                "max(cpid) cpid,max(type) type,max(sendnum) sendnum,max(syncurl) syncurl,max(parentid) parentid,flag " +
                "from sms_bill_temp where (btime>? or etime>?) and sendnum < 3 group by linkid having count(*)>1;";

        Iterator<Object[]> result = smsBillTempDao.getSession().createSQLQuery(sql)
                .setParameter(0, str).setParameter(1, str).list().iterator();
        List<SmsBillTemp> list = new ArrayList<SmsBillTemp>();

        Date now = new Date();

        Object[] objs;
        SmsBillTemp temp;
        while (result.hasNext()) {
            try {
                objs = result.next();
                temp = new SmsBillTemp();
                temp.setId((Integer) objs[0]);
                temp.setMobile(toString(objs[1]));
                temp.setSpnum(toString(objs[2]));
                temp.setMsg(toString(objs[3]));
                temp.setLinkid(toString(objs[4]));
                temp.setStatus(toString(objs[5]));
                temp.setBtime(now);
                temp.setEtime(now);
                temp.setProvince(toString(objs[8]));
                temp.setCity(toString(objs[9]));
                temp.setFee(Integer.parseInt(toString(objs[10])));
                temp.setSfid(Integer.parseInt(toString(objs[11])));
                temp.setCpid(Integer.parseInt(toString(objs[12])));
                temp.setType(Integer.parseInt(toString(objs[13])));
                temp.setSendnum(Integer.parseInt(toString(objs[14])));
                temp.setSyncurl(toString(objs[15]));
                temp.setParentid(Integer.parseInt(toString(objs[16])));
                temp.setFlag(Integer.parseInt(toString(objs[17])));
                list.add(temp);
            } catch (Exception e) {
                LogEnum.DEFAULT.error(e.toString());
            }
        }
        return list;
    }

    public String toString(Object object) {
        if (object == null) {
            return "";
        } else {
            return object.toString();
        }
    }

    @Override
    public void addSmsSendNum(int id) {
        String sql = "update sms_bill_temp set sendnum=sendnum+1 where id=" + id;
        billTempDao.executeSQL(sql);
    }

    @Override
    public void deleteSmsTemp(Integer id) {
        smsBillTempDao.delete(id);
    }

    @Override
    public void deleteSmsTempByLinkId(String linkId) {
        smsBillTempDao.executeSQL("delete from sms_bill_temp where linkid='" + linkId + "'");
    }

    @Override
    public List getSmsByCaller(PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsBillTemp.class);
        dc.add(Restrictions.eq("mobile", pageView.getCaller()));
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("btime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("etime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("btime"));
        return billTempDao.findByCriteria(dc);
    }

    @Override
    public int updateUserFlag(String mobile, Integer channelid) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusHours(-24);

        String sql = "update sms_bill_temp set flag=3,fee=200,channelid=" + channelid
                + ",status='DELIVRD', etime=now() where (btime>'"
                + dateTime.toString("yyyy-MM-dd HH:mm:ss") + "' or etime>'" +
                dateTime.toString("yyyy-MM-dd HH:mm:ss") + "') and mobile='" + mobile
                + "' and flag <3";
        return billTempDao.executeSQL(sql);
    }

    @Override
    public int getProvinceFee(String phoneNumber) {
        String province = HaoduanCache.getProvince(phoneNumber);
        DateTime dateTime = new DateTime();
        String currentMonth = dateTime.toString("yyyy-MM");
        return smsBillTempDao.getPrvonceFee(province, currentMonth+"-01");
    }
}
