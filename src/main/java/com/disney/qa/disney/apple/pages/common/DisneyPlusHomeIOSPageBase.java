package com.disney.qa.disney.apple.pages.common;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.disney.qa.common.constant.CollectionConstant;
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

    public DisneyPlusHomeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return homeContentView.isElementPresent(SHORT_TIMEOUT);
    }

    public void waitForHomePageToOpen() {
        LOGGER.info("Waiting for Home page to load");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Home page is not opened")
                .until(it -> homeContentView.isPresent(SHORT_TIMEOUT));
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

    public boolean isProfileNameDisplayed(String name) {
        return getTypeButtonByLabel(name).isPresent();
    }
}
