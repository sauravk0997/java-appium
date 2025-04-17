package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeDOBCollectionIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "birthdateEntryView")
    private ExtendedWebElement verifyAgeDOBPage;

    @ExtendedFindBy(accessibilityId = "verifyAgeButton")
    private ExtendedWebElement verifyAgeButton;

    //FUNCTIONS
    public DisneyPlusVerifyAgeDOBCollectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getStaticTextByLabel(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_DOB_PAGE_HEADER.getText())).isPresent(THREE_SEC_TIMEOUT);
    }

    public void waitForVerifyAgeDOBCollectionPageToOpen() {
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Password Page is not opened")
                .until(it -> verifyAgeDOBPage.isPresent(THREE_SEC_TIMEOUT));
    }

    public void clickVerifyAgeButton() {
        verifyAgeButton.click();
    }

    public boolean isR21VerifyYourAgeModalDisplayed() {
        return isViewAlertPresent() && staticTextByLabel.format(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_VERIFY_AGE_CANCEL_MODAL.getText())).isPresent();
    }

    public boolean isR21InvalidBirthdateErrorMessageDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_DOB_ERROR_INVALID_BIRTHDATE.getText())).isPresent();
    }


    public void clickBrowseOtherTitlesButton() {
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_VERIFY_AGE_MODEL_BUTTON.getText())).click();
    }
}