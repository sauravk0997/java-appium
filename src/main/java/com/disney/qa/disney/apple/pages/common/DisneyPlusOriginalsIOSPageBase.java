package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_ORIGINALS_TITLE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOriginalsIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeCollectionView[%s]")
    protected ExtendedWebElement collectionContainer;

    //FUNCTIONS

    public DisneyPlusOriginalsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isOriginalPageLoadPresent() {
        String originalLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_ORIGINALS_TITLE.getText());
        return getStaticTextByLabel(getDictionary().formatPlaceholderString(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                        false), Map.of(BRAND_NAME, originalLabel))).isPresent();
    }

    public void swipeInCollectionContainer(ExtendedWebElement element, int position){
        swipePageTillElementPresent(element, 10, collectionContainer.format(position), Direction.LEFT, 1500);
    }
}
