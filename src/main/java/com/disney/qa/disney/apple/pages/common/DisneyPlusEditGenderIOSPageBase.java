package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditGenderIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    private String genderPlaceholder = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PLACEHOLDER.getText());
    private String saveButton = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SETTINGS_GENDER_SAVE.getText());
    @ExtendedFindBy(iosPredicate = "label == '%s' AND name == 'alertAction:defaultButton'")
    private ExtendedWebElement genderOptionValue;

    //FUNCTIONS

    public DisneyPlusEditGenderIOSPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * For gender option validation
     */
    public enum GenderOption {
        GENDER_WOMEN,
        GENDER_MEN,
        GENDER_NOBINARY,
        GENDER_PREFERNOTTOSAY
    }

    public String getGenderLabel(GenderOption option) {
        String selection;
        switch (option) {
            case GENDER_WOMEN:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_WOMAN.getText());
                break;
            case GENDER_MEN:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_MAN.getText());
                break;
            case GENDER_NOBINARY:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_NON_BINARY.getText());
                break;
            case GENDER_PREFERNOTTOSAY:
                selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText());
                break;
            default:
                throw new InvalidArgumentException("Invalid selection made");
        }
        return selection;
    }
    /**
     * @param option - gender value Men, Woman, NoBinary, preferNotToSay
     * @return - true/false
     */
    public boolean isGenderOptionPresent(GenderOption option) {
        return genderOptionValue.format(getGenderLabel(option)).isElementPresent();
    }

    /**
     * @return - true/false, to verify gender page opened
     */
    @Override
    public boolean isOpened() {
        return staticTextByLabel.format(genderPlaceholder).isPresent();
    }

    /**
     * click on gender dropdown to select gender value
     */
    public void clickGenderDropDown() {
        dynamicBtnFindByLabel.format(getGenderLabel(GenderOption.GENDER_PREFERNOTTOSAY)).click();
    }

    /**
     * Click save button to update
     */
    public void tapSaveButton() {
        dynamicBtnFindByLabel.format(saveButton).click();
    }
}