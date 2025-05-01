package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.*;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomeIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private String brandLabelSubString = ", Select for details on this title.";
    @ExtendedFindBy(accessibilityId = "Disney, Select for details on this title.")
    private ExtendedWebElement disneyTile;
    @ExtendedFindBy(accessibilityId = "Pixar, Select for details on this title.")
    private ExtendedWebElement pixarTile;
    @ExtendedFindBy(accessibilityId = "Marvel, Select for details on this title.")
    private ExtendedWebElement marvelTile;
    @ExtendedFindBy(accessibilityId = "Star Wars, Select for details on this title.")
    private ExtendedWebElement starWarsTile;
    @ExtendedFindBy(accessibilityId = "National Geographic, Select for details on this title.")
    private ExtendedWebElement nationalGeographicTile;
    @ExtendedFindBy(accessibilityId = "ESPN, Select for details on this title.")
    private ExtendedWebElement espnTile;
    @ExtendedFindBy(accessibilityId = "c2688902-d618-4c6a-9ea0-2dad77274303")
    private ExtendedWebElement starTile;
    @ExtendedFindBy(accessibilityId = "TabBar/Home/active")
    private ExtendedWebElement activeHomeIcon;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS 'Mickey Mouse and Friends'`]")
    private ExtendedWebElement mickeyAndFriends;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$name = 'brandTileContentView'$]")
    private ExtendedWebElement brandTileCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == 'DETAILS'`]")
    private ExtendedWebElement continueWatchingDetailsBtn;

    public DisneyPlusHomeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getHomePageMainElement() {
        return disneyTile;
    }

    public ExtendedWebElement getActiveHomeIcon() {
        return activeHomeIcon;
    }

    @Override
    public boolean isOpened() {
        //There is no dict key available for this element
        return getActiveHomeIcon().isPresent();
    }

    public void waitForHomePageToOpen() {
        LOGGER.info("Waiting for Home page to load");
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Home page is not opened")
                .until(it -> getActiveHomeIcon().isPresent(THREE_SEC_TIMEOUT));
    }

    public boolean isKidsHomePageOpen() {
        return mickeyAndFriends.isElementPresent();
    }

    public boolean isHeroCarouselDisplayed(String carouselId) {
        return getHeroCarouselContainer(carouselId).isPresent();
    }

    public ExtendedWebElement getHeroCarouselContainer(String carouselId) {
        return getDynamicAccessibilityId(carouselId);
    }

    public void clickFirstCarouselPoster() {
        clickContent(4, 1);
        pause(5);
    }

    public void clickDynamicCollectionOrContent(int collectionViewColumn, int cellRow) {
        clickContent(collectionViewColumn, cellRow);
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

    public void clickEspnTile() {
        espnTile.click();
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

    public List<String> getOrderedBrandList() {
        List<String> brandList = new ArrayList<>();
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.DISNEY));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.PIXAR));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.MARVEL));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.STAR_WARS));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.NATIONAL_GEOGRAPHIC));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        brandList.add(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.ESPN));
        return brandList;
    }

    public ExtendedWebElement getDisneyTile() {
        return disneyTile;
    }

    public boolean isHuluTileVisible() {
        return getElementTypeCellByLabel("Hulu").isPresent();
    }

    public void tapHuluBrandTile() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusBrandIOSPageBase brandPage = new DisneyPlusBrandIOSPageBase(getDriver());
        homePage.getBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU)).click();
    }


    public ExtendedWebElement getBrandCell(String brand) {
        return getDynamicCellByLabel(String.format("%s%s", brand, brandLabelSubString));
    }

    public void clickOnBrandCell(String brand) {
        getBrandCell(brand).click();
    }

    public ExtendedWebElement getNetworkLogoImage(String item) {
        return imageLabelContains.format(getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                        false), Map.of(BRAND_NAME, item)));
    }

    public boolean isNetworkLogoImageVisible(String item) {
        return getNetworkLogoImage(item).isPresent(TEN_SEC_TIMEOUT);
    }

    public boolean isProfileNameDisplayed(String name) {
        return getTypeButtonByLabel(name).isPresent();
    }

    public boolean isCollectionTitlePresent(CollectionConstant.Collection collection){
        return getDynamicAccessibilityId(CollectionConstant.getCollectionTitle(collection)).isPresent();
    }

    public void swipeTillContinueWatchingCarouselPresent() {
        CollectionConstant.Collection continueWatching = CollectionConstant.Collection.CONTINUE_WATCHING;
        swipeTillCollectionTappable(continueWatching, Direction.UP, 5);
    }

    public void goToDetailsPageFromContinueWatching(String title) {
        swipeTillContinueWatchingCarouselPresent();
        getStaticTextByLabel(title).click();
        waitForPresenceOfAnElement(continueWatchingDetailsBtn);
        continueWatchingDetailsBtn.click();
    }

    public String getCurrentHeroCarouselTitle(String carouselId) {
        return firstCellElementFromCollection.format(carouselId).getText().split(",")[0];
    }

    public boolean isHeroCarouselAutoRotating(String title, String carouselId) {
        try {
            fluentWait(getDriver(), FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Hero Carousel did not auto-rotate")
                    .until(it -> !getCurrentHeroCarouselTitle(carouselId).equals(title));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public List<ExtendedWebElement> getBrandCells() {
        return findExtendedWebElements(brandTileCell.getBy());
    }

    public List<String> getBrandListFromUI() {
        List<String> brandListFromUI = new ArrayList<>();
        getAllCollectionCells(CollectionConstant.Collection.BRANDS_COLLECTION).forEach(
                cell -> brandListFromUI.add(cell.getText().replace(brandLabelSubString, "")));
        return brandListFromUI;
    }
}
