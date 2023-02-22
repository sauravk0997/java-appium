package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusContactCustomerServicePageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "title")
    private ExtendedWebElement cssTitle;

    @FindBy(id = "subtitle")
    private ExtendedWebElement cssSubtitle;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement standardButtonContainer;

    public boolean isTitleDisplayed(){
        return cssTitle.isElementPresent();
    }

    public boolean isSubtitleDisplayed(){
        return cssSubtitle.isElementPresent();
    }

    public void clickHelpCenter() {
        genericTextElementExact.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_HELP_CENTER.getText())).click();
    }

    public void clickDismiss() {
        genericTextElementExact.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DISMISS_BTN.getText())).click();
    }

    public DisneyPlusContactCustomerServicePageBase(WebDriver driver) {
        super(driver);
    }
}
