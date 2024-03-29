package com.disney.qa.disney.apple.pages.common;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.disney.proxy.RestTemplateBuilder;
import com.disney.qa.api.client.requests.content.DisneyContentParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.constant.CollectionConstant;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.WebDriver;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomeIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String RECOMMENDED_FOR_YOU_TITLE = "Recommended For You";
    private static final String BEARER = "bearer ";
    private static final String API_ERROR = "Request failed with the following exception: {}";
    private static final String DISNEY_CONTENT_BAMGRID_URL = "https://disney.content.edge.bamgrid.com/";
    protected static final String RECOMMENDATION_SET_NODE = "RecommendationSet";
    private static final String GENERIC_PATH = "version/6.1/region/%s/audience/false/maturity/1830/language/%s/setId/%s/pageSize/60/page/1";
    private static final String RECOMMENDATION_SET = DisneyContentParameters.getContentService(RECOMMENDATION_SET_NODE);
    private static final String SERIES_PATH = "/text/title/full/" + "series" + "/default/content";
    private static final String MOVIE_PATH = "/text/title/full/" + "program" + "/default/content";
    private final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();

    @ExtendedFindBy(accessibilityId = "bbbeb38b-d5ae-47dd-a049-b089735c7453")
    private ExtendedWebElement disneyTile;

    @ExtendedFindBy(accessibilityId = "Disney Plus")
    private ExtendedWebElement disneyPlusLogo;

    @ExtendedFindBy(accessibilityId = "Mickey and Friends")
    private ExtendedWebElement mickeyAndFriends;

    @ExtendedFindBy(accessibilityId = "b8b35f0b-342d-4128-87ac-d3d5353121fa")
    private ExtendedWebElement pixarTile;

    @ExtendedFindBy(accessibilityId = "152b43dd-e9df-4bc5-94f3-ee4ffe99c8ae")
    private ExtendedWebElement marvelTile;

    @ExtendedFindBy(accessibilityId = "6f7a054a-cdde-4285-ae4b-b0887ece18e8")
    private ExtendedWebElement starWarsTile;

    @ExtendedFindBy(accessibilityId = "3bf4b88f-49a0-4533-ad24-97af0ca9b1d3")
    private ExtendedWebElement nationalGeographicTile;

    @ExtendedFindBy(accessibilityId = "c2688902-d618-4c6a-9ea0-2dad77274303")
    private ExtendedWebElement starTile;

    @ExtendedFindBy(accessibilityId = "ff723d29-20d5-4303-9cce-4a9aac8e269e")
    private ExtendedWebElement huluTile;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage")
    private ExtendedWebElement brandNameCell;

    @ExtendedFindBy(accessibilityId = "homeContentView")
    protected ExtendedWebElement homeContentView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$label='%s'$]/**/XCUIElementTypeCell/**XCUIElementTypeCell[$label == '%s'$][1]")
    protected ExtendedWebElement continueWatchingContentView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell")
    protected ExtendedWebElement collectionCellNoRow;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]")
    protected ExtendedWebElement collectionCell;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label == \"placeholder accessibility title label\"`]")
    private ExtendedWebElement networkLogoImage;

    @ExtendedFindBy(accessibilityId = RECOMMENDED_FOR_YOU_TITLE)
    private ExtendedWebElement recommendedForYou;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]")
    private ExtendedWebElement firstCellElementFromCollection;

    public DisneyPlusHomeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getHomePageMainElement(){
        return  dynamicCellByLabel.format("Disney, , Select for details on this title.");
    }
    @Override
    public boolean isOpened() {
        //There is no dict key available for this element
        return getHomePageMainElement().isPresent();
    }

    public void waitForHomePageToOpen() {
        LOGGER.info("Waiting for Home page to load");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Home page is not opened")
                .until(it -> getHomePageMainElement().isPresent(SHORT_TIMEOUT));
    }

    public boolean isKidsHomePageOpen() {
        return mickeyAndFriends.isElementPresent();
    }

    public void clickFirstCarouselPoster() {
        clickContent(4, 1);
        pause(5);
    }

    public List<ExtendedWebElement> getKidsCarousels() {
        return getAllCollectionCells(CollectionConstant.Collection.KIDS_CAROUSEL);
    }

    public void clickPixarTile() {
        pixarTile.click();
    }

    public void clickDisneyTile() {
        disneyTile.click();
    }

    public void clickMarvelTile() {
        marvelTile.click();
    }

    public void clickStarWarsTile() {
        starWarsTile.click();
    }

    public void clickNatGeoTile() {
        nationalGeographicTile.click();
    }

    public boolean isStarTilePresent() {
        return starTile.isPresent();
    }
    public void clickStarTile() {
        starTile.click();
    }

    public void clickRandomBrandTile() {
        List<ExtendedWebElement> brandTiles = new ArrayList<>();
        brandTiles.add(pixarTile);
        brandTiles.add(disneyTile);
        brandTiles.add(marvelTile);
        brandTiles.add(starWarsTile);
        brandTiles.add(nationalGeographicTile);
        LOGGER.info("Selecting random brand tile..");
        brandTiles.get(new SecureRandom().nextInt(brandTiles.size() - 1)).click();
    }

    public void initiatePlaybackFromContinueWatching(String series) {
        ExtendedWebElement continueWatchingLabel = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CONTINUE_WATCHING_TITLE.getText()));
        String continueWatchingText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CONTINUE_WATCHING_TITLE.getText());
        swipeInContainerTillElementIsPresent(homeContentView, continueWatchingLabel, 3, Direction.UP);
        continueWatchingContentView.format(continueWatchingText, series).click();
    }

    public ExtendedWebElement getDisneyTile() {
        return disneyTile;
    }

    public boolean isHuluTileVisible(){
        return huluTile.isPresent();
    }

    public void tapHuluBrandTile(){
        huluTile.click();
    }

    public ExtendedWebElement getHomeContentView() { return homeContentView; }

    public ExtendedWebElement getNetworkLogoImage() {
        return networkLogoImage;
    }

    public boolean isNetworkLogoImageVisible(){
        return networkLogoImage.isPresent();
    }

    public ExtendedWebElement getBrandTile(String brand) {
        return getTypeCellLabelContains(brand);
    }

    public boolean isHomePageLoadPresent() {
        String homePageLoad = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.HOME_PAGE_LOAD.getText());
        return getDynamicAccessibilityId(homePageLoad).isPresent();
    }

    public void clickBrandTile(DisneyPlusBrandIOSPageBase.Brand brand) {
        switch (brand) {
            case DISNEY:
                clickDisneyTile();
                break;
            case PIXAR:
                clickPixarTile();
                break;
            case MARVEL:
                clickMarvelTile();
                break;
            case STAR_WARS:
                clickStarWarsTile();
                break;
            case NATIONAL_GEOGRAPHIC:
                clickNatGeoTile();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }

    public JsonNode getRecommendationSet(DisneyAccount account, String locale, String language, String setId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String genericSetPath = String.format(GENERIC_PATH, locale, language, setId);
            URI uri = new URI(DISNEY_CONTENT_BAMGRID_URL + RECOMMENDATION_SET + genericSetPath);
            headers.add(HttpHeaders.AUTHORIZATION, BEARER + account.getAccountToken());
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).build();
            RequestEntity<JsonNode> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
            return restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (URISyntaxException | MalformedURLException e) {
            LOGGER.error("API Error attempting to fetch set ID {}. {}: {}", setId, API_ERROR, e);
            throw new RuntimeException("API Error attempting to fetch set ID", e);
        }
    }

    public String getMediaTitle(JsonNode item) {
        if(item.at(SERIES_PATH).isTextual()){
            return item.at(SERIES_PATH).asText();
        }
        return item.at(MOVIE_PATH).asText();
    }

    public List<String> getTitleFromRecommendationSet(DisneyAccount account, String locale, String language){
        List<String> titlesFromApi = new ArrayList<>();
        JsonNode js =  getRecommendationSet(account, locale, language,
                CollectionConstant.getCollectionName(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));

        js.findPath("data").findPath(RECOMMENDATION_SET_NODE).findPath("items")
                .forEach(item -> titlesFromApi.add(getMediaTitle(item)));

        return titlesFromApi;
    }

    public boolean isProfileNameDisplayed(String name) {
        return getTypeButtonByLabel(name).isPresent();
    }

    public boolean isRecommendedForYouContainerPresent(){
        return recommendedForYou.isPresent();
    }

    public ExtendedWebElement getRecommendedForYouContainer(){
        return getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
    }

    public String getFirstCellTitleFromRecommendedForYouContainer() {
        return firstCellElementFromCollection.format(CollectionConstant.getCollectionName(CollectionConstant.Collection.RECOMMENDED_FOR_YOU)).getText();
    }
}
