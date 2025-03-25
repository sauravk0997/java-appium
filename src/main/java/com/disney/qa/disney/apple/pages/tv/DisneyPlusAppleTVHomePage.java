package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVHomePage extends DisneyPlusHomeIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(accessibilityId = "profileTab")
    private ExtendedWebElement profileBtnGlobalNav;

    @ExtendedFindBy(accessibilityId = "homeTab")
    private ExtendedWebElement homeButton;

    @ExtendedFindBy(accessibilityId = "homeContentView")
    private ExtendedWebElement homeContentView;

    @ExtendedFindBy(accessibilityId = "watchlistCell")
    private ExtendedWebElement watchlistButton;

    @ExtendedFindBy(accessibilityId = "%s")
    private ExtendedWebElement plate;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Access %s's profile\"`]")
    private ExtendedWebElement profileSelectionBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Access %s's pin protected profile\"`]")
    private ExtendedWebElement lockedProfileSelectionBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == 'Explore'`]")
    private ExtendedWebElement exploreText;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'Movies'`]")
    private ExtendedWebElement moviesBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[label == \"%s\"][1]")
    private ExtendedWebElement asset;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"Disney\"`]")
    private ExtendedWebElement disneyBrandTile;

    @ExtendedFindBy(accessibilityId = "viewAlert")
    private ExtendedWebElement viewAlert;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'settingsTab'`]")
    private ExtendedWebElement navMenuSettings;

    //When QAE-124 is fixed remove this element and related method
    @FindBy(xpath = "//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeCollectionView/XCUIElementTypeCell[2]")
    private ExtendedWebElement carouselFocusedElement;

    private ExtendedWebElement travelingAlertOkBtn = getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.BTN_TRAVEL_MESSAGE_OK.getText()));

    public DisneyPlusAppleTVHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? disneyBrandTile.isElementPresent() : homeContentView.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    @Override
    public void waitForHomePageToOpen() {
        LOGGER.info("Waiting for Home page to load");
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Home page is not opened")
                .until(it -> getHomePageMainElement().isPresent(THREE_SEC_TIMEOUT));
    }

    @Override
    public boolean isKidsHomePageOpen() {
        return getTypeCellNameContains("Disney Junior").isElementPresent();
    }

    public void clickHomeBtn() {
        homeButton.click();
    }

    public void clickNavMenuSettings() { navMenuSettings.click(); }

    public DisneyPlusAppleTVWatchListPage clickWatchlistButton() {
        watchlistButton.click();
        return new DisneyPlusAppleTVWatchListPage(getDriver());
    }

    public boolean isPlatePresent(String plateName) {
        return plate.format(plateName).isPresent(10);
    }

    public boolean isHomeBtnPresent() {
        return homeButton.isPresent();
    }

    public boolean isExploreTextPresent() {
        return exploreText.isPresent();
    }

    private boolean isTravelingAlertPresent() {
        return viewAlert.isElementPresent();
    }

    public void dismissTravelingAlert() {
        if (isTravelingAlertPresent()) {
            travelingAlertOkBtn.click();
        }
    }

    public void clickMoviesBtn() {
        moviesBtn.click();
    }

    public void clickProfileBtn(String profileName) {
        profileSelectionBtn.format(profileName).click();
    }

    public void clickLockedProfileBtn(String profileName) {
        lockedProfileSelectionBtn.format(profileName).click();
    }

    public String whichGlobalNavMenuIsFocused() {
        for (globalNavigationMenu menu : globalNavigationMenu.values()) {
            String currentMenu = menu.getText();
            LOGGER.info("Is current menu {} found? {} ", currentMenu, getDynamicAccessibilityId(menu.getText()).isPresent());
            System.out.println(getDriver().getPageSource());
            if (isFocused(getDynamicAccessibilityId(menu.getText()))) {
                LOGGER.info(String.format("%s is focused on global nav", currentMenu));
                return currentMenu;
            }
        }
        throw new SkipException("Failed to find focused global nav menu item");
    }

    public void openGlobalNavWithClickingMenu() {
        LOGGER.info("Opening Global Nav Menu with clicking menu");
        clickMenu();
    }

    public void navigateToOneGlobalNavMenu(String globalNav) {
        List<String> globalNavList = Stream.of(globalNavigationMenu.values())
                .map(globalNavigationMenu::getText)
                .collect(Collectors.toList());
        String currentMenu = whichGlobalNavMenuIsFocused();
        int timeToMove = globalNavList.indexOf(currentMenu) - globalNavList.indexOf(globalNav);
        if (timeToMove > 0) {
            moveUp(Math.abs(timeToMove), 1);
        } else {
            moveDown(Math.abs(timeToMove), 1);
        }
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        whichGlobalNavMenuIsFocused();
    }

    public enum globalNavigationMenu {
        PROFILE("profileTab"),
        SEARCH("searchTab"),
        HOME("homeTab"),
        WATCHLIST("watchlistCell"),
        MOVIES("Movies"),
        SERIES("Series"),
        ORIGINALS("Originals"),
        SETTINGS("settingsTab");

        private final String menu;

        globalNavigationMenu(String menu) {
            this.menu = menu;
        }

        public String getText() {
            return this.menu;
        }
    }

    // For Alice text validation
    public enum globalNavigationMenuText {
        PROFILE,
        SEARCH,
        HOME,
        WATCHLIST,
        MOVIES,
        SERIES,
        ORIGINALS,
        SETTINGS
    }

    public List<String> getEnumMenuText() {
        var list = new ArrayList<String>();
        Arrays.stream(globalNavigationMenuText.values()).forEach(
                item -> list.add(getNavigationMenuValue(item)));
        return list;
    }

    public String getNavigationMenuValue(globalNavigationMenuText option) {
        String selection;
        switch (option) {
            case PROFILE:
                selection = "Test";
                break;
            case SEARCH:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, CDNAV_SEARCH.getText());
                break;
            case HOME:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, CDNAV_HOME.getText());
                break;
            case WATCHLIST:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_WATCHLIST_TITLE.getText());
                break;
            case MOVIES:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_MOVIES_TITLE.getText());
                break;
            case SERIES:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_SERIES_TITLE.getText());
                break;
            case ORIGINALS:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_ORIGINALS_TITLE.getText());
                break;
            case SETTINGS:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, NAV_SETTINGS_TITLE.getText());
                break;
            default:
                throw new InvalidArgumentException("Invalid selection made");
        }
        return selection;
    }

    public boolean isProfileBtnFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(profileBtnGlobalNav);
    }

    public boolean isProfileBtnPresent() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return profileBtnGlobalNav.isElementPresent();
    }

    @Override
    public boolean isAlertPresent() {
        return viewAlert.isElementPresent();
    }

    public boolean isProfileNamePresent(String name) {
        return dynamicBtnFindByLabelContains.format(name).isElementPresent();
    }

    public void moveDownFromHeroTileToBrandTile() {
        moveDown(1, 1);
    }

    public void openAnyContentOnInnerPages(int times, int timeout) {
        pause(timeout);
        moveDown(times, timeout);
        clickSelect();
        pause(timeout);
    }

    public void hiddenNavStateOnInnerPages(String globalNavMenu) {
        pause(THREE_SEC_TIMEOUT); //if no pause, selecting menu back goes to native home outside of app
        LOGGER.info("Navigating to global nav menu: {}", globalNavMenu);
        if (globalNavMenu.equalsIgnoreCase(globalNavigationMenu.MOVIES.getText())){
            clickLeft();
        } else {
            clickMenuTimes(1, 2);
        }
        navigateToOneGlobalNavMenu(globalNavMenu);
        clickSelect();
    }

    public void openGlobalNavAndSelectOneMenu(String menu) {
        clickMenuTimes(1,1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        navigateToOneGlobalNavMenu(menu);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        clickSelect();
        //Sometimes global nav is not dismissed, to accommodate when clicking select does not work first time
        if (new DisneyPlusApplePageBase(getDriver()).isGlobalNavExpanded()) {
            clickSelect();
        }
    }

    public DisneyPlusAppleTVHomePage checkIfElementAttributeFound(ExtendedWebElement element, String name) {
        try {
            LOGGER.info("Looking for {}'s {} attribute", element, name);
            fluentWaitNoMessage(getDriver(), 250, 25).until(it -> doesAttributeEqualTrue(element, name));
        } catch (Exception e) {
            throw new SkipException(element + "'s " + name + " attribute not found");
        }
        return new DisneyPlusAppleTVHomePage(getDriver());
    }

    public boolean isCurrentCarouselAnthology(String label) {
        return doesAttributeEqualTrue(getStaticTextByLabel(label), "enabled");
    }

    public void clickAnthologyCarousel(String label) {
        //Sometimes current carousel slide is not still in focus, this function handles this issue
        if (!isCurrentCarouselAnthology(label)) {
            clickSelect();
        } else {
            moveLeft(1,1);
            isCurrentCarouselAnthology(label);
            clickSelect();
        }
    }

    public boolean isHomeContentViewPresent() {
        return homeContentView.isElementPresent();
    }

    public boolean isCarouselFocused() {
        return isFocused(carouselFocusedElement);
    }

    public List<ExtendedWebElement> getBrandTiles() {
        return findExtendedWebElements(
                getTypeCellLabelContains(String.format(
                        getLocalizationUtils().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.CONTENT_TILE_INTERACT.getText())))
                        .getBy());
    }

    public void clickBrandTile(String brandLabel) {
        boolean expectedBrandTileIsFocused = false;
        //Declared the following variable before because result of this call is not consistent
        // We want to avoid reevaluation of the expression on each iteration
        int brandTilesQuantity = getBrandTiles().size();
        for(int i = 0; i < brandTilesQuantity; i++) {
            clickRight();
            pause(1);
            if(isFocused(getBrandCell(brandLabel))) {
                expectedBrandTileIsFocused = true;
                break;
            }
        }
        if(expectedBrandTileIsFocused) { clickSelect(); }
        else { throw new SkipException(brandLabel + " brand tile was not focused"); }

    }

    public void navigateToShelf(ExtendedWebElement element) {
        int count = 10;
        while (count > 0) {
            moveDown(1, 1);
            if (element.isPresent(ONE_SEC_TIMEOUT)) {
                break;
            }
            count--;
        }
    }
}
