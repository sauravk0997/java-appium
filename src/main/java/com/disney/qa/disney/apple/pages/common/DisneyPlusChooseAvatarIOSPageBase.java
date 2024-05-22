package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.security.SecureRandom;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChooseAvatarIOSPageBase extends DisneyPlusApplePageBase {
    private static final String AVATAR_HEADER_TITLE = "headerViewTitleLabel";
    @ExtendedFindBy(accessibilityId = "avatarSelectionScreenView")
    ExtendedWebElement avatarSelectionScreenView;

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
                && getChooseAvatarTitle().isPresent();
    }

    public ExtendedWebElement getChooseAvatarTitle() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CHOOSE_AVATAR_TITLE.getText()));
    }

    public void verifyChooseAvatarPage() {
        backButton.isElementPresent();
        staticTextByLabel.format("Featured").isPresent();
        staticTextByLabel.format("Disney").isPresent();
    }

    public ExtendedWebElement getSkipButton() {
        return skipButton;
    }

    public ExtendedWebElement getAvatarHeaderTitle() {
        return getStaticTextByNameContains(AVATAR_HEADER_TITLE);
    }

    public List<ExtendedWebElement> getHeaderTitlesInView() {
        if (getAvatarHeaderTitle().isPresent()) {
            return findExtendedWebElements(getAvatarHeaderTitle().getBy(), SHORT_TIMEOUT);
        } else {
            throw new java.util.NoSuchElementException("Failing test, header view elements not found.");
        }
    }

    public int getRandomAvatarCollectionNum() {
        int randomCollectionNum = 0;
        try {
            //First two collection views are not avatar collections, therefore size() minus 2
            randomCollectionNum = new SecureRandom().nextInt(getCollectionViews().size()-2);
        } catch (IndexOutOfBoundsException e) {
            Assert.fail(String.format("Index out of bounds: %s", e));
        }
        return randomCollectionNum;
    }
}
