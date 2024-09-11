package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusKidProofExitIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"iconNavClose24LightActive\"`]")
    private ExtendedWebElement closeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type = " +
            "'XCUIElementTypeStaticText'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement digitsElement;

    public DisneyPlusKidProofExitIOSPageBase(WebDriver driver) {
        super(driver);
    }
    public boolean getKidProofDialogTitle() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                KIDPROOF_DIALOG_TITLE.getText())).isPresent();
    }

    public boolean getKidProofDialogIncorrectCode() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                KIDPROOF_DIALOG_ERROR_INCORRECT_CODE_1.getText())).isPresent();
    }

    public ExtendedWebElement getCloseButton() {
        return closeButton;
    }

    public ExtendedWebElement getDigitsElement() {
        return digitsElement;
    }
}
