package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Locale;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeDOBCollectionIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "birthdateEntryView")
    private ExtendedWebElement verifyAgeDOBPage;

    @ExtendedFindBy(accessibilityId = "verifyAgeButton")
    private ExtendedWebElement verifyAgeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`value == \"MM/DD/YYYY\"`]")
    private ExtendedWebElement monthDayYearBirthdayTextField;

    //FUNCTIONS
    public DisneyPlusVerifyAgeDOBCollectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getStaticTextByLabel(getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_DOB_PAGE_HEADER.getText())).isPresent(SHORT_TIMEOUT);
    }

    public void clickVerifyAgeButton() {
        verifyAgeButton.click();
    }

    public boolean isBackModalDisplayed() {
        return isViewAlertPresent() && staticTextByLabel.format(getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_VERIFY_AGE_CANCEL_MODAL.getText())).isPresent();
    }

    public ExtendedWebElement getR21Birthday() {
        String r21Format = getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_BIRTHDAY_FORMAT.getText()).toUpperCase();
        return getTextFieldValue(r21Format);
    }
}