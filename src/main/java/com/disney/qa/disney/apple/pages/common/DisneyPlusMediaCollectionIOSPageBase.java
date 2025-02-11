package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.disney.qa.common.constant.IConstantHelper.LABEL;
import static com.disney.qa.common.constant.IConstantHelper.PHONE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMediaCollectionIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String MOVIES_COLLECTION_LABEL = "On the Movies screen.";

    @FindBy(id = "segmentedControl")
    private ExtendedWebElement categoryScroller;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'segmentedControl'`]" +
            "/XCUIElementTypeScrollView/XCUIElementTypeButton[1]")
    protected ExtendedWebElement defaultContentPageFilterButtonForTablet;

    @ExtendedFindBy(accessibilityId = "selectorButton")
    protected ExtendedWebElement defaultContentPageFilterButtonForHandset;

    @FindBy(id = "selectorButton")
    private ExtendedWebElement mediaCategoryDropdown;

    private final ExtendedWebElement seriesHeader = xpathNameOrName.format(getLocalizationUtils().
                    getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.NAV_SERIES_TITLE.getText()),
            DictionaryKeys.NAV_SERIES_TITLE.getText());

    private final ExtendedWebElement moviesHeader = xpathNameOrName.format(getLocalizationUtils().
                    getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.NAV_MOVIES_TITLE.getText()),
            DictionaryKeys.NAV_MOVIES_TITLE.getText());

    public DisneyPlusMediaCollectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getMediaCategoryDropdown() {
        return mediaCategoryDropdown;
    }

    public ExtendedWebElement getCategoryScroller() {
        return categoryScroller;
    }

    public ExtendedWebElement getSeriesHeader() {
        return seriesHeader;
    }

    public ExtendedWebElement getMoviesHeader() {
        return moviesHeader;
    }

    public String getSelectedCategoryFilterName() {
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Getting selected category name using Handset element");
            return defaultContentPageFilterButtonForHandset.getAttribute(LABEL);
        }
        LOGGER.info("Getting selected category name using Tablet element");
        return defaultContentPageFilterButtonForTablet.getAttribute(LABEL);
    }

    public List<String> getCollectionTitles() {
        waitForPresenceOfAnElement(collectionCellNoRow.format(MOVIES_COLLECTION_LABEL));
        List<ExtendedWebElement> collectionTitles =
                findExtendedWebElements(collectionCellNoRow.format(MOVIES_COLLECTION_LABEL).getBy());
        if (collectionTitles.isEmpty()) {
            throw new NoSuchElementException("Collection titles list is empty");
        }
        return collectionTitles.stream()
                .map(element -> element.getAttribute(LABEL).split(",")[0])
                .collect(Collectors.toList());
    }
}
