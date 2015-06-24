package com.sp.platform.web.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by lei.a.yang on 2014/10/21.
 */
public class HttpUtils {

    public static HttpClient getSecuredHttpClient(){
        return getSecuredHttpClient(new DefaultHttpClient());
    }

    /**
     * 避免HttpClient的”SSLPeerUnverifiedException: peer not authenticated”异常
     * 不用导入SSL证书
     *
     * @author shipengzhi(shipengzhi@sogou-inc.com)
     */
    public static DefaultHttpClient getSecuredHttpClient(HttpClient httpClient) {
        final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return _AcceptedIssuers;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, new SecureRandom());
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            return new DefaultHttpClient(ccm, httpClient.getParams());
        } catch (Exception e) {
            System.out.println(" =====:===== ");
            e.printStackTrace();
        }
        return null;
    }
}
