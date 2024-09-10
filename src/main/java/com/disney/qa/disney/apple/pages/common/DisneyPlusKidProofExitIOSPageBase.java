package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

public class DisneyPlusKidProofExitIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"iconNavClose24LightActive\"`]")
    private ExtendedWebElement closeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \",\"`]")
    protected ExtendedWebElement staticTextNameContainsComma;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\". Text field.\"`]")
    private ExtendedWebElement emptyDigit;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\". Text field.\"`][1]")
    private ExtendedWebElement firstDigitTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e. Text field.\"`][2]")
    private ExtendedWebElement secondCharTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e e. Text field.\"`][3]")
    private ExtendedWebElement thirdCharTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e e e. Text field.\"`][3]")
    private ExtendedWebElement fourthCharTextField;

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

    public ExtendedWebElement getFirstTextValue() {
        return firstDigitTextField;
    }

    public ExtendedWebElement getSecondCharTextField() {
        return secondCharTextField;
    }

    public ExtendedWebElement getThirdCharTextField() {
        return thirdCharTextField;
    }

    public ExtendedWebElement getFourthCharTextField() {
        return fourthCharTextField;
    }
}
