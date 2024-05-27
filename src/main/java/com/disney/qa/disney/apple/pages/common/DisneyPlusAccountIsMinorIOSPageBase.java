package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Landing page when the user logs in with a minor account
 */

public class DisneyPlusAccountIsMinorIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusAccountIsMinorIOSPageBase(WebDriver driver) { super(driver); }

    private ExtendedWebElement notEligibleHeader = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ACCOUNT_BLOCK_HEADER);

    @ExtendedFindBy(accessibilityId = "Help Center")
    private ExtendedWebElement helpCenterButton;

    @ExtendedFindBy(accessibilityId = "Dismiss")
    private ExtendedWebElement dismissButton;

    @Override
    public boolean isOpened() { return notEligibleHeader.isElementPresent(); }

    public ExtendedWebElement getNotEligibleHeader() { return notEligibleHeader; }

    public ExtendedWebElement getHelpCenterButton() { return helpCenterButton; }

    public ExtendedWebElement getDismissButton() { return dismissButton; }

    public void clickHelpCenterButton() {
        helpCenterButton.click();
    }

    public void clickDismissButton() {
        dismissButton.click();
    }

    public ExtendedWebElement getNotEligibleSubText() {
        String subscribeText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ACCOUNT_BLOCK_BODY.getText());
        String subscribeText2 = getDictionary().formatPlaceholderString(subscribeText, Map.of("link_1", "here"));
        return staticTextByLabel.format(subscribeText2);
    }

}
