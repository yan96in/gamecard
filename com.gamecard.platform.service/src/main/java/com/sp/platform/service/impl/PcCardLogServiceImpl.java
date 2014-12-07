package com.sp.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.sp.platform.common.Constants;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.PcCardLogDao;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.service.CardPasswordService;
import com.sp.platform.service.PcCardLogService;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.PcVo1;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
        return null;
    }

    @Override
    public PcCardLog getPcCard(int cardId, int priceId, String phone, String code, String sid, int paytypeId) throws Exception {
        DetachedCriteria dc = DetachedCriteria.forClass(PcCardLog.class);
        dc.add(Restrictions.eq("sid", sid));
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("mobile", phone));
        List<PcCardLog> list = pcCardLogDao.findByCriteria(dc);
        String resultCode = null;
        if(CollectionUtils.isNotEmpty(list)){
            PcCardLog pcCardLog = list.get(0);
            if(StringUtils.isNotBlank(pcCardLog.getCardno())){
                return pcCardLog;
            }
            HttpClient httpClient = new DefaultHttpClient();
            if(paytypeId == 19){
                String resource = propertyUtils.getProperty("pc.order.url") +
                        "?sid=" + sid + "&vid=" + code + "&payid=test&amount=" + pcCardLog.getFee();
                HttpGet get = new HttpGet(resource);
                HttpResponse httpResponse = httpClient.execute(get);
                String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                LogEnum.DEFAULT.info(body);
                PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
                resultCode = resultVo.getResultCode();
                sid = resultVo.getSid();
            } else if(paytypeId == 20){
                String resource = propertyUtils.getProperty("pc.lt.order.url") +
                        "?sid=" + sid + "&vcode=" + code;
                HttpGet get = new HttpGet(resource);
                HttpResponse httpResponse = httpClient.execute(get);
                String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                LogEnum.DEFAULT.info(body);
                PcVo1 resultVo = JSON.parseObject(body, PcVo1.class);
                resultCode = resultVo.getResultCode();
            }
            pcCardLog.setEtime(new Date());
            if ((propertyUtils.getProperty("pc.success.result", "200000").equals(resultCode))
                    || (propertyUtils.getProperty("pc.lt.success.result", "200000").equals(resultCode))) {
                cacheCheckUser.addCallerFee(pcCardLog.getMobile() + Constants.split_str + "pc", pcCardLog.getFee());
                cacheCheckUser.addCalledProvinceFee(pcCardLog.getProvince() + Constants.split_str + "pc", pcCardLog.getFee(), false);

                CardPassword card = cardPasswordService.getUserCard(cardId, priceId);
                if(card == null){
                    pcCardLog.setStatus(3);
                    pcCardLogDao.save(pcCardLog);
                    throw new RuntimeException("取卡失败 sid=" + sid );
                }

                pcCardLog.setStatus(2);
                pcCardLog.setCardno(card.getCardno());
                pcCardLog.setCardpwd(card.getPassword());
                pcCardLogDao.save(pcCardLog);
                return pcCardLog;
            } else {
                pcCardLog.setResultcode(resultCode);
                pcCardLog.setStatus(0);
                pcCardLogDao.save(pcCardLog);
                throw new RuntimeException("计费失败 sid=" + sid );
            }
        } else {
            throw new RuntimeException("参数有误 sid=" + sid );
        }
    }
}
