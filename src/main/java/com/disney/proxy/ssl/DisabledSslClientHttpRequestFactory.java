package com.disney.proxy.ssl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mk on 6/30/15.
 */
public class DisabledSslClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if (connection instanceof HttpsURLConnection) {

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            TrustManager[] trustManagerArray = {new NullX509TrustManager()};
            try {
                sslContext.init(null, trustManagerArray, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
            ((HttpsURLConnection) connection).setHostnameVerifier(new NullHostnameVerifier());
        }
        super.prepareConnection(connection, httpMethod);
    }

}
