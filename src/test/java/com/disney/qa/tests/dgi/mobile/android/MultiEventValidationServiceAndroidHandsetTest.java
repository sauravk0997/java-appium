package com.disney.qa.tests.dgi.mobile.android;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.dgi.DGIBase;
import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.dust.DustPageKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.DuplicateKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.EventSequences;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.MultiEventValidateWithConfig;
import com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate.SdpDust;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.DisneyLanguageUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusBrandPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.listeners.CarinaListener;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MultiEventValidationServiceAndroidHandsetTest extends BaseAndroidDgiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected EventSequences[] eventSequences = {EventSequences.DEFAULT};
    DgiEndpoints[] sdpEndpoints = {DgiEndpoints.DUST};
    DgiEndpoints[] multiEventEndpoints = {DgiEndpoints.DUST, DgiEndpoints.TELEMETRY};

    private DisneyAccountApi accountApi;

    @BeforeSuite
    public void testSetup(){
        CarinaListener.disableDriversCleanup();
        LOGGER.info("Setting up accounts...");
        setLocationData();
        accountApi = new DisneyAccountApi(PLATFORM, ENV, PARTNER);
        languageUtils = new DisneyLanguageUtils(country, language);
        entitledUser = accountApi.createEntitledAccount(languageUtils.getLocale(), languageUtils.getUserLanguage());
    }

    @BeforeMethod
    private void setDictionaries() {
        languageUtils.setDictionaries();
    }

    @Test(description = "Capture dust events during on-boarding flow -> validate against sdp and multi-event validation service")
    public void onboardingFlowValidation(){
        internalSetup(true);
        SoftAssert softAssert = new SoftAssert();
        List<String> expectedKeys = Arrays.asList(DustPageKeys.WELCOME.getPageKey(), DustPageKeys.LOG_IN_ENTER_EMAIL.getPageKey(),
                DustPageKeys.LOG_IN_ENTER_PASSWORD.getPageKey(), DustPageKeys.HOME.getPageKey());
        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER, EventSequences.ONE_PAGE_ONE_CONTAINER_ONE_INTERACTION};
        mobileLogin(entitledUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);

        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));

        //Pause allows enough time to ensure the network transactions are made prior to assertion
        pause(30);

        BrowserMobProxy proxy = ProxyPool.getProxy();

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Capture dust events during home page carousel interaction -> validate against sdp and multi-event validation service")
    public void carouselInteractionValidation(){
        internalSetup(false);
        SoftAssert softAssert = new SoftAssert();
        List<String> expectedKeys = Arrays.asList(DustPageKeys.HOME.getPageKey());
        Object[] entityRelation = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER};
        DuplicateKeys[] duplicateKeys = {DuplicateKeys.PAGE_VIEW, DuplicateKeys.CONTAINER_VIEW};

        List<String> headers = getHomePageContentHeaders(PLATFORM, ENV, PARTNER);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);

        mobileLogin(entitledUser);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();

        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));

        baseDisneyTest.dismissChromecastOverlay();

        //test requirement: wait 5 seconds for heroCarousel to rotate once
        pause(5);

        //Iterates through the list until the Hero is not visible.
        for (String header : headers) {
            discoverPageBase.swipeToItem(header);
            if (!discoverPageBase.isHeroVisible()) {
                discoverPageBase.swipeInShelf(header, 6);
                break;
            }
        }

        //Returns to Hero
        discoverPageBase.returnToHero();

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, new Object[]{entityRelation}, eventSequences, duplicateKeys).verifyPostDataEvent();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Capture dust events during pixar tile interaction -> validate against sdp and multi-event validation service")
    public void pixarTileValidation(){
        internalSetup(false);
        List<String> expectedKeys = Arrays.asList(DustPageKeys.HOME.getPageKey(), DustPageKeys.PIXAR.getPageKey());
        SoftAssert softAssert = new SoftAssert();
        mobileLogin(entitledUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();

        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));

        baseDisneyTest.dismissChromecastOverlay();

        discoverPageBase.openBrand(DisneyPlusDiscoverPageBase.Brands.PIXAR.getText());
        //Pause allows enough time to ensure the network transactions are made prior to assertion
        pause(30);

        Assert.assertTrue(initPage(DisneyPlusBrandPageBase.class).isOpened(), String.format(pageLoadError, "Pixar"));

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Capture dust events while searching for content -> validate against sdp and multi-event validation service")
    public void searchValidation(){
        //List differs from other expectedKeys due to needing a media content ID value
        List<String> expectedKeys = Arrays.asList(DustPageKeys.HOME.getPageKey(), DustPageKeys.EXPLORE.getPageKey(), MARVEL_FUNKO_PAGE_KEY);

        Object[] entityRelation1 = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        Object[] entityRelation2 = { "urn:dss:event:glimpse:engagement:interaction", "interactionId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation3 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelation4 = {"urn:dss:event:glimpse:engagement:input", "inputId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation5 = {"urn:dss:event:glimpse:engagement:input", "inputId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelationshipList = {entityRelation1, entityRelation2, entityRelation3, entityRelation4, entityRelation5};

        EventSequences[] eventSequences = {
                EventSequences.ONE_PAGE_X_CONTAINER,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION_ONE_INPUT,
                EventSequences.ONE_PAGE_ONE_CONTAINER};

        DuplicateKeys[] duplicateKeys = {
                DuplicateKeys.PAGE_VIEW,
                DuplicateKeys.CONTAINER_VIEW,
                DuplicateKeys.INTERACTION,
                DuplicateKeys.INPUT
        };

        SoftAssert softAssert = new SoftAssert();

        internalSetup(false);
        mobileLogin(entitledUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();

        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));

        baseDisneyTest.dismissChromecastOverlay();
        discoverPageBase.navigateToPage(languageUtils.getApplicationItem(DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);

        Assert.assertTrue(searchPageBase.isOpened(), String.format(pageLoadError, "Search"));

        //Allows the app time to populate all possible containers
        pause(5);

        searchPageBase.searchForMedia(MARVEL_FUNKO_SHORTS);
        searchPageBase.openSearchResult(MARVEL_FUNKO_SHORTS);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        //Pause allows enough time to ensure the network transactions are made prior to assertion
        pause(30);

        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Capture dust events during autoplay -> validate against sdp and multi-event validation service")
    public void autoplayValidation() throws IOException, JSONException, URISyntaxException {
       accountApi.patchProfileAutoplayStatus(entitledUser, true);
        internalSetup(false);
        //List differs from other expectedKeys due to needing a media content ID value
        List<String> expectedKeys = Arrays.asList(ZENIMATION_PAGE_KEY);
        SoftAssert softAssert = new SoftAssert();
        mobileLogin(entitledUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();
        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));
        baseDisneyTest.dismissChromecastOverlay();

        new AndroidUtilsExtended().launchWithDeeplinkAddress(ZENIMATION_URL);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        mediaPageBase.playDesiredMedia(ZENIMATION_EPISODE);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video"));

        videoPageBase.playVideo(330,true, 45, 95);

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();
        
        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Capture dust events while click on 'Play Next Episode' during autoplay -> validate against sdp and multi-event validation service")
    public void autoplayClickPlayNextEpisodeValidation() throws IOException, JSONException, URISyntaxException {
        accountApi.patchProfileAutoplayStatus(entitledUser, false);
        internalSetup(false);
        //List differs from other expectedKeys due to needing a media content ID value
        List<String> expectedKeys = Arrays.asList(MARVEL_FUNKO_PAGE_KEY);
        SoftAssert softAssert = new SoftAssert();
        mobileLogin(entitledUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();
        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));
        baseDisneyTest.dismissChromecastOverlay();

        new AndroidUtilsExtended().launchWithDeeplinkAddress(MARVEL_FUNKO_URL);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));
        mediaPageBase.playDesiredMedia(FUNKO_EPISODE);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video"));

        videoPageBase.playVideo(130,false, 45, 95);

        //Playing video for 30 seconds
        pause(30);

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "End to end test combining all scenarios -> validate against sdp and multi-event validation service")
    public void endToEndValidation() throws IOException, JSONException, URISyntaxException {
        //New account is created to ensure video playback starts at 0:00
        DisneyAccount endToEndUser = accountApi.createEntitledAccount(country, language);
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        accountApi.patchProfileAutoplayStatus(endToEndUser, true);

        //List differs from other expectedKeys due to needing a media content ID value
        List<String> expectedKeys = Arrays.asList(DustPageKeys.WELCOME.getPageKey(), DustPageKeys.LOG_IN_ENTER_EMAIL.getPageKey(),
                DustPageKeys.LOG_IN_ENTER_PASSWORD.getPageKey(), DustPageKeys.HOME.getPageKey(), DustPageKeys.PIXAR.getPageKey(),
                DustPageKeys.EXPLORE.getPageKey(), MARVEL_FUNKO_PAGE_KEY, ZENIMATION_PAGE_KEY);

        Object[] entityRelation1 = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        Object[] entityRelation2 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation3 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelation4 = {"urn:dss:event:glimpse:engagement:input", "inputId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation5 = {"urn:dss:event:glimpse:engagement:input", "inputId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelationshipList = {entityRelation1, entityRelation2, entityRelation3, entityRelation4, entityRelation5};

        EventSequences[] eventSequences = {
                EventSequences.ONE_PAGE_X_CONTAINER,
                EventSequences.ONE_PAGE_ONE_CONTAINER,
                EventSequences.ONE_PAGE_ONE_CONTAINER_ONE_INTERACTION,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION_ONE_INPUT};

        DuplicateKeys[] duplicateKeys = {
                DuplicateKeys.PAGE_VIEW,
                DuplicateKeys.CONTAINER_VIEW,
                DuplicateKeys.INTERACTION,
                DuplicateKeys.INPUT
        };

        SoftAssert softAssert = new SoftAssert();

        internalSetup(true);

        /*
         * Onboarding flow:
         * Proceeds through onboarding to Discover page
         */
        mobileLogin(endToEndUser);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        baseDisneyTest.dismissChromecastOverlay();

        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));

        /*
         * Carousel interaction:
         * Scrolls down to a carousel, ensuring Hero is out of view, and swipes inside the desired row before returning
         * to the top of the screen
         */
        List<String> headers = getHomePageContentHeaders(PLATFORM, ENV, PARTNER);

        for (String header : headers) {
            discoverPageBase.swipeToItem(header);
            if (!discoverPageBase.isHeroVisible()) {
                discoverPageBase.swipeInShelf(header, 6);
                break;
            }
        }

        discoverPageBase.returnToHero();

        /*
         * Brand tile:
         * Opens designated brand page, returns to Home, then re-opens brand page again
         */
        discoverPageBase.openBrand(DisneyPlusDiscoverPageBase.Brands.PIXAR.getText());
        Assert.assertTrue(initPage(DisneyPlusBrandPageBase.class).isOpened(), String.format(pageLoadError, "Pixar"));
        util.pressBack();
        Assert.assertTrue(discoverPageBase.isOpened(), String.format(pageLoadError, "Home"));
        discoverPageBase.openBrand(DisneyPlusDiscoverPageBase.Brands.PIXAR.getText());
        Assert.assertTrue(initPage(DisneyPlusBrandPageBase.class).isOpened(), String.format(pageLoadError, "Pixar"));

        /*
         * Search:
         * Navigates to Search, enters criteria, and opens desired media page
         */
        discoverPageBase.navigateToPage(languageUtils.getApplicationItem(DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);

        Assert.assertTrue(searchPageBase.isOpened(), String.format(pageLoadError, "Search"));

        searchPageBase.searchForMedia(ZENIMATION);
        searchPageBase.openSearchResult(ZENIMATION);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        /*
         * Auto-Play:
         * Begins playback of desired content for 10 seconds, returns to media page, then reopens media and
         * plays through to end to allow for auto-play function to activate
         */
        mediaPageBase.playDesiredMedia(ZENIMATION_EPISODE);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video (AutoPlay)"));

        LOGGER.info("Media Playback started for: " + videoPageBase.getActiveMediaTitle());
        pause(10);
        videoPageBase.closeVideo();
        Assert.assertTrue(mediaPageBase.isTextElementPresent(ZENIMATION_EPISODE), String.format(pageLoadError, "Media"));
        mediaPageBase.playDesiredMedia(ZENIMATION_EPISODE);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video (AutoPlay Reopen)"));
        videoPageBase.playVideo(330,true, 45, 95);
        videoPageBase.closeVideo();

        /*
         * Click Play Next button:
         * Sets 'auto-play' profile setting to false, then opens desired content and plays through
         * to the end. Clicks on Next Video button.
         */
        accountApi.patchProfileAutoplayStatus(endToEndUser, false);
        util.launchWithDeeplinkAddress(MARVEL_FUNKO_URL);

        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        mediaPageBase.playDesiredMedia(FUNKO_EPISODE);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video (Click Next)"));
        videoPageBase.playVideo(130,false, 45, 95);

        BrowserMobProxy proxy = ProxyPool.getProxy();
        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Play Premier Access trailer -> validate against sdp and multi-event validation service")
    public void trailerValidationTest(){
        internalSetup(false);
        SoftAssert softAssert = new SoftAssert();
        //List differs from other expectedKeys due to needing a media content ID value
        List<String> expectedKeys = Arrays.asList("2jlgPK4K0ilR");
        mobileLogin(entitledUser);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();
        baseDisneyTest.dismissChromecastOverlay();

        AndroidUtilsExtended util = new AndroidUtilsExtended();
        util.launchWithDeeplinkAddress(MULAN_2020_URL);

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        //Pausing for container load delay
        pause(5);
        mediaPageBase.dismissPopup();
        mediaPageBase.playMediaTrailer("Mulan Trailer");
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(), String.format(pageLoadError, "Video"));
        LOGGER.info("Media Playback started for: " + videoPageBase.getActiveMediaTitle());

        //Play trailer for 15 seconds
        pause(15);
        videoPageBase.closeVideo();
        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        //Pausing for container load delay
        pause(5);

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }

    @Test(description = "Play media and cast to a connected receiver device -> validate against sdp and multi-event validation service")
    public void chromecastTest(){
        internalSetup(false);
        SoftAssert softAssert = new SoftAssert();
        List<String> expectedKeys = Arrays.asList("2jlgPK4K0ilR");
        mobileLogin(entitledUser);
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();

        Assert.assertTrue(baseDisneyTest.dismissChromecastOverlay(),
                "ERROR - Chromecast Receiver not detected. Test cannot be completed on this device.");

        AndroidUtilsExtended util = new AndroidUtilsExtended();
        util.launchWithDeeplinkAddress(MULAN_2020_URL);

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        Assert.assertTrue(mediaPageBase.isOpened(), String.format(pageLoadError, "Media"));

        //Pausing for container load delay
        pause(5);
        mediaPageBase.startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        LOGGER.info("Media Playback started for: " + videoPageBase.getActiveMediaTitle());

        /*
         * Play media for 10 seconds, begin casting, and allow media to continue playing for another
         * 10 seconds before stopping the casting and returning to the media details page
         */
        pause(10);
        videoPageBase.beginCasting();
        pause(10);
        videoPageBase.stopCasting();

        Assert.assertTrue(mediaPageBase.isOpened(),
                "Expected - User to be returned to media page after casting has stopped");

        //Pausing for container load delay
        pause(5);

        new DGIBase().verifyEntriesContainPageKeys(proxy, softAssert, expectedKeys);
        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        harValidation();
        softAssert.assertAll();
    }
    
    private void harValidation() {
        HARUtils harUtils = new HARUtils(ProxyPool.getProxy());
        List<HARUtils.RequestDataType> dataTypes = Arrays.asList(HARUtils.RequestDataType.URL, HARUtils.RequestDataType.RESPONSE_CODE, HARUtils.RequestDataType.POST_DATA);
        List<String> hosts = Arrays.asList("dust", "telemetry");
        harUtils.printSpecificHarDetails(dataTypes, hosts);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        harUtils.publishHAR(String.format("Mobile_Traffic_%s", dateFormat.format(new Date())));
    }
}
