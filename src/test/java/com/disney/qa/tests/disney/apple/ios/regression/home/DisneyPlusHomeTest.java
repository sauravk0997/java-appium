package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.disney.qa.api.search.assets.DisneyCollectionType.PERSONALIZED_COLLECTION;
import static com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest.SUB_VERSION;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String NEW_TO_DISNEY = "New To Disney+";
    private static final String COLLECTIONS = "Collections";
    private static final String DISNEY_PLUS = "Disney Plus";
    private static final String PERSONALIZED_COLLECTION = "PersonalizedCollection";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62276"})
    @Test(description = "Home - Deeplink", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_home_deeplink"), 10);
        homePage.clickOpenButton();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(description = "Home - Home Screen UI Elements", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeUIElements() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        setAppToHomeScreen(entitledUser);

        List<ContentSet> allSets = getSearchApi().getAllSetsInHomeCollection(entitledUser, getCountry(), getLanguage(), PERSONALIZED_COLLECTION);
        CollectionRequest collectionRequest = CollectionRequest.builder().region(getCountry()).collectionType(PERSONALIZED_COLLECTION).
                account(entitledUser).language(getLanguage()).slug(DisneyStandardCollection.HOME.getSlug()).
                contentClass(DisneyStandardCollection.HOME.getSlug()).build();
        ContentCollection collection = getSearchApi().getCollection(collectionRequest);
        LOGGER.info("Collection json node to string: " + collection.getJsonNode().toString());
//        List<String> collections = DisneySearchApi.parseValueFromJson(collection.getJsonNode().toString());
        for (int i=1; i<allSets.size(); i++) {
            var shelfTitle = allSets.get(i).getSetName();
            var getSetAssets = allSets.get(i).getTitles();
            LOGGER.info("What is shelf tile: " + shelfTitle);
            LOGGER.info("What are set assets: " + getSetAssets);
        }

        homePage.clickSearchIcon();
        searchPage.searchForMedia("Bluey");
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(90);
        videoPlayer.clickBackButton();
        detailsPage.clickHomeIcon();

        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found.");
        sa.assertTrue(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found.");
        sa.assertTrue(homePage.getTypeOtherContainsLabel(NEW_TO_DISNEY).isPresent(), "'New to Disney+' image was not found.");


        //Get top of home image
        BufferedImage topOfHome = getCurrentScreenView();

        //Validate Collections is present after swiping to end of home.
        swipePageTillElementPresent(homePage.getTypeOtherContainsLabel(COLLECTIONS), 10,null, Direction.UP, 300);
        sa.assertTrue(homePage.getTypeOtherContainsLabel(COLLECTIONS).isPresent(), "'Collections' container was not found.");
        sa.assertFalse(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "Disney Plus image was found after swiping up.");

        //Get bottom of image with first `collections` tile
        BufferedImage bottomOfHomeWithFirstCollectionsTile = getCurrentScreenView();

        //Validate swiping left through 'Collections'
        sa.assertTrue(homePage.isAllCollectionsPresent(), "`All Collections` was not found after swiping to end of Collection");

        //Get bottom of home with last 'collections' tile
        BufferedImage bottomOfHomeWithLastCollectionsTile = getCurrentScreenView();

        //Validate swiping right through 'Collections'
        sa.assertTrue(homePage.isBlackStoriesCollectionPresent(),
                "`Black Stories Collection` was not found after swiping to beginning of Collection");

        //Validate Recommended For You is present after swiping back to top of home.
        swipePageTillElementPresent(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU), 10,null, Direction.DOWN, 300);
        sa.assertTrue(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found.");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(bottomOfHomeWithFirstCollectionsTile, bottomOfHomeWithLastCollectionsTile),
                "Bottom of image with first `collections` tile is the same bottom of home with last 'collections' tile");
        sa.assertTrue(areImagesDifferent(topOfHome, bottomOfHomeWithFirstCollectionsTile),
                "Top of home image is the same as bottom of home image.");
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found after return to top of home.");
        sa.assertAll();
    }

    private void collectionBuild() {
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getContentClass())
                .account(testAccount)
                .build();

        collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.MOVIES.getSlug())
                .contentClass(DisneyStandardCollection.MOVIES.getContentClass())
                .account(testAccount)
                .build();

        ContentCollection contentCollection = getSearchApi().getCollection(collectionRequest);
        List<DisneyCollectionSet> setInfo = contentCollection.getCollectionSetsInfo();
    }
}
