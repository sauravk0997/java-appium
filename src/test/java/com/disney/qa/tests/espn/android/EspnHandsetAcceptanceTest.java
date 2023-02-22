package com.disney.qa.tests.espn.android;

import com.disney.qa.api.espn.mobile.EspnAndroidAcceptanceAPIcaller;
import com.disney.qa.espn.android.pages.authentication.EspnFirstTimeLaunchPageBase;
import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.espn.EspnPlusPageBase;
import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.disney.qa.espn.android.pages.media.EspnVideoPageBase;
import com.disney.qa.espn.android.pages.paywall.EspnEpaywallPageBase;
import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.disney.qa.espn.android.pages.settings.EspnSettingsSubscriptionSupportPageBase;
import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.qaprosoft.carina.core.foundation.listeners.CarinaListener;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * ESPN Handset Acceptance test
 *
 * @author bzayats
 */
public class EspnHandsetAcceptanceTest extends BaseEspnTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private static final String E_PAYWALL_CONTENT_SUBTITLE = "ESPN+";
    private static final String WATCH_TAB_VOD_CONTENT_SECTION_TITLE = "More On Demand";
    private static final String OOM_PAYWALL_CONTENT_MLB_PACKAGE = "ESPN_PLUS_MLB";
    private static final String OOM_PAYWALL_CONTENT_NHL_PACKAGE = "ESPN_PLUS_NHL";
    private static final String ESPN_PLUS_PACKAGE = "ESPN_PLUS";
    private static final String USER_WITH_BASE_E_ENTITLEMENTS = "baseE_entitlements";
    private static final String USER_NO_BASE_NO_ENTITLEMENTS = "no_base_no_entitlements";
    private static final String USER_BASE_E = "baseE";

    @BeforeSuite
    public void disableDriverRestart(){
        CarinaListener.disableDriversCleanup();
    }

    /**
     * internalSetup() - acting as 'beforeMethod' for all tests in group ''
     * */
    @QTestCases(id = "14621")
    @Test(description = "Verify: First launch location prompt - enable", groups = "appLaunch")
    public void testVerifyLocationAlertUponAppLaunch() {

        SoftAssert sa = new SoftAssert();

        initPage(EspnCommonPageBase.class).handleAccessToMediaAlert(true);

        initPage(EspnCommonPageBase.class).handleLocationAlert(true);

        sa.assertTrue(initPage(EspnFirstTimeLaunchPageBase.class).isOpened(),
                "Expected - 'Welcome Page' to be displayed");

        sa.assertAll();
    }

    @QTestCases(id = "13904")
    @Test(description = "Verify Welcome Page Basics", dependsOnMethods = "testVerifyLocationAlertUponAppLaunch",
            groups = "appLaunch")
    public void testVerifyPaywallBasics() {

        SoftAssert sa = new SoftAssert();

        sa.assertTrue(initPage(EspnFirstTimeLaunchPageBase.class).isOpened(),
                "Expected - 'Paywall Page' to be displayed");

        sa.assertFalse(initPage(EspnFirstTimeLaunchPageBase.class).checkForWelcomePageElements(),
                "Expected - 'Paywall' pages elements to be displayed");

        sa.assertAll();
    }

    @QTestCases(id = "46366")
    @Test(description = "Verify Logging In lands on HOME page", dependsOnMethods = "testVerifyPaywallBasics", groups = "appLaunch")
    public void testLoginWithExistingAcct() {

        SoftAssert sa = new SoftAssert();

        espnInitialLogin();

        sa.assertTrue(initPage(EspnHomePageBase.class).isOpened(),
                "Expected - 'Home Page' should be displayed");

        sa.assertAll();
    }

    //Watch Tab Section
    //disabling this test due to functionality change on UI layer
    @QTestCases(id = "14401")
    @Test(description = "Verify: On first launch, the first open of the Watch Tab should default to ESPN+",
            dependsOnMethods = "testLoginWithExistingAcct", groups = "appLaunch", enabled = false)
    public void testFirstTimeWatchTabLaunchDefaultsToESPNplusTab() {

        SoftAssert sa = new SoftAssert();

        internalSetup();

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        sa.assertTrue(initPage(EspnWatchPageBase.class).isWatchTabSelected("ESPN"),
                "Expected - 'ESPN+ Tab' should be selected by default");

        sa.assertAll();
    }

    @QTestCases(id = "14623")
    @Test(description = "Scenario: User opens Watch Tab from bottom Nav", groups = "functionality")
    public void testLaunchWatchTab() {

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        sa.assertAll();
    }

    @QTestCases(id = "14624")
    @Test(description = "Scenario: User navigates from Watch Tab to another section from the bottom Nav", groups = "functionality")
    public void testLaunchHomeTab() {

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        initPage(EspnCommonPageBase.class).navigateToPage(EspnCommonPageBase.FooterTabs.HOME_TAB.getText());

        sa.assertTrue(initPage(EspnHomePageBase.class).isOpened(),
                "Expected - 'Home Page' should be displayed");

        sa.assertAll();
    }


    @QTestCases(id = "14625")
    @Test(description = "Verify: Carousel header displayed", groups = "functionality")
    public void testCarouselHeaderDisplayed() {

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        if (!apiCaller.checkForAllContent().isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");
            EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

            //using second list item due to first being used in a different format than the rest, if present, continue with assert, if not, skip
            sa.assertTrue(espnPlusPageBase.isWatchTabSectionHeaderPresent(apiCaller.checkForAllContent().get(1), true),
                    "Expected - '"+ apiCaller.checkForAllContent().get(1) +"'section header title to be present");
        }else{
            skipExecution("Skipping this test since section title not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14626")
    @Test(description = "Verify: Carousel header NOT displayed", groups = "functionality")
    public void testCarouselHeaderNOTDisplayed() {

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        if (!apiCaller.checkForAllContent().isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");
            EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

            for (String str : apiCaller.checkForAllContent()) {
                if (str.isEmpty()){
                    sa.assertTrue(espnPlusPageBase.isWatchTabSectionHeaderPresent(str, false),
                        "Expected - 'an empty' section header title to be present");
                    break;
                }
            }
        }else{
            skipExecution("Skipping this test since 'Empty Header' section titles not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14627")
    @Test(description = "Verify: Schedule icon is displayed on all See All pages AND main Watch tab", groups = "functionality")
    public void testSeeAllPageDisplaysScheduleBtn() {

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        sa.assertTrue(espnPlusPageBase.isScheduleBtnPresent(),
                "Expected - 'See All' page should load and 'Schedule' button to be displayed in header toolbar");


        sa.assertAll();
    }

    @QTestCases(id = "14656")
    @Test(description = "User can navigate through Settings to Customer Support Code page", groups = "functionality")
    public void testLaunchSettingsESPNCustomerSupportPage() {

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        espnPlusPageBase.openSettingsPage();

        EspnSettingsSubscriptionSupportPageBase supportPageBase = initPage(EspnSettingsPageBase.class).
                openSettingsESPNSubscriptionSupposrtPage();

        sa.assertTrue(supportPageBase.isOpened(),
                "Expected - 'ESPN+ Customer Support' should load");

        sa.assertFalse(supportPageBase.getEmailContentInfo().isEmpty());

        sa.assertFalse(supportPageBase.getPhoneContentInfo().isEmpty());

        sa.assertFalse(supportPageBase.getWebSupportContentInfo().isEmpty());

        sa.assertAll();
    }

    @QTestCases(id = "14641")
    @Test(description = "Verify E+ Paywall Basics", groups = "functionality")
    public void testVerifyEpaywallBasics() {

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            //check for presence of desired content, if present, continue with assert, if not, skip
            sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(espnContent.get(0), false),
                    "Expected - '" + espnContent.get(0) + "'selected content launches");

            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' 'log in' button to be present");

            sa.assertTrue(espnEpaywallPageBase.isPaywallRestoreBtn(),
                    "Expected - 'E+ Paywall' 'restore' button to be present");
        }else{
            skipExecution("Skipping this test since '"+ ESPN_PLUS_PACKAGE + "' content not available today");
        }

        sa.assertAll();

        //internal tear down (login back to acct)
        espnLogin(USER_BASE_E);
    }

    @QTestCases(id = "14642")
    @Test(description = "E+ Paywall displays when user tries to open E+ content", groups = "functionality")
    public void testEpaywallDisplaysForEcontent(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay",
                false, ESPN_PLUS_PACKAGE);
        if (!espnContent.isEmpty()) {
            //this assert should take care of both video launch and paywall if not signed in
            sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(espnContent.get(0),
                    false),
                        "Expected - '" + espnContent.get(0) + "'selected content launches");

//            sa.assertTrue(espnEpaywallPageBase.isOpened(),
//                    "Expected - 'E+ Paywall' page to be launched");

            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                "Expected - 'E+ Paywall' 'log in' button to be present");
        }else{
            skipExecution("Skipping this test since '"+ ESPN_PLUS_PACKAGE + "' content not available today");
        }

        //internal tear down (login back to acct)
        espnLogin(USER_BASE_E);

        sa.assertAll();
    }

    @QTestCases(id = "14643")
    @Test(description = "E+ Not-logged-in user can successfully log in via paywall Log In button", groups = "functionality")
    public void testNotLoggedInUserCanLoginInViaEpaywall(){

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);
        EspnLoginPageBase loginPageBase = initPage(EspnLoginPageBase.class);
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        if (espnEpaywallPageBase.isPaywallLogInBtnPresent()) {
            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' login button present");
        }else{
            EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();
            List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

            if (!espnContent.isEmpty()) {
                //check for presence of desired content, if present, continue with assert, if not, skip
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(espnContent.get(0), false),
                        "Expected - '" + espnContent.get(0) + "'selected content launches");

                sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                        "Expected - 'E+ Paywall' 'log in' button to be present");
            }else{
                skipExecution("Skipping this test since '"+ ESPN_PLUS_PACKAGE + "' content not available today");
            }
        }

        espnEpaywallPageBase.openLoginScreen();

        sa.assertTrue(loginPageBase.isOpened(),
                "Expected - 'Log In' page should be displayed");

        login(USER_BASE_E);

        //asserting in case test goes both cases: if getting back to Watch page and video page being open
        if (espnPlusPageBase.isOpened()) {
            sa.assertFalse(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' login button should NOT be present");
        }

        if (videoPageBase.isOpened()){
            sa.assertTrue(videoPageBase.isMediaPlayerPresent(),
                    "Expected - 'Media player' should be present");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14644")
    @Test(description = "After successful login to entitled account, playback begins", groups = "functionality")
    public void testPlaybackStartsAfterSuccessfulLoginFromEpaywall(){

        SoftAssert sa = new SoftAssert();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);
        EspnLoginPageBase loginPageBase = initPage(EspnLoginPageBase.class);
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay",
                false, ESPN_PLUS_PACKAGE);
        if (espnEpaywallPageBase.isPaywallLogInBtnPresent()) {
            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' login button present");
        }else{
            if (!espnContent.isEmpty()) {
                //check for presence of desired content, if present, continue with assert, if not, skip
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(espnContent.get(0), false),
                        "Expected - '" + espnContent.get(0) + "'selected content launches");

                sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                        "Expected - 'E+ Paywall' 'log in' button to be present");
            }else{
                skipExecution("Skipping this test since '"+ ESPN_PLUS_PACKAGE + "' content not available today");
            }
        }

        espnEpaywallPageBase.openLoginScreen();

        sa.assertTrue(loginPageBase.isOpened(),
                "Expected - 'Log In' page should be displayed");

        login(USER_BASE_E);

        //currently, there is an error popping up afteer logging in from "logged out state".
        //This line of code is a workaround.
        espnEpaywallPageBase.clearPaywallUnableToRestorePurchaseDialog();

        //since content was never launched at this point, re-launching it again
        sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(espnContent.get(0), false),
                "Expected - '" + espnContent.get(0) + "'selected content launches");

        sa.assertTrue(videoPageBase.isMediaPlayerPresent(),
                "Expected - 'Media player' should be present");

        sa.assertAll();
    }
  
    @QTestCases(id = "14645")
    @Test(description = "Not logged in, no subscription, user taps on MLB/NHL content, cancels out",
            groups = "functionality", enabled = false)public void testOOMpaywallDisplaysForOOMcontentNotLoggedIn(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        //TODO: change this to OOM paywall
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);

        sa.assertTrue(espnPlusPageBase.isOpened(),
                "Expected - 'Watch' page should be loaded");

        List<String> espnContent = apiCaller.checkForSubtitleContent(E_PAYWALL_CONTENT_SUBTITLE);
        String upsellContent = " ";

        if (!espnContent.isEmpty()) {

            //TODO: revisit this based on the content returned from API (currently it's game titles which are sub-titles in the apps section carousels tiles)
            for (String str : espnContent){
                if (str.contains("NHL") || str.contains("MBL")){
                    LOGGER.info("Found a subTitle with upsell content..");
                    upsellContent = str;
                    break;
                }
            }
            //check for presence of desired content, if present, continue with assert, if not, skip
            sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(upsellContent, false),
                    "Expected - '"+ upsellContent +"'section header title to be present");

            //TODO: implement OOM Page Base and perform an assertion on it's members below
            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' 'log in' button to be present");
        }else{
            skipExecution("Skipping this test since '"+ E_PAYWALL_CONTENT_SUBTITLE + "' content not available today");
        }

        //internal tear down (login back to acct)
        espnLogin(USER_BASE_E);

        sa.assertAll();
    }
  
    @QTestCases(id = "14646")
    @Test(description = "Logged in, no subscription, user taps on MLB/NHL content",
            groups = "functionality", enabled = false)
    public void testOOMpaywallDisplaysForOOMcontentLoggedIn(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();
        espnLogin(USER_BASE_E);

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        //TODO: change this to OOM paywall
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);

        sa.assertTrue(espnPlusPageBase.isOpened(),
                "Expected - 'Watch' page should be loaded");

        List<String> espnContent = apiCaller.checkForSubtitleContent(E_PAYWALL_CONTENT_SUBTITLE);
        String upsellContent = " ";

        if (!espnContent.isEmpty()) {

            //TODO: revisit this based on the content returned from API (currently it's game titles which are sub-titles in the apps section carousels tiles)
            for (String str : espnContent){
                if (str.contains("NHL") || str.contains("MBL")){
                    LOGGER.info("Found a subTitle with upsell content..");
                    upsellContent = str;
                    break;
                }
            }
            //check for presence of desired content, if present, continue with assert, if not, skip
            sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(upsellContent, false),
                    "Expected - '"+ upsellContent +"'section header title to be present");

            //TODO: implement OOM Page Base and perform an assertion on it's members below
            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' 'log in' button to be present");
        }else{
            skipExecution("Skipping this test since '"+ E_PAYWALL_CONTENT_SUBTITLE + "' content not available today");
        }

        //internal tear down (login back to acct)
        espnLogOut();
        espnLogin(USER_BASE_E);

        sa.assertAll();
    }

    @QTestCases(id = "14647")
    @Test(description = "Logged in, E+ AND MLB/NHL sub, user taps on MLB/NHL content",
            groups = "functionality", enabled = false)
    public void testOOMpaywallDisplaysForOOMcontentLoggedInWithSubAndUpsell(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
        espnLogOut();
        espnLogin(USER_BASE_E);

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        //TODO: change this to OOM paywall
        EspnEpaywallPageBase espnEpaywallPageBase = initPage(EspnEpaywallPageBase.class);

        sa.assertTrue(espnPlusPageBase.isOpened(),
                "Expected - 'Watch' page should be loaded");

        List<String> espnContent = apiCaller.checkForContentNameBasedOnPackages("live", OOM_PAYWALL_CONTENT_MLB_PACKAGE);
        String upsellContent = " ";

        if (!espnContent.isEmpty()) {

            //TODO: revisit this based on the content returned from API (currently it's game titles which are sub-titles in the apps section carousels tiles)
            for (String str : espnContent) {
                if (!str.contains("ESPN,") && str.contains("MBL")){
                    LOGGER.info("Found a subTitle with upsell content..");
                    upsellContent = str;
                    break;
                }
                LOGGER.info("String contains: " + str);
            }
            //check for presence of desired content, if present, continue with assert, if not, skip
            sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(upsellContent, false),
                    "Expected - '"+ upsellContent +"'section header title to be present");

            //TODO: implement OOM Page Base and perform an assertion on it's members below
            sa.assertTrue(espnEpaywallPageBase.isPaywallLogInBtnPresent(),
                    "Expected - 'E+ Paywall' 'log in' button to be present");

        }else{
            skipExecution("Skipping this test since '"+ E_PAYWALL_CONTENT_SUBTITLE + "' content not available today");
        }

        //internal tear down (login back to acct)
        espnLogOut();
        espnLogin(USER_BASE_E);

        sa.assertAll();
    }

    //Video suite
    @QTestCases(id = "14653")
    @Test(description = "Subscribed user goes right into video player for VOD", groups = "functionality")
    public void testPlaybackStartsForLoggedInUserForVOD(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay",
                false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");
            initPage(EspnCommonPageBase.class).navigateToPage("watch");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(str, false),
                        "Expected - '"+ str +"' video to be launched");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14648")
    @Test(description = "Verify - When user is watching archive feed the following is present", groups = "functionality")
    public void testVideoControlsForVOD(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(str, false),
                        "Expected - '"+ str +"' video to be launched");
                break;
            }

            //Due to DRM limiting access, only visible controls are being checked for
            sa.assertFalse(videoPageBase.checkMediaPlayerControlsPresent(),
                    "Expected - All media controls should be present with activation of the media controls");
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    //disabling due to inability to see video controls during run-time
    @QTestCases(id = "14654")
    @Test(description = "Playback locations are stored for VODs", groups = "functionality", enabled = false)
    public void testPlaybackRetainsForVOD(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay",
                false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.verifyPlaybackPositionRetainedForReplayContent(str, false),
                        "Expected - '"+ str +"' video to have retained playback position");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "46367")
    @Test(description = "Verify LIVE content is launched", groups = "functionality")
    public void testLaunchLiveContent(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        List<String> espnContent = apiCaller.checkForContentNameBasedOnPackages("live", ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                byte[] ptext = str.getBytes(ISO_8859_1);
                String value = new String(ptext, UTF_8);
                sa.assertTrue(espnPlusPageBase.verifyLiveContent(value, false),
                        "Expected - '"+ str +"' video to have 'live' indicator present");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'live' section titles not available today");
        }

        sa.assertAll();
    }

    //disabling due to inability to see video controls during run-time
    @QTestCases(id = "14649")
    @Test(description = "Verify - Pressing the button when it is displaying the 'pause' symbol will pause the content the user is watching",
            groups = "functionality", enabled = false)
    public void testPauseFunctionality(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(str, false),
                        "Expected - '"+ str +"' video to launch");

                //TODO: don't forget to change the following back
