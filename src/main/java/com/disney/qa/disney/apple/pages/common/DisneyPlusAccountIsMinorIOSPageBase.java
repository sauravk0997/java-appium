package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Landing page when the user logs in with a minor account
 */

public class DisneyPlusAccountIsMinorIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusAccountIsMinorIOSPageBase(WebDriver driver) { super(driver); }

    @ExtendedFindBy(accessibilityId = "actionableAlertTitle")
    private ExtendedWebElement notEligibleHeader;

    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    private ExtendedWebElement helpCenterButton;

    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    private ExtendedWebElement dismissButton;

    private ExtendedWebElement notEligibleSubText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CONTACT_CSR_SUBTITLE);

    @Override
    public boolean isOpened() { return notEligibleHeader.isElementPresent(); }

    public ExtendedWebElement getNotEligibleHeader() { return notEligibleHeader; }

    public ExtendedWebElement getNotEligibleSubText() { return notEligibleSubText; }

    public ExtendedWebElement getHelpCenterButton() { return helpCenterButton; }

    public ExtendedWebElement getDismissButton() { return dismissButton; }
}
