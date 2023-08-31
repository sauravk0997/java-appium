package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditGenderIOSPageBase extends DisneyPlusApplePageBase {

    //TODO Refactor english hardcoded values to reference dictionary keys
    //LOCATORS

    private String genderWomen = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_WOMAN.getText());

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='GENDER']//following-sibling:: XCUIElementTypeButton /XCUIElementTypeStaticText")
    protected ExtendedWebElement genderPlaceholder;
    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='SAVE']")
    protected ExtendedWebElement saveBtn;

    @FindBy(xpath = "//*[@name='alertAction:defaultButton' and @label='%s']")
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

    public void selectGender(){
        dynamicBtnFindByLabel.format(genderWomen).click();
    }

    public void clickSaveBtn() { saveBtn.click(); }
}