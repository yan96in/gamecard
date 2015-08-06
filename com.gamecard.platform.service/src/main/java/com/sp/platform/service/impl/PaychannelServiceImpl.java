package com.sp.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.Constants;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaychannelDao;
import com.sp.platform.entity.NaHaoduan;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.service.NaHaoduanService;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.service.PcCardLogService;
import com.sp.platform.service.sp.DxService;
import com.sp.platform.service.sp.KzYdService;
import com.sp.platform.service.sp.SpYdService;
import com.sp.platform.service.sp.YlYdService;
import com.sp.platform.util.*;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.LtPcResult;
import com.sp.platform.vo.PcVo1;
import com.yangl.common.IpAddressUtil;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-25
 * Time: 下午10:36
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PaychannelServiceImpl implements PaychannelService {
    @Autowired
    private PaychannelDao paychannelDao;
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private PcCardLogService pcCardLogService;
    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private NaHaoduanService naHaoduanService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SpYdService spYdService;
    @Autowired
    private KzYdService kzYdService;
    @Autowired
    private YlYdService ylYdService;
    @Autowired
    private DxService dxService;

    @Override
    public Paychannel get(int id) {
        return paychannelDao.get(id);
    }

    @Override
    public void delete(int id) {
        paychannelDao.delete(id);
    }

    @Override
    public void save(Paychannel object) {
        paychannelDao.save(object);
    }

    @Override
    public List<Paychannel> getAll() {
        return paychannelDao.getAll();
    }

    public ChannelVo findChannels(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        chanels.setChannels1(find(cardId, priceId, paytypeId, 1, province, phone, null));
        chanels.setChannels2(find(cardId, priceId, paytypeId, 2, province, phone, null));
        return chanels;
    }

    public ChannelVo findPcChannels(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        List<Paychannel> paychannels = find(cardId, priceId, paytypeId, 1, province, phone, null);
        boolean flag = !CollectionUtils.isEmpty(paychannels);
        chanels.setPcflag(flag);
        return chanels;
    }

    public ChannelVo sendPcCode(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        int fee = 0;
        String resultCode = null;
        String resultMessage = null;
        String sid = null;
        String ext = "0";
        List<Paychannel> paychannels = find(cardId, priceId, paytypeId, 1, province, phone, null);
        if (!CollectionUtils.isEmpty(paychannels)) {
            try {
                Paychannel paychannel = paychannels.get(0);
                chanels.setChannelId(paychannel.getId());
                fee = paychannel.getFee();
                HttpClient httpClient = new DefaultHttpClient();
                if (paytypeId == 19) {
                    LtPcResult result = null;
                    boolean flag = true;
                    if (StringUtils.indexOf(propertyUtils.getProperty("yd.open.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.yd.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.yd.caller.week.limit." + paytypeId, 35),
                                propertyUtils.getInteger("pc.yd.caller.month.limit." + paytypeId, 100),
                                "移动游戏");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.yd.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.yd.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "移动游戏");
                        }
                        if (flag) {
                            result = spYdService.sendYdCode(phone, fee);  // 走移动游戏
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(3);
                        sid = result.getChanels().getSid();
                        ext = "3";
                        return result.getChanels();
                    }

                    // 走空中移动
                    if (StringUtils.indexOf(propertyUtils.getProperty("yd.kz.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.kz.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.kz.caller.week.limit." + paytypeId, 40),
                                propertyUtils.getInteger("pc.kz.caller.month.limit." + paytypeId, 100),
                                "空中移动");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.kz.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.kz.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "空中移动");
                        }
                        if (flag) {
                            result = kzYdService.sendYdCode(phone, fee, province);  // 走空中移动
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(5);
                        sid = result.getChanels().getSid();
                        ext = "5";
                        return result.getChanels();
                    }

                    // 走翼龙移动
                    if (StringUtils.indexOf(propertyUtils.getProperty("yd.yl.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.yl.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.yl.caller.week.limit." + paytypeId, 40),
                                propertyUtils.getInteger("pc.yl.caller.month.limit." + paytypeId, 100),
                                "翼龙");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.yl.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.yl.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "翼龙");
                        }
                        if (flag) {
                            result = ylYdService.sendYdCode(phone, fee, province);  // 走翼龙
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(4);
                        sid = result.getChanels().getSid();
                        ext = "4";
                        return result.getChanels();
                    }
                } else if (paytypeId == 20) {
                    LtPcResult result = null;
                    boolean flag = true;
                    if (StringUtils.indexOf(propertyUtils.getProperty("wo.open.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.wo.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.wo.caller.week.limit." + paytypeId, 35),
                                propertyUtils.getInteger("pc.wo.caller.month.limit." + paytypeId, 100),
                                "WO+");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.wo.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.wo.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "WO+");
                        }
                        if (flag) {
                            flag = pcCardLogService.isValidHour(phone, "1", province);
                        }
                        if (flag) {
                            fee = Integer.parseInt(paychannel.getSpnum());
                            result = getWoResult(phone, fee);  // 走联通WO+
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(1);
                        sid = result.getChanels().getSid();
                        ext = "1";
                        return result.getChanels();
                    }

                    // 走翼光支付
                    if (StringUtils.indexOf(propertyUtils.getProperty("yg.open.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.yg.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.yg.caller.week.limit." + paytypeId, 40),
                                propertyUtils.getInteger("pc.yg.caller.month.limit." + paytypeId, 100),
                                "翼光");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.yg.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.yg.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "翼光");
                        }
                        if (flag) {
                            flag = pcCardLogService.isValidHour(phone, "2", province);
                        }
                        if (flag) {
                            fee = Integer.parseInt(paychannel.getSpnum());
                            result = getYgResult(phone, fee, httpClient, chanels, paychannel);  // 走翼光
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(2);
                        sid = result.getChanels().getSid();
                        ext = "2";
                        return result.getChanels();
                    }

                    //最后走空中
                    fee = paychannel.getFee();
                    result = getKzResult(phone, fee, httpClient, chanels);

                    if (result != null) {
                        result.getChanels().setType(0);
                        sid = result.getChanels().getSid();
                        return result.getChanels();
                    }
                } else if (paytypeId == 21) {
                    LtPcResult result = null;
                    boolean flag = true;
                    if (StringUtils.indexOf(propertyUtils.getProperty("dx.open.provinces"), province) >= 0) {
                        flag = callerLimit(phone,
                                propertyUtils.getInteger("pc.dx.caller.day.limit." + paytypeId, 30),
                                propertyUtils.getInteger("pc.dx.caller.week.limit." + paytypeId, 35),
                                propertyUtils.getInteger("pc.dx.caller.month.limit." + paytypeId, 100),
                                "电信");
                        if (flag) {
                            int provinceMaxFee = propertyUtils.getInteger("pc.dx.province.day.limit." + paytypeId + "." + PinyinUtil.cn2FirstSpell(province));
                            if (provinceMaxFee == 0) {
                                provinceMaxFee = propertyUtils.getInteger("pc.dx.province.day.limit." + paytypeId);
                            }
                            flag = provinceLimit(phone, province, paytypeId, provinceMaxFee, "电信");
                        }
                        if (flag) {
                            result = dxService.sendYdCode(phone, fee, province);  // 走电信翼支付
                        }
                    }

                    if (result != null && result.getChanels().isPcflag()) {
                        result.getChanels().setType(6);
                        sid = result.getChanels().getSid();
                        ext = "6";
                        return result.getChanels();
                    }
                }
            } catch (Exception e) {
                LogEnum.TEMP.error("调用空中网PC网游接口Step1 Error：", e);
            } finally {
                PcCardLog pcCardLog = new PcCardLog();
                pcCardLog.setMobile(phone);
                pcCardLog.setProvince(province);
                pcCardLog.setCity(HaoduanCache.getCity(phone));
                pcCardLog.setCardId(cardId);
                pcCardLog.setPriceId(priceId);
                pcCardLog.setChannelid(paytypeId);
                pcCardLog.setFee(fee);
                pcCardLog.setResultcode(resultCode);
                pcCardLog.setResultmsg(resultMessage);
                pcCardLog.setSid(sid);
                pcCardLog.setStatus(1);
                pcCardLog.setExt(ext);
                pcCardLog.setIp(IpAddressUtil.getRealIp());
                pcCardLog.setBtime(new Date());
                pcCardLog.setEtime(new Date());
                pcCardLogService.save(pcCardLog);
            }
        } else {
            LogEnum.DEFAULT.info(cardId + " " + priceId + " " + paytypeId + " " + province + " " + phone + " 无可用通道");
        }
        chanels.setPcflag(false);
        return chanels;
    }


    private boolean callerLimit(String phoneNumber, int maxDayFee, int maxWeekFee, int maxMonthFee, String channelType) {
        //----------------------------- 用户日上限 -----------------------
        int type = getPcSpType(channelType);

        int tempFee = cacheCheckUser.getCallerDayFee(phoneNumber + Constants.split_str + "pc" + type);

        if (maxDayFee > 0 && tempFee >= maxDayFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户日上限 ").append(maxDayFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        //----------------------------- 用户月上限 -----------------------
        tempFee = cacheCheckUser.getCallerMonthFee(phoneNumber + Constants.split_str + "pc" + type);
        //如果没有设置上限， 或者费用未达到上限，继续
        if (maxMonthFee > 0 && tempFee >= maxMonthFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户月上限 ").append(maxMonthFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            return false;
        }

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(-7);
        String sql = "select sum(fee)/100 from tbl_user_pc_card_log where status=2 and btime>='"
                + dateTime.toString("yyyy-MM-dd") + "' and mobile='" + phoneNumber + "' and ext=" + type;

        Integer fee = jdbcTemplate.queryForObject(sql, Integer.class);
        if (fee != null && maxWeekFee > 0 && fee >= maxWeekFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户周上限 ").append(maxWeekFee)
                    .append(",周费用：")
                    .append(fee).toString());
            return false;
        }

        dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(-3);
        sql = "select count(*) from tbl_user_pc_card_log where status=2 and btime>='"
                + dateTime.toString("yyyy-MM-dd HH:mm:ss") + "' and mobile='" + phoneNumber + "'";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count >= 1) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 三分钟内只能成单一次 ").append(count).toString());
            return false;
        }

        return true;
    }

    private int getPcSpType(String channelType) {
        int type = 0;
        if (StringUtils.equals("WO+", channelType)) {
            type = 1;
        } else if (StringUtils.equals("翼光", channelType)) {
            type = 2;
        } else if (StringUtils.equals("移动游戏", channelType)) {
            type = 3;
        } else if (StringUtils.equals("翼龙", channelType)) {
            type = 4;
        } else if (StringUtils.equals("空中移动", channelType)) {
            type = 5;
        } else if (StringUtils.equals("电信", channelType)) {
            type = 6;
        }
        return type;
    }

    private boolean provinceLimit(String phoneNumber, String province, int paytypeId, int maxDayFee, String channelType) {
        //----------------------------- 省份日上限 -----------------------
        int type = getPcSpType(channelType);

        int tempFee = cacheCheckUser.getCalledProvinceDayFee(province + Constants.split_str + "pc" + paytypeId + type);

        if (maxDayFee > 0 && tempFee >= maxDayFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超省份日上限 ").append(maxDayFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        StringBuilder message = new StringBuilder(channelType + "  " + phoneNumber).
                append("---- 省份").append(province).append("收入正常，")
                .append("日费用：")
                .append(tempFee).append("  最高上限:" + maxDayFee);
        return true;
    }


    public List<Paychannel> find(int cardId, int priceId, int paytypeId, int feetype, String province, String phone, String msg) {
        if (HaoduanCache.NA.equals(province)) {
            NaHaoduan naHaoduan = new NaHaoduan();
            naHaoduan.setCaller(phone);
            naHaoduanService.save(naHaoduan);
        }
        String parameter = cardId + ":" + priceId + ":" + paytypeId + ":" + feetype + ":" + phone;
        DetachedCriteria dc = DetachedCriteria.forClass(Paychannel.class);
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        dc.add(Restrictions.eq("paytypeId", paytypeId));
        dc.add(Restrictions.eq("feetype", feetype));
        dc.add(Restrictions.or(Restrictions.like("province", province, MatchMode.ANYWHERE), Restrictions.eq("province", "ALL")));
        dc.addOrder(Order.asc("sort"));
        List<Paychannel> list = paychannelDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            Iterator<Paychannel> iterator = list.iterator();
            while (iterator.hasNext()) {
                Paychannel paychannel = iterator.next();

                // 空中地网北京
                if (StringUtils.contains(propertyUtils.getProperty("kz.sms.spnum.bj"), paychannel.getSpnum())) {
                    try{
                        String tempType = propertyUtils.getProperty("kz.sms.spnum.bj.type"+paychannel.getFee());
                        String url = "http://202.108.24.55:8081/NewmobileNotify.jsp?mobile=" + phone + "&type=" + tempType;
                        LogEnum.DEFAULT.info("申请空中北京地网短信指令, 参数：" + url);
                        HttpClient client = new DefaultHttpClient();
                        HttpGet get = new HttpGet(url);
                        HttpResponse response = client.execute(get);
                        String body = StringUtils.trim(IOUtils.toString(response.getEntity().getContent(), "GBK"));
                        LogEnum.DEFAULT.info("申请空中北京地网短信指令, 返回：" + response.getStatusLine().getStatusCode() + "--" + body);
                        if (StringUtils.startsWith(body, "true") || StringUtils.startsWith(body, "True")) {
                            String[] strs = body.split(":");
                            paychannel.setMsg(strs[6]);
                        } else if (StringUtils.startsWith(body, "1")) {
                            LogEnum.DEFAULT.error("空中北京地网短信获取通道失败" + parameter + ", 返回结果：" + body);
                            paychannel.setErrorFlg(11);
                            paychannel.setErrorMessage("您当月订购次数已满，请下个月继续使用该业务。");
                        } else {
                            LogEnum.DEFAULT.error("空中北京地网短信获取通道失败" + parameter + ", 返回结果：" + body);
                            paychannel.setErrorFlg(10);
                            paychannel.setErrorMessage("该号码所属省份无可用通道，请选择其它方式。");
                        }

                    }catch (Exception e){
                        LogEnum.DEFAULT.error("空中北京地网短信获取通道异常：" + parameter + "，异常信息： " + e);
                        iterator.remove();
                    }
                } else if (StringUtils.contains(propertyUtils.getProperty("kz.sms.spnum"), paychannel.getSpnum())) {
                    try {
                        if (StringUtils.isNotBlank(msg)) {
                            paychannel.setMsg(msg);
                        } else {
                            if (!checkByKz(phone)) {
                                iterator.remove();
                                LogEnum.DEFAULT.warn("checkByKz 该用户暂时不能使用该业务" + phone);
                                continue;
                            }

                            HttpClient client = new DefaultHttpClient();
                            String url = propertyUtils.getProperty("kz.sms.url");
                            String type = propertyUtils.getProperty("kz.sms.type." + cardId + "." + priceId);
                            LogEnum.DEFAULT.info("申请空中网短信指令, 参数：" + url + "?mobile=" + phone + "&type=" + type);
                            HttpGet get = new HttpGet(url + "?mobile=" + phone + "&type=" + type);
                            HttpResponse response = client.execute(get);
                            String body = StringUtils.trim(IOUtils.toString(response.getEntity().getContent(), "GBK"));
                            LogEnum.DEFAULT.info("申请空中网短信指令, 返回：" + response.getStatusLine().getStatusCode() + "--" + body);
                            if (StringUtils.startsWith(body, "true") || StringUtils.startsWith(body, "True")) {
                                String[] strs = body.split(":");
                                paychannel.setMsg(strs[6]);
                            } else if (StringUtils.startsWith(body, "-1")) {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                paychannel.setErrorFlg(9);
                                paychannel.setErrorMessage("您有未完成订单，请先完成之前的订单。");
                            } else if (StringUtils.startsWith(body, "0")) {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                paychannel.setErrorFlg(10);
                                paychannel.setErrorMessage("该号码所属省份无可用通道，请选择其它方式。");
                            } else if (StringUtils.startsWith(body, "1")) {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                paychannel.setErrorFlg(11);
                                paychannel.setErrorMessage("您当月订购次数已满，请下个月继续使用该业务。");
                            } else if (StringUtils.startsWith(body, "2")) {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                paychannel.setErrorFlg(12);
                                paychannel.setErrorMessage("您已经定购过10元业务，本月不能再订购10元业务。");
                            } else if (StringUtils.startsWith(body, "3")) {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                paychannel.setErrorFlg(13);
                                paychannel.setErrorMessage("您已经定购过16元业务，本月不能再订购16元业务。");
                            } else {
                                LogEnum.DEFAULT.error("空中短信获取通道失败" + parameter + ", 返回结果：" + body);
                                iterator.remove();
                            }
                        }
                    } catch (Exception e) {
                        LogEnum.DEFAULT.error("空中短信获取通道异常：" + parameter + "，异常信息： " + e);
                        iterator.remove();
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Paychannel getPayChannel(int channelid, int cardId, int priceId) {
        DetachedCriteria dc = DetachedCriteria.forClass(Paychannel.class);
        dc.add(Restrictions.eq("paytypeId", channelid));
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        List<Paychannel> list = paychannelDao.findByCriteria(dc);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    private boolean checkByKz(String phoneNumber) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://61.135.202.118:8083/indexForDB.jsp?mobile=" + phoneNumber + "&channel=XZHJW000S00");
            HttpResponse response = client.execute(get);
            String body = StringUtils.trim(IOUtils.toString(response.getEntity().getContent(), "GBK"));
            if (StringUtils.equals("-2", body)) {
                LogEnum.DEFAULT.error("调用空中判断用户接口正常 {}", phoneNumber);
                return true;
            } else {
                LogEnum.DEFAULT.warn("blackUser of kz : " + phoneNumber + " error code :" + body);
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用空中判断用户接口异常 {}", e);
            return true;
        }
        return false;
    }

    private LtPcResult getWoResult(String phone, int fee) throws IOException {
        LtPcResult pcResult = new LtPcResult();
        ChannelVo chanels = new ChannelVo();
        String appKey = propertyUtils.getProperty("wo.appKey");
        String appSecret = propertyUtils.getProperty("wo.appSecret");
        String appToken = propertyUtils.getProperty("wo.appToken");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String outTradeNo = IdUtils.idGenerator("gw");
            map.put("outTradeNo", outTradeNo);
            map.put("paymentUser", phone); //13292640301  15564678648 15652651321  13141151451
            map.put("paymentAcount", "001");
            map.put("paymentType", 0);
            map.put("subject", "金币");
            map.put("totalFee", fee / 100);
            String jsonBody = JSON.toJSONString(map);
            LogEnum.DEFAULT.info("请求计费2.0: " + jsonBody);
            HttpClient httpClient = HttpUtils.getSecuredHttpClient();

            HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/paymentcodesms/v2.0");
            StringEntity se = new StringEntity(jsonBody, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.addHeader("Authorization", "appKey=\"" + appKey + "\",token=\"" + appToken + "\"");
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("accept", "application/json;charset=UTF-8");
            post.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(post);
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            LogEnum.DEFAULT.info(phone + " 【WO+】联通申请指令返回1:" + body);

            JSONObject result = JSON.parseObject(body);
            if (StringUtils.equals("0", result.getString("resultCode"))) {
                chanels.setPcflag(true);
                chanels.setSid(outTradeNo);
                pcResult.setChanels(chanels);
                return pcResult;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("调用WO+接口异常：" + e.toString());
        }
        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    private LtPcResult getKzResult(String phone, int fee, HttpClient httpClient, ChannelVo chanels) throws IOException {
        LtPcResult pcResult = new LtPcResult();

        String resource = propertyUtils.getProperty("pc.lt.pay.url") +
                "?uid=" + phone + "&bid=" + fee;
        LogEnum.DEFAULT.info(resource);
        HttpGet get = new HttpGet(resource);
        HttpResponse httpResponse = httpClient.execute(get);
        if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            String resultCode = reader.readLine();
            LogEnum.DEFAULT.info(phone + " 【空中】联通申请指令返回1:" + resultCode);
            if (StringUtils.isNotBlank(resource) && resultCode.indexOf(":") >= 0) {
                resultCode = resultCode.split(":")[1];
            }
            if ("1".equals(resultCode)) {
                chanels.setPcflag(true);
                String temp = reader.readLine();
                LogEnum.DEFAULT.info(phone + " 联通申请指令返回2:" + temp);
                String sid = temp.split(":")[1];
                chanels.setSid(sid);
                pcResult.setChanels(chanels);
                return pcResult;
            } else {
                int i = 2;
                String body = reader.readLine();
                while (StringUtils.isNotBlank(body)) {
                    LogEnum.DEFAULT.info(phone + " 【翼光】联通申请指令返回" + i++ + ":" + body);
                    body = reader.readLine();
                }
            }
        }
        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    private LtPcResult getYgResult(String phone, int fee, HttpClient httpClient, ChannelVo chanels, Paychannel paychannel) throws IOException {
        LtPcResult pcResult = new LtPcResult();

        HttpClient client = new DefaultHttpClient();
        String sid = IdUtils.idGenerator("yg");
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("cpid", "1011"));
        formparams.add(new BasicNameValuePair("orderid", sid));
        formparams.add(new BasicNameValuePair("serviceid", paychannel.getMsg()));
        formparams.add(new BasicNameValuePair("mobile", phone));
        formparams.add(new BasicNameValuePair("operator", "2"));
        DateTime dateTime = new DateTime();
        String time = dateTime.toString("yyyyMMddHHmmss");
        formparams.add(new BasicNameValuePair("datetime", time));
        String str = sid + "1011" + paychannel.getMsg() + phone + "2" + time + "43c802069b46c70504f631306a2b9e5b";
        formparams.add(new BasicNameValuePair("sign", Encrypt.md532(str)));
        formparams.add(new BasicNameValuePair("subject", ""));
        formparams.add(new BasicNameValuePair("username", ""));
        formparams.add(new BasicNameValuePair("description", ""));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");

        LogEnum.DEFAULT.info(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("http://58.67.196.166:17200/rest/payment/api/1.0/sendsmscode");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + " 【翼光】联通申请指令返回1:" + body);
        JSONObject result = JSON.parseObject(body.replace("\uFEFF\uFEFF", ""));

        if ("0000".equals(result.getString("code"))) {
            chanels.setPcflag(true);
            chanels.setSid(sid);
            pcResult.setChanels(chanels);
            return pcResult;
        }
        chanels.setPcflag(false);
        pcResult.setChanels(chanels);
        return pcResult;
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }

}
