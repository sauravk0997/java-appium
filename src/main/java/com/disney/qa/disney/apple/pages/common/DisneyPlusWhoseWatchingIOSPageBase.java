package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/*
 * Who's Watching Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWhoseWatchingIOSPageBase extends DisneyPlusApplePageBase {

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
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isHeaderTextDisplayed() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return whosWatchingTitle.isPresent();
    }

    public boolean isHeaderTextPresent() {
        return whosWatchingTitle.isPresent();
    }

    public boolean isAddProfileBtnPresent() {
        UniversalUtils.captureAndUpload(getCastedDriver());
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        ExtendedWebElement profileIcon;
        if(onboarding) {
            profileIcon = dynamicAccessProfileIcon.format(
                    getDictionary().replaceValuePlaceholders(
                            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), name));
            profileIcon.click();
        } else {
            profileIcon = dynamicAccessProfileIcon.format(
                    getDictionary().replaceValuePlaceholders(
                            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText(), false), name));
            profileIcon.click();
        }
    }

    public void clickProfile(String name) {
        clickProfile(name, false);
    }

    public void clickPinProtectedProfile(String name) {
        dynamicAccessProfileIcon.format(
                getDictionary().replaceValuePlaceholders(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), name))
                .click();
    }

    public boolean isAccessModeProfileIconPresent(String username) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return dynamicAccessProfileIcon.format(
                getDictionary().replaceValuePlaceholders(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), username))
                .isElementPresent();
    }

    public void waitForProfileButton(String username) {
        LOGGER.info("Waiting for loading of profile button");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Profile button is not present")
                .until(it -> dynamicAccessProfileIcon.format(
                        getDictionary().formatPlaceholderString(
                                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of("user_profile", username))).isElementPresent());
        LOGGER.info("Profile button is present");
    }
}