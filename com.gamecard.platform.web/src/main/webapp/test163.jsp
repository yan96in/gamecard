<%@ page import="com.sp.platform.util.IdUtils" %>
<%@ page import="com.sp.platform.util.LogEnum" %>
<%@ page import="com.sp.platform.web.util.Encrypt" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.sp.platform.web.sign.RSA" %>
<%
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
    String signature = RSA.sign(key, "MIICXAIBAAKBgQCv+lnJCDze8jyKWOsQD6YrodR5RDuatbJyAeKecSBu9P8OFQW9eUFw/xDTEahtDvbWtuOxMbJBF3tycNus20Jo6PkBzm1fZFzBLEza41eVOtPV4bB8kTB3oN/rjMM1f9gtLJuSGUdIH/73YM7gwG+dft5V6+Cr+3lkSrJ39F314QIDAQABAoGAalUry31eypg0XKQ95QaPse2nJV9FZMZFfP4GoRZaMrF3LFsnjMWn9U8qK1+QFdXk8PixvG/69bnqrcvj8R7LkPeU6N9z4dvCllAF0B/OjHQbzMPkpp30ePvdfLnbMF8irTHabAlM5TtG0p9Tr6M42CwmHUXdNXcuMeGdfkY2UQECQQDji66HnWvKcjW0ejj2cNqWGZ3l99mTC9MGl7B2P/9lcoKi3sf6nLiysJy+x4saxLBaXE1CY+5Qg1nYkEN9H4f1AkEAxfva2dBA3nbkTSEjY4yO+hxfSlJ6ovIfRgt1lZUKE6d5YJ/x6v6pcIm9KoVtfu2r19/JslCayDWYJvDeEvx+vQJBAIfl862xGiCFx4dre7vLuPDuibxAI6TL6hPE4rItFLadBmKJlXwav8CQ9HyIpo6tK/o7qDTfhAR3/l/2ASiofUUCQB7REgKfWMmH5gdfZhvDsCmcKvTtm6srVtTXSckrtrLSeaEhpE04sHUU633n4OBF7F6gtctsonsRZIqDKX35EV0CQAkQ2WNqlk22b1teU47qTxxb8/wyvrHKuxUxoI01u2fS2WCPdwklHjYp5RgQ0GIKlfwPnyZVqIuhQSu/YpQu6a0=", "utf-8");
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