package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEditProfileIOSPageBase.class)
public class DisneyPlusAppleTVEditProfilePage extends DisneyPlusEditProfileIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Edit %s's profile.\"`]")
    private ExtendedWebElement dynamicEditProfileIcon;

    public DisneyPlusAppleTVEditProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isEditModeProfileIconPresent(String username) {
        return dynamicEditProfileIcon.format(username).isElementPresent();
    }

    @Override
    public void toggleAutoplayButton(String newState) {
        String currentState = autoplayToggleCell.getText();
        LOGGER.info("Current state of autoplay: {}, requested state: {}", currentState, newState);
        if (!currentState.equalsIgnoreCase(newState)) {
            autoplayToggleCell.click();
        }
    }

    public ExtendedWebElement getGenderLabel() {
        return getTypeOtherContainsLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.GENDER_LABEL.getText()));
    }
}
