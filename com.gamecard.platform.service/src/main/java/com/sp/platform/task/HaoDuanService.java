package com.sp.platform.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.entity.Haoduan;
import com.sp.platform.entity.NaHaoduan;
import com.sp.platform.service.HaoduanService;
import com.sp.platform.service.NaHaoduanService;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * User: yangl
 * Date: 13-6-23 下午2:37
 */
@Service
public class HaoDuanService {
    @Autowired
    private NaHaoduanService naHaoduanService;
    @Autowired
    private HaoduanService haoduanService;
    @Autowired
    private PropertyUtils propertyUtils;
    private HttpClient client = new DefaultHttpClient();

    private final static String url = "http://api.showji.com/Locating/www.showji.co.m.aspx?output=json&callback=querycallback&m=";

    public void processNaHaoduan() {
        List<NaHaoduan> list = naHaoduanService.getAll();
        if (list != null && list.size() > 0) {
            for (NaHaoduan naHaoduan : list) {
                getHaoduan(naHaoduan);
            }
        }
    }

    private void getHaoduan(NaHaoduan naHaoduan) {
        if (!HaoduanCache.NA.equals(HaoduanCache.getProvince(naHaoduan.getCaller()))) {
            LogEnum.DEFAULT.info("已经入库" + StringUtils.left(naHaoduan.getCaller(), 7));
            naHaoduanService.delete(naHaoduan.getId());
            return;
        }
        if(StringUtils.contains(naHaoduan.getCaller(), " ") || StringUtils.contains(naHaoduan.getCaller(), "-")){
            naHaoduanService.delete(naHaoduan.getId());
            return;
        }
        if(StringUtils.length(naHaoduan.getCaller()) != 13){
            naHaoduanService.delete(naHaoduan.getId());
            return;
        }
        try {
            HttpGet get = new HttpGet(propertyUtils.getProperty("haoduan_url") + StringUtils.left(naHaoduan.getCaller(), 7));
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            List<String> list = IOUtils.readLines(entity.getContent(), "UTF-8");
            for (String string : list) {
                try {
                    string = string.trim();
                    if (StringUtils.isBlank(string)) {
                        continue;
                    }

                    string = StringUtils.substring(string, 14);
                    string = StringUtils.substring(string, 0, string.length() - 2);
                    JSONObject json = JSON.parseObject(string);

                    if (StringUtils.equals("false", json.getString("QueryResult"))) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(json.getString("Province"))) {
                        Haoduan haoduan1 = new Haoduan();
                        haoduan1.setProvince(json.getString("Province"));
                        haoduan1.setCity(json.getString("City"));
                        haoduan1.setCode(json.getString("Mobile"));
                        if (HaoduanCache.NA.equals(HaoduanCache.getProvince(naHaoduan.getCaller()))) {
                            haoduanService.save(haoduan1);
                            HaoduanCache.setHaoduan(haoduan1);
                        }
                        naHaoduanService.delete(naHaoduan.getId());
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.contains("159 8894 967", " "));
        System.out.println(StringUtils.contains("157-5779-231", "-"));
    }
}
