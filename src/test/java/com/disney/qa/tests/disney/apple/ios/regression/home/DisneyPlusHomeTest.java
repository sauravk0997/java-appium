package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.config.DisneyParameters;
import com.disney.qa.api.explore.ExploreApi;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.explore.response.ExploreSetResponse;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String DISNEY_PLUS = "Disney Plus";
    private static final String PARTNER = "disney";
    private static final String RECOMMENDATIONS_SET_ID = "7894d9c6-43ab-4691-b349-cf72362095dd";

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
        setAppToHomeScreen(getAccount());

        //Validate top of home
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found.");
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");
        homePage.swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouLastTileInView = getElementImage(homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        homePage.swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouFirstTileInView = getElementImage(homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        sa.assertTrue(areImagesDifferent(recommendedForYouFirstTileInView, recommendedForYouLastTileInView), "Recommended For You first tile in view and last tile in view images are the same.");

        BufferedImage topOfHome = getCurrentScreenView();

        //Capture bottom of home
        swipeInContainer(null, Direction.UP, 5, 500);
        BufferedImage closeToBottomOfHome = getCurrentScreenView();

        //Validate back at top of home
        swipePageTillElementPresent(homePage.getImageLabelContains(DISNEY_PLUS), 10, null, Direction.DOWN, 300);
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found after return to top of home.");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(topOfHome, closeToBottomOfHome),
                "Top of home image is the same as bottom of home image.");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67377"})
    @Test(description = "Home - Recommended for You", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyRecommendedForYouContainer() throws URISyntaxException, JsonProcessingException {
        int limit = 30;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount account = getAccount();
        setAppToHomeScreen(account);
        Assert.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");

        ArrayList<Item> recommendationSetItemsFromApi = getContainerDetailsFromAPI(account, RECOMMENDATIONS_SET_ID, limit);
        List<String> recommendationTitlesFromApi = new ArrayList<>();
        recommendationSetItemsFromApi.forEach(item ->
                recommendationTitlesFromApi.add(item.getVisuals().getTitle()));

        int size = recommendationTitlesFromApi.size();
        String firstCellTitle = homePage.getFirstCellTitleFromRecommendedForYouContainer().split(",")[0];
        ExtendedWebElement firstTitle = homePage.getCellElementFromRecommendedForYouContainer(recommendationTitlesFromApi.get(0));
        ExtendedWebElement lastTitle = homePage.getCellElementFromRecommendedForYouContainer(recommendationTitlesFromApi.get(size-1));
        Assert.assertTrue(firstCellTitle.equals(recommendationTitlesFromApi.get(0)), "UI title value not matched with API title value");

        homePage.swipeInContainerTillElementIsPresent(homePage.getRecommendedForYouContainer(), lastTitle, 30, Direction.LEFT );
        Assert.assertTrue(lastTitle.isPresent(), "User is not able to swipe through end of container");

        homePage.swipeInContainerTillElementIsPresent(homePage.getRecommendedForYouContainer(), firstTitle, 30, Direction.RIGHT);
        Assert.assertTrue(firstTitle.isPresent(), "User is not able to swipe to the begining of container");

        firstTitle.click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page was not opened");
        sa.assertTrue(detailsPage.getMediaTitle().equals(firstCellTitle), "Content title not matched");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isOpened(), "Home page did not open");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");
        sa.assertTrue(firstTitle.isPresent(), "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }

    private ArrayList<Item> getContainerDetailsFromAPI(DisneyAccount account, String setId, int limit) {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .partner(PARTNER)
                .environment(DisneyParameters.getEnv())
                .build();
        ExploreApi exploreApi = new ExploreApi(apiConfiguration);
        ExploreSearchRequest exploreSetRequest = ExploreSearchRequest.builder().setId(setId)
                .profileId(account.getProfileId())
                .limit(limit)
                .build();
        try{
            ExploreSetResponse containerSet = exploreApi.getSet(exploreSetRequest);
            return containerSet.getData().getSet().getItems();
        } catch (URISyntaxException | JsonProcessingException e){
            UNIVERSAL_UTILS_LOGGER.error(String.valueOf(e));
            return ExceptionUtils.rethrow(e);
        }
    }
}
