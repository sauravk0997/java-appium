package com.disney.qa.disney.web.appex.homepage;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.DataMapKeyConstant;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.util.disney.DisneyGlobalUtils;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomePageCarouselPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='arrow-right']")
    public List<ExtendedWebElement> dynamicCarouselRightArrow;

    @FindBy(xpath = "//*[@data-testid='arrow-left']")
    public List<ExtendedWebElement> dynamicCarouselLeftArrow;

    @FindBy(xpath = "//div[@aria-label= '%s']")
    private ExtendedWebElement brandTiles;

    @FindBy(xpath = "//*[@class = 'slick-dots']/li/button")
    private List<ExtendedWebElement> paginationDots;

    @FindBy(xpath = "//*[@id='home-collection']/div[3]//a")
    private List<ExtendedWebElement> gridSize;

    @FindBy(xpath = "//div[@data-testid='continue-watching']//*[@data-gv2elementvalue='%s']")
    private ExtendedWebElement continueWatchingTileByGv2Key;

    @FindBy(xpath = "//div[@id='home-collection']/div")
    private List<ExtendedWebElement> homeCollectionList;

    @FindBy(xpath = "//*[@tabindex='%d']")
    private List<ExtendedWebElement> carouselIndex;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-%s']")
    private ExtendedWebElement carouselCardNumber;

    @FindBy(xpath = "//*[@aria-label='Disney']")
    private ExtendedWebElement disneyTile;

    @FindBy(xpath = "//*[@aria-label='Pixar']")
    private ExtendedWebElement pixarTile;

    @FindBy(xpath = "//*[@aria-label='Marvel']")
    private ExtendedWebElement marvelTile;

    @FindBy(xpath = "//*[@aria-label='Star Wars']")
    private ExtendedWebElement starWarsTile;

    @FindBy(xpath = "//*[@aria-label='National Geographic']")
    private ExtendedWebElement nationalGeographicTile;

    @FindBy(xpath = "//h4[text()='Watchlist']")
    private ExtendedWebElement headerWatchListOnHomePage;

    @FindBy(xpath = "//h4[text()='Watchlist']/../div//a")
    private ExtendedWebElement assetUnderWatchlistOnHomePage;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']//video")
    private ExtendedWebElement carouselBrandTileVideo;

    @FindBy(xpath = "//*[@data-testid='container-1']//div[@class='gv2-asset']")
    private List<ExtendedWebElement> brandTilesContainer;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']")
    private ExtendedWebElement carouselBrandTile;

    @FindBy(xpath = "//*[@id='section_index']/video")
    private ExtendedWebElement brandBackgroundVideo;

    public DisneyPlusHomePageCarouselPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getCarouselItem(int starCardIndex, int disneyCardIndex) {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? carouselCardNumber.format(starCardIndex) : carouselCardNumber.format(disneyCardIndex);
    }

    public boolean dynamicCarouselRightArrowIsPresent(int row, int timeout, int maxAttempts) {
        boolean isPresent = false;
        while (!isPresent && maxAttempts-- >= ONE_SEC_TIMEOUT) {
            try {
                dynamicCarouselRightArrow.get(row).hover();
                isPresent = dynamicCarouselRightArrow.get(row).isElementPresent(timeout);
            } catch (IndexOutOfBoundsException e) {
                pause(timeout);
            }
        }
        return isPresent;
    }

    public ExtendedWebElement getDynamicCarouselRightArrow(int row) {
        waitFor(dynamicCarouselRightArrow.get(row));
        return dynamicCarouselRightArrow.get(row);
    }

    public ExtendedWebElement getDynamicCarouselLeftArrow(int row) {
        waitFor(dynamicCarouselLeftArrow.get(row));
        return dynamicCarouselLeftArrow.get(row);
    }

    public enum BrandTiles {
        DISNEY("Disney"),
        PIXAR("Pixar"),
        MARVEL("Marvel"),
        STAR_WARS("Star Wars"),
        NATIONAL_GEOGRAPHIC("National Geographic");

        String names;

        BrandTiles(String names) {
            this.names = names;
        }

        public String getNames() {
            return names;
        }
    }

    public void getBrandTile(BrandTiles name) {
        switch (name) {
            case DISNEY:
                disneyTile.click();
                break;
            case PIXAR:
                pixarTile.click();
                break;
            case MARVEL:
                marvelTile.click();
                break;
            case STAR_WARS:
                starWarsTile.click();
                break;
            case NATIONAL_GEOGRAPHIC:
                nationalGeographicTile.click();
                break;
            default:
                LOGGER.error("Tile interaction not set!");
        }
    }

    //Getters

    private ExtendedWebElement getContentTileByKey(String key) {
        waitFor(continueWatchingTileByGv2Key.format(key));
        return continueWatchingTileByGv2Key.format(key);
    }

    //Click

    public void clickContentTileByKey(String key) {
        getContentTileByKey(key).click();
    }

    //Booleans

    public boolean isContentTileByKeyPresent(String key) {
        getContentTileByKey(key).scrollTo();
        return getContentTileByKey(key).isElementPresent();
    }

    //Test Flows

    public void verifyBrandTiles(SoftAssert softAssert, String brandTileName, String url) {
        waitFor(brandTiles.format(brandTileName));
        brandTiles.format(brandTileName).click();
        LOGGER.info(brandTileName + " : clicked");
        verifyUrlText(softAssert, url);
        waitForPageToFinishLoading();
        analyticPause();
        clickOnHomeMenuOption();
        analyticPause();
    }

    public void clickThroughTopCarousel(SoftAssert softAssert, ExtendedWebElement arrow) {
        int count = 1;
        while (paginationDots.size() + 1 >= count) {
            arrow.hover();
            arrow.clickIfPresent(5);
            count++;
            LOGGER.info(count + " times featured " + arrow + " clicked");

            softAssert.assertTrue(paginationDots.size() > 1,
                    String.format("Pagination buttons should be more than 1: %s", paginationDots.size()));
        }
    }

    public void clickThroughButtonCarousel(SoftAssert softAssert, ExtendedWebElement arrow, int numOfClicks) {
        int clicks = 1;
        arrow.hover();
        while (!arrow.getAttribute("class").contains("slick-disabled")) {
            arrow.click();
            clicks++;
            if (clicks >= numOfClicks) {
                LOGGER.info(arrow + " clicked");
                break;
            }

            softAssert.assertTrue(gridSize.size() > 4,
                    String.format("Grid size should be more than 1: %s", gridSize.size()));
        }
    }

    public void clickRightArrow(int carouselRow, int numberOfTimes, int pauseFor) {
        waitFor(dynamicCarouselRightArrow.get(carouselRow));
        dynamicCarouselRightArrow.get(carouselRow).hover();
        for (int i = 1; i <= numberOfTimes; i++) {
            dynamicCarouselRightArrow.get(carouselRow).clickIfPresent(DELAY);
            pause(pauseFor);
        }
    }

    public List<ExtendedWebElement> getHomeCollectionList() {
        int maxAttempts = LONG_TIMEOUT;
        while (homeCollectionList.isEmpty() && maxAttempts-- > 0) {
            LOGGER.debug("Populating homepage...");
            pause(ONE_SEC_TIMEOUT);
        }
        if (homeCollectionList.isEmpty()) {
            LOGGER.warn("Home collection list is empty after waiting for {} seconds", maxAttempts);
        }
        return homeCollectionList;
    }

    public DisneyPlusHomePageCarouselPage getHomeCollectionListItem(int index) {
        getHomeCollectionList().get(index);
        return this;
    }

    public boolean isNonDetailsPage() {
        return (getCurrentUrl().contains("franchise") || getCurrentUrl().contains("editorial")
                || getCurrentUrl().contains("event") || getCurrentUrl().contains("character"));
    }

    public DisneyPlusHomePageCarouselPage clickMainCarouselItem(int starCardIndex, int disneyCardIndex) {
        getCarouselItem(starCardIndex, disneyCardIndex).clickByJs();
        int retry = 1;
        while (retry < 5 && isNonDetailsPage()) {
            waitForSeconds(5); // Needed for loading for the next step
            LOGGER.info("Clicked on non-details page. Retrying..");
            clickOnHomeMenuOption();
            getCarouselItem(starCardIndex++, disneyCardIndex++).clickByJs(5);
            retry++;
        }
        return this;
    }

    public boolean isWatchListCategoryOnHomepagePresent(String text) {
        return headerWatchListOnHomePage.getText().equalsIgnoreCase(text);
    }

    public boolean watchlistCategoryOnHomepageNotEmpty() {
        return assetUnderWatchlistOnHomePage.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        if (!getHomeCollectionList().isEmpty()) {
            LOGGER.info("Home page is opened");
            return true;
        } else {
            LOGGER.error("Home page did not open!");
            Screenshot.capture(driver, "Home page did not open!");
        }
        return false;
    }

    public ExtendedWebElement getBrandTileVideo(int tileIndex) {
        LOGGER.info("Get the brand tile video at position {}", tileIndex);
        return carouselBrandTileVideo.format(tileIndex);
    }

    public ExtendedWebElement getCarouselBrandTile(int i) {
        LOGGER.info("Get the brand tile in carousel in Home Page");
        return carouselBrandTile.format(i);
    }

    public int getNumberOfTilesInContainer() {
        LOGGER.info("Get the number of tiles in container-1");
        return brandTilesContainer.size();
    }

    public boolean isBackgroundVideoPlayed(ExtendedWebElement webElement) {
        LOGGER.info("Verify the video playback");
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        double duration = Double.parseDouble(getVideoDurationValue(webElement));
        double currentTime = Double.parseDouble(getVideoCurrentTimeValue(webElement));
        boolean attributePresent = appExUtil.isAttributePresent(webElement, WebConstant.AUTO_PLAY);
        if (currentTime == 0) {
            waitForSeconds(1);
            duration = Double.parseDouble(getVideoDurationValue(webElement));//Need to get duration again as the duration is not available immediately for background video in Hub page
            currentTime = Double.parseDouble(getVideoCurrentTimeValue(webElement));
        }
        return currentTime != 0 && currentTime <= duration && attributePresent;
    }

    public String getVideoDurationValue(ExtendedWebElement webElement) {
        LOGGER.info("Get duration value of video");
        return webElement.getAttribute(DataMapKeyConstant.DURATION);
    }

    public String getVideoCurrentTimeValue(ExtendedWebElement webElement) {
        LOGGER.info("Get current time value of video");
        return webElement.getAttribute(DataMapKeyConstant.CURRENT_TIME);
    }

    public boolean verifyBrandTileVideo(int tileCount) {
        for (int tileIndex = 0; tileIndex < tileCount; tileIndex++) {
            if (!verifyBrandTileAndHubVideo(tileIndex))
                return false;
        }
        return true;
    }

    public boolean verifyBrandTileAndHubVideo(int tileIndex) {
        getBrandTileVideo(tileIndex).hover();
        boolean videoPlayedInHomePage = isBackgroundVideoPlayed(getBrandTileVideo(tileIndex));
        getCarouselBrandTile(tileIndex).click();
        boolean videoPlayedInHubPage = isBackgroundVideoPlayed(brandBackgroundVideo);
        if (!videoPlayedInHomePage || !videoPlayedInHubPage)
            return false;
        clickOnHomeMenuOption();
        return true;
    }

    public DisneyPlusHomePageCarouselPage clickOnViewMoreButton() {
        LOGGER.info("Click on the View More button");
        getButtonContainText("VIEW MORE").click();
        return this;
    }

    public boolean isViewMoreBtnVisible() {
        LOGGER.info("Verify if View More button is visible");
        return getButtonContainText("VIEW MORE").isVisible();
    }

    public DisneyPlusHomePageCarouselPage scrollToViewLionsGateCollection() {
        LOGGER.info("Scroll down to view LionsGate collection");
        SeleniumUtils utils = new SeleniumUtils(getDriver());
        while(!isViewMoreBtnVisible())
            utils.scrollToBottom();
        return this;
    }
}
