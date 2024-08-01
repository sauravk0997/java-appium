package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.*;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomeIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DISNEY_TILE = "Disney, Select for details on this title.";
    @ExtendedFindBy(accessibilityId = "bbbeb38b-d5ae-47dd-a049-b089735c7453")
    private ExtendedWebElement disneyTile;
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
    @ExtendedFindBy(accessibilityId = "Mickey and Friends")
    private ExtendedWebElement mickeyAndFriends;

    public DisneyPlusHomeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getHomePageMainElement() {
        return dynamicCellByLabel.format(DISNEY_TILE);
    }

    @Override
    public boolean isOpened() {
        //There is no dict key available for this element
        return getHomePageMainElement().isPresent();
    }

    public void waitForHomePageToOpen() {
        LOGGER.info("Waiting for Home page to load");
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Home page is not opened")
                .until(it -> getHomePageMainElement().isPresent(THREE_SEC_TIMEOUT));
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

    public ExtendedWebElement getDisneyTile() {
        return disneyTile;
    }

    public boolean isHuluTileVisible() {
        return getElementTypeCellByLabel("Hulu").isPresent();
    }

    public void tapHuluBrandTile() {
        getElementTypeCellByLabel("Hulu").click();
    }

    public ExtendedWebElement getBrandTile(String brand) {
        return getTypeCellLabelContains(brand);
    }

    public ExtendedWebElement getBrandCell(String brand) {
        return getDynamicCellByLabel(String.format("%s, Select for details on this title.", brand));
    }

    public void clickOnBrandCell(String brand) {
        getBrandCell(brand).click();
    }

    public ExtendedWebElement getNetworkLogoImage(String item) {
        return imageLabelContains.format(getDictionary().formatPlaceholderString(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                        false), Map.of(BRAND_NAME, item)));
    }

    public boolean isNetworkLogoImageVisible(String item) {
        return getNetworkLogoImage(item).isPresent();
    }

    public boolean isProfileNameDisplayed(String name) {
        return getTypeButtonByLabel(name).isPresent();
    }

    public boolean isCollectionTitlePresent(CollectionConstant.Collection collection){
        return getDynamicAccessibilityId(CollectionConstant.getCollectionTitle(collection)).isPresent();
    }

    public void goToContinueWatchingCarousel() {
        String wordSeparator = " ";
        String continueWatchingText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CONTINUE_WATCHING_TITLE.getText());
        String expectedTitle = convertToTitleCase(continueWatchingText, wordSeparator);
        ExtendedWebElement continueWatchingHeader = getDynamicAccessibilityId(expectedTitle);
        Assert.assertTrue(swipe(continueWatchingHeader, Direction.UP, 3, 400), "Couldn't scroll to continue watching carousel");
    }

    public void goToDetailsPageFromContinueWatching(String title) {
        goToContinueWatchingCarousel();
        getDynamicAccessibilityId(title).click();
    }
}
