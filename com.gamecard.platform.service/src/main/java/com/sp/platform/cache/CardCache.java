package com.sp.platform.cache;

import com.sp.platform.common.Constants;
import com.sp.platform.entity.Card;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.Price;
import com.sp.platform.service.CardService;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.service.PriceService;
import com.sp.platform.timer.AbstractBaseTimer;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: yangl
 * Date: 13-5-25 下午10:52
 */
@Service
public class CardCache extends AbstractBaseTimer {
    /**
     * 被叫号码：通道实体类
     */
    private static Map<Integer, Card> cardMap = new ConcurrentHashMap<Integer, Card>();
    private static Map<Integer, Price> priceMap = new ConcurrentHashMap<Integer, Price>();

    @Autowired
    private CardService cardService;
    @Autowired
    private PriceService priceService;

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Map<Integer, Card> temp;
        Map<Integer, Price> temp2;
        List<Card> cards = cardService.getAll();
        if (cards != null && cards.size() > 0) {
            temp = new HashMap<Integer, Card>();
            for (Card bean : cards) {
                temp.put(bean.getId(), bean);
            }
            cardMap.clear();
            cardMap.putAll(temp);
        }
        LogEnum.TEMP.info("缓存中共存放{}条Card信息", cardMap.size());

        List<Price> prices = priceService.getAll();
        if (prices != null && prices.size() > 0) {
            temp2 = new HashMap<Integer, Price>();
            for (Price bean : prices) {
                temp2.put(bean.getId(), bean);
            }
            priceMap.clear();
            priceMap.putAll(temp2);
        }
        LogEnum.TEMP.info("缓存中共存放{}条Price信息", priceMap.size());
    }

    public static Card getCard(Integer key) {
        if (key == null) {
            return null;
        }
        return cardMap.get(key);
    }

    public static Price getPrice(Integer key) {
        if (key == null) {
            return null;
        }
        return priceMap.get(key);
    }
}
