package com.disney.qa.disney.web.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisneyPlusBaseTilesPage extends DisneyPlusBaseNavPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusBaseTilesPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[@class='gv2-asset']")
    private List <ExtendedWebElement> dPlusTilesAssetList;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-%s']/div")
    private ExtendedWebElement dPlusTilesAssetIndex;

    @FindBy(xpath = "((//div[@class='gv2-asset'])//div[contains(@alt,'%s')])[1]")
    private ExtendedWebElement dPlusTilesAssetIndexContent;

    @FindBy(xpath = "//div[@class='gv2-asset']//img[1]")
    private List<ExtendedWebElement> brandAssetTileImages;

    @FindBy(xpath = "//div[@class='gv2-asset']//img[2]")
    private List<ExtendedWebElement> brandAssetTileHoverImages;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']//img[1]")
    private ExtendedWebElement brandTileImage;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']//img[2]")
    private ExtendedWebElement brandTileHoverImage;

    @FindBy(xpath = "//*[@data-testid='lionsgate-test']//a")
    private List<ExtendedWebElement> lionsGateBrandTiles;

    @FindBy(xpath = "//*[@data-testid='lionsgate-test']//div[@data-index='%s']//a//img[1]")
    private ExtendedWebElement lionsGateTileImage;


    @FindBy(xpath = "//*[@data-testid='lionsgate-test']//div[@data-index='%s']//a//img[1]")
    private ExtendedWebElement lionsGateTileHoverImage;

    @FindBy(xpath = "//div[@class='gv2-asset']//a")
    private List<ExtendedWebElement> brandPageAssetTiles;

    @FindBy(xpath = "//*[@data-testid='hero-inline-lionsgate']//div[@data-index='%s']//a")
    private ExtendedWebElement lionsGateBrandTile;

    @FindBy(xpath = "//*[@data-testid='search-result-%s']//img")
    private ExtendedWebElement searchResultTileImg;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-%s']/div//img")
    private ExtendedWebElement watchlistTileImg;


    @FindBy(xpath = "//*[@data-testid='recommended-for-you']/h4[contains(text(), 'Recommended For You')]")
    private ExtendedWebElement recommendedForYouCollection;

    @FindBy(xpath = "//*[@data-testid='recommended-for-you']//div[@class='slick-track']/div")
    private List<ExtendedWebElement> recommendedForYouCollectionList;

    @FindBy(xpath = "//*[@data-testid='recommended-for-you']//div[@data-index='%s']//a//img[1]")
    private ExtendedWebElement recommendedForYouTileImg;

    @FindBy(xpath = "//*[@data-testid='recommended-for-you']//div[@data-index='%s']//a//img[2]")
    private ExtendedWebElement recommendedForYouTileHoverImg;

    @FindBy(xpath = "//*[@data-testid='recommended-for-you']//button[@data-testid='arrow-right']")
    private ExtendedWebElement recommendedForYouRightArrow;


    public List<ExtendedWebElement> getBrandAssetTiles() {
        return brandPageAssetTiles;
    }

    public List<ExtendedWebElement> getBrandAssetTileImages() {
        return brandAssetTileImages;
    }

    public List<ExtendedWebElement> getBrandAssetTileHoverImages() {
        return brandAssetTileHoverImages;
    }

    public ExtendedWebElement getLionsGateTileImage(int tileIndex) {
        return lionsGateTileImage.format(tileIndex);
    }

    public ExtendedWebElement getLionsGateTile(int tileIndex) { return lionsGateBrandTile.format(tileIndex); }

    public ExtendedWebElement getSearchResultTileImg(int tileIndex) { return searchResultTileImg.format(tileIndex); }

    public ExtendedWebElement getWatchlistTileImg(int tileIndex) { return watchlistTileImg.format(tileIndex); }

    public ExtendedWebElement getRecommendedForYouCollection() {
        return recommendedForYouCollection;
    }

    public List<ExtendedWebElement> getRecommendedForYouCollectionList() {
        return recommendedForYouCollectionList;
    }

    public ExtendedWebElement getLionsGateTileHoverImages(int tileIndex) {
        return lionsGateTileHoverImage.format(tileIndex);
    }

    public ExtendedWebElement getRecommendedForYouTileImg(int tileIndex) {
        return recommendedForYouTileImg.format(tileIndex);
    }

    public ExtendedWebElement getRecommendedForYouTileHoverImg(int tileIndex) {
        return recommendedForYouTileHoverImg.format(tileIndex);
    }
    public ExtendedWebElement getBrandTileImage(int tileIndex) {
        LOGGER.info("Get the brand tile image for tile {}", tileIndex);
        return brandTileImage.format(tileIndex);
    }

    public ExtendedWebElement getBrandTileHoverImage(int tileIndex) {
        LOGGER.info("Get the brand tile hover image for tile {}", tileIndex);
        return brandTileHoverImage.format(tileIndex);
    }

    public String getImageSrc(ExtendedWebElement brandTile) {
        return brandTile.getAttribute("src");
    }

    public boolean isTileImageBadgingVisible(ExtendedWebElement brandTile, String logo) {
        LOGGER.info("Verify tile image badging is available on the tile");
        return getImageSrc(brandTile).contains(logo);
    }

    public boolean isRecommendedForYouCollectionVisible() {
        LOGGER.info("Verify if Recommended for you collection is visible");
        return getRecommendedForYouCollection().isVisible();
    }

    public boolean verifyTileImageBadging(String logo) {
        LOGGER.info("Verify the tile image badging for LionsGate tiles in Home Page");
        waitForSeconds(2);
        for (int i = 0; i < lionsGateBrandTiles.size(); i++) {
            if (!isTileImageBadgingVisible(getLionsGateTileImage(i), logo) &&
                    isTileImageBadgingVisible(getLionsGateTileHoverImages(i), logo))
                return false;
        }
        return true;
    }

    //Getters

    public ExtendedWebElement getdPlusTileByIndex(String index) {
        return dPlusTilesAssetIndex.format(index);
    }

    //Clicks

    public DisneyPlusBaseTilesPage clickDplusTileByIndex(int index) {
        dPlusTilesAssetIndex.format(Integer.toString(index)).clickByJs();
        return this;
    }

    //Booleans

    public boolean isDplusTilePresentByIndex(int index) {
        return dPlusTilesAssetIndex.format(Integer.toString(index)).isElementPresent();
    }

    public boolean isTilePresentByContentName(String contentName) {
        waitFor(dPlusTilesAssetIndexContent.format(contentName));
        return dPlusTilesAssetIndexContent.format(contentName).isElementPresent();
    }

    //Methods

    public boolean waitForDplusTileVisibleByIndex(int index) {
        return dPlusTilesAssetIndex.format(Integer.toString(index)).isVisible();
    }

    public boolean waitForDplusTileNotVisibleByIndex(int index) {
        return dPlusTilesAssetIndex.format(Integer.toString(index)).waitUntilElementDisappear(DELAY);
    }

    /**
     * Method iterates through asset tile index, grabs and returns parameterized attribute as list
     */

    public List<String> getTileAssetAttributes(int index, String attributeType) {
        String attribute;
        ExtendedWebElement currentState;
        List<String> assets = new ArrayList<>();
        LOGGER.info(String.format("Grabbing first %s attributes", index));

        for (int x = 1; x<= index; x+=2) {
            try {
                currentState = getdPlusTileByIndex(Integer.toString(x));
                attribute = currentState.getAttribute(attributeType);
                assets.add(attribute);
            }catch (NoSuchElementException e){
                LOGGER.info(String.format("No element attribute found for %s index, skipping...", x));
                break;
            }
        }
        assets.removeAll(Collections.singletonList(null));
        LOGGER.info("Assets grabbed: {}", assets);

        return assets;
    }

    public DisneyPlusBaseTilesPage clickOnLionsGateTile(int tileIndex) {
        LOGGER.info("Click on tile {} ", tileIndex);
        getLionsGateTile(tileIndex).click();
        return this;
    }

    public DisneyPlusBaseTilesPage clickOnRecommendedForYouRightArrow() {
        LOGGER.info("Click on right arrow in Recommended for you set");
        recommendedForYouRightArrow.click();
        return this;
    }
}
