package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusBrandIOSPageBase.class)
public class DisneyPlusAppleTVBrandsPage extends DisneyPlusBrandIOSPageBase {

    @ExtendedFindBy(iosPredicate = "name == \"headerViewTitleLabel\" AND label == '%s'")
    protected ExtendedWebElement brandShelf;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeImage")
    private ExtendedWebElement brandLogoImage;

    public DisneyPlusAppleTVBrandsPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getBrandShelf(String element) {
        return brandShelf.format(element);
    }

    @Override
    public boolean isBrandScreenDisplayed(String brandName) {
        return getCollectionViewLabelContains(
                String.format(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils()
                                .getDictionaryItem(
                                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                        DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                                        false),
                        Map.of(BRAND_NAME, brandName))))
                .isPresent();
    }

    @Override
    public ExtendedWebElement getBrandLogoImage() {
        return brandLogoImage;
    }
}