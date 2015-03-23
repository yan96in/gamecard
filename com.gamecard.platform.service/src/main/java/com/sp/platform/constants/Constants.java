package com.sp.platform.constants;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/2/8.
 */
public class Constants {
    public static final Map<String, Integer> channelMap = new HashMap<String, Integer>();

    static {
        initKzChannelConfig();
    }

    private static void initKzChannelConfig() {
        channelMap.put("54282", 54);
        channelMap.put("84482", 55);
        channelMap.put("YXX82", 56);
        channelMap.put("54252", 57);
        channelMap.put("84452", 58);
        channelMap.put("YXX52", 59);

        channelMap.put("54251", 60);
        channelMap.put("84451", 61);
        channelMap.put("YXX51", 62);
        channelMap.put("54281", 63);
        channelMap.put("84481", 64);
        channelMap.put("YXX81", 65);
        //空中五元
        channelMap.put("54283", 66);
        channelMap.put("84483", 67);
        channelMap.put("YXX83", 68);

        //天下5元
        channelMap.put("54284", 69);
        channelMap.put("84484", 70);
        channelMap.put("YXX84", 71);
    }

    public static Integer getChannelId(String key){
        if(StringUtils.isBlank(key)){
            return 0;
        }
        Integer channelId = channelMap.get(key);
        if(channelId == null){
            return 0;
        }
        return channelId;
    }

    public static void main(String[] args) {

        initKzChannelConfig();

        System.out.println(channelMap.size());
    }
}
