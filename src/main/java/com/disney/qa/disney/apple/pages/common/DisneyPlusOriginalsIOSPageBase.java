package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOriginalsIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    private final ExtendedWebElement originalsPageLoad = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.ORIGINALS_PAGE_LOAD.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeCollectionView[%s]")
    protected ExtendedWebElement collectionContainer;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"Originals\"`]")
    protected ExtendedWebElement originalLabel;

    //FUNCTIONS

    public DisneyPlusOriginalsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isOriginalPageLoadPresent() {
        return originalsPageLoad.isElementPresent();
    }

    public void swipeInCollectionContainer(ExtendedWebElement element, int position){
        swipePageTillElementPresent(element, 10, collectionContainer.format(position), Direction.LEFT, 1500);
    }

    public ExtendedWebElement getOriginalLabel(){
        return originalLabel;
    }

    public boolean isOriginalLabelPresent() {
        return originalLabel.isElementPresent();
    }
}
