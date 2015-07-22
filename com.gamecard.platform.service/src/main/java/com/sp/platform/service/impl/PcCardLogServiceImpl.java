package com.sp.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.common.Constants;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.PcCardLogDao;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.service.CardPasswordService;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.service.PcCardLogService;
import com.sp.platform.service.sp.DxService;
import com.sp.platform.service.sp.KzYdService;
import com.sp.platform.service.sp.SpYdService;
import com.sp.platform.service.sp.YlYdService;
import com.sp.platform.util.*;
import com.sp.platform.vo.PcBillVo;
import com.sp.platform.vo.PcVo1;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 下午10:33
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PcCardLogServiceImpl implements PcCardLogService {
    @Autowired
    private PcCardLogDao pcCardLogDao;
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private CardPasswordService cardPasswordService;
    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private PaychannelService paychannelService;
    @Autowired
    private SpYdService spYdService;
    @Autowired
    private KzYdService kzYdService;
    @Autowired
    private YlYdService ylYdService;
    @Autowired
    private DxService dxService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PcCardLog get(int id) {
        return pcCardLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        pcCardLogDao.delete(id);
    }

    @Override
    public void save(PcCardLog object) {
        pcCardLogDao.save(object);
    }

    @Override
    public List<PcCardLog> getAll() {
        return pcCardLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(PcCardLog.class);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("btime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("etime", DateTime.parse(pageView.getEtime(), format).toDate()));
        if (StringUtils.isNotBlank(pageView.getCaller())) {
            dc.add(Restrictions.eq("mobile", pageView.getCaller()));
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            dc.add(Restrictions.eq("sid", pageView.getCalled()));
        }
        if (2 == pageView.getType()) {
            dc.add(Restrictions.eq("status", pageView.getType()));
        }
        if (pageView.getSpid() > 0) {
            dc.add(Restrictions.eq("channelid", pageView.getSpid()));
        }
        dc.addOrder(Order.desc("btime"));
        return pcCardLogDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public PcCardLog getPcCard(int cardId, int priceId, String phone, String code, String sid, int paytypeId, int channeType) throws Exception {
        DetachedCriteria dc = DetachedCriteria.forClass(PcCardLog.class);
        dc.add(Restrictions.eq("sid", sid));
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("mobile", phone));
        List<PcCardLog> list = pcCardLogDao.findByCriteria(dc);
        String resultCode = null;
        if (CollectionUtils.isNotEmpty(list)) {
            PcCardLog pcCardLog = list.get(0);
            if (StringUtils.isNotBlank(pcCardLog.getCardno())) {
                return pcCardLog;
            }
            boolean isOK = false;
            if (paytypeId == 19) {
                if (channeType == 5) {
                    resultCode = kzYdService.commitPaymentCode(phone, code, sid, channeType, pcCardLog);
                } else if (channeType == 4) {
                    resultCode = ylYdService.commitPaymentCode(phone, code, sid, channeType, pcCardLog);
                } else if (channeType == 3) {
                    resultCode = spYdService.commitPaymentCode(phone, code, sid, channeType, pcCardLog);
                }
                isOK = (propertyUtils.getProperty("pc.success.result", "200000").equals(resultCode));
            } else if (paytypeId == 20) {
                resultCode = commitPaymentCode(phone, code, sid, channeType, pcCardLog);
                isOK = (propertyUtils.getProperty("pc.lt.success.result", "200000").equals(resultCode));
            } else if (paytypeId == 21) {
                if (channeType == 6) {
                    resultCode = dxService.commitPaymentCode(phone, code, sid, channeType, pcCardLog);
                }
                isOK = (propertyUtils.getProperty("pc.dx.success.result", "00").equals(resultCode));
            }
            pcCardLog.setEtime(new Date());
            if (isOK) {
                cacheCheckUser.addCallerFee(pcCardLog.getMobile() + Constants.split_str + "pc" + channeType, pcCardLog.getFee());
                cacheCheckUser.addCalledProvinceFee(pcCardLog.getProvince() + Constants.split_str + "pc" + paytypeId + channeType, pcCardLog.getFee(), false);
                cacheCheckUser.addCalledFee("pc" + paytypeId + channeType, pcCardLog.getFee(), false);

                CardPassword card = cardPasswordService.getUserCard(cardId, priceId);
                if (card == null) {
                    pcCardLog.setStatus(3);
                    pcCardLogDao.save(pcCardLog);
                    LogEnum.DEFAULT.warn(phone + "  提交验证码 " + "取卡失败 sid=" + sid + " cardId=" + cardId + " priceId=" + priceId);
                    return null;
                }
                XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));

                pcCardLog.setStatus(2);
                pcCardLog.setCardno(xdEncodeHelper.XDDecode(card.getCardno(), true));
                pcCardLog.setCardpwd(xdEncodeHelper.XDDecode(card.getPassword(), true));
                pcCardLogDao.save(pcCardLog);
                return pcCardLog;
            } else {
                pcCardLog.setResultcode(resultCode);
                pcCardLog.setStatus(0);
                pcCardLogDao.save(pcCardLog);
                LogEnum.DEFAULT.warn(phone + "  提交验证码 " + "计费失败 sid=" + sid + " resultCode=" + resultCode);
                return null;
            }
        } else {
            LogEnum.DEFAULT.error(phone + "  提交验证码 " + "参数有误 sid=" + sid + " cardId=" + cardId + " phone=" + phone);
            return null;
        }
    }

    private String commitPaymentCode(String phone, String code, String sid, int channeType, PcCardLog pcCardLog) throws IOException {
        if (channeType == 0) {
            return commitKzPaymentCode(phone, code, sid);
        } else if (channeType == 2) {
            return commitYgPaymentCode(phone, code, sid, pcCardLog);
        } else if (channeType == 1) {
            return commitWoPaymentCode(phone, code, sid, pcCardLog);
        } else if (channeType == 3) {
            return commitWoPaymentCode(phone, code, sid, pcCardLog);
        } else if (channeType == 4) {
            return commitWoPaymentCode(phone, code, sid, pcCardLog);
        } else if (channeType == 5) {
            return commitWoPaymentCode(phone, code, sid, pcCardLog);
        }
        return "null";
    }

    private String commitKzPaymentCode(String phone, String code, String sid) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String resource = propertyUtils.getProperty("pc.lt.order.url") +
                "?sid=" + sid + "&vcode=" + code;
        HttpGet get = new HttpGet(resource);
        HttpResponse httpResponse = httpClient.execute(get);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(phone + "  提交验证码 " + body + " LT sid=" + sid);
        PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
        return resultVo.getResultCode();
    }

    private String commitYgPaymentCode(String phone, String code, String sid, PcCardLog pcCardLog) throws IOException {
        Paychannel paychannel = paychannelService.getPayChannel(pcCardLog.getChannelid(), pcCardLog.getCardId(), pcCardLog.getPriceId());
        HttpClient client = new DefaultHttpClient();
        //设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("cpid", "1011"));
        formparams.add(new BasicNameValuePair("serviceid", paychannel.getMsg()));
        formparams.add(new BasicNameValuePair("orderid", sid));
        formparams.add(new BasicNameValuePair("mobile", phone));
        formparams.add(new BasicNameValuePair("operator", "2"));
        DateTime dateTime = new DateTime();
        String time = dateTime.toString("yyyyMMddHHmmss");
        formparams.add(new BasicNameValuePair("datetime", time));
        String str = sid + "1011" + paychannel.getMsg() + phone + "2" + time + "43c802069b46c70504f631306a2b9e5b";
        formparams.add(new BasicNameValuePair("sign", Encrypt.md532(str)));
        formparams.add(new BasicNameValuePair("smscode", code));
        formparams.add(new BasicNameValuePair("subject", ""));
        formparams.add(new BasicNameValuePair("username", ""));
        formparams.add(new BasicNameValuePair("description", ""));
        formparams.add(new BasicNameValuePair("showUrl", ""));
        formparams.add(new BasicNameValuePair("notifyUrl", ""));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
        System.out.println(IOUtils.toString(entity1.getContent()));
        //新建Http  post请求
        HttpPost httppost = new HttpPost("http://58.67.196.166:17200/rest/payment/api/1.0/smscharge");
        httppost.setEntity(entity1);

        //处理请求，得到响应
        HttpResponse httpResponse = client.execute(httppost);
        System.out.println(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        String body = IOUtils.toString(httpResponse.getEntity().getContent());
        LogEnum.DEFAULT.info(phone + "  提交验证码 " + " LT sid=" + sid + " body " + body);
        JSONObject object = JSON.parseObject(body.replace("\uFEFF\uFEFF", ""));
        String rcode = object.getString("code");
        if (StringUtils.equals("0000", rcode)) {
            return "0";
        }
        return rcode;
    }

    private String commitWoPaymentCode(String phone, String code, String sid, PcCardLog pcCardLog) throws IOException {

        String appKey = propertyUtils.getProperty("wo.appKey");
        String appSecret = propertyUtils.getProperty("wo.appSecret");
        String appToken = propertyUtils.getProperty("wo.appToken");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paymentUser", phone); //13292640301  15564678648 15562092589  13141151451
        map.put("paymentAcount", "001");
        map.put("outTradeNo", sid);
        map.put("subject", "金币");
        map.put("totalFee", pcCardLog.getFee() / 100);
        DateTime dateTime = new DateTime();
        map.put("timeStamp", dateTime.toString("yyyyMMddHHmmss"));
        map.put("paymentcodesms", Integer.parseInt(code));

        String key = appKey + "&" + appSecret;
        String signature = Encrypt.encryptHmacSha1(map, key);

        map.put("signType", "HMAC-SHA1");
        map.put("signature", signature);

        String jsonBody = JSON.toJSONString(map);
        LogEnum.DEFAULT.info("提交WO+计费2.0验证码: " + jsonBody);
        HttpClient httpClient = HttpUtils.getSecuredHttpClient();

        HttpPost post = new HttpPost("https://open.wo.com.cn/openapi/rpc/apppayment/v2.0");
        StringEntity se = new StringEntity(jsonBody, "UTF-8");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
        post.addHeader("Authorization", "appKey=\"" + appKey + "\",token=\"" + appToken + "\"");
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.addHeader("accept", "application/json;charset=UTF-8");
        post.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(post);
        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
        JSONObject object = JSON.parseObject(body);
        return object.getString("resultCode");
    }

    @Override
    public List<PcBillVo> getBillInfo(PageView pageView) {
        if (pageView.getCpid() == 1) {
            return pcCardLogDao.getProvinceBillInfo(pageView);
        } else {
            return pcCardLogDao.getBillInfo(pageView);
        }
    }

    @Override
    public boolean isValidUser(String phone) {
        DetachedCriteria dc = DetachedCriteria.forClass(PcCardLog.class);
        dc.add(Restrictions.eq("mobile", phone));

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(propertyUtils.getInteger("pc.caller.day.limit.time"));
        dc.add(Restrictions.gt("btime", dateTime.toDate()));
        dc.addOrder(Order.desc("btime"));
        List<PcCardLog> list = pcCardLogDao.findByCriteria(dc);
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        LogEnum.DEFAULT.info(phone + " 申请过快。" + list.get(0).getBtime());
        return false;
    }

    @Override
    public boolean isValidHour(String phone, String ext, String province) throws ParseException {
        int maxFee = propertyUtils.getInteger("pc.hour.limit." + ext, 0);
        DateTime dateTime = new DateTime();
        String currentHour = dateTime.toString("yyyy-MM-dd HH:00:00");
        if (maxFee > 0) {
            String sql = "select sum(fee) from tbl_user_pc_card_log where ext='" + ext + "' and btime >='" + currentHour + "' and status in(2,3)";
            Integer totalFee = jdbcTemplate.queryForObject(sql, Integer.class);
            if (totalFee == null) {
                return true;
            }
            if (totalFee / 100 >= maxFee) {
                LogEnum.DEFAULT.info(phone + " " + ext + " 单小时申请量过大 " + (totalFee / 100) + "  大于上限: " + maxFee);
                return false;
            }
        }

        maxFee = propertyUtils.getInteger("pc.province.hour.limit." + ext, 0);
        if (maxFee > 0) {
            String sql = "select sum(fee) from tbl_user_pc_card_log where ext='" + ext +
                    "' and btime >='" + currentHour + "' and status in(2,3) and province='" + province + "'";
            Integer totalFee = jdbcTemplate.queryForObject(sql, Integer.class);
            if (totalFee == null) {
                return true;
            }
            if (totalFee / 100 >= maxFee) {
                LogEnum.DEFAULT.info(phone + " " + ext + " " + province + " 省份单小时申请量过大 " + (totalFee / 100) + "  大于上限: " + maxFee);
                return false;
            }
        }

        maxFee = propertyUtils.getInteger("pc.max.day.fee." + ext, 0);
        if (maxFee > 0) {
            String sql = "select sum(fee) from tbl_user_pc_card_log where ext='" + ext +
                    "' and btime >='" + StringUtils.left(currentHour, 10) + "' and status in(2,3)";
            Integer totalFee = jdbcTemplate.queryForObject(sql, Integer.class);
            if (totalFee == null) {
                return true;
            }
            if (totalFee / 100 >= maxFee) {
                LogEnum.DEFAULT.info(phone + " " + ext + " " + province + " 日申请量过大 " + (totalFee / 100) + "  大于上限: " + maxFee);
                return false;
            }
        }

        return true;
    }
}
