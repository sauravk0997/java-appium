package com.disney.qa.disney.apple.pages.tv;

import com.disney.exceptions.FailedToFocusElementException;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.CDNAV_HOME;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.CDNAV_SEARCH;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_MOVIES_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_ORIGINALS_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_SERIES_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_SETTINGS_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_WATCHLIST_TITLE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVHomePage extends DisneyPlusHomeIOSPageBase {

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Disney\"`]")
    private ExtendedWebElement disneyBrandTile;

    @ExtendedFindBy(accessibilityId = "viewAlert")
    private ExtendedWebElement viewAlert;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'settingsTab'`]")
    private ExtendedWebElement navMenuSettings;

    private ExtendedWebElement travelingAlertOkBtn = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.BTN_TRAVEL_MESSAGE_OK.getText()));

    public DisneyPlusAppleTVHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? disneyBrandTile.isElementPresent() : homeContentView.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    @Override
    public boolean isKidsHomePageOpen() {
        return getDynamicCellByLabel("Mickey and Friends").isElementPresent();
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
            if (isFocused(getDynamicAccessibilityId(menu.getText()))) {
                LOGGER.info(String.format("%s is focused on global nav", currentMenu));
                return currentMenu;
            }
        }
        throw new FailedToFocusElementException("Failed to find focused global nav menu item");
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

        private String menu;

        globalNavigationMenu(String menu) {
            this.menu = menu;
        }

        public String getText() {
            return this.menu;
        }
    }

    // For Alice text validation
    public enum globalNavigationMenuText {
        PROFILE("Test"),
        SEARCH(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, CDNAV_SEARCH.getText())),
        HOME(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, CDNAV_HOME.getText())),
        WATCHLIST(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_WATCHLIST_TITLE.getText())),
        MOVIES(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_MOVIES_TITLE.getText())),
        SERIES(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_SERIES_TITLE.getText())),
        ORIGINALS(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_ORIGINALS_TITLE.getText())),
        SETTINGS(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, NAV_SETTINGS_TITLE.getText()));

        private String menu;

        globalNavigationMenuText(String menu) {
            this.menu = menu;
        }

        public String getText() {
            return this.menu;
        }
    }

    public boolean isProfileBtnFocused() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isFocused(profileBtnGlobalNav);
    }

    public boolean isProfileBtnPresent() {
        UniversalUtils.captureAndUpload(getCastedDriver());
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
        pause(SHORT_TIMEOUT); //if no pause, selecting menu back goes to native home outside of app
        openGlobalNavWithClickingMenu();
        LOGGER.info("Navigating to global nav menu: {}", globalNavMenu);
        navigateToOneGlobalNavMenu(globalNavMenu);
        clickSelect();
        openAnyContentOnInnerPages(1, 1);
        if (globalNavMenu.equalsIgnoreCase(globalNavigationMenu.SEARCH.getText()) ||
                globalNavMenu.equalsIgnoreCase(globalNavigationMenu.MOVIES.getText()) ||
                globalNavMenu.equalsIgnoreCase(globalNavigationMenu.SERIES.getText())) {
            clickMenuTimes(2,1);
        } else {
            clickMenuTimes(1,1);
        }
    }

    public void openGlobalNavAndSelectOneMenu(String menu) {
        clickMenuTimes(1,1);
        UniversalUtils.captureAndUpload(getCastedDriver());
        navigateToOneGlobalNavMenu(menu);
        UniversalUtils.captureAndUpload(getCastedDriver());
        clickSelect();
    }

    public void traverseAndVerifyHomepageLayout(List<ContentSet> sets, List<String> brands, SoftAssert sa) {
        brands.forEach(item -> {
            sa.assertTrue(isFocused(getDynamicCellByLabel(item)), "The following brand tile was not focused: " + item);
            UniversalUtils.captureAndUpload(getCastedDriver());
            moveRight(1, 1);
        });

        moveDown(1, 1);
        moveLeft(4, 1);
        moveRight(4, 1);
        for (int i=0; i<sets.size(); i++) {
            var shelfTitle = sets.get(i).getSetName();
            var getSetAssets = sets.get(i).getTitles();

            sa.assertTrue(isAIDElementPresentWithScreenshot(shelfTitle), "Following shelf container not found " + shelfTitle);

            String item = getSetAssets.get(4);

            boolean isPresent = dynamicCellByLabel.format(item).isElementPresent();
            boolean isFocused = isFocused(dynamicCellByLabel.format(item));
            UniversalUtils.captureAndUpload(getCastedDriver());

            if(!isFocused){
                moveLeft(1,1);
                isFocused = isFocused(dynamicCellByLabel.format(getSetAssets.get(3)));
                moveRight(1,1);
                UniversalUtils.captureAndUpload(getCastedDriver());
            }
            sa.assertTrue(isPresent, "The following content was not found " + item);
            sa.assertTrue(isFocused, "The following content was not focused " + item);

            moveDown(1, 1);
        }
    }

    public DisneyPlusAppleTVHomePage checkIfElementAttributeFound(ExtendedWebElement element, String name) {
        try {
            LOGGER.info("Looking for {}'s {} attribute", element, name);
            fluentWaitNoMessage(getCastedDriver(), 250, 25).until(it -> doesAttributeEqualTrue(element, name));
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
}
