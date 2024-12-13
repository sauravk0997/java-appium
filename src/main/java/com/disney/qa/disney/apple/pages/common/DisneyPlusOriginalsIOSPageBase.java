package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.NAV_ORIGINALS_TITLE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOriginalsIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    //FUNCTIONS

    public DisneyPlusOriginalsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        String originalLabel = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, NAV_ORIGINALS_TITLE.getText());
        return getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                        false), Map.of(BRAND_NAME, originalLabel))).isPresent();
    }

    public void swipeInCollectionContainer(ExtendedWebElement element, String id){
        swipePageTillElementPresent(element, 10, collectionCell.format(id), Direction.LEFT, 1500);
    }

    public ExtendedWebElement getOriginalsTitle() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.NAV_ORIGINALS_TITLE.getText()));
    }
}
