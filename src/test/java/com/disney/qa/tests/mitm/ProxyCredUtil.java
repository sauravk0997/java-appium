package com.disney.qa.tests.mitm;

import com.disney.qa.carina.GeoedgeProxyServer;
import com.proxy.ProxyCreds;
import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class ProxyCredUtil {

    /**
     * Get Brightdata credentials for a given country code (ex: mx)
     * @param country the country code to get the proxy for
     * @return the proxy credentials for the country
     */
    public static ProxyCreds getBrightdataProxy(String country) {
        ProxyCreds proxyCreds = ProxyCreds.builder().proxyHost(R.TESTDATA.get("bright_data_super_proxy")).proxyPort("22225").proxyPassword(R.TESTDATA.get("bright_data_residential_pass")).build();
        proxyCreds.setProxyUsername(R.TESTDATA.get("bright_data_residential_un") + country.toLowerCase());
        return proxyCreds;
    }

    /**
     * Get geoedgeProxy credentials for a given country name (ex: Mexico)
     * @param country the country name to get the proxy for
     * @return the proxy credentials for the country
     */
    public static ProxyCreds getGeoedgeProxy(String country) {
        GeoedgeProxyServer server = new GeoedgeProxyServer();
        String hostIp = server.getGeoEdgeProxyIp(country);
        String port = "443";

        if (hostIp.contains(":")) {
            String[] splitHost = hostIp.split(":");
            hostIp = splitHost[0];
            port = splitHost[1];
        }

        CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
        String username = R.TESTDATA.get("browsermob_un");
        String password = cryptoTool.decrypt(R.TESTDATA.get("browsermob_pw"));
        ProxyCreds proxyCreds = ProxyCreds.builder().proxyHost(hostIp).proxyPort(port).proxyUsername(username).proxyPassword(password).build();
        return proxyCreds;
    }
}
