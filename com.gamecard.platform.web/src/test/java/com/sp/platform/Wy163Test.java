package com.sp.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.sign.EsalesRAS;
import org.junit.Test;

/**
 * Created by yanglei on 15/11/28.
 */
public class Wy163Test {
    @Test
    public void test(){
        String body = "{\"status\": \"OK\", \"to_urs\": \"jayven@163.com\", \"sign_fields\": [\"status\", \"to_urs\", \"_\"], \"_\": \"413812\", \"sign\": \"9a6b1d008b8723446afa622cbeac3b18882e5bf8e93dc1a3aa0600119bc85f90d3c8bfedd66ebdc24661078a3a5c6407fdc0bfcc75bec1b4ad84180635f171db2ea67f0b5007627827eec2a3a48643fa3f2b82f886ceb91bfa2b0ce697b973be087b32e9a6157fc4701eae11b30ca36ec815404f4c582a82d0670e980c10d778\"}";
        JSONObject object = JSON.parseObject(body);
        JSONArray jsonArray = object.getJSONArray("sign_fields");

        Object[] fields = jsonArray.toArray();
        String pubKey = "30819f300d06092a864886f70d010101050003818d0030818902818100aead2fa0c97106c8dc4a72ed496b42fab8deff4c130d430fc382272f7ed1315ebbacd734cf2f98d27bf7ce8c0aacb0ee763e56b4525ba020081acd89ff1cb8c45afb604a3b2a8bae51fb815b0bde4144e291d6a86c028db16f6e4467f01bf78921c656014ed01f485713f5d2173faae6996db04a59c83924b12e995f8fb2388d0203010001";

        String source = "";
        for (Object str : fields){
            source = source + object.get(str).toString();
        }
        String sign_org = object.get("sign").toString();
        EsalesRAS ras = new EsalesRAS();
        System.out.println(ras.verifySHA1withRSASigature(sign_org, source, pubKey));
    }
}
