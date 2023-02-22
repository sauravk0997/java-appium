package com.disney.qa.tests.dgi.web;

import com.disney.qa.api.dgi.DGIBase;
import com.disney.qa.api.dgi.dust.DustPageKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.Events;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class DgiBaseWebTest extends DisneyPlusBaseTest  {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected DisneyAccount entitledUser;
    protected BrowserMobProxy proxy;
    DisneyPlusBaseNavPage dNavPage;
    protected String[] events = R.CONFIG.get("custom_string").split(",");

    public BrowserMobProxy getProxy(Set<CaptureType> captureTypes) {
        R.CONFIG.put("browsermob_proxy", "true");
        proxy = new BrowserMobProxyServer();
        ProxyPool.registerProxy(proxy);
        proxy = ProxyPool.getProxy();
        proxy.enableHarCaptureTypes(captureTypes);
        return proxy;
    }

    @BeforeMethod
    public void setup() {
        dNavPage = new DisneyPlusBaseNavPage(getDriver());
        proxy = dNavPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT,
                countryData.searchAndReturnCountryData(locale, "code", "country"));

        entitledUser = getAccountApi().createAccount("Yearly", locale, language, "V2");

        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
    }

    @AfterTest
    public void harValidation() {

        HARUtils harUtils = new HARUtils(proxy);

        harUtils.printHarDetails();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        harUtils.publishHAR(String.format("MultiEvent_Web_Api_Traffic_%s", dateFormat.format(new Date())));
    }

    public void login(boolean enableEventTriggerWait) {

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        DGIBase dgiBase = new DGIBase(proxy);

        if(enableEventTriggerWait) {
            dgiBase.addEventTriggerDelay(5, true,
                    new DustPageKeys[]{DustPageKeys.WELCOME, DustPageKeys.WELCOME},
                    new Events[]{Events.PAGE_VIEW, Events.CONTAINER_VIEW});
        }

        disneyPlusBasePage.clickDplusBaseLoginBtn();

        if(enableEventTriggerWait) {
            dgiBase.addEventTriggerDelay(5,true,
                    new DustPageKeys[]{DustPageKeys.LOG_IN_ENTER_EMAIL, DustPageKeys.LOG_IN_ENTER_EMAIL},
                    new Events[]{Events.PAGE_VIEW, Events.CONTAINER_VIEW});
        }

        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());

        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();

        if(enableEventTriggerWait) {
            dgiBase.addEventTriggerDelay(5, true,
                    new DustPageKeys[]{DustPageKeys.LOG_IN_ENTER_PASSWORD, DustPageKeys.LOG_IN_ENTER_PASSWORD, DustPageKeys.LOG_IN_ENTER_EMAIL},
                    new Events[]{Events.PAGE_VIEW, Events.CONTAINER_VIEW, Events.INTERACTION});
        }

        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());

        disneyPlusBasePage.clickLoginBtn();

        if(!enableEventTriggerWait) {
            LOGGER.info("Recording events...");
            proxy.newHar();
        }

        if(!new DisneyPlusHomePageCarouselPage(getDriver()).isOpened()) {
            skipExecution("Stopping execution as Home page did not load.");
        }

        //TODO monitor behavior, might start late after clicking Log In btn
        dgiBase.addEventTriggerDelay(5,true,
                new DustPageKeys[]{DustPageKeys.LOG_IN_ENTER_PASSWORD, DustPageKeys.HOME, DustPageKeys.HOME},
                new Events[]{Events.INTERACTION, Events.PAGE_VIEW, Events.CONTAINER_VIEW});
    }

    public boolean isStaging() {
        return !new DisneyPlusBasePage(getDriver()).ENVIRONMENT.equalsIgnoreCase("PROD");
    }

}
