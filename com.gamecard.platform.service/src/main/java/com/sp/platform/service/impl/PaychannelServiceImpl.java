package com.sp.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaychannelDao;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.service.PcCardLogService;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.PcVo1;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
        chanels.setPcflag(!CollectionUtils.isEmpty(paychannels));
        return chanels;
    }

    public ChannelVo sendPcCode(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        int fee = 0;
        String resultCode = null;
        String resultMessage = null;
        String sid = null;
        List<Paychannel> paychannels = find(cardId, priceId, paytypeId, 1, province, phone, null);
        if (!CollectionUtils.isEmpty(paychannels)) {
            try {
                Paychannel paychannel = paychannels.get(0);
                chanels.setChannelId(paychannel.getId());
                fee = paychannel.getFee();
                HttpClient httpClient = new DefaultHttpClient();
                if (paytypeId == 19) {
                    String resource = propertyUtils.getProperty("pc.pay.url") +
                            "?uid=" + phone + "&bid=" + fee + "&ext=test";
                    if (StringUtils.indexOf(propertyUtils.getProperty("pc.province.qzsj"), HaoduanCache.getProvince(phone)) >= 0) {
                        resource = propertyUtils.getProperty("pc.province.qzsj.url") +
                                "?uid=" + phone + "&bid=" + fee + "&ext=test";
                    } else if (StringUtils.indexOf(propertyUtils.getProperty("pc.province.gfyx"), HaoduanCache.getProvince(phone)) >= 0) {
                        resource = propertyUtils.getProperty("pc.province.gfyx.url") +
                                "?uid=" + phone + "&bid=" + fee + "&ext=test";
                    }
                    LogEnum.DEFAULT.info(resource);
                    HttpGet get = new HttpGet(resource);
                    HttpResponse httpResponse = httpClient.execute(get);
                    if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                        String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                        LogEnum.DEFAULT.info(phone + " 移动申请指令返回1:" + body);
                        PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
                        resultCode = resultVo.getResultCode();
                        resultMessage = resultVo.getResultMsg();
                        sid = resultVo.getSid();
                        if ("0".equals(resultCode)) {
                            chanels.setPcflag(true);
                            chanels.setSid(sid);
                            return chanels;
                        } else {
                            chanels.setResultCode(resultCode);
                        }
                    }
                } else if (paytypeId == 20) {
                    String resource = propertyUtils.getProperty("pc.lt.pay.url") +
                            "?uid=" + phone + "&bid=" + fee;
                    LogEnum.DEFAULT.info(resource);
                    HttpGet get = new HttpGet(resource);
                    HttpResponse httpResponse = httpClient.execute(get);
                    if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

                        resultCode = reader.readLine();
                        LogEnum.DEFAULT.info(phone + " 联通申请指令返回1:" + resultCode);
                        if (StringUtils.isNotBlank(resource) && resultCode.indexOf(":") >= 0) {
                            resultCode = resultCode.split(":")[1];
                        }
                        if ("1".equals(resultCode)) {
                            chanels.setPcflag(true);
                            String temp = reader.readLine();
                            LogEnum.DEFAULT.info(phone + " 联通申请指令返回2:" + temp);
                            sid = temp.split(":")[1];
                            chanels.setSid(sid);
                            return chanels;
                        } else {
                            int i = 2;
                            String body = reader.readLine();
                            while (StringUtils.isNotBlank(body)) {
                                LogEnum.DEFAULT.info(phone + " 联通申请指令返回" + i++ + ":" + body);
                                body = reader.readLine();
                            }
                        }
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

    public List<Paychannel> find(int cardId, int priceId, int paytypeId, int feetype, String province, String phone, String msg) {
        String parameter = cardId + ":" + priceId + ":" + paytypeId + ":" + feetype + ":" + phone;
        DetachedCriteria dc = DetachedCriteria.forClass(Paychannel.class);
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        dc.add(Restrictions.eq("paytypeId", paytypeId));
        dc.add(Restrictions.eq("feetype", feetype));
        dc.add(Restrictions.or(Restrictions.like("province", province, MatchMode.ANYWHERE), Restrictions.eq("province", "ALL")));
        List<Paychannel> list = paychannelDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            Iterator<Paychannel> iterator = list.iterator();
            while (iterator.hasNext()) {
                Paychannel paychannel = iterator.next();

                if (StringUtils.contains(propertyUtils.getProperty("kz.sms.spnum"), paychannel.getSpnum())) {
                    try {
                        if (StringUtils.isNotBlank(msg)) {
                            paychannel.setMsg(msg);
                        } else {
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
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
