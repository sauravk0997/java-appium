package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "verifyAge")
    private ExtendedWebElement verifyAge;

    @ExtendedFindBy(accessibilityId = "verifyMaturity")
    private ExtendedWebElement acceptMaturityButton;

    @ExtendedFindBy(accessibilityId = "underAgeButton")
    private ExtendedWebElement declineMaturityButton;

    //FUNCTIONS
    public DisneyPlusVerifyAgeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Verify Age Page is not opened")
                .until(it -> verifyAge.isPresent(THREE_SEC_TIMEOUT));
    }

    public void clickIAm21PlusButton() {
        waitForPresenceOfAnElement(acceptMaturityButton);
        tap(acceptMaturityButton);
    }

    public void clickNoButton() {
        waitForPresenceOfAnElement(declineMaturityButton);
        tap(declineMaturityButton);
    }

    public boolean isR21MustBe21YearOlderModalDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_VERIFY_AGE_MODEL_HEADER.getText())).isPresent();
    }

    public boolean isBrowseOtherTitlesButtonDisplayed() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_VERIFY_AGE_MODEL_BUTTON.getText())).isPresent();
    }
}
