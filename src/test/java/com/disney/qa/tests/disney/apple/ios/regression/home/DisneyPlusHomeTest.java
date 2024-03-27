package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.proxy.RestTemplateBuilder;
import com.disney.qa.api.client.requests.content.DisneyContentParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String DISNEY_PLUS = "Disney Plus";
    private static final String BEARER = "bearer ";
    private static final String API_ERROR = "Request failed with the following exception: {}";
    private static final String DISNEY_CONTENT_BAMGRID_URL = "https://disney.content.edge.bamgrid.com/";
    private static final String RECOMMENDATION_SET_NODE = "RecommendationSet";
    private static final String GENERIC_PATH = "version/6.1/region/%s/audience/false/maturity/1830/language/%s/setId/%s/pageSize/60/page/1";
    private static final String RECOMMENDATION_SET = DisneyContentParameters.getContentService(RECOMMENDATION_SET_NODE);
    private final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();

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
    public void verifyRecommendedForYouContainer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount account = getAccount();
        setAppToHomeScreen(account);
        Assert.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");
        List<String> tilesFromApi = new ArrayList<>();

        JsonNode js = getRecommendationSet(account, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(),
                CollectionConstant.getCollectionName(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        js.findPath("data").findPath(RECOMMENDATION_SET_NODE).findPath("items")
                .forEach(item -> tilesFromApi.add(getMediaTitle(item)));

        int size = tilesFromApi.size();
        String firstCellTitle = homePage.getFirstCellTitleFromRecommendedForYouContainer();
        ExtendedWebElement firstTitle = homePage.getStaticCellByLabel(tilesFromApi.get(0));
        ExtendedWebElement lastTitle = homePage.getStaticCellByLabel(tilesFromApi.get(size-1));
        Assert.assertTrue(firstCellTitle.equals(tilesFromApi.get(0)), "UI Title value not matched with API Title value ");

        homePage.swipeInContainerTillElementIsPresent(homePage.getRecommendedForYouContainer(), lastTitle, 20, Direction.LEFT );
        Assert.assertTrue(lastTitle.isPresent(), "User is not able to swipe through end of container");

        homePage.swipeInContainerTillElementIsPresent(homePage.getRecommendedForYouContainer(), firstTitle, 20, Direction.RIGHT);
        Assert.assertTrue(firstTitle.isPresent(), "User is not able to swipe to the begining of container");

        firstTitle.click();
        sa.assertTrue(detailsPage.isOpened(), "Detais Page was not opened");
        sa.assertTrue(detailsPage.getMediaTitle().equals(firstCellTitle), "Different details page opened");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");
        sa.assertTrue(firstTitle.isPresent(), "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }

    private JsonNode getRecommendationSet(DisneyAccount account, String locale, String language, String setId){
        try {
            HttpHeaders headers = new HttpHeaders();
            String genericSetPath = String.format(GENERIC_PATH, locale, language, setId);
            URI uri = new URI(DISNEY_CONTENT_BAMGRID_URL + RECOMMENDATION_SET + genericSetPath);
            headers.add(HttpHeaders.AUTHORIZATION, BEARER + getAccount().getAccountToken());
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).build();
            RequestEntity<JsonNode> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
            return restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (MalformedURLException | URISyntaxException e) {
            LOGGER.error("API Error attempting to fetch set ID {}. {}: {}", setId, API_ERROR, e);
            throw new RuntimeException(e);
        }
    }

    private String getMediaTitle(JsonNode item) {
        if(item.at("/text/title/full/" + "series" + "/default/content").isTextual()){
            return item.at("/text/title/full/" + "series" + "/default/content").asText();
        }
        return item.at("/text/title/full/" + "program" + "/default/content").asText();
    }
}
