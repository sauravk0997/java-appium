package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditGenderIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    private String genderPlaceholder = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PLACEHOLDER.getText());
    protected ExtendedWebElement genderPreferNotToSay = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText()));
    private ExtendedWebElement saveButton = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SAVE.getText()));;
    @ExtendedFindBy(iosPredicate = "label == '%s' AND name == 'alertAction:defaultButton'")
    protected ExtendedWebElement genderOptionValue;

    //FUNCTIONS

    public DisneyPlusEditGenderIOSPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * For gender option validation
     */
    public enum GenderOption {
        GENDER_WOMEN(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_WOMAN.getText()), 1),
        GENDER_MEN(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_MAN.getText()), 2),
        GENDER_NOBINARY(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_NON_BINARY.getText()), 3),
        GENDER_PREFERNOTTOSAY(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText()), 4);

        String genderValue;
        int index;

        GenderOption(String genderValue, int index) {
            this.genderValue = genderValue;
            this.index = index;
        }

        public String getGenderOption() {
            return genderValue;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * @param option - gender value Men, Woman, NoBinary, preferNotToSay
     * @return - true/false
     */
    public boolean isGenderOptionPresent(GenderOption option) {
        return genderOptionValue.format(option.getGenderOption()).isElementPresent();
    }

    /**
     * @return - true/false, to verify gender page opened
     */
    @Override
    public boolean isOpened() {
        return dynamicBtnFindByLabel.format(genderPlaceholder).isPresent();
    }

    /**
     * click on gender dropdown to select gender value
     */
    public void clickGenderDropDown() {
        genderPreferNotToSay.click();
    }

    /**
     * @param gender - Pass the value which need to be selected
     */
    public void selectGender(String gender) {
        dynamicBtnFindByLabel.format(gender).click();
    }

    /**
     * Click save button to update
     */
    public void tapSaveButton() {
        saveButton.click();
    }
}