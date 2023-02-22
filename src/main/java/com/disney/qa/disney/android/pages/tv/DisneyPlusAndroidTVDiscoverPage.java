package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.api.client.responses.content.ContentSeason;
import com.disney.qa.api.client.responses.content.ContentSeries;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyCollectionType;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_ENCODED_SERIES_ID;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_ITEMS_IN_HERO_CONTAINER;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.REF_ID_BY_TYPE;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:CallToDeprecatedMethod"})
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusDiscoverPageBase.class)
public class DisneyPlusAndroidTVDiscoverPage extends DisneyPlusDiscoverPageBase {

    private static final String INDICATOR_PATHS = "$..['android.widget.LinearLayout'].['android.widget.ImageView'][%s]..selected";

    @FindBy(id = "com.disney.disneyplus:id/logo")
    private ExtendedWebElement welcomeLogo;

    @FindBy(id = "brandNormalLogoImage")
    private ExtendedWebElement brandLogo;

    @FindBy(id = "brandWhiteLogoImage")
    private ExtendedWebElement brandWhiteLogoImage;

    @FindBy(id = "com.disney.disneyplus:id/shelfItemLayout")
    private ExtendedWebElement heroTile;

    @FindBy(id = "titleView")
    private ExtendedWebElement assetTitle;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/discoverRecyclerView']/*[%d]/*[2]/*/*")
    private ExtendedWebElement avatarIconsShelf;

    @FindBy(id = "shelfIndicatorView")
    private ExtendedWebElement heroCarouselIndicators;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/poster']")
    private ExtendedWebElement posters;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/shelfRecyclerView']/*/*[@resource-id='com.disney.disneyplus:id/poster']")
    private ExtendedWebElement contentPoster;

    @FindBy(xpath = "//*[contains(@content-desc, 'Home,')]")
    protected ExtendedWebElement globalNavHome;

    @FindBy(id = "com.disney.disneyplus:id/sideMenuBackground")
    private ExtendedWebElement sideMenuBackground;

    @FindBy(id = "full_bleed_item")
    protected ExtendedWebElement fullBleedHeroCarousel;

    @FindBy(id = "playIcon")
    protected ExtendedWebElement playIcon;

    @FindBy(xpath = "//*[contains(@resource-id,':id/shelfItemLayout') and @focused='true']")
    protected ExtendedWebElement focusedHeroTile;

    @FindBy(xpath = "//*[contains(@resource-id,':id/menuViewLayout') and @focused='true']")
    protected ExtendedWebElement focusedNavItem;

    public ExtendedWebElement getFocusedHeroTile() {
        return focusedHeroTile;
    }

    public ExtendedWebElement getFocusedNavItem() {
        return focusedNavItem;
    }

    public ExtendedWebElement getNavHome() {
        return globalNavHome;
    }

    public ExtendedWebElement getSideMenuBackground() {
        return sideMenuBackground;
    }

    public enum GlobalNavItem {
        PROFILE("Test"),
        SEARCH("SEARCH"),
        HOME("HOME"),
        WATCHLIST("WATCHLIST"),
        MOVIES("MOVIES"),
        SERIES("SERIES"),
        ORIGINALS("ORIGINALS"),
        SETTINGS("SETTINGS");

        private String navItemText;

        public static final Map<GlobalNavItem, Integer> globalNavMap = IntStream.range(0, GlobalNavItem.values().length)
                .boxed().collect(Collectors.toMap(i -> GlobalNavItem.values()[i], i -> i, Integer::sum));

        GlobalNavItem(String navItemText) {
            this.navItemText = navItemText;
        }

        public String getText() {
            return this.navItemText;
        }
    }

