package com.disney.qa.tests.dgi.androidtv.multievent;

import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.DuplicateKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.EventSequences;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.MultiEventValidateWithConfig;
import com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate.SdpDust;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.tests.dgi.androidtv.DisneyPlusAndroidTVBaseDGITest;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.commons.lang.ArrayUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.Locale;

public class DisneyPlusAndroidTVMultiEventValidationTests extends DisneyPlusAndroidTVBaseDGITest {

    private static final String WEB_LINK_TV_SHORT = String.format("%s/series/marvel-funko-shorts/1zpXcAgarSdO", R.TESTDATA.get("disney_prod_discover_deeplink"));
    private static final String TV_SHORT = "Marvel Funko Shorts";

    DgiEndpoints[] sdpEndpoints = {DgiEndpoints.DUST};
    DgiEndpoints[] multiEventEndpoints = {DgiEndpoints.DUST, DgiEndpoints.TELEMETRY};
    Object[] entityRelation1 = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
    Object[] entityRelation2 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
    Object[] entityRelation3 = {"urn:dss:event:glimpse:engagement:interaction", "interactionId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
    Object[] entityRelation4 = {"urn:dss:event:glimpse:engagement:input", "inputId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", false};
    Object[] entityRelation5 = {"urn:dss:event:glimpse:engagement:input", "inputId", "containerViewId", "urn:dss:event:glimpse:impression:containerView", false};
    Object[] entityRelationshipList = {entityRelation1, entityRelation2, entityRelation3, entityRelation4, entityRelation5};


    @BeforeMethod
    public void setup() {
        initiateProxy(new Locale("", country).getDisplayCountry());
        new AndroidUtilsExtended().clearAppCache();
        androidTVUtils.set(new AndroidTVUtils(getDriver()));
    }

    @Test
    public void onboardingFlowValidation() {
        SoftAssert sa = new SoftAssert();
        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER, EventSequences.ONE_PAGE_ONE_CONTAINER_ONE_INTERACTION};

        proxy.get().newHar();

        loginSlowly(entitledUser.get());

        //Adding pause here to ensure homepage is fully loaded, also during this time a few carousel rotations occur
        pause(30);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, eventSequences).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void carouselInteractionValidation() {
        SoftAssert sa = new SoftAssert();
        EventSequences[] eventSequences = {EventSequences.ONE_PAGE_X_CONTAINER};
        Object[] entityRelation = {"urn:dss:event:glimpse:impression:containerView", "containerViewId", "pageViewId", "urn:dss:event:glimpse:impression:pageView", true};
        DuplicateKeys[] duplicateKeys = {DuplicateKeys.PAGE_VIEW, DuplicateKeys.CONTAINER_VIEW};

        //Removing interaction event here
        String[] eventsUpdated = Arrays.copyOf(events, events.length);
        eventsUpdated = (String[]) ArrayUtils.removeElement(eventsUpdated, "urn:dss:event:glimpse:engagement:interaction");

        loginSlowly(entitledUser.get());

        proxy.get().newHar();
        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //wait 5 seconds here for carousel rotation to happen
        pause(10);

        disneyPlusAndroidTVCommonPage.get().pressDown(3);
        disneyPlusAndroidTVCommonPage.get().pressRight(3);
        disneyPlusAndroidTVCommonPage.get().pressUp(3);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, eventsUpdated, new Object[]{entityRelation}, eventSequences, duplicateKeys).verifyPostDataEventVer2();
    }

    @Test
    public void pixarTileValidation() {
        SoftAssert sa = new SoftAssert();

        loginSlowly(entitledUser.get());

        proxy.get().newHar();

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Ensure Home page loads properly
        pause(10);

        disneyPlusAndroidTVBrandPage.get().selectBrandTileFromHeroCarousel(1);
        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, sdpEndpoints, events, EventSequences.DEFAULT).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void searchValidation() {
        SoftAssert sa = new SoftAssert();

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

        loginSlowly(entitledUser.get());

        proxy.get().newHar();

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Let homepage load properly
        pause(10);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(
                DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        disneyPlusAndroidTVSearchPage.get().isOpened();

        //Ensure search page loads properly
        pause(10);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(TV_SHORT);
        disneyPlusAndroidTVCommonPage.get().isContentDescElementPresent(TV_SHORT);
        disneyPlusAndroidTVSearchPage.get().selectFirstSearchedItem();

        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        //LetDetails page load properly
        pause(10);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void autoplayValidation() {
        SoftAssert sa = new SoftAssert();

        loginSlowly(entitledUser.get());

        proxy.get().newHar();

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Ensure home page loads properly
        pause(10);

        getDriver().get(WEB_LINK_TV_SHORT);

        disneyPlusAndroidTVDetailsPageBase.get().isOpened();
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(1, 1);
        pause(5);
        disneyPlusAndroidTVDetailsPageBase.get().selectFocusedElement();
        disneyPlusAndroidTVVideoPlayerPage.get().playVideoTillUpNextScreen(300, 4);
        disneyPlusAndroidTVDetailsPageBase.get().waitTillUpNextScreenIsGone(100, 5);

        //Let the next video play for a bit
        pause(20);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();

        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, EventSequences.DEFAULT).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void autoplayClickPlayNextEpisodeValidation() {
        SoftAssert sa = new SoftAssert();

        loginSlowly(entitledUser.get());

        proxy.get().newHar();

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Ensure home page loads properly
        pause(10);

        getDriver().get(WEB_LINK_TV_SHORT);
        disneyPlusAndroidTVDetailsPageBase.get().isOpened();
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(1, 1);
        pause(5);
        disneyPlusAndroidTVDetailsPageBase.get().selectFocusedElement();
        disneyPlusAndroidTVVideoPlayerPage.get().playVideoTillUpNextScreen(300, 4);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.get().waitTillUpNextScreenIsGone(100, 5);

        //Let the next video play for a bit
        pause(20);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, EventSequences.DEFAULT).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void premierAccessTrailerValidation() {
        SoftAssert sa = new SoftAssert();

        loginSlowly(entitledUser.get());

        proxy.get().newHar();

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        getDriver().get(R.TESTDATA.get("disney_prod_mulan_deeplink"));

        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        //Ensure details page loads successfully
        pause(10);

        disneyPlusAndroidTVDetailsPageBase.get().clickTrailerBtn();

        //Ensure trailer plays
        pause(30);

        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);

        //Ensure details page loads properly
        pause(10);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();

        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, EventSequences.DEFAULT).verifyPostDataEventVer2();

        sa.assertAll();
    }

    @Test
    public void endToEndValidation() {
        SoftAssert sa = new SoftAssert();

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

        proxy.get().newHar();

        //Onboarding flow
        loginSlowly(entitledUser.get());

        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Carousel Interaction, let hero rotation occur at least once
        pause(10);

        disneyPlusAndroidTVCommonPage.get().pressDown(3);
        disneyPlusAndroidTVCommonPage.get().pressRight(3);
        disneyPlusAndroidTVCommonPage.get().pressUp(3);

        //Pixar Tile validation
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        disneyPlusAndroidTVCommonPage.get().pressLeft(2);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        //Ensure brand page loads properly
        pause(10);

        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        disneyPlusAndroidTVDiscoverPage.get().isOpened();

        //Ensure home page loads properly
        pause(10);

        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        //Search validation
        getDriver().get(R.TESTDATA.get("disney_prod_search_deeplink"));
        disneyPlusAndroidTVSearchPage.get().isOpened();

        //Ensure Explore/Search page loads properly
        pause(10);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(TV_SHORT);
        disneyPlusAndroidTVCommonPage.get().isContentDescElementPresent(TV_SHORT);
        disneyPlusAndroidTVSearchPage.get().selectFirstSearchedItem();

        disneyPlusAndroidTVDetailsPageBase.get().isOpened();

        //AutoPlay Validation without clicking play on up next
        //ensure details page loads properly
        pause(10);
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(1, 1);
        pause(5);
        disneyPlusAndroidTVDetailsPageBase.get().selectFocusedElement();
        //ensure details page loads properly
        pause(20);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        disneyPlusAndroidTVDetailsPageBase.get().isOpened();
        //ensure details page loads properly
        pause(10);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVVideoPlayerPage.get().playVideoTillUpNextScreen(300, 4);
        disneyPlusAndroidTVDetailsPageBase.get().waitTillUpNextScreenIsGone(100, 5);

        //AutoPlay Validation with clicking play on up next
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        //ensure details page loads properly
        pause(10);

        disneyPlusAndroidTVDetailsPageBase.get().pressUp(2);

        if (!disneyPlusAndroidTVDetailsPageBase.get().isStartPlayerBtnFocused()) {
            disneyPlusAndroidTVDetailsPageBase.get().pressLeft(1);
            disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        } else {
            androidTVUtils.get().keyPressTimes(AndroidTVUtils::pressSelect, 1, 1);
        }

        disneyPlusAndroidTVVideoPlayerPage.get().playVideoTillUpNextScreen(300, 4);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.get().waitTillUpNextScreenIsGone(100, 5);

        //Let the next video play for a bit
        pause(20);

        new SdpDust(proxy.get(), sa, sdpEndpoints, events).verifyPostDataEventVer2();
        new MultiEventValidateWithConfig(proxy.get(), sa, multiEventEndpoints, events, entityRelationshipList, eventSequences, duplicateKeys).verifyPostDataEventVer2();

        sa.assertAll();
    }
}