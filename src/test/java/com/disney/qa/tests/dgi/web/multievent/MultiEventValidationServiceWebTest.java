package com.disney.qa.tests.dgi.web.multievent;

import java.lang.invoke.MethodHandles;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.DuplicateKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.EventSequences;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.MultiEventValidateWithConfig;
import com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate.SdpDust;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusBrandPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusVideoPlayerPage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusAutoplayPage;
import com.qaprosoft.carina.core.foundation.utils.R;

/**
 * Test cases: https://docs.google.com/spreadsheets/d/1Od6bTfOHZWet-uMHHASDveU_YaX2qtsP-U0ZKhpv3jQ/edit#gid=1777730556
 */

//TODO move validations to @AfterTest when Zafira adds support for annotations
public class MultiEventValidationServiceWebTest extends MultiEventBaseWebTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String RECORDING_EVENTS = "Recording events...";
    private static final String TV_SHORT_URL = String.format("%s/series/marvel-funko-shorts/1zpXcAgarSdO", R.TESTDATA.get("disney_prod_discover_deeplink"));
    private static final String EPISODE_NAME = "1. Spellbound";
    private static final String TV_SHORT = "Marvel Funko Shorts";
    private static final String PROFILE_NAME = "Profile";
    private static int customTimeout = R.CONFIG.getInt("custom_string2");
    private static final String BETA_WEB_ENV = "beta-web";
    EventSequences[] eventSequences = {EventSequences.DEFAULT};
    DgiEndpoints[] multiEventEndpoints = {DgiEndpoints.DUST, DgiEndpoints.TELEMETRY};
    DgiEndpoints[] sdpEndpoints = {DgiEndpoints.DUST};

    @Test(description = "Capture dust events during on-boarding flow -> validate against sdp and multi-event validation service")
    public void onboardingFlowValidation() {

        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER, EventSequences.ONE_PAGE_ONE_CONTAINER_ONE_INTERACTION};

        SoftAssert softAssert = new SoftAssert();

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        pause(customTimeout);

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();

        pause(customTimeout);

        disneyPlusBasePage.cookieCatcherAfterLogin();
        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();

        pause(customTimeout);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Capture dust events during home page carousel interaction -> validate against sdp and multi-event validation service")
    public void carouselInteractionValidation() {

        Object[] entityRelation = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER};
        DuplicateKeys[] duplicateKeys = {DuplicateKeys.PAGE_VIEW, DuplicateKeys.CONTAINER_VIEW};

        SoftAssert softAssert = new SoftAssert();
        SeleniumUtils utils = new SeleniumUtils(getDriver());

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();

        pause(customTimeout);

        DisneyPlusHomePageCarouselPage disneyPlusHomePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());

        do utils.scroll(SeleniumUtils.ScrollDirection.DOWN, 1);
        while (!disneyPlusHomePageCarouselPage.dynamicCarouselRightArrowIsPresent(2, 10, 5));

        disneyPlusHomePageCarouselPage.clickRightArrow(2, 3,2);

        utils.scroll(SeleniumUtils.ScrollDirection.UP, 1);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, new Object[]{entityRelation}, eventSequences, duplicateKeys).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Capture dust events during pixar tile interaction -> validate against sdp and multi-event validation service")
    public void pixarTileValidation() {

        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        DisneyPlusHomePageCarouselPage disneyPlusHomePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());

        disneyPlusHomePageCarouselPage.isOpened();

        pause(customTimeout);

        disneyPlusHomePageCarouselPage.getBrandTile(DisneyPlusHomePageCarouselPage.BrandTiles.PIXAR);

        new DisneyPlusBrandPage(getDriver()).isOpened();

        pause(customTimeout);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    //Playback test has added by AnalyticsQA
    @Test(description = "Capture dust events while searching for content -> validate against sdp and multi-event validation service")
    public void searchValidation() {

        Object[] entityRelation1 = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        Object[] entityRelation2 = { "urn:dss:event:glimpse:engagement:interaction", "interactionId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation3 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelation4 = {"urn:dss:event:glimpse:engagement:input", "inputId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
        Object[] entityRelation5 = {"urn:dss:event:glimpse:engagement:input", "inputId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
        Object[] entityRelationshipList = {entityRelation1, entityRelation2, entityRelation3, entityRelation4, entityRelation5};

        EventSequences[] eventSequences = {
                EventSequences.ONE_PAGE_X_CONTAINER,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION,
                EventSequences.ONE_PAGE_X_CONTAINER_ONE_INTERACTION_ONE_INPUT};

        DuplicateKeys[] duplicateKeys = {
                DuplicateKeys.PAGE_VIEW,
                DuplicateKeys.CONTAINER_VIEW,
                DuplicateKeys.INTERACTION,
                DuplicateKeys.INPUT
        };

        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();

        pause(customTimeout);

        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        homePageSearchPage.clickOnSearchBar();
        homePageSearchPage.isOpened();

        pause(customTimeout);

        homePageSearchPage.searchFor(TV_SHORT);
        homePageSearchPage.clickOnWatchlistItem(TV_SHORT);

        new DisneyPlusBaseDetailsPage(getDriver()).isDetailsPagePlayBtnPresent();

        pause(customTimeout);

        new DisneyPlusBaseDetailsPage(getDriver()).clickContent(EPISODE_NAME);
        new DisneyPlusVideoPlayerPage(getDriver()).playVideo(130,true, 10);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Capture dust events during autoplay -> validate against sdp and multi-event validation service")
    public void autoplayValidation() {

        String detailsPageUrl = TV_SHORT_URL;

        SoftAssert softAssert = new SoftAssert();

        if(R.CONFIG.get("env").equalsIgnoreCase("BETA")) {
            detailsPageUrl = detailsPageUrl.replace("www", BETA_WEB_ENV);
        }

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();

        pause(customTimeout);

        getDriver().navigate().to(detailsPageUrl);

        new DisneyPlusBaseDetailsPage(getDriver()).isDetailsPagePlayBtnPresent();

        pause(customTimeout);

        new DisneyPlusBaseDetailsPage(getDriver()).clickContent(EPISODE_NAME);
        new DisneyPlusVideoPlayerPage(getDriver()).playVideo(130,true, 10);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Capture dust events while click on 'Play Next Episode' during autoplay -> validate against sdp and multi-event validation service")
    public void autoplayClickPlayNextEpisodeValidation() {

        String editProfileUrl = R.TESTDATA.get("disney_prod_edit_profiles_deeplink");
        String detailsPageUrl = TV_SHORT_URL;

        if(R.CONFIG.get("env").equalsIgnoreCase("BETA")) {
            editProfileUrl = editProfileUrl.replace("www", BETA_WEB_ENV);
            detailsPageUrl = detailsPageUrl.replace("www", BETA_WEB_ENV);
        }

        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();
        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();

        pause(customTimeout);

        DisneyPlusAutoplayPage autoPlayPage = new DisneyPlusAutoplayPage(getDriver());
        getDriver().get(editProfileUrl);
        autoPlayPage.selectProfileToEdit(PROFILE_NAME);
        autoPlayPage.clickAutoplay(DisneyPlusAutoplayPage.ButtonActions.OFF, 5);
        //TODO Remove condition when new feature is deployed to PROD
        if(R.CONFIG.get("env").equalsIgnoreCase("PROD")
                || R.CONFIG.get("env").equalsIgnoreCase("QA")) {
            new DisneyPlusBaseProfileViewsPage(getDriver()).clickOnEditProfileDoneButton();
        } else {
            LOGGER.info("Waiting for profile changes to take affect");
            pause(5);
        }        autoPlayPage.isProfilePresent(PROFILE_NAME);

        LOGGER.info(RECORDING_EVENTS);

        proxy.newHar();

        getDriver().navigate().to(detailsPageUrl);
        new DisneyPlusBaseDetailsPage(getDriver()).isDetailsPagePlayBtnPresent();

        pause(customTimeout);

        new DisneyPlusBaseDetailsPage(getDriver()).clickContent(EPISODE_NAME);
        DisneyPlusVideoPlayerPage disneyPlusVideoPlayer = new DisneyPlusVideoPlayerPage(getDriver());
        disneyPlusVideoPlayer.playVideo(120, false);

        //Playing video for 10 seconds
        pause(10);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Play Premier Access trailer -> validate against sdp and multi-event validation service")
    public void premierAccessTrailerValidation() {

        SoftAssert softAssert = new SoftAssert();

        String mulanDeeplink = R.TESTDATA.get("disney_prod_mulan_deeplink");

        if(R.CONFIG.get("env").equalsIgnoreCase("BETA")) {
            mulanDeeplink = mulanDeeplink.replace("www", "BETA_WEB_ENV");
        }

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        getDriver().get(mulanDeeplink);
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        detailsPage.isLogoPresent("Mulan");

        pause(customTimeout);

        detailsPage.clickTrailerBtn();

        //Play trailer for 15 seconds
        pause(15);

        getDriver().navigate().back();
        detailsPage.isLogoPresent("Mulan");

        pause(customTimeout);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, eventSequences).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "End to end test combining all scenarios -> validate against sdp and multi-event validation service")
    public void endToEndValidation() {

        Object[] entityRelation1 = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        Object[] entityRelation2 = { "urn:dss:event:glimpse:engagement:interaction", "interactionId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
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
        SeleniumUtils utils = new SeleniumUtils(getDriver());
        DisneyPlusHomePageCarouselPage carouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        DisneyPlusVideoPlayerPage videoPlayer = new DisneyPlusVideoPlayerPage(getDriver());
        DisneyPlusAutoplayPage autoPlayPage = new DisneyPlusAutoplayPage(getDriver());
        DisneyPlusBrandPage brandPage = new DisneyPlusBrandPage(getDriver());

        LOGGER.info(RECORDING_EVENTS);
        proxy.newHar();

        /*
         * Onboarding flow
         */
        pause(customTimeout);
        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.clickDplusBaseLoginBtn();
        pause(customTimeout);
        disneyPlusBasePage.typeDplusBaseEmailFieldId(entitledUser.getEmail());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.typeDplusBasePasswordFieldId(entitledUser.getUserPass());
        disneyPlusBasePage.clickDplusBaseLoginFlowBtn();
        disneyPlusBasePage.cookieCatcherAfterLogin();
        new DisneyPlusHomePageCarouselPage(getDriver()).isOpened();
        pause(customTimeout);

        /*
         * Carousel interaction
         */
        DisneyPlusHomePageCarouselPage disneyPlusHomePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        do utils.scroll(SeleniumUtils.ScrollDirection.DOWN, 1);
        while (!disneyPlusHomePageCarouselPage.dynamicCarouselRightArrowIsPresent(2, 10, 5));
        disneyPlusHomePageCarouselPage.clickRightArrow(2, 3,2);
        utils.scroll(SeleniumUtils.ScrollDirection.UP, 1);

        /*
         * Pixar tile validation
         */
        carouselPage.getBrandTile(DisneyPlusHomePageCarouselPage.BrandTiles.PIXAR);
        brandPage.isOpened();
        pause(customTimeout);
        getDriver().navigate().back();
        carouselPage.isOpened();
        pause(customTimeout);
        carouselPage.getBrandTile(DisneyPlusHomePageCarouselPage.BrandTiles.PIXAR);
        brandPage.isOpened();
        pause(customTimeout);

        /*
         * Search validation + Playback Test
         */
        homePageSearchPage.clickOnSearchBar();
        homePageSearchPage.isOpened();
        pause(customTimeout);
        homePageSearchPage.searchFor(TV_SHORT);
        homePageSearchPage.clickOnWatchlistItem(TV_SHORT);
        detailsPage.isDetailsPagePlayBtnPresent();
        pause(customTimeout);
        new DisneyPlusBaseDetailsPage(getDriver()).clickContent(EPISODE_NAME);
        new DisneyPlusVideoPlayerPage(getDriver()).playVideo(130,true, 10);

        /*
         * Autoplay validation
         */
        detailsPage.clickContent(EPISODE_NAME);
        //Playing video for 10 seconds
        pause(10);
        getDriver().navigate().back();
        detailsPage.isDetailsPagePlayBtnPresent();
        pause(customTimeout);
        detailsPage.clickContent(EPISODE_NAME);
        videoPlayer.playVideo(130, true, 10);


        /*
         * autoplayClickPlayNextEpisodeValidation
         */
        String editProfileUrl = R.TESTDATA.get("disney_prod_edit_profiles_deeplink");
        String detailsPageUrl = TV_SHORT_URL;

        if(R.CONFIG.get("env").equalsIgnoreCase("BETA")) {
            editProfileUrl = editProfileUrl.replace("www", "BETA_WEB_ENV");
            detailsPageUrl = detailsPageUrl.replace("www", "BETA_WEB_ENV");
        }
        getDriver().get(editProfileUrl);
        autoPlayPage.selectProfileToEdit(PROFILE_NAME);
        autoPlayPage.clickAutoplay(DisneyPlusAutoplayPage.ButtonActions.OFF, 5);
        //TODO Remove condition when new feature is deployed to PROD
        if(R.CONFIG.get("env").equalsIgnoreCase("PROD")
                || R.CONFIG.get("env").equalsIgnoreCase("QA")) {
            new DisneyPlusBaseProfileViewsPage(getDriver()).clickOnEditProfileDoneButton();
        } else {
            LOGGER.info("Waiting for profile changes to take affect");
            pause(5);
        }        autoPlayPage.isProfilePresent(PROFILE_NAME);
        getDriver().navigate().to(detailsPageUrl);

        detailsPage.clickContent(EPISODE_NAME);
        videoPlayer.playVideo(120, false);
        //Playing video for 10 seconds
        pause(10);

        new SdpDust(proxy, softAssert, sdpEndpoints, events).verifyPostDataEvent();

        new MultiEventValidateWithConfig(proxy, softAssert, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEvent();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

}
