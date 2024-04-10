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
    private ExtendedWebElement editProfilesBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]")
    private ExtendedWebElement dynamicAccessProfileIcon;

    @ExtendedFindBy(accessibilityId = "addProfileCell")
    private ExtendedWebElement addProfile;

    public DisneyPlusWhoseWatchingIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = whosWatchingTitle.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isHeaderTextDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return whosWatchingTitle.isPresent();
    }

    public boolean isHeaderTextPresent() {
        return whosWatchingTitle.isPresent();
    }

    public boolean isAddProfileBtnPresent() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return addProfile.isElementPresent();
    }

    public void clickAddProfile() {
        addProfile.click();
    }

    public boolean isEditProfilesButtonDisplayed() {
        return editProfilesBtn.isElementPresent();
    }

    public void clickEditProfile() {
        editProfilesBtn.click();
    }

    public void clickProfile(String name, boolean onboarding) {
        LOGGER.info("Clicking profile name '{}'...", name);
        ExtendedWebElement profileIcon;
        if(onboarding) {
            profileIcon = dynamicAccessProfileIcon.format(
                    getDictionary().formatPlaceholderString(
                            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), Map.of(USER_PROFILE, name)));
            waitForPresenceOfAnElement(profileIcon);
            profileIcon.click();
        } else {
            profileIcon = dynamicAccessProfileIcon.format(
                    getDictionary().formatPlaceholderString(
                            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, name)));
            waitForPresenceOfAnElement(profileIcon);
            profileIcon.click();
        }
    }

    public boolean isProfileIconPresent(String profileName) {
        ExtendedWebElement profileIcon;
        profileIcon = dynamicAccessProfileIcon.format(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), Map.of(USER_PROFILE, profileName)));
        return profileIcon.isPresent();

    }

    public void clickProfile(String name) {
        clickProfile(name, false);
    }

    public void clickPinProtectedProfile(String name) {
        dynamicAccessProfileIcon.format(
                        getDictionary().formatPlaceholderString(
                                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, name)))
                .click();
    }

    public boolean isAccessModeProfileIconPresent(String username) {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return dynamicAccessProfileIcon.format(
                        getDictionary().formatPlaceholderString(
                                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, username)))
                .isPresent();
    }

    public void waitForProfileButton(String username) {
        LOGGER.info("Waiting for loading of profile button");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Profile button is not present")
                .until(it -> dynamicAccessProfileIcon.format(
                        getDictionary().formatPlaceholderString(
                                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, username))).isElementPresent());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }
}