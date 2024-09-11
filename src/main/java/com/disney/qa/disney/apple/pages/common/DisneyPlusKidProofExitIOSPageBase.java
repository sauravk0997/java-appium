package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.KIDPROOF_DIALOG_TITLE;

public class DisneyPlusKidProofExitIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"iconNavClose24LightActive\"`]")
    private ExtendedWebElement closeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \",\"`][2]")
    protected ExtendedWebElement staticTextNameContainsComma;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\". Text field.\"`][1]")
    private ExtendedWebElement firstDigitTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e. Text field.\"`][1]")
    private ExtendedWebElement secondCharTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e e. Text field.\"`][1]")
    private ExtendedWebElement thirdCharTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name ==\"e e e. Text field.\"`][1]")
    private ExtendedWebElement fourthCharTextField;

    public DisneyPlusKidProofExitIOSPageBase(WebDriver driver) {
        super(driver);
    }
    public boolean getKidProofDialogTitle() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                KIDPROOF_DIALOG_TITLE.getText())).isPresent();
    }

    public ExtendedWebElement getCloseButton() {
        return closeButton;
    }

    public String parseExitCode() {
        String exitCode = staticTextNameContainsComma.getText();
        List<String> values = Arrays.asList(exitCode.split("[,、]"));
        System.out.println("*** values: " + values.toString());
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, String> kidProofDigits = Map.of(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_ZERO.getText()), "0",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_ONE.getText()), "1",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_TWO.getText()), "2",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_THREE.getText()), "3",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_FOUR.getText()), "4",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_FIVE.getText()), "5",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_SIX.getText()), "6",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_SEVEN.getText()), "7",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_EIGHT.getText()), "8",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.KIDPROOF_DIGIT_NINE.getText()), "9"
        );

        values.forEach(value -> {
            value = value.trim();
            stringBuilder.append(kidProofDigits.get(value));
        });
     //   LOGGER.info("Found String: '{}' parsed value is '{}'", exitCode, stringBuilder);
        return stringBuilder.toString();
    }

    public ExtendedWebElement getFirstTextValue() {
        return dynamicOtherFindByNameContains.format(firstDigitTextField);
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
