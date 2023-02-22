package com.disney.qa.tests.disney.web.redirect;

import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.preview.DisneyPlusPreviewPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import net.lightbody.bmp.BrowserMobProxy;
import org.testng.SkipException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DisneyPlusRedirectBlockTest extends DisneyPlusBaseTest {
    private Yaml yaml = new Yaml();
    private InputStream suffixStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/disney/country-specific.yaml");
    private ArrayList<Object> suffixYml = yaml.load(suffixStream);
    private ArrayList<String> suffixList = new ArrayList<>();

    @Parameters({"countryProxy", "qtestId"})
    @Test(description = "Confirm redirect URL region blocking")
    public void checkSplashRedirectBlocking(String countryProxy, String qtestId) {
        SoftAssert sa = new SoftAssert();
        setCases(qtestId);

        getDriver().manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        DisneyPlusPreviewPage page = new DisneyPlusPreviewPage(getDriver());
        ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(countryProxy));

        int registeredPort = geoedgeProxyFreshInstance.getProxyPortForThread();
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.start(registeredPort);

        page.open(getDriver());
        checkForFailedProxy();

        String currentUrl = getDriver().getCurrentUrl();

        ArrayList<String> itr = getCountrySuffixList();

        for (int i = 0; i < itr.size(); i++) {

            String iteratedSuffix = itr.get(i);

            if (iteratedSuffix.equals("unavailable/")) {

                iteratedSuffix = "";

            }

            page.openURL(DisneyWebParameters.DISNEY_PROD_WEB_PREVIEW.getValue() + "/" + iteratedSuffix);

            checkForFailedProxy();

            String assertUrl = getDriver().getCurrentUrl();

            sa.assertEquals(assertUrl, currentUrl,
                        String.format("Failure to block redirect region, redirect url: %s, current url: %s", assertUrl, currentUrl));

        }

        sa.assertAll();
    }

    private ArrayList<String> getCountrySuffixList() {

        for (Object yaml : suffixYml) {
            Map<String, String> server = (HashMap<String, String>) yaml;
            String suffix = server.get("suffix");
            if (suffix == null) {
                suffix = "";
            }

            suffixList.add(suffix);

            Set<String> linkedHashSet = new HashSet(suffixList);

            //Removes all duplicate Suffixes
            suffixList.clear();
            suffixList.addAll(linkedHashSet);

        }

        return suffixList;
    }

    private void checkForFailedProxy() {
        String pageSource = getDriver().getPageSource();

        List<String> failedProxyMessages = new ArrayList<>();
        failedProxyMessages.add("ERR_CONNECTION_TIMED_OUT");
        failedProxyMessages.add("ERR_TIMED_OUT");
        failedProxyMessages.add("ERR_TUNNEL_CONNECTION_FAILED");

        for (String failedProxy : failedProxyMessages) {
            if (pageSource.contains(failedProxy)) {
                //Assert.fail(String.format("Proxy has returned '%s'", failedProxy));
                throw new SkipException(String.format("Proxy has returned '%s'", failedProxy));
            }
        }
    }

}


