package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditGenderIOSPageBase extends DisneyPlusApplePageBase {

    //TODO Refactor english hardcoded values to reference dictionary keys
    //LOCATORS

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Prefer not to say'`]")
    protected ExtendedWebElement genderPlaceholder;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'SAVE'`]")
    protected ExtendedWebElement saveBtn;
    @ExtendedFindBy(iosPredicate = "label == '%s' AND name == 'alertAction:defaultButton'")
    protected ExtendedWebElement genderOptionValue;

    //FUNCTIONS

    public DisneyPlusEditGenderIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public enum GenderOption {
        GENDER_WOMEN(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_WOMAN.getText()), 1),
        GENDER_MEN(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_MAN.getText()), 2),
        GENDER_NOBINARY(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_NON_BINARY.getText()), 3),
        GENDER_PREFERNOTTOSAY(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText()), 4);

        String genderOption;
        int index;

        GenderOption(String genderOption, int index) {
            this.genderOption = genderOption;
            this.index = index;
        }

        public String getGenderOption() {
            return genderOption;
        }

        public int getIndex() {
            return index;
        }
    }

    public boolean isGenderOptionPresent(GenderOption option) {
        return genderOptionValue.format(option.getGenderOption()).isElementPresent();
    }

    public boolean isOpened() {
        return genderPlaceholder.isPresent();
    }

    public void clickGenderDropDown() {
        genderPlaceholder.click();
    }

    public void selectGender(String gender){
        dynamicBtnFindByLabel.format(gender).click();
    }

    public void clickSaveBtn() { saveBtn.click(); }
}