//                pause(20);
//                int x = 1100;
//                int y = 350;
//
//                LOGGER.info("Clicking desired coords.. ");
////                WebDriver drv = getCastedDriver();
////                TouchAction<?> touchAction = new TouchAction((MobileDriver) drv);
////                PointOption point = PointOption.point(x, y);
////                touchAction.tap(point);
//                tap(x, y);
//
//                LOGGER.info("Is video player open: " + videoPageBase.isMediaPlayerPresent());

                //TODO: original code
                sa.assertTrue(videoPageBase.verifyPauseFunctionality(),
                        "Expected - 'Pause' to function properly -> 'new current time' to be the same as 'original current time' right after 'Pause'");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    //disabling due to inability to see video controls during run-time
    @QTestCases(id = "14650")
    @Test(description = "Verify - Pressing the button when it is displaying the \"play\" symbol will resume playing the content the user is watching",
            groups = "functionality", enabled = false)
    public void testPausePlayFunctionality(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.launchContentItemBySubtitle(str, false),
                        "Expected - '"+ str +"' video to launch");
                sa.assertFalse(videoPageBase.verifyPausePlayFunctionality(),
                        "Expected - 'Pause/Play' to function properly -> 'new current time' to be different from 'original current time'  after 'Play' is executed");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14628")
    @Test(description = "Verify: Subnav is present in US edition", groups = "functionality")
    public void testVerifySubnavIsPresentInUSedition(){

        SoftAssert sa = new SoftAssert();
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        LOGGER.info("The page title is: " + L10N.getText("espnWatchPageTitle"));

        sa.assertTrue(L10N.getText("espnWatchPageTitle").equalsIgnoreCase(watchPageBase.getPageHeaderText()),
                "Expected - '" + L10N.getText("espnWatchPageTitle") +  "' page title should be present on a page");
        sa.assertTrue(L10N.getText("espnWatchFeaturedTab").equalsIgnoreCase(watchPageBase.
                        getWatchTabText(EspnWatchPageBase.WatchTabs.FEATURED)),
                "Expected - '" + L10N.getText("espnWatchFeaturedTab") +  "' tab name should be present on a page");
        sa.assertTrue(L10N.getText("espnWatchOriginalsTab").equalsIgnoreCase(watchPageBase.
                        getWatchTabText(EspnWatchPageBase.WatchTabs.ORIGINALS)),
                "Expected - '" + L10N.getText("espnWatchOriginalsTab") +  "' tab name should be present on a page");

        sa.assertAll();
    }

    @QTestCases(id = "14629")
    @Test(description = "Verify Watch Tab Subnav contents", groups = "functionality")
    public void testVerifyWatchTabSubnavContents(){

        SoftAssert sa = new SoftAssert();
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        sa.assertTrue(L10N.getText("espnWatchPageTitle").equalsIgnoreCase(watchPageBase.getPageHeaderText()),
                "Expected - '" + L10N.getText("espnWatchPageTitle") +  "' page title should be present on a page");
        sa.assertFalse(watchPageBase.isWatchTabSelected(L10N.getText("espnWatchOriginalsTab")),
                "Expected - '" + L10N.getText("espnWatchOriginalsTab") +  "' tab name should be present and selected");
        sa.assertTrue(watchPageBase.isWatchTabSelected(L10N.getText("espnWatchFeaturedTab")),
                "Expected - '" + L10N.getText("espnWatchFeaturedTab") +  "' tab name should be present and selected");

        sa.assertAll();
    }

    //disabling this test due to functionality change on UI layer
    @QTestCases(id = "14630")
    @Test(description = "Scenario: User taps Subnav option", groups = "functionality", enabled = false)
    public void testUserTapsSubnavOption(){

        SoftAssert sa = new SoftAssert();
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        watchPageBase.setWatchTab(EspnWatchPageBase.WatchTabs.FEATURED);

        sa.assertTrue(L10N.getText("espnWatchPageTitle").equalsIgnoreCase(watchPageBase.getPageHeaderText()),
                "Expected - '" + L10N.getText("espnWatchPageTitle") +  "' page title should be present on a page");
        sa.assertTrue(watchPageBase.isWatchTabSelected("FEATURED"),
                "Expected - '" + L10N.getText("espnWatchFeaturedTab") +  "' tab name should be present and selected");


        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        //this part is getting data for ESPN+ that was previusly a subnav tab.
        // Therefore, test currently fails since it searches for ESPN+ content under Watch > Featured Tab
        // Searching for content outside of ESPN+ is outside of this test suite scope
        if (apiCaller.checkForAllContent().size() > 0) {
            LOGGER.info("Carousel headers sections content found.. ");

            //using second list item due to first being used in a different format than the rest, if present, continue with assert, if not, skip
            sa.assertTrue(watchPageBase.isWatchTabSectionHeaderPresent(apiCaller.checkForAllContent().get(1), true),
                    "Expected - '"+ apiCaller.checkForAllContent().get(1) +"'section header title to be present");
        }else{
            skipExecution("Skipping this test since content not available today");
        }

        sa.assertAll();
    }

    //this test case is not applicable anymore, disabling this test case
    @QTestCases(id = "14631")
    @Test(description = "Scenario: Subnav hides when user scrolls down", groups = "functionality", enabled = false)
    public void testSubnavHidesDuringScroll(){

        SoftAssert sa = new SoftAssert();
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        sa.assertTrue(initPage(EspnWatchPageBase.class).isOpened(),
                "Expected - 'Watch Page' should be displayed");

        //swiping down to hide Subnav
        watchPageBase.swipeWatchListContainer(10, Direction.UP);

        sa.assertTrue(watchPageBase.isWatchTabPresent(EspnWatchPageBase.WatchTabs.ORIGINALS),
                "Expected - '" + L10N.getText("espnWatchOriginalsTab") +  "' tab should be present on a page");
        sa.assertTrue(watchPageBase.isWatchTabPresent(EspnWatchPageBase.WatchTabs.FEATURED),
                "Expected - '" + L10N.getText("espnWatchFeaturedTab") +  "' tab should be present on a page");

        //swiping up to check if Subnav is present
        watchPageBase.swipeWatchListContainer(10, Direction.DOWN);

        sa.assertTrue(watchPageBase.isWatchTabPresent(EspnWatchPageBase.WatchTabs.ORIGINALS),
                "Expected - '" + L10N.getText("espnWatchOriginalsTab") +  "' tab should be present on a page");
        sa.assertTrue(watchPageBase.isWatchTabPresent(EspnWatchPageBase.WatchTabs.FEATURED),
                "Expected - '" + L10N.getText("espnWatchFeaturedTab") +  "' tab should be present on a page");

        sa.assertAll();
    }

    @QTestCases(id = "14632")
    @Test(description = "Verify: As user scrolls through subnav pages, carousels remain at their scroll point", groups = "functionality")
    public void testVerifyCarouselRemainsViewportAfterScrollMainListView(){

        SoftAssert sa = new SoftAssert();
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);
        String sectionTitle = "";

        internalSetup(EspnCommonPageBase.FooterTabs.WATCH_TAB.getText());

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        if (!apiCaller.checkForAllContent().isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            //TODO: Challenge to find carousel content
            for (String str : apiCaller.checkForAllContent()){
                LOGGER.info("Section Title: " + str);
                if (!str.contains("Featured") && !str.equals("Live")){
                    sectionTitle = str;
                    break;

                }
            }
        }

        watchPageBase.setWatchTab(EspnWatchPageBase.WatchTabs.FEATURED);

        //using second list item due to first being used in a different format than the rest, if present, continue with assert, if not, skip
        LOGGER.info("Searching for '" + sectionTitle + "' section header...");
        sa.assertTrue(watchPageBase.isWatchTabSectionHeaderPresent(sectionTitle, true),
                "Expected - '"+ sectionTitle +"'section header title to be present");
        List<String> tileTitles = watchPageBase.getVisibleSectionTileTitles(sectionTitle,
                Direction.LEFT, 5, true);

        watchPageBase.swipeWatchListContainer(5, Direction.UP);
        watchPageBase.swipeWatchListContainer(5, Direction.DOWN);

        //ATTENTION: do not modify this assertion block, it works as expected > fails
        //right now due to a bug where carousel does NOT retain viewport of scrolled tiles
        sa.assertFalse(watchPageBase.getVisibleSectionTileTitles(sectionTitle,
                Direction.LEFT,5, false).containsAll(tileTitles),
                "Expected - '"+ tileTitles +"' to be present in '"+ sectionTitle + "'carousel container");

        sa.assertAll();
    }

    @QTestCases(id = "14633")
    @Test(description = "Verify: Carousel without 'See All'", groups = "functionality")
    public void testVerifyCarouselWithoutSeeAllOption(){

        SoftAssert sa = new SoftAssert();
        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        List<String> espnContent = apiCaller.checkForSectionTitlesWithOrWithoutSeeAllOption(false);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                if(str.equals("Live"))
                    continue;
                //check for "See ALL" option not being present for the given section title
                LOGGER.info("Verifying Carousel: " + str);
                sa.assertFalse(espnPlusPageBase.checkSeeAllBtnPresent(str),
                        "Expected - '"+ str +"' section to not have 'See All' btn attached to it");
            }
        }else{
            skipExecution("Skipping this test since section titles without 'See All' options not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14634")
    @Test(description = "Verify: Carousel with 'See All'", groups = "functionality")
    public void testVerifyCarouselWithSeeAllOption(){

        //TODO: keep an eye on this test case due to inconsistent results due to ESPN not giving access to appium to obtain screen elements.
        //TODO: this failure usually is followed after all desired elements are located by scroller
        SoftAssert sa = new SoftAssert();
        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        List<String> espnContent = apiCaller.checkForSectionTitlesWithOrWithoutSeeAllOption(true);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                //check for "See ALL" option being present for the given section title
                LOGGER.info("Verifying Carousel: " + str);
                sa.assertTrue(espnPlusPageBase.checkSeeAllBtnPresent(str),
                        "Expected - '"+ str +"' section to have 'See All' btn attached to it");
            }
        }else{
            skipExecution("Skipping this test since section titles with 'See All' options not available today");
        }

        sa.assertAll();
    }

    //disabling this test case due to media player elements not accessible for LIVE content at this point
    @QTestCases(id = "14635")
    @Test(description = "Verify: Live assets' metadata includes title, network, sport/league", groups = "functionality",
    enabled = false)
    public void testLiveContentMetadata(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        List<String> espnContent = apiCaller.checkForContentNameBasedOnPackages("live", ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertFalse(espnPlusPageBase.verifyContentMetadata("live", str),
                        "Expected - '"+ str +"' item to have all metadata present");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'live' section titles not available today");
        }

        sa.assertAll();
    }

    //disabling this test case due to possible LIVE event present during test runtime
    //(confirmed to cause element accessibility issue by running same test against Watch screen that doesn't
    // automatically playback live content)
    @QTestCases(id = "14636")
    @Test(description = "Verify: VOD assets' metadata includes title", groups = "functionality",
    enabled = false)
    public void testVODcontentMetadata(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        //TODO test withDate to true
        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {

                sa.assertFalse(espnPlusPageBase.verifyContentMetadata("vod", str),
                        "Expected - '"+ str +"' item to have all metadata present");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    @QTestCases(id = "14637")
    @Test(description = "Verify: Non-playable assets do not show meta data", groups = "functionality")
    public void testNonPlayableContentMetadata(){

        SoftAssert sa = new SoftAssert();

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        internalSetup(EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());

        EspnPlusPageBase espnPlusPageBase = initPage(EspnPlusPageBase.class);

        List<String> espnContent = apiCaller.getNonPlayableAssets();

        if (!espnContent.isEmpty()) {
            LOGGER.info("Carousel headers sections content found.. ");

            for (String str : espnContent) {
                sa.assertTrue(espnPlusPageBase.verifyContentMetadata("nonMetaData", str),
                        "Expected - '"+ str +"' item not to have any metadata present");
                break;
            }
        }else{
            skipExecution("Skipping this test since 'replay' section titles not available today");
        }

        sa.assertAll();
    }

    //For debugging purposes
    @QTestCases(id = "46365")
    @Test(description = "Debugging ESPN OOM paywall content API call", groups = "functionality")
    public void testApiCall(){
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        EspnAndroidAcceptanceAPIcaller apiCaller = new EspnAndroidAcceptanceAPIcaller();

        List<String> espnContent = apiCaller.checkForVODcontentNameBasedOnPackages("replay", false, ESPN_PLUS_PACKAGE);
        String upsellContent = " ";

        if (!espnContent.isEmpty()) {
            for (String str : espnContent) {
                LOGGER.info("Content contains: " + str);
            }
        }
    }
}
