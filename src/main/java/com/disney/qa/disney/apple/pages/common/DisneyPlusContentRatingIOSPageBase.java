package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusContentRatingIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusContentRatingIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton")
    private ExtendedWebElement contentRatingInfoButton;


    private ExtendedWebElement gotItButton = xpathNameOrName.format(getDictionary()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                    DictionaryKeys.BTN_GOT_IT.getText()), DictionaryKeys.BTN_GOT_IT.getText());

    public ExtendedWebElement getGotItButton() {
        return gotItButton;
    }

    public ExtendedWebElement getContentRatingInfoButton() {
        return contentRatingInfoButton;
    }
}
