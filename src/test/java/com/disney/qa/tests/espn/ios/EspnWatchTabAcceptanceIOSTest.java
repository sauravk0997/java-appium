package com.disney.qa.tests.espn.ios;

import com.disney.qa.api.espn.mobile.EspnIOSAcceptanceAPIcaller;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.home.EspnHomeIOSPageBase;
import com.disney.qa.espn.ios.pages.listen.EspnListenIOSPageBase;
import com.disney.qa.espn.ios.pages.scores.EspnScoresIOSPageBase;
import com.disney.qa.espn.ios.pages.sports.EspnSportsIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchEspnPlusIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchFeaturedIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchOriginalsIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.report.testrail.TestRailCases;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;


public class EspnWatchTabAcceptanceIOSTest extends BaseMobileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private static final String EXPECTED = "Expected: ";
    private static final String DEVICE_TYPE = "capabilities.deviceType";
    private static final String GAME_STATUS_LIVE = "Live";


    @TestRailCases(testCasesId = "1514504")
    @Test(description = "ESPN+ is Default Tab Sub Nav")
    public void testEspnPlusDefaultView() {

        SoftAssert softAssert = new SoftAssert();

        Screenshot.capture(getDriver(), "ESPN+");

        softAssert.assertTrue(initPage(EspnWatchIOSPageBase.class).isEspnPlusDefaultSubNav(),
                EXPECTED + "ESPN+ sub nav page should be displayed");

        softAssert.assertAll();
    }


    @TestRailCases(testCasesId = "1514505")
    @Test(description = "Watch Tab is Opened when clicked")
    public void testWatchTabOpen() {

        SoftAssert softAssert = new SoftAssert();

        Screenshot.capture(getDriver(), "Watch Title");

        softAssert.assertTrue(initPage(EspnWatchIOSPageBase.class).isOpened(),
                EXPECTED + "Watch Title should be displayed");
        /**
         *
         * Missing Second Assertion: the app navigation changes to the dark version of the UI
         * (Test Rail link = https://qa-reporting.us-east-1.bamgrid.net/testrail/index.php?/cases/view/1484557)
         *
         */

        softAssert.assertAll();
    }


    @TestRailCases(testCasesId = "1514506,1514509")
    @Test(description = "Other Tabs are Accessible and Schedule button is not present")
    public void testOtherSectionsAndScheduleBtn() {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

        EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(EspnWatchIOSPageBase.class);

        softAssert.assertTrue(espnWatchIOSPageBase.isScheduleBtnPresent(20),
                EXPECTED + "Schedule button to be displayed in Watch page");

        espnIOSPageBase.getTab(EspnIOSPageBase.TabOptions.HOME);

        softAssert.assertTrue(initPage(EspnHomeIOSPageBase.class).isOpened(),
                EXPECTED + "Espn Home Logo should be displayed");

        softAssert.assertTrue(!espnWatchIOSPageBase.isScheduleBtnPresent(3),
                "Schedule icon should not be present in Home tab");

        if("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {

            espnIOSPageBase.getTab(EspnIOSPageBase.TabOptions.SCORES);

            softAssert.assertTrue(initPage(    EspnScoresIOSPageBase.class).isOpened(),
                    EXPECTED + "'Scores' title should be displayed");

            softAssert.assertTrue(!espnWatchIOSPageBase.isScheduleBtnPresent(3),
                    "Schedule icon should not be present in Scores tab");
        }
        espnIOSPageBase.getTab(EspnIOSPageBase.TabOptions.LISTEN);

        softAssert.assertTrue(initPage(EspnListenIOSPageBase.class).isOpened(),
                EXPECTED + "'Listen' title should be displayed");

        softAssert.assertTrue(!espnWatchIOSPageBase.isScheduleBtnPresent(3),
                "Schedule icon should not be present in Listen tab");

        espnIOSPageBase.getTab(EspnIOSPageBase.TabOptions.SPORTS);

        softAssert.assertTrue(initPage(EspnSportsIOSPageBase.class).isOpened(),
                EXPECTED + "'Sports' title should be displayed");

        softAssert.assertTrue(!espnWatchIOSPageBase.isScheduleBtnPresent(3),
                "Schedule icon should not be present in Sports tab");

        if("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            LOGGER.info("Tapping screen to dismiss Sports subnav page");
            new IOSUtils().screenPress(2,4);
        }

        espnIOSPageBase.getTab(EspnIOSPageBase.TabOptions.WATCH);

        softAssert.assertAll();
    }


    @TestRailCases(testCasesId = "1514507")
    @Test(description = "Verify: Carousel header displayed")
    public void testCarouselHeaderDisplayed() {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        initPage(EspnIOSPageBase.class).logInNavigateToWatchPage("QA");

        if(!espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED).isEmpty()) {

            LOGGER.info("Checking carousel header for (first) bucket name: '" +
                    espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED).get(2)
                    + "'");

            initPage(    EspnIOSPageBase.class).refreshPage();

            Screenshot.capture(getDriver(), "Carousel Header");

            initPage(    EspnWatchIOSPageBase.class).getSubnav(EspnIOSPageBase.WatchSubnavOptions.FEATURED);

            softAssert.assertTrue(initPage(EspnWatchEspnPlusIOSPageBase.class)
                            .isWatchTabSectionHeaderPresent(espnIOSAcceptanceAPIcaller
                                    .getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                                    .get(2)),
                    EXPECTED + "'" + espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED).get(2).toUpperCase() + "' section header title to be present");

        } else skipExecution(GAME_STATUS_LIVE + " content not available, skipping test");

        initPage(EspnWatchIOSPageBase.class).scrollToSubNavOptions();

        softAssert.assertAll();
    }


    @TestRailCases(testCasesId = "1514508")
    @Test(description = "Verify: Carousel header NOT displayed")
    public void testCarouselHeaderNOTDisplayed() {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        if(espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                .contains(GAME_STATUS_LIVE)){

            LOGGER.info(GAME_STATUS_LIVE + " content found.. ");

            initPage(    EspnIOSPageBase.class).refreshPage();

            Screenshot.capture(getDriver(), "No Carousel Header");

            for (String str : espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED)) {
                if (str.isEmpty()){
                    softAssert.assertTrue(initPage(            EspnWatchEspnPlusIOSPageBase.class)
                                    .isWatchTabSectionHeaderNotPresent(espnIOSAcceptanceAPIcaller
                                            .getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                                            .get(1)),
                            "Expected - 'an empty' section header title to be present");
                    break;
                }
            }
        } else skipExecution("Skipping this test since 'Empty Header' section titles not available today");

        softAssert.assertAll();
    }


    /** Subnav section **/

    @TestRailCases(testCasesId = "1537099")
    @Test(description = "Verifying Subnav contents are displayed")
    public void testSubnavContents() {

        SoftAssert softAssert = new SoftAssert();

        Screenshot.capture(getDriver(), "Subnav Contents");

        softAssert.assertTrue(initPage(EspnWatchIOSPageBase.class).areSubnavContentsPresent(),
               EXPECTED + "All Subnav options to be present");

        softAssert.assertAll();
    }

    @TestRailCases(testCasesId = "1537100")
    @Test(description = "Verify other subnav sections open")
    public void testSubnavSectionsOpen() {

        SoftAssert softAssert = new SoftAssert();

        EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(EspnWatchIOSPageBase.class);

        espnWatchIOSPageBase.getSubnav(EspnIOSPageBase.WatchSubnavOptions.ORIGINALS);

        Screenshot.capture(getDriver(), "Original Page");

        softAssert.assertTrue(initPage(EspnWatchOriginalsIOSPageBase.class).isOpened(),
                EXPECTED + "Originals sub page to be opened");

        espnWatchIOSPageBase.getSubnav(EspnIOSPageBase.WatchSubnavOptions.FEATURED);

        Screenshot.capture(getDriver(), "Featured Page");

        softAssert.assertTrue(initPage(EspnWatchFeaturedIOSPageBase.class).isOpened(),
                EXPECTED + "Featured sub page to be opened");

        softAssert.assertAll();
    }


    //TODO #C1537101 testSubnavHorizontalScroll()


    @TestRailCases(testCasesId = "1537102")
    @Test(description = "Scrolling Down Hides Subnav Options")
    public void testSubnavVerticalScroll() {

        SoftAssert softAssert = new SoftAssert();

        EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(EspnWatchIOSPageBase.class);

        softAssert.assertTrue(espnWatchIOSPageBase.scrollDownAndCheckSubnavNotPresent(),
                EXPECTED + "Subnav should not be visible");

        softAssert.assertTrue(espnWatchIOSPageBase.scrollUpAndCheckSubnavPresent(),
                EXPECTED + "Subnav should be visible");

        softAssert.assertAll();
    }



    /** Asset/Content Layout **/

    @TestRailCases(testCasesId = "1548553")
    @Test(description = "Verifying Live asset metadata")
    public void testLiveAssetMetadata() {

        SoftAssert softAssert = new SoftAssert();

        initPage(EspnWatchIOSPageBase.class).getSubnav(EspnIOSPageBase.WatchSubnavOptions.FEATURED);

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        if(espnIOSAcceptanceAPIcaller.getBucketNames(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                .contains(GAME_STATUS_LIVE)) {

            LOGGER.info(GAME_STATUS_LIVE + " content found..");

            initPage(EspnIOSPageBase.class).refreshPage();

            softAssert.assertTrue(initPage(    EspnWatchFeaturedIOSPageBase.class).isLiveMetadataPresent(1),
                    EXPECTED + "'" + espnIOSAcceptanceAPIcaller
                            .getLiveCarouselTitles(EspnIOSPageBase.WatchSubnavOptions.FEATURED).get(1)
                            + "'" + " Metadata should be displayed for Live content");

            Screenshot.capture(getDriver(), "Live Asset Metadata");

        } else skipExecution(GAME_STATUS_LIVE + " content not available, skipping test");

        softAssert.assertAll();

    }

    @TestRailCases(testCasesId = "1548554")
    @Test(description = "VOD asset metada contains title only")
    public void testVODassetMetadata() {

        int firstVodContent = 0;

        int minimumCarouselCount = 0;

        SoftAssert softAssert = new SoftAssert();

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        if(espnIOSAcceptanceAPIcaller.getContentTypes(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                .contains("vod")) {

            LOGGER.info("VOD's found..");

            initPage(EspnIOSPageBase.class).refreshPage();

            EspnWatchFeaturedIOSPageBase espnWatchFeaturedIOSPageBase = initPage(    EspnWatchFeaturedIOSPageBase.class);

            espnWatchFeaturedIOSPageBase.scrollToVODContent(firstVodContent, minimumCarouselCount);

            softAssert.assertTrue(espnWatchFeaturedIOSPageBase.isVODMetadataNotPresent(0),
                    EXPECTED + "Only VOD title should be displayed");

            Screenshot.capture(getDriver(), "VOD asset metada");
        }
       initPage(EspnWatchIOSPageBase.class).scrollToSubNavOptions();

        softAssert.assertAll();
    }

    @TestRailCases(testCasesId = "1548555")
    @Test(description = "Non playable asset shows no metadata")
    public void testNonPlayableAsset() {

        SoftAssert softAssert = new SoftAssert();

        initPage(EspnIOSPageBase.class).navigateToWatchPageWithoutLogin().getSubnav(EspnIOSPageBase.WatchSubnavOptions.FEATURED);

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        if(!espnIOSAcceptanceAPIcaller.getNonPlayableAssets(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                .isEmpty()) {

            LOGGER.info("Non Playable Asset present");

            initPage(    EspnIOSPageBase.class).refreshPage();

            softAssert.assertTrue(initPage(    EspnWatchFeaturedIOSPageBase.class).noMetadataForNonPlayableAsset(),
                    EXPECTED + "Non playable asset should have no metadata");

        } else skipExecution("Playable Asset currently not available");

        softAssert.assertAll();
    }


    //TODO #C1548556 espnPlusContentLogoCheck
    //TODO #C1548557 nonEspnPlusContentLogoCheck
    //TODO #C1548558 mlbBrandingCheck
    //TODO #C1548559 nhlBrandingCheck


    /** Carousels **/

    @TestRailCases(testCasesId = "1548560")
    @Test(description = "Swiping subnav retains position after scroll")
    public void testSubnavPositionAfterSwipe() {

        int numberOfSwipes = 3;

        int firstVodContent = 0;

        int minimumCarouselCount = 3;

        SoftAssert softAssert = new SoftAssert();

        initPage(EspnWatchIOSPageBase.class).getSubnav(EspnIOSPageBase.WatchSubnavOptions.FEATURED);

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        if(espnIOSAcceptanceAPIcaller.getContentTypes(EspnIOSPageBase.WatchSubnavOptions.FEATURED)
                .contains("vod")) {

            LOGGER.info("'VOD'" + " content found..");

            initPage(EspnIOSPageBase.class).refreshPage();

            EspnWatchFeaturedIOSPageBase espnWatchFeaturedIOSPageBase = initPage(    EspnWatchFeaturedIOSPageBase.class);

            espnWatchFeaturedIOSPageBase.scrollToVODContent(firstVodContent, minimumCarouselCount);

            espnWatchFeaturedIOSPageBase.swipeVODCarousel(numberOfSwipes);

            EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(    EspnWatchIOSPageBase.class);

            espnWatchIOSPageBase.scrollToSubNavOptions();

            Screenshot.capture(getDriver(), "Scrolled Up to Subnav Options");

            espnWatchFeaturedIOSPageBase.scrollToVODContent(numberOfSwipes, minimumCarouselCount);

            Screenshot.capture(getDriver(), "Scrolled Down to VOD content");

            softAssert.assertTrue(initPage(    EspnWatchFeaturedIOSPageBase.class).isVODMetadataNotPresent(firstVodContent),
                    EXPECTED + "First Carousel should NOT be displayed after scrolling down");

            espnWatchIOSPageBase.scrollToSubNavOptions();

            softAssert.assertAll();
        }
    }

    @TestRailCases(testCasesId = "1548561")
    @Test(description = "Carousel less than ten assets don't display See All option")
    public void testCarouselWithoutSeeAll() {

        SoftAssert softAssert = new SoftAssert();

        EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(EspnWatchIOSPageBase.class);

        espnWatchIOSPageBase.getSubnav(EspnIOSPageBase.WatchSubnavOptions.ESPNPLUS);

        EspnWatchEspnPlusIOSPageBase espnWatchEspnPlusIOSPageBase = initPage(EspnWatchEspnPlusIOSPageBase.class);

        espnWatchEspnPlusIOSPageBase.scrollToFirstNoSelfTitle();

        softAssert.assertTrue(espnWatchEspnPlusIOSPageBase.isFirstSeeAllNotPresent(),
                EXPECTED + "See All button shouldn't be displayed since no self link present");

        espnWatchIOSPageBase.scrollToSubNavOptions();

        softAssert.assertAll();
    }

    @TestRailCases(testCasesId = "1548562")
    @Test(description = "Carousel less than ten assets don't display See All option")
    public void testCarouselWithSeeAll() {

        SoftAssert softAssert = new SoftAssert();

        EspnWatchEspnPlusIOSPageBase espnWatchEspnPlusIOSPageBase = initPage(EspnWatchEspnPlusIOSPageBase.class);

        espnWatchEspnPlusIOSPageBase.scrollToFirstSelfTitle();

        Screenshot.capture(getDriver(), "Self Title");

        softAssert.assertTrue(espnWatchEspnPlusIOSPageBase.isFirstSeeAllPresent(),
                EXPECTED + "See All button should be displayed since self link present");

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        String firstSelfCarouselHeader = espnIOSAcceptanceAPIcaller
                .getSelfTitles(EspnIOSPageBase.WatchSubnavOptions.ESPNPLUS).get(1);

        EspnWatchIOSPageBase espnWatchIOSPageBase = initPage(EspnWatchIOSPageBase.class);

        espnWatchIOSPageBase.getSeeAll(firstSelfCarouselHeader);

        softAssert.assertTrue(espnWatchIOSPageBase.isSeeAllOpened(firstSelfCarouselHeader),
                EXPECTED + "See All page should be opened");

        espnWatchIOSPageBase.getWatchPage();

        initPage(EspnWatchIOSPageBase.class).scrollToSubNavOptions();

        softAssert.assertAll();
    }


}
