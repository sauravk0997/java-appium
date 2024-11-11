package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusKidProofExitIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"iconNavClose24LightActive\"`]")
    private ExtendedWebElement closeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type = " +
            "'XCUIElementTypeStaticText'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement codeInputField;

    //TODO: Update to accessibility ID when bug IOS-12731 is attended
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \",\"`][2]")
    protected ExtendedWebElement codeText;

    //TODO: Update to accessibility ID when bug IOS-12731 is attended
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView/XCUIElementTypeOther[1]")
    private ExtendedWebElement doorIcon;
    
    public DisneyPlusKidProofExitIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isKidProofDialogTitleDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                KIDPROOF_DIALOG_TITLE.getText())).isPresent();
    }

    public boolean isKidProofIncorrectCodeErrorMessageDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                KIDPROOF_INCORRECT_CODE_ERROR_MESSAGE.getText())).isPresent();
    }

    public ExtendedWebElement getCloseButton() {
        return closeButton;
    }

    public ExtendedWebElement getCodeInputField() {
        return codeInputField;
    }

    public ExtendedWebElement getCodeText() {
        return codeText;
    }

    public ExtendedWebElement getDoorIcon() {
        return doorIcon;
    }

    public String parseExitDigitsCode() {
        String exitCodeNumber = codeText.getText();
        List<String> values = Arrays.asList(exitCodeNumber.split("[,、]"));
        StringBuilder stringBuilder = new StringBuilder();
        values.forEach(value -> stringBuilder.append(getKidProofExitCodeDigits().get(value.trim())));
        return stringBuilder.toString();
    }

    private Map<String, String> getKidProofExitCodeDigits() {
       return Map.of(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_ZERO.getText()), "0",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_ONE.getText()), "1",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_TWO.getText()), "2",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_THREE.getText()), "3",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_FOUR.getText()), "4",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_FIVE.getText()), "5",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_SIX.getText()), "6",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_SEVEN.getText()), "7",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_EIGHT.getText()), "8",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.KIDPROOF_DIGIT_NINE.getText()), "9"
        );
    }
}
