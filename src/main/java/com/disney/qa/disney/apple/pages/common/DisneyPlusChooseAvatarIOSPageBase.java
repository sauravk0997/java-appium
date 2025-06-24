package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChooseAvatarIOSPageBase extends DisneyPlusApplePageBase {
    private static final String AVATAR_HEADER_TITLE = "headerViewTitleLabel";

    @ExtendedFindBy(accessibilityId = "avatarSelectionScreenView")
    ExtendedWebElement avatarSelectionScreenView;

    private ExtendedWebElement skipButton = getDynamicAccessibilityId(
            getLocalizationUtils().getDictionaryItem(
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
                && getChooseAvatarTitle().isPresent();
    }

    public ExtendedWebElement getChooseAvatarTitle() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CHOOSE_AVATAR_TITLE.getText()));
    }

    public ExtendedWebElement getSkipButton() {
        return skipButton;
    }

    public ExtendedWebElement getAvatarHeaderTitle() {
        return getStaticTextByNameContains(AVATAR_HEADER_TITLE);
    }

    public List<ExtendedWebElement> getHeaderTitlesInView() {
        if (getAvatarHeaderTitle().isPresent()) {
            return findExtendedWebElements(getAvatarHeaderTitle().getBy(), THREE_SEC_TIMEOUT);
        } else {
            throw new java.util.NoSuchElementException("Failing test, header view elements not found.");
        }
    }
}
