package com.disney.qa.disney.apple.pages.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.WebDriver;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.mobile.IMobileUtils.Direction;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomeIOSPageBase extends DisneyPlusApplePageBase {

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"continueWatchingContentView\"`]")
    protected ExtendedWebElement continueWatchingContentView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell")
    protected ExtendedWebElement collectionCellNoRow;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]")
    protected ExtendedWebElement collectionCell;


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

    public void initiatePlaybackFromContinueWatching() {
        ExtendedWebElement continueWatchingLabel = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CONTINUE_WATCHING_TITLE.getText()));
        swipeInContainerTillElementIsPresent(homeContentView, continueWatchingLabel, 3, Direction.UP);
        continueWatchingContentView.click();
    }

    public ExtendedWebElement getDisneyTile() {
        return disneyTile;
    }

    public boolean isHuluTileVisible(){
        return dynamicCellByLabel.format("hulu").isPresent();
    }

    public void tapHuluBrandTile(){
        dynamicCellByLabel.format("hulu").click();
    }

    public enum Collection {
        ACTION_AND_ADVENTURE,
        ANIMATED_MOVIES,
        ANIMATED_SERIES,
        CLASSIC_SHORTS,
        CAROUSEL, //carousel rotating slides
        COLLECTIONS,
        COMEDIES,
        CONTINUE_WATCHING,
        DISNEY_JUNIOR,
        DOCUMENTARIES_AND_REALITY,
        MUSIC_AND_DANCE,
        NEW_TO_DISNEY,
        ORIGINALS,
        REALITY_SERIES,
        RECOMMENDED_FOR_YOU,
        SHORTS,
        TRENDING,
        ANIMATED_MOVIES_QA,
        NEW_TO_DISNEY_QA;
    }

    public String getCollectionName(Collection collection) {
        switch (collection) {
            case ACTION_AND_ADVENTURE:
                return "f977cd3e-9983-417d-90e7-dd40101cbda6";
            case ANIMATED_MOVIES:
                return "a221f47c-0e16-476b-bdc0-35ed038b72b8";
            case ANIMATED_SERIES:
                return "0af2fea3-8cc5-45e0-90d4-7e964d4ac4f8";
            case CAROUSEL:
                return "5c3a73f7-f06c-42c6-ab6c-525ba9af1327";
            case CLASSIC_SHORTS:
                return "dd04d249-96f2-4149-a2da-a9f2302d157b";
            case COLLECTIONS:
                return "1da80bf5-b832-44fd-b6ab-4b257e319094";
            case COMEDIES:
                return "cad0303b-5f54-4276-8303-7f9ba3d4303e";
            case CONTINUE_WATCHING:
                return "76aed686-1837-49bd-b4f5-5d2a27c0c8d4";
            case DOCUMENTARIES_AND_REALITY:
                return "5537a5bf-697a-403e-875d-40794922fd13";
            case DISNEY_JUNIOR:
                return "ce35f2ea-ba2c-4892-9c12-d4a4ab3bfb32";
            case MUSIC_AND_DANCE:
                return "73d4a334-87c8-4059-91de-9d49171c41fb";
            case NEW_TO_DISNEY:
                return "3c61bd68-b639-44a9-97b4-5bed89a7c479";
            case ORIGINALS:
                return "6711b1f3-da45-4236-8ae6-faf241d66102";
            case REALITY_SERIES:
                return "bd532890-e3d4-415e-831d-348eb949b9ec";
            case RECOMMENDED_FOR_YOU:
                return "7894d9c6-43ab-4691-b349-cf72362095dd";
            case SHORTS:
                return "c082e3f6-4e7e-44f3-b6d8-aab7a8bf7367";
            case TRENDING:
                return "25b87551-fd19-421a-be0f-b7f2eea978b3";
            case ANIMATED_MOVIES_QA:
                return "43b6e7d1-ee42-4699-ac18-17c4658f5219";
            case NEW_TO_DISNEY_QA:
                return "76317bf0-3465-48d1-a7f5-80e37e3338b2";
            default:
                throw new IllegalArgumentException(String.format("'%s collection is not found", collection));
        }
    }

    /**
     * Select random tile, scroll to specific collection, then selects random tile
     * @param collection gets collection name from enum Collection
     * @param count swipe collection for number of times
     * @param direction Up or Down homeContentView
     */
    public void clickRandomCollectionTile(Collection collection, int count, Direction direction) {
        swipeTillCollectionPresent(collection, count, direction);
        getAllCollectionCells(collection).get(new SecureRandom().nextInt(getAllCollectionCells(collection).size() - 1)).click();
    }

    public List<ExtendedWebElement> getAllCollectionCells (Collection collection) {
        return findExtendedWebElements(collectionCellNoRow.format(getCollectionName(collection)).getBy());
    }

    public void swipeTillCollectionPresent(Collection collection, int count, Direction direction) {
        while (collectionCell.format(getCollectionName(collection)).isElementNotPresent(SHORT_TIMEOUT) && count >= 0) {
            swipeInContainer(homeContentView, direction, 1, 1200);
            count--;
        }
    }
}
