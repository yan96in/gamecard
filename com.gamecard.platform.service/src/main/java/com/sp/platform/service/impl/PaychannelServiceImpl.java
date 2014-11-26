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
import org.apache.http.HttpResponse;
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

    public ChannelVo findChannels(int cardId, int priceId, int paytypeId, String province) {
        ChannelVo chanels = new ChannelVo();
        chanels.setChannels1(find(cardId, priceId, paytypeId, 1, province));
        chanels.setChannels2(find(cardId, priceId, paytypeId, 2, province));
        return chanels;
    }

    public ChannelVo findPcChannels(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        List<Paychannel> paychannels = find(cardId, priceId, paytypeId, 1, province);
        chanels.setPcflag(!CollectionUtils.isEmpty(paychannels));
        return chanels;
    }

    public ChannelVo sendPcCode(int cardId, int priceId, int paytypeId, String province, String phone) {
        ChannelVo chanels = new ChannelVo();
        int fee = 0;
        String resultCode = null;
        String resultMessage = null;
        String sid = null;
        List<Paychannel> paychannels = find(cardId, priceId, paytypeId, 1, province);
        if (!CollectionUtils.isEmpty(paychannels)) {
            try {
                Paychannel paychannel = paychannels.get(0);
                chanels.setChannelId(paychannel.getId());
                fee = paychannel.getFee();
                HttpClient httpClient = new DefaultHttpClient();
                String resource = propertyUtils.getProperty("pc.pay.url") +
                        "?uid=" + phone + "&bid=" + fee + "&ext=test";
                HttpGet get = new HttpGet(resource);
                HttpResponse httpResponse = httpClient.execute(get);
                String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                LogEnum.DEFAULT.info(body);
                PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
                resultCode = resultVo.getResultCode();
                resultMessage = resultVo.getResultMsg();
                sid = resultVo.getSid();
                if ("0".equals(resultCode)) {
                    chanels.setPcflag(true);
                    chanels.setSid(sid);
                    return chanels;
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
                pcCardLog.setFee(fee);
                pcCardLog.setResultcode(resultCode);
                pcCardLog.setResultmsg(resultMessage);
                pcCardLog.setSid(sid);
                pcCardLog.setStatus(1);
                pcCardLogService.save(pcCardLog);
            }
        }
        chanels.setPcflag(false);
        return chanels;
    }

    public List<Paychannel> find(int cardId, int priceId, int paytypeId, int feetype, String province) {
        DetachedCriteria dc = DetachedCriteria.forClass(Paychannel.class);
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        dc.add(Restrictions.eq("paytypeId", paytypeId));
        dc.add(Restrictions.eq("feetype", feetype));
        dc.add(Restrictions.or(Restrictions.like("province", province, MatchMode.ANYWHERE), Restrictions.eq("province", "ALL")));
        return paychannelDao.findByCriteria(dc);
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
