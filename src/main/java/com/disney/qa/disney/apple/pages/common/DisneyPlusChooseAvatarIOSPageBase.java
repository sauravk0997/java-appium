package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChooseAvatarIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "avatarSelectionScreenView")
    ExtendedWebElement avatarSelectionScreenView;

    ExtendedWebElement chooseAvatarTitle = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CHOOSE_AVATAR_TITLE.getText()));

    private ExtendedWebElement skipButton = getDynamicAccessibilityId(
            getDictionary().getDictionaryItem(
                    DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.CHOOSE_PROFILE_ICON_SKIP.getText()));

    public DisneyPlusChooseAvatarIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public void typeProfileName(String name) {
        textEntryField.type(name);
    }

    public void clickSkipButton() {
        skipButton.click();
    }

    public boolean isSkipButtonPresent() {
        return skipButton.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return avatarSelectionScreenView.isPresent()
                && chooseAvatarTitle.isPresent();
    }

    public void verifyChooseAvatarPage() {
        backButton.isElementPresent();
        staticTextByLabel.format("Featured").isPresent();
        staticTextByLabel.format("Disney").isPresent();
    }

    public ExtendedWebElement getSkipButton() {
        return skipButton;
    }
}
