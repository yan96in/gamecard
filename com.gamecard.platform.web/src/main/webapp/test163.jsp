<%@ page import="com.sp.platform.util.IdUtils" %>
<%@ page import="com.sp.platform.util.LogEnum" %>
<%@ page import="com.sp.platform.web.sign.ExampleForRSA" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.joda.time.DateTime" %>
<%
    String privateKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100affa59c9083cdef23c8a58eb100fa62ba1d479443b9ab5b27201e29e71206ef4ff0e1505bd794170ff10d311a86d0ef6d6b6e3b131b241177b7270dbacdb4268e8f901ce6d5f645cc12c4cdae357953ad3d5e1b07c913077a0dfeb8cc3357fd82d2c9b921947481ffef760cee0c06f9d7ede55ebe0abfb79644ab277f45df5e102030100010281806a552bcb7d5eca98345ca43de5068fb1eda7255f4564c6457cfe06a1165a32b1772c5b278cc5a7f54f2a2b5f9015d5e4f0f8b1bc6ffaf5b9eaadcbe3f11ecb90f794e8df73e1dbc2965005d01fce8c741bccc3e4a69df478fbdd7cb9db305f22ad31da6c094ce53b46d29f53afa338d82c261d45dd35772e31e19d7e46365101024100e38bae879d6bca7235b47a38f670da96199de5f7d9930bd30697b0763fff657282a2dec7fa9cb8b2b09cbec78b1ac4b05a5c4d4263ee508359d890437d1f87f5024100c5fbdad9d040de76e44d2123638c8efa1c5f4a527aa2f21f460b7595950a13a779609ff1eafea97089bd2a856d7eedabd7dfc9b2509ac8359826f0de12fc7ebd02410087e5f3adb11a2085c7876b7bbbcbb8f0ee89bc4023a4cbea13c4e2b22d14b69d066289957c1abfc090f47c88a68ead2bfa3ba834df840477fe5ff60128a87d4502401ed112029f58c987e6075f661bc3b0299c2af4ed9bab2b56d4d749c92bb6b2d279a121a44d38b07514eb7de7e0e045ec5ea0b5cb6ca27b11648a83297df9115d02400910d9636a964db66f5b5e538eea4f1c5bf3fc32beb1cabb1531a08d35bb67d2d9608f7709251e3629e51810d0620a95fc0f9f2655a88ba1412bbf62942ee9ad";
    String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100aead2fa0c97106c8dc4a72ed496b42fab8deff4c130d430fc382272f7ed1315ebbacd734cf2f98d27bf7ce8c0aacb0ee763e56b4525ba020081acd89ff1cb8c45afb604a3b2a8bae51fb815b0bde4144e291d6a86c028db16f6e4467f01bf78921c656014ed01f485713f5d2173faae6996db04a59c83924b12e995f8fb2388d0203010001";

    String orderId = IdUtils.idGenerator("wy");
    DateTime date = new DateTime();
    String d = date.toString("yyyyMMddHHmmss");
    String site_id = "95212";
    String user_id = "61981700";
    String order_id = orderId;
    String order_time = d;
    String urs = "jayven";
    String reason = "1";
    String pts = "100";
    StringBuilder builder = new StringBuilder();
    builder.append("?site_id=").append(site_id);
    builder.append("&user_id=").append(user_id);
    builder.append("&order_id=").append(orderId);
    builder.append("&order_time=").append(d);
    builder.append("&urs=").append(urs);
    builder.append("&reason=").append(reason);
    builder.append("&pts=").append(pts);

    String key = site_id + user_id + order_id + order_time + urs + reason + pts;
    LogEnum.DEFAULT.info(key);
    //私钥加密过程
    ExampleForRSA temp = new ExampleForRSA();

    String signature = temp.generateSHA1withRSASigature(key);

    builder.append("&sign=").append(signature);
    LogEnum.DEFAULT.info(builder.toString());

//    String url = "http://esales.163.com/script/interface/dc_input";
    String url = "https://esales.dev.webapp.163.com:8043/script/interface/dc_input";

    HttpClient httpClient = com.sp.platform.util.HttpUtils.getSecuredHttpClient();
    HttpGet get = new HttpGet(url + builder.toString());
    HttpResponse httpResponse = httpClient.execute(get);
    String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
    LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
%>