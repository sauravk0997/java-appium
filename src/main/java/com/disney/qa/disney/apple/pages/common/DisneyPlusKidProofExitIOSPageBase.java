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

    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeScrollView[$type='XCUIElementTypeStaticText'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement digitsElement;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \",\"`][2]")
    protected ExtendedWebElement staticTextNumbers;

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

    public ExtendedWebElement getDigitsElement() {
        return digitsElement;
    }

    public String parseExitDigitsCode() {
        String exitCode = staticTextNumbers.getText();
        List<String> values = Arrays.asList(exitCode.split("[,、]"));
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
        return stringBuilder.toString();
    }
}
