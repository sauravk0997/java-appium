package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/*
 * Who's Watching Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWhoseWatchingIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(accessibilityId = "collectionHeadlineTitle")
    private ExtendedWebElement whosWatchingTitle;

    @ExtendedFindBy(accessibilityId = "editProfileButton")
    private ExtendedWebElement editProfileBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]")
    private ExtendedWebElement dynamicAccessProfileIcon;

    @ExtendedFindBy(accessibilityId = "addProfileCell")
    private ExtendedWebElement addProfile;

    @ExtendedFindBy(accessibilityId = "unlockedProfileCell")
    private ExtendedWebElement unlockedProfile;

    public DisneyPlusWhoseWatchingIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = whosWatchingTitle.isElementPresent();
        return isPresent;
    }

    public ExtendedWebElement getUnlockedProfileCell() {
        return unlockedProfile;
    }

    public ExtendedWebElement getEditProfileButton() {
        return editProfileBtn;
    }

    public boolean isHeaderTextDisplayed() {
        return whosWatchingTitle.isPresent();
    }

    public boolean isHeaderTextPresent() {
        return whosWatchingTitle.isPresent();
    }

    public boolean isAddProfileBtnPresent() {
        return addProfile.isElementPresent();
    }

    public void clickAddProfile() {
        addProfile.click();
    }

    public boolean isEditProfileButtonDisplayed() {
        return editProfileBtn.isElementPresent();
    }

    public void clickEditProfile() {
        editProfileBtn.click();
    }

    public void clickProfile(String name, boolean onboarding) {
        LOGGER.info("Clicking profile name '{}'...", name);
        ExtendedWebElement profileIcon;
        if(onboarding) {
            profileIcon = dynamicAccessProfileIcon.format(
                    getLocalizationUtils().formatPlaceholderString(
                            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), Map.of(USER_PROFILE, name)));
            waitForPresenceOfAnElement(profileIcon);
            profileIcon.click();
        } else {
            profileIcon = dynamicAccessProfileIcon.format(
                    getLocalizationUtils().formatPlaceholderString(
                            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, name)));
            waitForPresenceOfAnElement(profileIcon);
            profileIcon.click();
        }
    }

    public boolean isProfileIconPresent(String profileName) {
        ExtendedWebElement profileIcon;
        profileIcon = dynamicAccessProfileIcon.format(
                getLocalizationUtils().formatPlaceholderString(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), Map.of(USER_PROFILE, profileName)));
        return profileIcon.isPresent();

    }

    public void clickProfile(String name) {
        clickProfile(name, false);
    }

    public void clickPinProtectedProfile(String name) {
        waitForPresenceOfAnElement(getCellPinProtectedProfileIcon(name));
        getCellPinProtectedProfileIcon(name).click();
    }

    public boolean isAccessModeProfileIconPresent(String username) {
        return dynamicAccessProfileIcon.format(getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, username))).isPresent();
    }
}