    //TODO: Move all Dictionary keys/api queries to an enum "Class"
    public enum HomePageItems {
        BRIEF_DESCRIPTION_PROGRAM(".text..description.brief.program..content"),
        CONTENT_PROGRAM_TYPE(".programType"),
        CONTENT_TITLES("$..title.full..content"),
        CONTINUE_WATCHING_SET_TITLE("continue_watching_set_title"),
        DEEP_SCAN_VIDEOS_NODE("$..videos[%d]"),
        GET_CONTAINER_LENGTH("$..[?(@.type == '%s')]..items.length()"),
        GET_CONTAINER_ORIGINAL_TYPE("$..[?(@.type == '%s')]..items..tags.[?(@.type == 'disneyPlusOriginal')].value"),
        GET_DISNEY_BRAND_KEYS("$..[?(@.type == 'GridContainer')]..items..key"),
        GET_ENCODED_SERIES_ID(".encodedSeriesId"),
        GET_EPISODES_IN_SEASON("$..seasons.seasons[%d].episodes_meta.hits"),
        GET_HERO_TILE("..images..[?(@.aspectRatio == '3.91' && @.purpose == 'hero_tile')]."),
        GET_HERO_VIDEO_TYPE(".type"),
        GET_ITEMS_IN_HERO_CONTAINER("$..items[%d]"),
        GET_SEASON_ID("$..seasons.seasons[%d].seasonId"),
        GET_SET_ID_USING_REF_TYPE("$..[?(@.refType=='%s')].refId"),
        GET_TOTAL_SEASONS("$..seasons.meta.hits"),
        HOME_PAGE_ROW_TITLES("$..[?(@.type == 'ShelfContainer')]..text..full..content"),
        CONTINUE_WATCHING_SHELF_TITLE("$..[?(@.type == 'ShelfContainer')]..[?(@.refId=='76aed686-1837-49bd-b4f5-5d2a27c0c8d4')]..text..content"),
        META_HITS("$..meta.hits"),
        REF_ID_BY_TYPE("$..[?(@.type == '%s')]..refId"),
        RIPCUT_MASTER_ID("masterId"),
        RIPCUT_MASTER_WIDTH("masterWidth"),
        SETTINGS_NAV_BAR("nav_settings"),
        SET_CONTENT_TITLES("$..[?(@.field=='title' && @.type=='full' )].content"),
        SET_TITLE("$..%s..text..set..content"),
        SHELF_CONTAINER_TYPE("ShelfContainer");

        private String query;

        HomePageItems(String query) {
            this.query = query;
        }

        public String getValue() {
            return this.query;
        }
    }

    public DisneyPlusAndroidTVDiscoverPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        final ExtendedWebElement pageElement = isStar() ? shelfItem : getHeroCarousel();
        fluentWait(getDriver(), 30, 2, "Page did not open before timeout.")
                .until(it -> pageElement.isVisible());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return pageElement.isElementPresent();
    }

    @Override
    public boolean focusHeroTile() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(driver);
        if (!androidTVUtils.isElementFocused(heroTile)) {
            androidTVUtils.pressUp();
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(heroTile);
    }

    public boolean isElementWithTextPresent(String text) {
        return genericTextElement.format(text).isElementPresent();
    }

    /**
     * Use the currently focused hero tile to derive a locator to the parent element that contains the shelf title text.
     * @return name of shelf if on shelf with name.
     */
    public String getFocusedShelfTitle() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(driver);
        ExtendedWebElement focusedTile = getFocusedHeroTile();
        String focusedElementText = "";
        if(focusedTile.isVisible(2)) {
            String focusedHeroTileDesc = androidTVUtils.getContentDescription(focusedTile);
            String locator = "//android.widget.FrameLayout[@content-desc=\""+ focusedHeroTileDesc + "\"]/../../*[@resource-id='com.disney.disneyplus:id/shelfTitle']";
            // Use vanilla web element to work around issue /w ExtendedWebElement
            WebElement focusedShelfTextElement = driver.findElement(By.xpath(locator));

            // A focused tile may be found, but may be in a row without a shelf title...
            if(focusedShelfTextElement.isDisplayed()) {
                focusedElementText = focusedShelfTextElement.getText();
                LOGGER.info("Focused shelf text: {}", focusedElementText);
            }
        }
        return focusedElementText;
    }

    /**
     * Only 2 shelves with content wil be present after landing on the home screen this method will go through those and
     * compare them with the response returned from Search API.
     *
     * @param language - language
     * @param disneySearchApi - disneySearchApi
     * @param user     - disney account
     * @param sa       - SoftAssert instance
     */
    public void verifyHomePageLayout(String language, DisneySearchApi disneySearchApi, DisneyAccount user, SoftAssert sa) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        List<ContentSet> sets = disneySearchApi.getAllSetsInHomeCollection(user, user.getCountryCode(), language, DisneyCollectionType.PERSONALIZED_COLLECTION.getCollectionType());
        List<ExtendedWebElement> shelfTitles = findExtendedWebElements(shelfTitle.getBy());

        for (int i=0; i<sets.size(); i++) {
            if (i == shelfTitles.size())
                break;
            ContentSet contentRow = sets.get(i+1);
            sa.assertEquals(shelfTitles.get(i).getText(), contentRow.getSetName());
            List<ExtendedWebElement> shelfContent = findExtendedWebElements(avatarIconsShelf.format(i + 3).getBy());
            for (int j=0; j<shelfContent.size(); j++) {
                sa.assertEquals(androidTVUtils.getContentDescription(shelfContent.get(j)), contentRow.getTitle(j));
            }
        }
    }

    public void navigateToShelf(String shelf) {
        boolean shelfInFocus = false;
        while (!shelfInFocus) {
            List<ExtendedWebElement> shelfTitles = findExtendedWebElements(shelfTitle.getBy());
            for (ExtendedWebElement element : shelfTitles) {
                if (element.getText().equals(shelf)) {
                    shelfInFocus = true;
                    break;
                } else {
                    new AndroidTVUtils(getDriver()).keyPressTimes(AndroidTVUtils::pressDown, 1, 1);
                }
            }
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public int getHeroIndicatorSize() {
        return findExtendedWebElement(heroCarouselIndicators.getBy()).findExtendedWebElements(By.className("android.widget.ImageView")).size();
    }


    public boolean isKidsHomePageOpen() {
        return getHeroCarousel().isElementPresent();
    }


    public int getHeroCarouselIndexSelected() {
        List<Boolean> statusOfHeroIndicators = JsonPath.read(DisneyPlusAndroidTVCommonPage.getXMLToJsonString(getCastedDriver().getPageSource()),
                String.format(INDICATOR_PATHS, "*"));
        int index = 0;
        for (int i = 0; i < statusOfHeroIndicators.size(); i++) {
            if (statusOfHeroIndicators.get(i)) {
                return i;
            }
        }
        LOGGER.info("Index selected: " + index);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return index;
    }

    public int getOriginalIndexIfPresent(List<String> originalsList) {
        for (int i = 0; i < originalsList.size(); i++) {
            if (originalsList.get(i).equals("true"))
                return i;
        }
        throw new SkipException("No originals content found on hero carousel");
    }

    public void moveToHeroCarousel(int currentIndex, int moveToIndex) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        if (moveToIndex > currentIndex) {
            androidTVUtils.keyPressTimes(AndroidTVUtils::pressRight, moveToIndex - currentIndex, ONE_SEC_TIMEOUT);
        } else if (moveToIndex < currentIndex) {
            androidTVUtils.keyPressTimes(AndroidTVUtils::pressLeft, currentIndex - moveToIndex, ONE_SEC_TIMEOUT);
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public int getSeriesIndex(DisneySearchApi disneySearchApi, JsonNode collectionBody, int sizeOfContainer, DisneyAccount user) {
        for (int i = 0; i < sizeOfContainer; i++) {
            String videoType = DisneySearchApi.parseValueFromJson(collectionBody.toString(), String.format(HomePageItems.GET_ITEMS_IN_HERO_CONTAINER.getValue(), i) + HomePageItems.GET_HERO_VIDEO_TYPE.getValue()).get(0);
            if (videoType.equals("DmcVideo")) {
                String programType = DisneySearchApi.parseValueFromJson(collectionBody.toString(), String.format(HomePageItems.GET_ITEMS_IN_HERO_CONTAINER.getValue(), i) + HomePageItems.CONTENT_PROGRAM_TYPE.getValue()).get(0);
                if (programType.equalsIgnoreCase("episode"))
                    return i;
            } else if (videoType.equals("DmcSeries")) {
                String seriesId = DisneySearchApi.parseValueFromJson(collectionBody.toString(), String.format(GET_ITEMS_IN_HERO_CONTAINER.getValue(), i) + GET_ENCODED_SERIES_ID.getValue()).get(0);
                ContentSeries seriesInformation = disneySearchApi.getSeries(seriesId, user.getCountryCode(), user.getProfileLang());
                int totNumOfSeasons = seriesInformation.getNumberOfSeasons();
                if (seriesInformation.getAllSeasonsEpisodeCounts().get(totNumOfSeasons - 1) == 0)
                    continue;
                return i;
            }
        }
        throw new SkipException("No episodes content found on hero carousel");
    }

    public List<String> getCarouselToMoveAndDescription(DisneySearchApi disneySearchApi, DisneyAccount user, Integer ratingsLimit) {
        JsonNode collectionBody = disneySearchApi.getPersonalizedCollection(user, user.getCountryCode(), user.getProfileLang(), "home", "home").getJsonNode();
        String containerType = fullBleedHeroCarousel.isElementPresent(DELAY) ? "HeroFullBleedContainer" : "HeroContainer";
        String heroContainerSetId = DisneySearchApi.parseValueFromJson(collectionBody.toString(), String.format(REF_ID_BY_TYPE.getValue(), containerType)).get(0);
        ContentSet heroSet = disneySearchApi.getCuratedSet(user, heroContainerSetId, user.getCountryCode(), user.getProfileLang());
        int heroCarouselSize = heroSet.getHits();
        List<String> results = new ArrayList<>();

        // Find a carousel item that meets the ratings level if desired, otherwise return the first item.
        // This is only good for systems with numeric age ratings (IE: France)
        for (JsonNode heroContainer : heroSet.getJsonNode()) {
            // Even if the hits come back > 15 the page size is set to 15, the hero container will only display 15 items max
            heroCarouselSize = Math.min(heroCarouselSize, 15);
            int carouselToMove = getSeriesIndex(disneySearchApi, heroContainer, heroCarouselSize, user);
            String seriesId = DisneySearchApi.parseValueFromJson(heroContainer.toString(), String.format(GET_ITEMS_IN_HERO_CONTAINER.getValue(), carouselToMove) + GET_ENCODED_SERIES_ID.getValue()).get(0);
            ContentSeries contentSeries = disneySearchApi.getSeries(seriesId, user.getCountryCode(), user.getProfileLang());

            if (ratingsLimit != -1) {
                try {
                    int rating = Integer.parseInt(contentSeries.getSeriesRatingsValue());
                    if (rating > ratingsLimit) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    // non-numeric rating, take first carousel item.
                }
            }
            int seasonNumber = contentSeries.getNumberOfSeasons();
            int episodeNumber = contentSeries.getAllSeasonsEpisodeCounts().get(seasonNumber - 1);
            String seasonId  = contentSeries.getSeasonIds().get(seasonNumber - 1);
            ContentSeason season = disneySearchApi.getSeason(seasonId, user.getCountryCode(), user.getProfileLang());
            String briefDescription = season.getEpisodeDescriptions().get(episodeNumber - 1);

            results = Stream.of(String.valueOf(carouselToMove), String.valueOf(seasonNumber), String.valueOf(episodeNumber), briefDescription).collect(Collectors.toList());
        }
        return results;
    }

    public BufferedImage getCurrentlySelectedLogo() {
        return new UniversalUtils().getElementImage(findExtendedWebElements(welcomeLogo.getBy()).get(1));
    }

    public List<String> getBrandTilesContentDesc(int brandCount) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        List<ExtendedWebElement> brandContainer = findExtendedWebElements(heroTile.getBy());
        //Starting from fourth index here because first 3 are part of the hero tile
        UniversalUtils.captureAndUpload(getCastedDriver());
        return IntStream.range(3, 3 + brandCount).mapToObj(i -> androidTVUtils.getContentDescription(brandContainer.get(i))).collect(Collectors.toList());
    }

    public String getBrandTileContentDesc(int index) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        List<ExtendedWebElement> brandContainer = findExtendedWebElements(heroTile.getBy());
        List<String> sortedTiles = new ArrayList<>();
        for (ExtendedWebElement brandTile : brandContainer) {
            String tiles = androidTVUtils.getContentDescription(brandTile);
            sortedTiles.add(tiles);
        }
        // Remove the hero carousel tile so Brand tiles are first
        sortedTiles.remove(1);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return sortedTiles.get(index);
    }

    public List<ExtendedWebElement> getAllTiles() {
        return findExtendedWebElements(heroTile.getBy());
    }

    public BufferedImage getBrandTileImage(UniversalUtils.ImageMeta imageMeta) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return new UniversalUtils().getElementImage(imageMeta.getPoint(), imageMeta.getWidth(), imageMeta.getHeight());
    }


    public List<ExtendedWebElement> getBrandTileList(int index) {
        List<ExtendedWebElement> brandContainer = findExtendedWebElements(heroTile.getBy());
        return IntStream.range(2, 4 + index).mapToObj(brandContainer::get).collect(Collectors.toList());
    }

    public boolean isBrandTileFocused(int index) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        heroTile.isElementPresent();
        return new AndroidTVUtils(getDriver()).isFocused(getBrandTileList(index).get(index));
    }

    public List<ExtendedWebElement> getShelfContentPosters() {
        List<ExtendedWebElement> postersList = findExtendedWebElements(contentPoster.getBy());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return postersList;
    }

    public String getAssetTitle() {
        return assetTitle.getText();
    }

    public List<UniversalUtils.ImageMeta> getImagesMeta(int index) {
        List<ExtendedWebElement> list = getBrandTileList(index);
        return list.stream()
                .map(UniversalUtils.ImageMeta::new)
                .collect(Collectors.toList());
    }

    public List<String> getShelfTitles() {
        List<String> shelfTitles = findExtendedWebElements(shelfTitle.getBy()).stream().map(ExtendedWebElement::getText).collect(Collectors.toList());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return shelfTitles;
    }

    public List<String> getVisibleCollectionTitles() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(driver);
        List<String> tileTitles = new ArrayList<>();

        for (ExtendedWebElement tile : getAllTiles()) {
            String title = androidTVUtils.getContentDescription(tile);
            tileTitles.add(title);
        }

        UniversalUtils.captureAndUpload(getCastedDriver());
        return tileTitles;
    }

    public boolean isHomeGlobalNavHomeFocused() {
        globalNavHome.isElementPresent();
        return new AndroidTVUtils(getDriver()).isFocused(globalNavHome);
    }

    public boolean isPlayIconVisible() {
       return playIcon.isElementPresent();
    }
}
