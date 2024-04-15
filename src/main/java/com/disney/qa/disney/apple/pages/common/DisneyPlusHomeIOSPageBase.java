package com.disney.qa.disney.apple.pages.common;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.disney.config.DisneyParameters;
import com.disney.qa.api.explore.ExploreApi;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.explore.response.ExploreSetResponse;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.constant.CollectionConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomeIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String RECOMMENDED_FOR_YOU_TITLE = "Recommended For You";
    private static final String APPLE = "apple";
    private static final String PARTNER = "disney";

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[$label CONTAINS '%s'$]")
    private ExtendedWebElement cellElementFromCollection;


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
        return getElementTypeCellByLabel("Hulu").isPresent();
    }

    public void tapHuluBrandTile(){
        getElementTypeCellByLabel("Hulu").click();
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

    public ExtendedWebElement getBrandCell(String brand) {
        return getDynamicCellByLabel(String.format("%s, , Select for details on this title.", brand));
    }

    public void clickOnBrandCell(String brand) {
        getBrandCell(brand).click();
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

    public ExtendedWebElement getCellElementFromRecommendedForYouContainer(String title){
        return cellElementFromCollection.format(CollectionConstant.getCollectionName(CollectionConstant.Collection.RECOMMENDED_FOR_YOU), title);
    }

    public ArrayList<Item> getContainerDetailsFromAPI(DisneyAccount account, String setId, int limit) {
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

    public List<String> getContainerTitlesFromApi(DisneyAccount account, String setID, int limit) {
        ArrayList<Item> SetItemsFromApi = getContainerDetailsFromAPI(account, setID, limit);
        List<String> TitlesFromApi = new ArrayList<>();
        SetItemsFromApi.forEach(item ->
                TitlesFromApi.add(item.getVisuals().getTitle()));
        return TitlesFromApi;
    }
}
